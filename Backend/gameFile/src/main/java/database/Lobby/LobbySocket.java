package database.Lobby;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import database.Users.UserRepository;
import database.Websocketconfig.WebsocketConfig;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import jakarta.websocket.OnOpen;

@Controller      // this is needed for this to be an endpoint to springboot
@ServerEndpoint(value = "/lobby/{lobbyId}/{username}")  // this is Websocket url
public class LobbySocket {
    private static LobbyRepository lobbyRepository;
    private final Logger logger = LoggerFactory.getLogger(LobbySocket.class);

    private static Map<Session, Integer> sessionLobbyMap = new Hashtable<>();
    private static Map<String, Session> lobbySessionMap = new Hashtable<>();
    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    @Autowired
    public void setLobbyRepository(LobbyRepository repo) {
        lobbyRepository = repo;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("lobbyId") int lobbyId, @PathParam("username") String username) throws IOException {

        UserRepository userRepository = WebsocketConfig.getUserRepository();


        if (lobbyId == 0) {  // If no lobbyId was provided, this user wants to create a new lobby.
            Lobby newLobby = new Lobby();
           boolean notFull = newLobby.addUser(userRepository.findByUserEmail(username)); // Add the user to the lobby, returns false if full
            if(notFull){
                logger.info("User: {} connected to the WebSocket", username);
                sessionUsernameMap.put(session, username);
                usernameSessionMap.put(username,session);
                broadcast(username + " has joined the lobby");
                lobbyRepository.save(newLobby); // Save the new lobby
                broadcast("Is the lobby full?: " + newLobby.isFull());
                broadcast("The lobby ID is: " + newLobby.getId());
                StringBuilder users = new StringBuilder("Users in all lobbies: ");
                if(newLobby.getUser1() != null){
                    users.append(newLobby.getUser1());
                }
                if(newLobby.getUser2() != null){
                    users.append(newLobby.getUser2());
                }
                if(newLobby.getUser3() != null){
                    users.append(newLobby.getUser3());
                }
                if(newLobby.getUser4() != null){
                    users.append(newLobby.getUser4());
                }
                broadcast(users.toString());
                }else{
                broadcast("Lobby Full");
            }
        } else {
            // If a lobbyId was provided, the user wants to join an existing lobby.
            Lobby existingLobby = lobbyRepository.findById(lobbyId);
            if (existingLobby != null) {
              boolean notFull = existingLobby.addUser(userRepository.findByUserEmail(username)); // Add the user to the lobby.
                if(notFull) {
                    logger.info("User: {} connected to the WebSocket", username);
                    sessionUsernameMap.put(session, username);
                    usernameSessionMap.put(username,session);
                    broadcast(username + " has joined the lobby");
                    lobbyRepository.save(existingLobby); // Update the existing lobby.
                    if(existingLobby.isFull()){
                        broadcast("lobby is full");
                    }else{
                        broadcast("Not full");
                    }
                    // Send a message to all clients in the lobby to indicate that this user has joined.
                    broadcast(username + " has joined the lobby, The ID is " +lobbyId);
                    StringBuilder users = new StringBuilder("Users in lobbies: ");
                    if(existingLobby.getUser1() != null){
                        users.append(" " + existingLobby.getUser1());
                    }
                    if(existingLobby.getUser2() != null){
                        users.append(" " + existingLobby.getUser2());
                    }
                    if(existingLobby.getUser3() != null){
                        users.append(" " + existingLobby.getUser3());
                    }
                    if(existingLobby.getUser4() != null){
                        users.append(" " + existingLobby.getUser4());
                    }
                    broadcast(users.toString());
                }else{
                    broadcast("Lobby Full");
                }
            } else {
                // If the lobby doesn't exist or is full
                session.getBasicRemote().sendText("Lobby does not exist");
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "Lobby is full or does not exist"));
                return;
            }
        }

        // Store the user's session and associate it with the lobbyId.
        sessionLobbyMap.put(session, lobbyId);
    }


    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("Entered into Close");

        // remove the user connection information
        String username = sessionUsernameMap.get(session);
        sessionLobbyMap.remove(session);
        lobbySessionMap.remove(username);

        // broadcase that the user disconnected
        String message = username + " left the lobby";
        broadcast(message);
    }

    private void broadcast(String message) {
        sessionUsernameMap.forEach((session, userName) -> {
            try {
                session.getBasicRemote().sendText(message);
            }
            catch (IOException e) {
                logger.info("Exception: " + e.getMessage().toString());
                e.printStackTrace();
            }
        });


//        private void broadcast(String message) {
//            sessionLobbyMap.forEach((session, lobbyId) -> {
//                try {
//                    // Adjust broadcasting to include user information
//                    String username = sessionUsernameMap.get(session);
//                    message = username + ": " + message;
//                    session.getBasicRemote().sendText(message);
//                } catch (IOException e) {
//                    logger.error("Error broadcasting message: " + e.getMessage());
//                }
//            });

    }



    }
