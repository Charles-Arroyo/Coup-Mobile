package database.Signin;

import database.Users.User;
import database.Users.UserRepository;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@ServerEndpoint(value = "/signin/{username}")
public class SigninSocket {

    private static Map<String, Session> userSessions = new HashMap<>();

    @Autowired
    private UserRepository userRepository;


    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        userSessions.put(username, session);
        // Perform any necessary actions when a user connects
        System.out.println("User connected: " + username);
    }

    @OnMessage
    public void onMessage(Session session, String message, @PathParam("username") String username) {
        // Handle incoming messages from the user
        System.out.println("Received message from " + username + ": " + message);
        // Perform any necessary actions based on the received message
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        userSessions.remove(username);
        User closeUserSession = userRepository.findByUserEmail(username);
        if (closeUserSession != null) {
            closeUserSession.setActive(false);
            userRepository.save(closeUserSession);
        }
        // Perform any necessary actions when a user disconnects
        System.out.println("User disconnected: " + username);
    }

    @OnError
    public void onError(Session session, Throwable throwable, @PathParam("username") String username) {
        // Handle any errors that occur during the WebSocket session
        System.out.println("Error occurred for user " + username + ": " + throwable.getMessage());
    }

    public static void sendMessage(String username, String message) {
        Session session = userSessions.get(username);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}