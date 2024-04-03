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
@ServerEndpoint(value = "/signin/{userEmail}")
public class SigninSocket {



    private static Map<String, Session> userSessions = new HashMap<>();


//    @Autowired
    private static UserRepository userRepository;

    private static SigninRepository signinRepository;

    @Autowired
    public void setUserRepository(UserRepository repo) {
        userRepository = repo;  // we are setting the static variable
    }

    @Autowired
    public void setSigninRepository(SigninRepository repo){
        signinRepository = repo;
    }



    @OnOpen
    public void onOpen(Session session, @PathParam("userEmail") String userEmail) {
        userSessions.put(userEmail, session);
        User user = userRepository.findByUserEmail(userEmail);
        if(user.getIsActive() == false) {
            user.setActive(true);
            userRepository.save(user);
        }

        Signin existingSignin = signinRepository.findTopByUserOrderByLastSignInTimestampDesc(user);

        Signin newSignin = new Signin(user);
        newSignin.setSignInCount(existingSignin.getSignInCount());
        newSignin.updateSignInInfo();
        signinRepository.save(newSignin);

        // Perform any necessary actions when a user connects
        System.out.println("User connected: " + userEmail);
    }

    @OnMessage
    public void onMessage(Session session, String message, @PathParam("userEmail") String userEmail) {
        // Handle incoming messages from the user
        System.out.println("Received message from " + userEmail + ": " + message);
        // Perform any necessary actions based on the received message
    }

    @OnClose
    public void onClose(Session session, @PathParam("userEmail") String userEmail) {
        User closeUserSession = userRepository.findByUserEmail(userEmail);
        if (closeUserSession != null) {
            closeUserSession.setActive(false);
            userRepository.save(closeUserSession);
        }
        userSessions.remove(userEmail);

        // Perform any necessary actions when a user disconnects
        System.out.println("User disconnected: " + userEmail);
    }

    @OnError
    public void onError(Session session, Throwable throwable, @PathParam("userEmail") String userEmail) {
        // Handle any errors that occur during the WebSocket session
        System.out.println("Error occurred for user " + userEmail + ": " + throwable.getMessage());
    }

    public static void sendMessage(String userEmail, String message) {
        Session session = userSessions.get(userEmail);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}