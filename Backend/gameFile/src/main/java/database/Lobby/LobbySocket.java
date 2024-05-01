package database.Lobby;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.util.JSONPObject;
import database.Chat.MessageRepository;
import database.Game.Card;
import database.Game.Game;
import database.Game.Player;
import database.Spectator.Spectator;
import database.Spectator.SpectatorRepository;
import database.Stats.Stat;
import database.Stats.StatRepository;
import database.Users.User;
import database.Users.UserRepository;
import database.Websocketconfig.WebsocketConfig;
import jakarta.persistence.Lob;
import jakarta.transaction.Transactional;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import jakarta.websocket.OnOpen;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.json.JSONObject;


@Controller      // this is needed for this to be an endpoint to springboot
@ServerEndpoint(value = "/lobby/{lobbyId}/{username}")  // this is WebSocket URL
public class LobbySocket {

    private static LobbyRepository lobbyRepository; //Lobby Repo

    private static UserRepository userRepository; // User Repo

    private static SpectatorRepository spectatorRepository;

    private static StatRepository statRepository;
    private static boolean exchangeBluffHappened;



    private static Game game;


    @Autowired
    public void setUserRepository(UserRepository repo) {
        userRepository = repo;  // we are setting the static variable
    }
    @Autowired
    public void setLobbyRepository(LobbyRepository repo) {
        lobbyRepository = repo;  // we are setting the static variable
    }

    @Autowired
    public void setStatRepository(StatRepository repo) {
        statRepository = repo;  // we are setting the static variable
    }

    @Autowired
    public void setSpectatorRepository(SpectatorRepository repo) {spectatorRepository = repo;}

    private final Logger logger = LoggerFactory.getLogger(LobbySocket.class);
    private static Map<Session,User> sessionLobbyMap = new HashMap<>(); // Associate a Session with Users to find users to remove and add them

    private static Map<Session,User> sessionUserMap = new HashMap<>(); // Associate a Session with Users to find users to remove and add them

    private static Map < User, Session > userSessionMap = new Hashtable < > ();

    private static Lobby existingLobby;

    private static User userForStats;
    private static Stat stateForUser;
    private boolean firstUser;


    @OnOpen
    public void onOpen(Session session, @PathParam("lobbyId") int lobbyId, @PathParam("username") String username) throws IOException {

        User user = userRepository.findByUserEmail(username); // find user by their email


        sessionUserMap.put(session, user); // map the WebSocket session to the user
        userSessionMap.put(user, session); // map the user to the WebSocket session

        if (lobbyId == 0) { // USER WANTS TO CREATE LOBBY
            Lobby newLobby = new Lobby(); // Create a new Lobby
            newLobby.addUser(user); // add user to the lobby
            lobbyRepository.save(newLobby); // save the new lobby
            broadcastToAllInLobby(newLobby, "Users in lobby: " + newLobby.getUsers() + " The ID is: " + newLobby.getId());
            firstUser = true;
        } else { // USER WANTS TO JOIN LOBBY
            firstUser = false;
            existingLobby = lobbyRepository.findById(lobbyId); // find lobby by ID
            if (existingLobby == null) {
                session.getBasicRemote().sendText("Lobby not found");
                session.close();
                return;
            }
            if (existingLobby.getUserArraylist().size() < 4) { // Check if lobby has less than 4 users
                existingLobby.addUser(user); // Add user to the lobby
                lobbyRepository.save(existingLobby); // Save Lobby
                broadcastToAllInLobby(existingLobby, username + ": Joined the lobby");

                if (existingLobby.getUserArraylist().size() == 4 && !existingLobby.hasGameStarted()) { // START GAME
                    existingLobby.setGameStarted(true);
                    existingLobby.setFull(true);
                    lobbyRepository.save(existingLobby);

                    /**
                     * Stat Code
                     */

                    for(User stat : existingLobby.getUserArraylist()){
                        Stat stat1 = stat.getStat();
                        stat1.addGamePlayed();
                        statRepository.save(stat1);
                    }

                    /**
                     * Stat Code
                     */

                    startGame(existingLobby); // function to handle game initialization

                }
            } else if (existingLobby.getUserArraylist().size() >= 4 || existingLobby.hasGameStarted()) { // Lobby is full or game has started
                Spectator spectator = new Spectator(user);
                spectator.joinLobby(existingLobby);
                spectatorRepository.save(spectator); // Save the spectator to the database
                broadcastToAllInLobby(existingLobby, username + " has joined the lobby as a spectator.");
                existingLobby.addSpectator(user); // Add Spectator to Array list
                broadcastToSpecificUser(username,"spec");
            }
        }
    }

    private void broadcastToAllInLobby(Lobby lobby, String message) {
        // Send message to all active participants and spectators
        lobby.getUserArraylist().forEach(u -> broadcastToSpecificUser(u.getUserEmail(), message));
        lobby.getSpectators().forEach(u -> broadcastToSpecificUser(u.getUserEmail(), message));
    }

    private void startGame(Lobby lobby) {
        if (lobby.getUserArraylist().size() == 4) {
            // Convert each User in the lobby into a Player object using their individual email
            List<Player> players = new ArrayList<>(4); // Create an ArrayList of Players

            game = new Game(players); // Pass the list of Player objects to the Game constructor

            // Add each user in the lobby as a Player object to the players list
            game.initGame(
                    lobby.getUserArraylist().get(0).getUserEmail(),
                    lobby.getUserArraylist().get(1).getUserEmail(),
                    lobby.getUserArraylist().get(2).getUserEmail(),
                    lobby.getUserArraylist().get(3).getUserEmail()
            );

            // Mark the game as started in the lobby
            lobby.setGameStarted(true);

            // Notify all users in the lobby that the game is starting
            broadcastToAllInLobby(lobby, "Lobby is full");


        }
//        else {
//            // Notify all users in the lobby that the game cannot start yet
//            broadcastToAllInLobby(lobby, "Not enough players to start the game. Waiting for more players to join.");
//        }
    }




    @OnMessage
    public void onMessage(Session session, String message) throws IOException {



        //GAME LOGIC TESTING
        JSONObject jsonpObject = new JSONObject(message); // Create JSON object
        String email = jsonpObject.getString("playerEmail"); // Get Player Email
        String state = jsonpObject.getString("move"); // Actions/Game State, Can be one of the following: Ready, Bluffing, Action, waiting
        String targetPlayer = jsonpObject.getString("targetPlayer"); // Get opponentEmail
        String card1 = jsonpObject.getString("card1");
        String card2 = jsonpObject.getString("card2");
        String currentMove = state;
        Player p;
        userForStats = userRepository.findByUserEmail(email);
        stateForUser = userForStats.getStat(); // Find their stats


        if(state.equals("spectate")){ // Find spectator
            User Userspectator = userRepository.findByUserEmail(email);
            Spectator spectator = new Spectator(Userspectator);
            spectatorRepository.save(spectator); // Save the spectator to the database
            p = new Spectator();
            p.setUserEmail(email);


        }else{
            p = game.getPlayer(email); //Find player by their email
        }



        if(state.contains("pass")){
            p.setPlayerState("wait");
            for(Player player : game.getPlayerArrayList()){
                broadcastToSpecificUser(player.getUserEmail(),  "The Coup Conductor: " + p.getUserEmail()+ " wants to pass."); //Charles took: income
            }

            for(User spectator : existingLobby.getSpectators()){
                broadcastToSpecificUser(spectator.getUserEmail(),  "The Coup Conductor: " + p.getUserEmail()+ " wants to pass."); //Charles took: income
            }
        }

        if(state.startsWith("@")){
            for(Player player : game.getPlayerArrayList()){
                broadcastToSpecificUser(player.getUserEmail(),p.getUserEmail() + ": " + state.substring(1));
            }

            for(User spectator : existingLobby.getSpectators()){
                broadcastToSpecificUser(spectator.getUserEmail(),p.getUserEmail() + ": " + state.substring(1));
            }

        }

        if (state.equals("ready")) {  //If the player message says ready to listen, give them the game
            broadcastToSpecificUserGAMEJSON(p.getUserEmail(), game); // Broadcast to each player indivual so front end can unqiuely set up UI
        }else if(state.equals("spectate")){
            broadcastToSpecificUserGAMEJSON(email, game); // Broadcast to each player indivual so front end can unqiuely set up UI
        } else if (state.startsWith("*") && !state.contains("Coup") && !state.contains("Income")) { // Set Action for Players
            currentMove = state.substring(1); // save move for current player
            p.setCurrentMove(currentMove);
            stateForUser.incrementSpecialMoves(currentMove);

            //He will send me the action, it is my job to change all the other players to contest
            p.setPlayerState("wait"); //Send action player to wait
            if(card1.equals("null") && card2.equals("null")){  // If FE is sending cards, we don't want to change states
                if(game.getCurrentPlayer().getCurrentMove().equals("Tax") || game.getCurrentPlayer().getCurrentMove().equals("Exchange")){ // Set challange (no Blocking)
                    for(Player player : game.getPlayerArrayList()){
                        if (!player.equals(game.getCurrentPlayer()) && !player.getPlayerState().equals("dead")) {
                            player.setPlayerState("challenge"); //set other players to contest
                        }
                    }
                }else{
                    for(Player player : game.getPlayerArrayList()){
                        if (!player.equals(game.getCurrentPlayer()) && !player.getPlayerState().equals("dead")) { // Set contest, no blocking
                            player.setPlayerState("contest"); //set other players to contest
                        }
                    }
                }

                if(targetPlayer.contains("null")){
                    for(Player player : game.getPlayerArrayList()){
                        broadcastToSpecificUser(player.getUserEmail(),  "The Coup Conductor: " + p.getUserEmail()+ " wants to "+ currentMove); //Charles took: income
                    }

                    for(User spectator : existingLobby.getSpectators()){
                        broadcastToSpecificUser(spectator.getUserEmail(),  "The Coup Conductor: " + p.getUserEmail()+ " wants to "+ currentMove); //Charles took: income
                    }


                }else{
                    for(Player player : game.getPlayerArrayList()){
                        broadcastToSpecificUser(player.getUserEmail(),  "The Coup Conductor: " + p.getUserEmail()+ " wants to "+ currentMove + " " + targetPlayer); //Charles took: income
                    }

                    for(User spectator : existingLobby.getSpectators()){
                        broadcastToSpecificUser(spectator.getUserEmail(),  "The Coup Conductor: " + p.getUserEmail()+ " wants to "+ currentMove + " " + targetPlayer); //Charles took: income
                    }

                    p.setTargetPlayer(targetPlayer);
                    game.getCurrentPlayer().setTargetPlayer(targetPlayer);
                }
            }
        } else if (state.equals("Bluff")) {
            stateForUser.increaseBluff();
            //If any player called bluff, go into bluffing
            if(game.getBlocker().getUserEmail().equals("null")){ // Regular bluff for player
                for(Player player : game.getPlayerArrayList()){
                    broadcastToSpecificUser(player.getUserEmail(),  "The Coup Conductor: " + p.getUserEmail()+ " Calls bluff on " + game.getCurrentPlayer().getUserEmail()); //Charles took: income
                }
                for(User spectator : existingLobby.getSpectators()){
                    broadcastToSpecificUser(spectator.getUserEmail(),  "The Coup Conductor: " + p.getUserEmail()+ " Calls bluff on " + game.getCurrentPlayer().getUserEmail()); //Charles took: income
                }


                if(game.getCurrentPlayer().revealCard(game.associate(game.getCurrentPlayer().getCurrentMove()),game.getCurrentPlayer()).equals(game.getCurrentPlayer().getUserEmail() + " Was a Liar")){ //if player is a liar, remove their card
                    Card playerCard = new Card(game.getCurrentPlayer().loseInfluence(game.getCurrentPlayer()));
                    game.getDeckDeck().addCardToBottomOfDeck(playerCard);
                    for(Player player : game.getPlayerArrayList()){
                        broadcastToSpecificUser(player.getUserEmail(),  "The Coup Conductor: " + game.getCurrentPlayer().getUserEmail()+ " Lost their card: " + playerCard.getName()); //Charles took: income
                    }

                    for(User spectator : existingLobby.getSpectators()){
                        broadcastToSpecificUser(spectator.getUserEmail(),  "The Coup Conductor: " + game.getCurrentPlayer().getUserEmail()+ " Lost their card: " + playerCard.getName()); //Charles took: income
                    }

                    game.nextTurn();
                }else{
                    Card playerCard  = new Card(p.loseInfluence(p));  // The bluffer loses Influence
                    game.getDeckDeck().addCardToBottomOfDeck(playerCard);
                    for(Player player : game.getPlayerArrayList()){
                        broadcastToSpecificUser(player.getUserEmail(),  "The Coup Conductor: " + p.getUserEmail()+ " Lost their card: " + playerCard.getName()); //Charles took: income
                    }

                    for(User spectator : existingLobby.getSpectators()){
                        broadcastToSpecificUser(spectator.getUserEmail(),  "The Coup Conductor: " + p.getUserEmail()+ " Lost their card: " + playerCard.getName()); //Charles took: income
                    }

                    String card = game.getCurrentPlayer().removeCard(game.associate(game.getCurrentPlayer().getCurrentMove()),game.getCurrentPlayer()); // The Player their card
                    String drawCard = game.getDeckDeck().drawCard();  //Draw Card from deck
                    Card card4Deck = new Card(card);
                    game.getDeckDeck().addCardToBottomOfDeck(card4Deck);
                    game.getCurrentPlayer().gainInfluence(drawCard,game.getCurrentPlayer());

                    if(game.getCurrentPlayer().getTargetPlayer().equals("null") && !game.getCurrentPlayer().getCurrentMove().contains("Exchange")){
                        game.getCurrentPlayer().action(game.getCurrentPlayer().getCurrentMove(),game.getCurrentPlayer());
                        game.nextTurn();
                    }else if (!game.getCurrentPlayer().getCurrentMove().contains("Exchange")){
                        game.getCurrentPlayer().action(game.getCurrentPlayer().getCurrentMove(),game.getPlayer(game.getCurrentPlayer().getTargetPlayer()));
                        game.nextTurn();
                    }

                    /**
                     * Special Case for Exchange
                     */
                    if(game.getCurrentPlayer().getCurrentMove().contains("Exchange")){
                        for(Player player : game.getPlayerArrayList()){ // Set all players to wait so exchange can happen
                            if(!player.equals(game.getCurrentPlayer())){
                                player.setPlayerState("wait");
                                exchangeBluffHappened = true;
                            }
                        }
                    }


                }
            }else{ //Special Bluff that reveals Blockers card.
                // Find Blocking player, if the blocking player has a Card that can block the current players move,
                // Current player loses card, if not, blocking player loses card.
                Player blocker = game.getBlocker();
                for(Player player : game.getPlayerArrayList()){
                    broadcastToSpecificUser(player.getUserEmail(),p.getUserEmail() + ": calls bluff on: " + game.getBlocker().getUserEmail()); //Charles took: income
                }
                for(User spectator : existingLobby.getSpectators()){
                    broadcastToSpecificUser(spectator.getUserEmail(),p.getUserEmail() + ": calls bluff on: " + game.getBlocker().getUserEmail()); //Charles took: income
                }


                if(blocker.revealCard(blocker.getCurrentMove(), blocker).equals(blocker.getUserEmail() + " Was a Liar")){ //If blocker was lying about his block
                    Card blockerCard = new Card(blocker.loseInfluence(blocker)); // Removes Card
                    game.getDeckDeck().addCardToBottomOfDeck(blockerCard);
                    for(Player player : game.getPlayerArrayList()){
                        broadcastToSpecificUser(player.getUserEmail(),  "The Coup Conductor: " + blocker.getUserEmail()+ " Lost Their Card: " + blockerCard.getName()); //Charles took: income
                    }

                    for(User spectator : existingLobby.getSpectators()){
                        broadcastToSpecificUser(spectator.getUserEmail(),  "The Coup Conductor: " + blocker.getUserEmail()+ " Lost Their Card: " + blockerCard.getName()); //Charles took: income
                    }

                    Player blockerRestart = new Player("null",2,false,2,"null","null");
                    game.setBlocker(blockerRestart);
                    /**
                     * Move Should still happen
                     */
                    if(game.getCurrentPlayer().getTargetPlayer().equals("null") && !game.getCurrentPlayer().getCurrentMove().contains("Exchange")){
                        game.getCurrentPlayer().action(game.getCurrentPlayer().getCurrentMove(),game.getCurrentPlayer());
                        game.nextTurn();
                    }else if (!game.getCurrentPlayer().getCurrentMove().contains("Exchange")){
                        game.getCurrentPlayer().action(game.getCurrentPlayer().getCurrentMove(),game.getPlayer(game.getCurrentPlayer().getTargetPlayer()));
                        game.nextTurn();
                    }

                }else{ //If the blocker was not lying, bluff caller loses card and blocker gets a new card
                    Card playerCard = new Card(p.loseInfluence(p)); // Bluff Caller loses Card
                    Card blockerCard = new Card(blocker.removeCard(blocker.getCurrentMove(),blocker)); // Blockers Loses Card, because it was reveled.
                    game.getDeckDeck().addCardToBottomOfDeck(playerCard); //Adds Both Cards to Deck
                    game.getDeckDeck().addCardToBottomOfDeck(blockerCard); // Adds Both Cards to Deck
                    String drawCard = game.getDeckDeck().drawCard();  //Draw Card from deck for Blocker
                    blocker.gainInfluence(drawCard,blocker); // Blocker gets the card drawed from deck
                    for(Player player : game.getPlayerArrayList()){
                        broadcastToSpecificUser(player.getUserEmail(),  "The Coup Conductor: " + p.getUserEmail()+ " Lost Their Card: " + playerCard.getName()); //Charles took: income
                    }

                    for(User spectator : existingLobby.getSpectators()){
                        broadcastToSpecificUser(spectator.getUserEmail(),  "The Coup Conductor: " + p.getUserEmail()+ " Lost Their Card: " + playerCard.getName()); //Charles took: income
                    }


                    Player blockerRestart = new Player("null",2,false,2,"null","null");
                    game.setBlocker(blockerRestart);
                    game.nextTurn();
                }

            }
        } else if(state.contains("Block")) {
            stateForUser.increaseBlock();
            if(state.equals("Block")){ // If it just a block, return the corresponding blocking card
                game.setBlocker(p);
                game.getBlocker().setCurrentMove(game.associateBlock(game.getCurrentPlayer().getCurrentMove()));
                for(Player player : game.getPlayerArrayList()){
                    broadcastToSpecificUser(player.getUserEmail(),  "The Coup Conductor: " + p.getUserEmail()+ " blocks " + game.getCurrentPlayer().getUserEmail()); //Charles took: income
                }

                for(User spectator : existingLobby.getSpectators()){
                    broadcastToSpecificUser(spectator.getUserEmail(),  "The Coup Conductor: " + p.getUserEmail()+ " blocks " + game.getCurrentPlayer().getUserEmail()); //Charles took: income
                }

            }else{ // If it is steal, we need either captain or ambassador
                game.setBlocker(p); // Save Blocker
                game.getBlocker().setCurrentMove(state.substring(6)); //Saves Move ex (Block Duke)
                for(Player player : game.getPlayerArrayList()){
                    broadcastToSpecificUser(player.getUserEmail(),  "The Coup Conductor: " + p.getUserEmail()+ " blocks " + game.getCurrentPlayer().getUserEmail() + " with " + game.getBlocker().getCurrentMove()); //Charles took: income
                }

                for(User spectator : existingLobby.getSpectators()){
                    broadcastToSpecificUser(spectator.getUserEmail(),  "The Coup Conductor: " + p.getUserEmail()+ " blocks " + game.getCurrentPlayer().getUserEmail() + " with " + game.getBlocker().getCurrentMove()); //Charles took: income
                }

            }
            for(Player player : game.getPlayerArrayList()){
                if(!player.getPlayerState().equals("dead")){
                    if (!player.equals(game.getBlocker())) {
                        player.setPlayerState("challenge"); //set other players to contest
                    }else{
                        player.setPlayerState("wait"); // Set Blocker to Wait
                    }
                }
            }
        }
        //AutoMatic Moves
        else if(state.contains("Coup")){
            //Messages for Coup
            for(Player player : game.getPlayerArrayList()){
                broadcastToSpecificUser(player.getUserEmail(),p.getUserEmail() + ": Couped " + targetPlayer);
            }
            for(User spectator : existingLobby.getSpectators()){
                broadcastToSpecificUser(spectator.getUserEmail(),p.getUserEmail() + ": Couped " + targetPlayer);
            }
            stateForUser.increaseCoup();
        }else if(state.equals("*Income")){
            //Messages for Income
            for(Player player : game.getPlayerArrayList()){
                broadcastToSpecificUser(player.getUserEmail(),  "The Coup Conductor: " + p.getUserEmail()+ " took income"); //Charles took: income
            }

            for(User spectator : existingLobby.getSpectators()){
                broadcastToSpecificUser(spectator.getUserEmail(),  "The Coup Conductor: " + p.getUserEmail()+ " took income."); //Charles took: income
            }
            stateForUser.increaseIncome();

        }


        //Final BroadCast
        if(!state.equals("ready") && !state.contains("@") && !state.equals("spectate")){ // If it is not a message or ready
            if((state.contains("Income"))){ // If income, automatic Turn
                currentMove = state.substring(1); // save move for current player
                p.action(currentMove,game.getPlayer(p.getUserEmail())); // Does the player action for each player
                game.nextTurn();

            } else if ((state.contains("Coup"))) { // If Coup, Automatic Turn
                p.action(currentMove,game.getPlayer(targetPlayer)); // Does the player action for each player
                game.nextTurn();
            }

            //Blocking Final Checks for all other moves
            boolean truth = checkPass(game);
            if(checkPass(game) && (!state.contains("Income") && !state.contains("Coup")) && game.getBlocker().getUserEmail().equals("null") && (!state.equals("Bluff") || exchangeBluffHappened)){ //if all players passed, and block did not happen do move (tax, etc)
                if(game.getCurrentPlayer().getTargetPlayer().equals("null") && !game.getCurrentPlayer().getCurrentMove().contains("Exchange")){
                    for(Player player : game.getPlayerArrayList()){
                        broadcastToSpecificUser(player.getUserEmail(), "The Coup Conductor: Everyone Passed, move stands"); //Charles took: income
                    }

                    for(User spectator : existingLobby.getSpectators()){
                        broadcastToSpecificUser(spectator.getUserEmail(), "The Coup Conductor: Everyone Passed, move stands"); //Charles took: income
                    }

                    game.getCurrentPlayer().action(game.getCurrentPlayer().getCurrentMove(),game.getCurrentPlayer());
                    game.nextTurn();
                }else if(!game.getCurrentPlayer().getCurrentMove().contains("Exchange")){
                    for(Player player : game.getPlayerArrayList()){
                        broadcastToSpecificUser(player.getUserEmail(), "The Coup Conductor: Everyone Passed, move stands");
                    }

                    for(User spectator : existingLobby.getSpectators()){
                        broadcastToSpecificUser(spectator.getUserEmail(), "The Coup Conductor: Everyone Passed, move stands");
                    }

                    game.getCurrentPlayer().action(game.getCurrentPlayer().getCurrentMove(),game.getPlayer(game.getCurrentPlayer().getTargetPlayer()));
                    for(Player player : game.getPlayerArrayList()){
                        broadcastToSpecificUser(player.getUserEmail(), "The Coup Conductor: "+game.getCurrentPlayer().getUserEmail()+ " " + game.getCurrentPlayer().getCurrentMove() + game.getCurrentPlayer().getTargetPlayer()); //Charles took: income
                    }
                    for(User spectator : existingLobby.getSpectators()){
                        broadcastToSpecificUser(spectator.getUserEmail(), "The Coup Conductor: "+game.getCurrentPlayer().getUserEmail()+ " " + game.getCurrentPlayer().getCurrentMove() + game.getCurrentPlayer().getTargetPlayer()); //Charles took: income
                    }


                    game.nextTurn();
                }else{ // If we got ambassador
                    if(card1.equals("null") && card2.equals("null")){ // If we have not gotten card
                        if(!exchangeBluffHappened){
                            for(Player player : game.getPlayerArrayList()){
                                broadcastToSpecificUser(player.getUserEmail(), "The Coup Conductor: Everyone Passed, move stands");
                            }
                            for(User spectator : existingLobby.getSpectators()){
                                broadcastToSpecificUser(spectator.getUserEmail(), "The Coup Conductor: Everyone Passed, move stands");
                            }


                        }else{
                            exchangeBluffHappened = false;
                        }

                        if(game.getCurrentPlayer().getLives() == 1){
                            String cardOne = game.getDeckDeck().drawCard();
                            game.getCurrentPlayer().setExchangeCard1(cardOne);
                            game.getCurrentPlayer().setPlayerState("Exchange1");
                        }else{
                            String cardOne = game.getDeckDeck().drawCard();
                            String cardTwo = game.getDeckDeck().drawCard();
                            game.getCurrentPlayer().setExchangeCard1(cardOne);
                            game.getCurrentPlayer().setExchangeCard2(cardTwo);
                            game.getCurrentPlayer().setPlayerState("Exchange2");
                        }


                    }else { // If we have gotten card now, remember we still have the cards he originally had.
                        //Two cards will go back in deck (his oringal), the other  will go with him

                        if(game.getCurrentPlayer().getCardOne().equals("null")){
                            Card cardUno = new Card(game.getCurrentPlayer().getCardTwo());
                            game.getDeckDeck().addCardToBottomOfDeck(cardUno);
                            game.getCurrentPlayer().setCardTwo(card1);
                            game.getCurrentPlayer().setExchangeCard1("null");
                            game.getCurrentPlayer().setExchangeCard2("null");
                            game.nextTurn();
                        }else if(game.getCurrentPlayer().getCardTwo().equals("null")){
                            Card cardUno = new Card(game.getCurrentPlayer().getCardOne());
                            game.getDeckDeck().addCardToBottomOfDeck(cardUno);
                            game.getCurrentPlayer().setCardOne(card1);
                            game.getCurrentPlayer().setExchangeCard1("null");
                            game.getCurrentPlayer().setExchangeCard2("null");
                            game.nextTurn();
                        }else{ //both cards are present
                            Card cardUno = new Card(game.getCurrentPlayer().getCardOne());
                            Card cardDos = new Card(game.getCurrentPlayer().getCardTwo());
                            game.getDeckDeck().addCardToBottomOfDeck(cardUno);
                            game.getDeckDeck().addCardToBottomOfDeck(cardDos);
                            game.getCurrentPlayer().setCardOne(card1);
                            game.getCurrentPlayer().setCardTwo(card2);
                            game.getCurrentPlayer().setExchangeCard1("null");
                            game.getCurrentPlayer().setExchangeCard2("null");
                            game.nextTurn();
                        }



                    }


                }

            }else if((!state.contains("Income") && !state.contains("Coup")) && checkPass(game) && !state.equals("Bluff")){ // if block happened, and players did not challange (bluff) (They passed), next turn
                if(!game.getBlocker().getUserEmail().equals("null")){ // Blocker is present
                    for(Player player : game.getPlayerArrayList()){
                        broadcastToSpecificUser(player.getUserEmail(), "The Coup Conductor: Everyone Passed, Block Stands"); //Charles took: income
                    }

                    for(User spectator : existingLobby.getSpectators()){
                        broadcastToSpecificUser(spectator.getUserEmail(), "The Coup Conductor: Everyone Passed, Block Stands"); //Charles took: income
                    }

                    game.nextTurn();
                    Player blockerRestart = new Player("null",2,false,2,"null","null");
                    game.setBlocker(blockerRestart);
                }
                //Do nothing if blocking has not happened
            }


            if(checkGameEnd(game)){
                for(Player player : game.getPlayerArrayList()){
                    broadcastToSpecificUser(player.getUserEmail(),  "The Coup Conductor: The game is over. WINNER: " + game.getWinner().getUserEmail());
                }
                for(User spectator : existingLobby.getSpectators()){
                    broadcastToSpecificUser(spectator.getUserEmail(),  "The Coup Conductor: The game is over. WINNER: " + game.getWinner().getUserEmail());
                }


                /**
                 * Stat Code
                 */
                User winner = userRepository.findByUserEmail(game.getWinner().getUserEmail()); // Find Winner
                Stat winnerStat = winner.getStat(); // Find their stats
                winnerStat.incrementGameWon(); // increment game won
                winnerStat.setAverages();
                statRepository.save(winnerStat); // Save Repo
                for(Player losers : game.getPlayerArrayList()){ // Set losers to lost
                    if(!losers.getUserEmail().equals(game.getWinner().getUserEmail())){
                        User loser = userRepository.findByUserEmail(losers.getUserEmail());
                        Stat loserStat = loser.getStat();
                        loserStat.incrementGameLost();
                        loserStat.setAverages();
                        loserStat.findMostUsedMove();
                        loserStat.findMostUsedMove();
                        statRepository.save(loserStat);
                    }
                }



                /**
                 * Stat Code
                 */

                for(Player player : game.getPlayerArrayList()){
                    broadcastToSpecificUser(player.getUserEmail(),  "over");
                }

                for(User spectator : existingLobby.getSpectators()){
                    broadcastToSpecificUser(spectator.getUserEmail(),  "over");
                }




            }else{
                for (Player player : game.getPlayerArrayList()) {
                    broadcastToSpecificUserGAMEJSON(player.getUserEmail(), game);
                }

                if(!existingLobby.isEmpty()){
                    for(User spectator : existingLobby.getSpectators()){
                        broadcastToSpecificUserGAMEJSON(spectator.getUserEmail(),  game);
                    }
                }

                statRepository.save(stateForUser); //Saving stats
            }
        }
    }

    public boolean checkPass(Game game){
        boolean allPlayersPassed = false;
        for(Player player : game.getPlayerArrayList()){
//            if(!player.getUserEmail().equals(game.getCurrentPlayer().getUserEmail())){
            if((player.getPlayerState().equals("pass") || player.getPlayerState().equals("wait")) || player.getPlayerState().equals("dead")){ // If all players are waiting
                allPlayersPassed = true;
            }else{
                return false;
            }
//            }

        }
        return allPlayersPassed;
    }

    public boolean checkGameEnd(Game game) {
        int aliveCount = 0;

        for (Player player : game.getPlayerArrayList()) {
            if (player.getLives() > 0) {
                aliveCount++;
                game.setWinner(player);
            }
        }

        if (aliveCount == 1) {
            // Only one player is alive, game should end
            return true;
        } else if (aliveCount == 0) {
            // No players are alive, handle this case if needed
            return true;
        }
        // More than one player is alive, game continues
        return false;
    }





//    @OnClose
//    public void onClose(Session session, @PathParam("lobbyId") int lobbyId, @PathParam("username")String username) {
//        Lobby lobby = lobbyRepository.findById(userRepository.findByUserEmail(username).getLobby().getId()); //Find Lobby here it shows 0
//        User user = userRepository.findByUserEmail(username); //Find UserName
//        lobby.removeUser(user); // Remove user from lobby
//        lobbyRepository.save(lobby); // Save Lobby
//        sessionUserMap.remove(session); // Remove Users Session
//        userSessionMap.remove(user);
//        for(User userList : lobby.getUserArraylist()) {
//            broadcastToSpecificUser(userList.getUserEmail(), username + " Has left");
//        }
//        ////NEW CODE NEW CODE
//        if(lobby.isEmpty()){
////          sessionLobbyMap.remove(session);
//            lobbyRepository.delete(lobby);
//        }
//        ////NEW CODE NEW CODE
//    }


    @OnClose
    public void onClose(Session session, @PathParam("lobbyId") int lobbyId, @PathParam("username")String username) {
        Lobby lobby = lobbyRepository.findById(userRepository.findByUserEmail(username).getLobby().getId()); //Find Lobby here it shows 0
        User user = userRepository.findByUserEmail(username); //Find UserName

        // Check if the user is a spectator


        if(!lobby.getSpectators().isEmpty()){
            Spectator spectator = spectatorRepository.findByUser(user);
            if (spectator != null && spectator.getActive()) {
                spectator.leaveLobby();
                spectatorRepository.save(spectator); // Update the spectator in the database
                broadcastToAllInLobby(lobby, username + " has left the lobby as a spectator.");
            }
        } else {
            // User is a regular participant
            lobby.removeUser(user); // Remove user from lobby
            lobbyRepository.save(lobby); // Save Lobby
            for(User userList : lobby.getUserArraylist()) {
                broadcastToSpecificUser(userList.getUserEmail(), username + " has left the lobby.");
            }
        }

        sessionUserMap.remove(session); // Remove Users Session
        userSessionMap.remove(user);

        ////NEW CODE NEW CODE
        if(lobby.isEmpty() && lobby.hasGameStarted()){
            lobbyRepository.delete(lobby);
            for(Player users : game.getPlayerArrayList()){ // Set losers to lost
                User usersStat = userRepository.findByUserEmail(users.getUserEmail());
                Stat usersStatStat = usersStat.getStat();
                usersStatStat.incrementGameLost();
                usersStatStat.setAverages();
                usersStatStat.findMostUsedMove();
                usersStatStat.findMostUsedMove();
                statRepository.save(usersStatStat);
            }
        }else{
            lobbyRepository.delete(lobby);
        }
        ////NEW CODE NEW CODE
    }


    private void broadcastToSpecificUserGAMEJSON(String userEmail, Game data) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("Game", data);

        try {
            String message = mapper.writeValueAsString(wrapper);
            Session userSession = getSessionByEmail(userEmail);
            if (userSession != null) {
                userSession.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            logger.error("Error broadcasting to specific user: " + e.getMessage(), e);
        }
    }


    private Session getSessionByEmail(String userEmail) {
        for (Map.Entry<Session, User> entry : sessionUserMap.entrySet()) { //Looks through userSessionMap
            if (entry.getValue().getUserEmail().equals(userEmail)) { //Finds Match with userEmail
                return entry.getKey(); //Returns Session
            }
        }
        return null;
    }



    private void broadcastToSpecificUser(String userEmail, String message) {
        // Iterate over the sessionUserMap to find the session associated with the userEmail
        sessionUserMap.forEach((session, user) -> {
            // Check if the user matches the userEmail we want to send a message to
            if (user.getUserEmail().equals(userEmail)) {
                try {
                    // Use the session to send the message
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    logger.error("Error broadcasting to specific user: " + e.getMessage(), e);
                }
            }
        });
    }










}