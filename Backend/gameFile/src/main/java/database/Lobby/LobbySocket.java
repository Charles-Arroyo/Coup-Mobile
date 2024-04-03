package database.Lobby;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.util.JSONPObject;
import database.Chat.MessageRepository;
import database.Game.Game;
import database.Game.Player;
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

    private static Game game;

    @Autowired
    public void setUserRepository(UserRepository repo) {
        userRepository = repo;  // we are setting the static variable

    }
    @Autowired
    public void setLobbyRepository(LobbyRepository repo) {
        lobbyRepository = repo;  // we are setting the static variable
    }
    private final Logger logger = LoggerFactory.getLogger(LobbySocket.class);

    private static Map<Session, Lobby> sessionLobbyMap = new Hashtable<>(); // Associate a Sessions with Lobbys to find lobbies and to terminate lobbies
    private static Map<Session,User> sessionUserMap = new HashMap<>(); // Associate a Session with Users to find users to remove and add them

    private static Map < User, Session > userSessionMap = new Hashtable < > ();


    @OnOpen
    public void onOpen(Session session, @PathParam("lobbyId") int lobbyId, @PathParam("username") String username) throws IOException {
        User user = userRepository.findByUserEmail(username); // find user
        sessionUserMap.put(session, user);
        userSessionMap.put(user,session);
        if(lobbyId == 0){ //USER WANTS TO CREATE LOBBY
            Lobby newLobby = new Lobby(); // Create new Lobby
            newLobby.addUser(user); // add user
//          sessionLobbyMap.put(session,newLobby); // Save Session with lobby
            lobbyRepository.save(newLobby); // Save lobby for admin use
            for(User userList : newLobby.getUserArraylist()) {
                broadcastToSpecificUser(userList.getUserEmail(), "Users in lobby: " + newLobby.getUsers());
                broadcastToSpecificUser(userList.getUserEmail(), "The ID is: " + newLobby.getId());
            }

//            broadcastToSpecificLobby(newLobby.getId(), "The ID is: " + newLobby.getId());
//            broadcastToSpecificLobby(newLobby.getId(),user.getUserEmail() + " Joined the lobby");
//            broadcastToSpecificUser(user.getUserEmail(),"Users: " + newLobby.getUsers());
          //  broadcastToSpecificLobby(newLobby.getId(),"Users: " +newLobby.getUsers());
        }else{ //USER WANTS TO JOIN LOBBY
            Lobby existingLobby = lobbyRepository.findById(lobbyId); // Find Lobby By ID
            if(!existingLobby.isFull()) {
                existingLobby.addUser(user); // Add User to this Lobby
                lobbyRepository.save(existingLobby); // Save Lobby
                for(User userList : existingLobby.getUserArraylist()) {
                    broadcastToSpecificUser(userList.getUserEmail(), username + " Joined the lobby");
                }
                //broadcastToSpecificLobby(existingLobby.getId(),user.getUserEmail() + " Joined the lobby");
//                sessionLobbyMap.put(session, existingLobby);
//                broadcastToSpecificUser(user.getUserEmail(),existingLobby.getUsers());
                if(existingLobby.isFull()){ //START GAME
                    /**
                     * INIT GAME
                     */
                   // broadcastToSpecificLobby(existingLobby.getId(),"lobby is full");

                    for(User userList : existingLobby.getUserArraylist()) {
                        broadcastToSpecificUser(userList.getUserEmail(), "Lobby is full");
                    }

                    List<Player> players = new ArrayList<>(4); // Create an Array list of Players

                    game = new Game(players); //Pass in Deck and Array List
                    game.initGame(existingLobby.getUserArraylist().get(0).getUserEmail(),
                            existingLobby.getUserArraylist().get(1).getUserEmail(),
                            existingLobby.getUserArraylist().get(2).getUserEmail(),
                            existingLobby.getUserArraylist().get(3).getUserEmail()); // Sends four players, see init game method


                    //For loop for normal String
//                    for(User printGameState : existingLobby.getUserArraylist()) {
//                        Player player = game.getPlayer(printGameState.getUserEmail());
//                        broadcastToSpecificUser(printGameState.getUserEmail(), game.getPlayerStats(player));
//                    }

//                    for(User printGameState : existingLobby.getUserArraylist()) { //Itterate through Lobby
//                        Player player = game.getPlayer(printGameState.getUserEmail()); // Find Player
//                        if (player != null) {
//                            broadcastToSpecificUserJSON(printGameState.getUserEmail(), player); //broadcast to User the Their Player JSON Object
//                        }
//                    }


                    /**
                     * INIT GAME
                     */
                }

            }else{
                //broadcastToSpecificLobby(existingLobby.getId(),"lobby is full");
                for(User userList : existingLobby.getUserArraylist()) {
                    broadcastToSpecificUser(userList.getUserEmail(), "Lobby is full");
                }
            }

        }
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
        JSONObject jsonpObject = new JSONObject(message); // Create JSON object
        String email = jsonpObject.getString("playerEmail"); // Get Player Email
        String state = jsonpObject.getString("move"); // Actions
        String targetPlayer = jsonpObject.getString("targetPlayer"); // Get opponentEmail
        //Update move for current player
        // all other players get contest
        Player p = game.getPlayer(email); //Find player by their email
        if(state.equals("ready")){  //If the player message says ready to listen, give them the game
            broadcastToSpecificUserGAMEJSON(p.getUserEmail(),game);
        } else{
            p.action(state,game.getPlayer(targetPlayer)); // Does the player action.
        }

        if(p.getUserEmail().equals(game.getCurrentPlayer().getUserEmail())){ // next turn
            game.nextTurn();
        }

        if(!state.equals("ready") && game.getCurrentPlayer().getUserEmail().equals(p.getUserEmail())) {
            broadcastToSpecificUserGAMEJSON(p.getUserEmail(), game); //Broadcast once

        }


    }

    @OnClose
    public void onClose(Session session, @PathParam("lobbyId") int lobbyId, @PathParam("username")String username) {
        /**
         * ToDo: Make it so a user can join back to a lobby after they disconnect/create new after disconnect
         */

        Lobby lobby = lobbyRepository.findById(userRepository.findByUserEmail(username).getLobby().getId()); //Find Lobby here it shows 0
        User user = userRepository.findByUserEmail(username); //Find UserName
        lobby.removeUser(user); // Remove user from lobby
        lobbyRepository.save(lobby); // Save Lobby
        sessionUserMap.remove(session); // Remove Users Session
        userSessionMap.remove(user);
      //  broadcastToSpecificLobby(lobbyId,"THIS USER HAS LEFT:" + user.getUserEmail());
//        for(User user1 : lobby.getUserArraylist()) {
//            broadcastToSpecificLobby(lobby.getId(),user1.getUserEmail());
//        }

        for(User userList : lobby.getUserArraylist()) {
            broadcastToSpecificUser(userList.getUserEmail(), username + " Has left");
        }

        ////NEW CODE NEW CODE
        if(lobby.isEmpty()){
//          sessionLobbyMap.remove(session);
            lobbyRepository.delete(lobby);
        }
        ////NEW CODE NEW CODE
    }




    private void broadcastToAllUsers(String message) { // This method broadcasts to all user
        sessionUserMap.forEach((session, user) -> {
            try {
                session.getBasicRemote().sendText(message);
            }
            catch (IOException e) {
                logger.info("Exception: " + e.getMessage().toString());
                e.printStackTrace();
            }
        });
    }

    private void broadcastToSpecificLobby(int lobbyId, String message) {
        // Iterate over the sessionLobbyMap to find sessions associated with the specific lobbyId
        sessionLobbyMap.forEach((session, lobby) -> {
            // Check if the lobby matches the lobbyId we want to broadcast to
            if (lobby.getId() == lobbyId) {
                try {
                    // Use the session to send the message
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    logger.error("Error broadcasting to specific lobby: " + e.getMessage(), e);
                }
            }
        });
    }


    private void broadcastToSpecificLobbyGAMEJSON(int lobbyId, Game data) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("Game", data);

        String message;
        try {
            // Serialize the Game object to JSON
            message = mapper.writeValueAsString(wrapper);
        } catch (IOException e) {
            logger.error("Error serializing Game data: " + e.getMessage(), e);
            return; // Stop execution if serialization fails
        }

        // Iterate over the sessionLobbyMap to find sessions associated with the specific lobbyId
        sessionLobbyMap.forEach((session, lobby) -> {
            // Check if the lobby matches the lobbyId we want to broadcast to
            if (lobby.getId() == lobbyId) {
                try {
                    // Use the session to send the JSON message
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    logger.error("Error broadcasting to specific lobby: " + e.getMessage(), e);
                }
            }
        });
    }

//    private void broadcastToSpecificUserJSON(String userEmail, Object data) {
//        ObjectMapper mapper = new ObjectMapper();
//
//        try {
//            String message = mapper.writeValueAsString(data);
//            Session userSession = getSessionByEmail(userEmail);
//            if (userSession != null) {
//                userSession.getBasicRemote().sendText(message);
//            }
//        } catch (IOException e) {
//            logger.error("Error broadcasting to specific user: " + e.getMessage(), e);
//        }
//    }


    private void broadcastToSpecificUserJSON(String userEmail, Player data) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("player", data);

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
