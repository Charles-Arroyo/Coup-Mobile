package database.Lobby;

import java.io.IOException;
import java.util.*;

import database.Chat.MessageRepository;
import database.Users.User;
import database.Users.UserRepository;
import database.Websocketconfig.WebsocketConfig;
import jakarta.persistence.Lob;
import jakarta.transaction.Transactional;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import jakarta.websocket.OnOpen;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller      // this is needed for this to be an endpoint to springboot
@ServerEndpoint(value = "/lobby/{lobbyId}/{username}")  // this is WebSocket URL
public class LobbySocket {
    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository repo) {
        userRepository = repo;  // we are setting the static variable

    }

    @Autowired
    public void setLobbyRepository(LobbyRepository repo) {
        lobbyRepository = repo;  // we are setting the static variable

    }
    private final Logger logger = LoggerFactory.getLogger(LobbySocket.class);

    private static Map<Session, Lobby> sessionLobbyMap = new Hashtable<>(); // Associate of Session with Lobby
    private static Map<Session,User> sessionUserMap = new HashMap<>(); // Associate of Session with Users


//    @Autowired
//    public void setLobbyRepository(LobbyRepository repo) {
//        lobbyRepository = repo;
//    }
//
//    @Autowired
//    public void setUserRepository(UserRepository repo) {
//        userRepository = repo;
//    }

    @OnOpen
    public void onOpen(Session session, @PathParam("lobbyId") int lobbyId, @PathParam("username") String username) throws IOException {
        User user = WebsocketConfig.getUserRepository().findByUserEmail(username); // find user
        Lobby newLobby = new Lobby(); // Create new Lobby
        newLobby.addUser(user); // add user
        sessionLobbyMap.put(session,newLobby); // Save Session with lobby
        sessionUserMap.put(session, user);
        WebsocketConfig.getLobbyRepository().save(newLobby);

//        if(lobbyId == 0){
//            Lobby newLobby = new Lobby(); // Create new Lobby
//            newLobby.addUser(user); // add user
//            sessionLobbyMap.put(session,newLobby); // Save Session with lobby
//            sessionUserMap.put(session, user);
//            lobbyRepository.save(newLobby);
//
//        }else{
//            Lobby exisitingLobby = lobbyRepository.findById(lobbyId);
//            exisitingLobby.addUser(user); // add user
//            lobbyRepository.save(exisitingLobby);
//        }
    }

    @OnClose
    public void onClose(Session session) {
//        // Step 1: Find which lobby the session belongs to.
//        Lobby lobby = sessionLobbyMap.get(session);
//        if (lobby != null) {
//            // Step 2: Remove the user from the lobby.
//            User user = findUserBySession(session); // Assuming you implement this method.
//            if (user != null) {
//                lobby.removeUser(user); // Implement this method in Lobby to remove a user.
//                if (lobby.isEmpty()) { // Check if the lobby is now empty.
//                    // Step 3: Delete the lobby if empty.
//                    lobbyRepository.delete(lobby); // Adjust based on your actual repository method.
//                } else {
//                    lobbyRepository.save(lobby); // Save the updated lobby state.
//                }
//            }
//            // Clean up the session mappings.
//            sessionLobbyMap.remove(session);
//        }
//        // Additional cleanup or logging can be performed here.
    }

    private User findUserBySession(Session session) {
        return sessionUserMap.get(session);
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

    private void broadcast(String message, Integer lobbyId) {
        sessionLobbyMap.forEach((session, sessionLobbyId) -> {
            if (sessionLobbyId.equals(lobbyId)) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    logger.error("Error broadcasting message to session: " + session.getId(), e);
                }
            }
        });
    }

}
