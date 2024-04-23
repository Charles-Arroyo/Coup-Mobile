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
    public void setSpectatorRepository(SpectatorRepository repo) {spectatorRepository = repo;}

    private final Logger logger = LoggerFactory.getLogger(LobbySocket.class);
    private static Map<Session,User> sessionLobbyMap = new HashMap<>(); // Associate a Session with Users to find users to remove and add them

    private static Map<Session,User> sessionUserMap = new HashMap<>(); // Associate a Session with Users to find users to remove and add them

    private static Map < User, Session > userSessionMap = new Hashtable < > ();




//        @OnOpen
//        public void onOpen(Session session, @PathParam("lobbyId") int lobbyId, @PathParam("username") String username) throws IOException {
//            User user = userRepository.findByUserEmail(username); // find user
//            sessionUserMap.put(session, user);
//            userSessionMap.put(user,session);
//
//            if(lobbyId == 0){ //USER WANTS TO CREATE LOBBY
//                Lobby newLobby = new Lobby(); // Create new Lobby
//                newLobby.addUser(user); // add user
//                lobbyRepository.save(newLobby); // Save lobby for admin use
//                for(User userList : newLobby.getUserArraylist()) {
//                    broadcastToSpecificUser(userList.getUserEmail(), "Users in lobby: " + newLobby.getUsers());
//                    broadcastToSpecificUser(userList.getUserEmail(), "The ID is: " + newLobby.getId());
//                }
//            }else {
//                //USER WANTS TO JOIN LOBBY
//                Lobby existingLobby = lobbyRepository.findById(lobbyId); // Find Lobby By ID
//                if(!existingLobby.isFull()) {
//                    existingLobby.addUser(user); // Add User to this Lobby
//                    lobbyRepository.save(existingLobby); // Save Lobby
//                    for(User userList : existingLobby.getUserArraylist()) {
//                        broadcastToSpecificUser(userList.getUserEmail(), username + ": Joined the lobby");
//                    }
//
//                    for(User userList : existingLobby.getUserArraylist()) {
//                        broadcastToSpecificUser(userList.getUserEmail(), "Users in lobby: " + existingLobby.getUsers());
//                    }
//
//                    if(existingLobby.isFull()){ //START GAME
//                        /**
//                         * INIT GAME
//                         */
//                        existingLobby.setGameStarted(true);
//                        for(User userList : existingLobby.getUserArraylist()) {
//                            broadcastToSpecificUser(userList.getUserEmail(), "Lobby is full");
//                        }
//                        if(existingLobby.hasGameStarted()) {
//                            List<Player> players = new ArrayList<>(4); // Create an Array list of Players
//
//                            game = new Game(players); //Pass in Deck and Array List
//                            game.initGame(existingLobby.getUserArraylist().get(0).getUserEmail(),
//                                    existingLobby.getUserArraylist().get(1).getUserEmail(),
//                                    existingLobby.getUserArraylist().get(2).getUserEmail(),
//                                    existingLobby.getUserArraylist().get(3).getUserEmail()); // Sends four players, see init game method
//
//                            /**
//                             * INIT GAME
//                             */
//                        }
//                    }
//
//                }else if(existingLobby.hasGameStarted()){
//                    for(User userList : existingLobby.getUserArraylist()) {
//                        broadcastToSpecificUser(userList.getUserEmail(), "Lobby is full");
//                    }
//                }
//
//            }
//        }

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
        } else { // USER WANTS TO JOIN LOBBY
            Lobby existingLobby = lobbyRepository.findById(lobbyId); // find lobby by ID
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
                    startGame(existingLobby); // function to handle game initialization
                }
            } else if (existingLobby.getUserArraylist().size() >= 4 || existingLobby.hasGameStarted()) { // Lobby is full or game has started
                Spectator spectator = new Spectator(user);
                spectator.joinLobby(existingLobby);
                spectatorRepository.save(spectator); // Save the spectator to the database
                broadcastToAllInLobby(existingLobby, username + " has joined the lobby as a spectator.");
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
        /** Switch statements could be good
         * 1. Listen for Front end to Start Game
         * 2. Broadcast to Front End the Game, so it init the game on UI
         * 3. Listen for current Player's move
         * 4. Broadcast to non-current player's current Players move
         * 5. Listen to All non-current player's move/Counter
         *     if(ANY player sends Bluff)
         *          Reveal Card of current Player
         *          Remove Influence of currentPlayer, or bluffer.
         *          End turn
         *          Send front end new UI
         *      else if(ANY Player Blocks)
         *        1. Broadcast to current Player blocking players Block
         *        2. Listen for Current Players move
         *              if(Call Bluff)
         *                   Revel blocking players card
         *                   Remove Influence of currentPlayer, or blocker.
         *                   End Turn
         *              else if(Block)
         *                  Listen for Blockers Counter
         *                     if(call bluff)
         *                          Reveal Card of current Player
         *                          Remove Influence of currentPlayer, or bluffer.
         *                          End turn
         *                          Send front end new UI
         *                      else
         *                      Current Player perform move
         *                      End turn
         *                      Send front end new UI
         *               else if(no bluff)
         *               Deny action for current player
         *               end turn.
         *               Send front end new UI
         *      else if(ALL players/Player attacked do nothing)
         *         Current Player perform move
         *         End turn
         *         Send front end new UI
         */

        /**
         * todo: Need contest logic in game
         */
        //todo need to listen after I sent to FE contest
        //GAME LOGIC TESTING
        JSONObject jsonpObject = new JSONObject(message); // Create JSON object
        String email = jsonpObject.getString("playerEmail"); // Get Player Email
        String state = jsonpObject.getString("move"); // Actions/Game State, Can be one of the following: Ready, Bluffing, Action, waiting
        String targetPlayer = jsonpObject.getString("targetPlayer"); // Get opponentEmail
        Player p = game.getPlayer(email); //Find player by their email
        String currentMove = state;


        if(state.startsWith("@")){
            for(Player player : game.getPlayerArrayList()){
                broadcastToSpecificUser(player.getUserEmail(),p.getUserEmail() + ": " + state.substring(1));
            }
        }
        if (state.equals("ready")) {  //If the player message says ready to listen, give them the game
            broadcastToSpecificUserGAMEJSON(p.getUserEmail(), game); // Broadcast to each player indivual so front end can unqiuely set up UI
        } else if (state.startsWith("*") && !state.contains("Coup") && !state.contains("Income")) { // Set Action for Players
            currentMove = state.substring(1); // save move for current player
            p.setCurrentMove(currentMove);
            //He will send me the action, it is my job to change all the other players to contest
            p.setPlayerState("wait"); //Send action player to wait
            for(Player player : game.getPlayerArrayList()){
                if (!player.equals(game.getCurrentPlayer())) {
                    player.setPlayerState("contest"); //set other players to contest
                }
            }
            //Message to Chat
//            for(Player player : game.getPlayerArrayList()){
//                broadcastToSpecificUser(player.getUserEmail(),p.getUserEmail() + " wants to: " + state.substring(1)); //Charles want to: income
//            }


        } else if (state.equals("Bluff")) {
            //If any player called bluff, go into bluffing
            //Set each player to waiting.
            if(game.getBlocker().getUserEmail().equals("Null")){ // Regular bluff for player
                game.associate(game.getCurrentPlayer().getCurrentMove());
                if(game.getCurrentPlayer().revealCard(game.associate(game.getCurrentPlayer().getCurrentMove()),game.getCurrentPlayer()).equals(game.getCurrentPlayer().getUserEmail() + " Was a Liar")){ //if player is a liar, remove their card
                  Card playerCard = new Card(game.getCurrentPlayer().loseInfluence(game.getCurrentPlayer()));
                  game.nextTurn();
                }else{
                    Card playerCard = new Card(p.loseInfluence(p)); // The bluffer loses Influence
                    game.getCurrentPlayer().removeCard(game.associate(game.getCurrentPlayer().getCurrentMove()),game.getCurrentPlayer()); // The Player their card
                    game.getDeckDeck().addCardToBottomOfDeck(playerCard);
                    String drawCard = game.getDeckDeck().drawCard();  //Draw Card from deck
                    game.getCurrentPlayer().gainInfluence(drawCard,game.getCurrentPlayer());
                    game.nextTurn();
                }
            }else{ //Special Bluff that reveals Blockers card.
                // Find Blocking player, if the blocking player has a Card that can block the current players move,
                // Current player loses card, if not, blocking player loses card.
                Player blocker = game.getBlocker();
                if(blocker.revealCard(blocker.getCurrentMove(), blocker).equals(blocker.getUserEmail() + " Was a liar")){ //If blocker was lying about his block
                    blocker.loseInfluence(blocker);
                    Card blockerCard = new Card(blocker.loseInfluence(blocker)); // Removes Card
                    game.getDeckDeck().addCardToBottomOfDeck(blockerCard);
                    game.nextTurn();
                }else{ //If the blocker was not lying, bluff caller loses card and blocker gets a new card
                    Card playerCard = new Card(p.loseInfluence(p)); // Save card for deck, and remove card
                    Card blockerCard = new Card(blocker.removeCard(blocker.getCurrentMove(),blocker)); // Removes Card, duke is
                    game.getDeckDeck().addCardToBottomOfDeck(playerCard);
                    game.getDeckDeck().addCardToBottomOfDeck(blockerCard);
                    String drawCard = game.getDeckDeck().drawCard();  //Draw Card from deck
                    blocker.gainInfluence(drawCard,blocker);
                    game.nextTurn();
                }

            }
        } else if(state.contains("Block")) {
            if(state.equals("Block")){
                game.setBlocker(p);
                game.getBlocker().setCurrentMove(game.associateBlock(game.getCurrentPlayer().getCurrentMove()));
            }else{
                game.setBlocker(p); // Save Blocker
                game.getBlocker().setCurrentMove(state.substring(6)); //Saves Move ex (Block Duke)
            }

            // Now we need to set current Player to Contest, and All other players to wait
            for(Player player : game.getPlayerArrayList()){
                if (!player.equals(game.getBlocker())) {
                    player.setPlayerState("Contest"); //set other players to contest
                }else{
                    player.setPlayerState("wait"); // Set Blocker to Wait
                }
            }
            // Now we See if player wants to allow or not.
        }
        //AutoMatic Moves
        else if(state.contains("Coup")){
            //Messages for Coup
//            for(Player player : game.getPlayerArrayList()){
//                broadcastToSpecificUser(player.getUserEmail(),p.getUserEmail() + "took: " + state.substring(1) + " on: " + targetPlayer); //Charles want to: income
//            }
            p.action("Coup",game.getPlayer(targetPlayer));
        }else if(state.equals("*Income")){
            //Messages for Income
            for(Player player : game.getPlayerArrayList()){
                broadcastToSpecificUser(player.getUserEmail(),p.getUserEmail() + " took: " + state.substring(1)); //Charles took: income
            }
            currentMove = state.substring(1); // save move for current player
            p.action(currentMove,game.getPlayer(targetPlayer)); // Does the player action for each player
            game.nextTurn();
        }


        if(!state.equals("ready") && !state.contains("@")) { //Send game to everyone after a move.
            for (Player player : game.getPlayerArrayList()) {
                broadcastToSpecificUserGAMEJSON(player.getUserEmail(), game);
            }
        }

        if(!state.equals("ready") && !state.contains("@") && (!state.contains("Income") && (!state.contains("Coup")))){
            if(checkPass(game)){ //if all players or a player passed, do next turn
                if(game.getBlocker().getUserEmail().equals("Null")){ // If all players passed, and blocking happened, Move can happen
                    if(currentMove.contains("Exchange")){
                        //Draw Card
                        //Tell FE what card it is
                        //Wait for choice
                        //
                    }else{
                        p.action(currentMove,game.getPlayer(targetPlayer)); // Does the player action for each player
                        game.nextTurn();
                    }

                }else{ // If all players passed, and blocking did happen, move cannot happen.
                    game.nextTurn();
                    game.getBlocker().setUserEmail("Null");
                }
                for (Player player : game.getPlayerArrayList()) {
                    broadcastToSpecificUserGAMEJSON(player.getUserEmail(), game);
                }
            }
        }

    }
    
    public boolean checkPass(Game game){
        boolean allPlayersPassed = false;
        for(Player player : game.getPlayerArrayList()){
            if((player.getCurrentMove().equals("pass") || player.getCurrentMove().equals("wait")) && !player.getUserEmail().equals(game.getCurrentPlayer().getUserEmail())){
                allPlayersPassed = true;
            }else{
               return false;
            }
        }
        return allPlayersPassed;
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
        Spectator spectator = spectatorRepository.findByUser(user);
        if (spectator != null && spectator.getActive()) {
            spectator.leaveLobby();
            spectatorRepository.save(spectator); // Update the spectator in the database
            broadcastToAllInLobby(lobby, username + " has left the lobby as a spectator.");
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
        if(lobby.isEmpty()){
            lobbyRepository.delete(lobby);
        }
        ////NEW CODE NEW CODE
    }




//    private void broadcastToAllUsers(String message) { // This method broadcasts to all user
//        sessionUserMap.forEach((session, user) -> {
//            try {
//                session.getBasicRemote().sendText(message);
//            }
//            catch (IOException e) {
//                logger.info("Exception: " + e.getMessage().toString());
//                e.printStackTrace();
//            }
//        });
//    }




//    private void broadcastToSpecificUserJSON(String userEmail, Player data) {
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object> wrapper = new HashMap<>();
//        wrapper.put("player", data);
//
//        try {
//            String message = mapper.writeValueAsString(wrapper);
//            Session userSession = getSessionByEmail(userEmail);
//            if (userSession != null) {
//                userSession.getBasicRemote().sendText(message);
//            }
//        } catch (IOException e) {
//            logger.error("Error broadcasting to specific user: " + e.getMessage(), e);
//        }
//    }



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


//    private void broadcastToSpecificLobby(int lobbyId, String message) {
//        // Iterate over the sessionLobbyMap to find sessions associated with the specific lobbyId
//        sessionLobbyMap.forEach((session, lobby) -> {
//            // Check if the lobby matches the lobbyId we want to broadcast to
//            if (lobby.getId() == lobbyId) {
//                try {
//                    // Use the session to send the message
//                    session.getBasicRemote().sendText(message);
//                } catch (IOException e) {
//                    logger.error("Error broadcasting to specific lobby: " + e.getMessage(), e);
//                }
//            }
//        });
//    }

//    private void broadcastToSpecificLobbyGAMEJSON(int lobbyId, Game data) {
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object> wrapper = new HashMap<>();
//        wrapper.put("Game", data);
//
//        String message;
//        try {
//            // Serialize the Game object to JSON
//            message = mapper.writeValueAsString(wrapper);
//        } catch (IOException e) {
//            logger.error("Error serializing Game data: " + e.getMessage(), e);
//            return; // Stop execution if serialization fails
//        }
//
//        // Iterate over the sessionLobbyMap to find sessions associated with the specific lobbyId
//        sessionLobbyMap.forEach((session, lobby) -> {
//            // Check if the lobby matches the lobbyId we want to broadcast to
//            if (lobby.getId() == lobbyId) {
//                try {
//                    // Use the session to send the JSON message
//                    session.getBasicRemote().sendText(message);
//                } catch (IOException e) {
//                    logger.error("Error broadcasting to specific lobby: " + e.getMessage(), e);
//                }
//            }
//        });
//    }





}