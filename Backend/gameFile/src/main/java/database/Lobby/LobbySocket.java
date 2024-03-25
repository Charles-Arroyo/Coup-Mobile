package database.Lobby;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import jakarta.websocket.OnOpen;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller      // this is needed for this to be an endpoint to springboot
@ServerEndpoint(value = "/lobby/{lobbyId}/{username}")  // this is Websocket url
public class LobbySocket {
    private static LobbyRepository lobbyRepository;
    private final Logger logger = LoggerFactory.getLogger(LobbySocket.class);

    private static Map<Session, Integer> sessionLobbyMap = new Hashtable<>();
    private static Map<Session, String> sessionUsernamemap = new Hashtable<>();
    private static Map<String, Session> lobbySessionMap = new Hashtable<>();
    @Autowired
    public void setLobbyRepository(LobbyRepository repo) {
        lobbyRepository = repo;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("lobbyId") int lobbyId, @PathParam("username") String username) throws IOException {
        logger.info("User: {} connected to the WebSocket", username);
        broadcast("test");
        sessionLobbyMap.put(session, lobbyId);
        sessionUsernamemap.put(session, username);
        broadcast(username + " has joined the lobby");

        if (lobbyId == 0) {  // If no lobbyId was provided, this user wants to create a new lobby.
            Lobby newLobby = new Lobby();
           boolean notFull = newLobby.addUser(username); // Add the user to the lobby.
            if(notFull){
            lobbyRepository.save(newLobby); // Save the new lobby.broadcast
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
                if(newLobby.getUser3() != null){
                    users.append(newLobby.getUser3());
                }
                broadcast(users.toString());




//            lobbyId = newLobby.getId().toString(); // Get the newly assigned lobby ID.
                }else{
                System.out.println("FUll");
            }
        } else {
            // If a lobbyId was provided, the user wants to join an existing lobby.
            Lobby existingLobby = lobbyRepository.findById(lobbyId);
            if (existingLobby != null) {
              boolean notFull =  existingLobby.addUser(username); // Add the user to the lobby.
                if(notFull) {
                    lobbyRepository.save(existingLobby); // Update the existing lobby.
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
                    if(existingLobby.getUser3() != null){
                        users.append(" " + existingLobby.getUser3());
                    }
                    broadcast(users.toString());
                }else{
                    System.out.println("FUll");
                }
            } else {
                // If the lobby doesn't exist or is full
                session.getBasicRemote().sendText("Lobby is full or does not exist");
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
        String username = sessionUsernamemap.get(session);
        sessionLobbyMap.remove(session);
        lobbySessionMap.remove(username);

        // broadcase that the user disconnected
        String message = username + " left the lobby";
        broadcast(message);
    }

    private void broadcast(String message) {
        sessionLobbyMap.forEach((session, username) -> {
            try {
                session.getBasicRemote().sendText(message);
            }
            catch (IOException e) {
                logger.info("Exception: " + e.getMessage().toString());
                e.printStackTrace();
            }
        });

    }



    }
