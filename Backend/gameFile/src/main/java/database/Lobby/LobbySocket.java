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

    private static LobbyRepository lobbyRepository; //Lobby Repo


    private static UserRepository userRepository; // User Repo

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


    @OnOpen
    public void onOpen(Session session, @PathParam("lobbyId") int lobbyId, @PathParam("username") String username) throws IOException {
        User user = userRepository.findByUserEmail(username); // find user
        sessionUserMap.put(session, user);
        if(lobbyId == 0){ //USER WANTS TO CREATE LOBBY
            Lobby newLobby = new Lobby(); // Create new Lobby
            newLobby.addUser(user); // add user
            sessionLobbyMap.put(session,newLobby); // Save Session with lobby
            lobbyRepository.save(newLobby); // Save lobby for admin use
            broadcastUsers("The ID is: " + newLobby.getId());
            broadcastUsers(user.getUserEmail() + "Joined the lobby");

            for(User user1 : newLobby.getUserArraylist()) {
                broadcastUsers("Users in lobby:" + user1.getUserEmail());
            }

        }else{ //USER WANTS TO JOIN LOBBY
            Lobby existingLobby = lobbyRepository.findById(lobbyId); // Find Lobby By ID
            if(!existingLobby.isFull()) {
                existingLobby.addUser(user); // Add User to this Lobby
                lobbyRepository.save(existingLobby); // Save Lobby
                broadcastUsers(user.getUserEmail() + "Joined the lobby");
                for (User user1 : existingLobby.getUserArraylist()) {
                    broadcastUsers(user1.getUserEmail());
                }
                if(existingLobby.isFull()){
                    broadcastUsers("lobby is full");
                }


                sessionLobbyMap.put(session, existingLobby);

            }else{
                broadcastUsers("lobby is full");
            }

        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("lobbyId") int lobbyId, @PathParam("username")String username) {
        /**
         * ToDo: Make it so it closes the session for user, but not lobby
         */
        Lobby lobby = lobbyRepository.findById(lobbyId); //Find Lobby
        User user = userRepository.findByUserEmail(username); //Find UserName
        lobby.removeUser(user); // Remove user from lobby
        lobbyRepository.save(lobby); // Save Lobby
        sessionUserMap.remove(session);
        broadcastUsers("THIS USER HAS LEFT:" + user.getUserEmail());
        for(User user1 : lobby.getUserArraylist()) {
            broadcastUsers(user1.getUserEmail());
        }

    }

    private void broadcast(String message) {
        sessionLobbyMap.forEach((session, lobby) -> {
            try {
                session.getBasicRemote().sendText(message);
            }
            catch (IOException e) {
                logger.info("Exception: " + e.getMessage().toString());
                e.printStackTrace();
            }
        });
    }


    private void broadcastUsers(String message) {
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

}
