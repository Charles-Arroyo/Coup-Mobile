//package database.Signin;
//
//import database.Users.User;
//import database.Users.UserRepository;
//import jakarta.websocket.*;
//import jakarta.websocket.server.PathParam;
//import jakarta.websocket.server.ServerEndpoint;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@ServerEndpoint(value = "/signin/{userEmail}")
//public class SigninSocket {
//
//    // A static map to store the active WebSocket sessions for each connected user, keyed by their email
//    private static Map<String, Session> userSessions = new HashMap<>();
//
//    // Repository instances injected through the constructor
//    private final UserRepository userRepository;
//    private final SigninRepository signinRepository;
//
//    // Constructor to inject the repository instances
//    @Autowired
//    public SigninSocket(UserRepository userRepository, SigninRepository signinRepository) {
//        this.userRepository = userRepository;
//        this.signinRepository = signinRepository;
//    }
//
//    /**
//     * This method is called when a new WebSocket connection is established.
//     * It updates the user's active status, handles sign-in logging, and stores the new session in the userSessions map.
//     *
//     * @param session   The WebSocket session object
//     * @param userEmail The user's email, used as the key in the userSessions map
//     */
//    @OnOpen
//    public void onOpen(Session session, @PathParam("userEmail") String userEmail) {
//        userSessions.put(userEmail, session);
//        User user = userRepository.findByUserEmail(userEmail);
//        if (user != null && !user.isActive()) {
//            user.setActive(true);
//            userRepository.save(user);
//        }
//
//        Signin existingSignin = signinRepository.findTopByUserOrderByLastSignInTimestampDesc(user);
//        Signin newSignin;
//        if (existingSignin != null) {
//            newSignin = existingSignin;
//            newSignin.updateSignInInfo();
//        } else {
//            newSignin = new Signin(user);
//            newSignin.setSignInCount(1);
//            newSignin.updateSignInInfo();
//        }
//        signinRepository.save(newSignin);
//
//        System.out.println("User connected: " + userEmail);
//    }
//
//    /**
//     * This method is called when a message is received from the client.
//     * It logs the received message and can be extended to handle specific message types or perform additional actions.
//     *
//     * @param session   The WebSocket session object
//     * @param message   The message received from the client
//     * @param userEmail The user's email
//     */
//    @OnMessage
//    public void onMessage(Session session, String message, @PathParam("userEmail") String userEmail) {
//        System.out.println("Received message from " + userEmail + ": " + message);
//        // Perform any necessary actions based on the received message
//    }
//
//    /**
//     * This method is called when a WebSocket connection is closed.
//     * It updates the user's active status to false and removes the user's session from the userSessions map.
//     *
//     * @param session   The WebSocket session object
//     * @param userEmail The user's email
//     */
//    @OnClose
//    public void onClose(Session session, @PathParam("userEmail") String userEmail) {
//        User closeUserSession = userRepository.findByUserEmail(userEmail);
//        if (closeUserSession != null) {
//            closeUserSession.setActive(false);
//            userRepository.save(closeUserSession);
//        }
//        userSessions.remove(userEmail);
//
//        System.out.println("User disconnected: " + userEmail);
//    }
//
//    /**
//     * This method is called when an error occurs during the WebSocket session.
//     * It logs the error message.
//     *
//     * @param session   The WebSocket session object
//     * @param throwable The exception or error that occurred
//     * @param userEmail The user's email
//     */
//    @OnError
//    public void onError(Session session, Throwable throwable, @PathParam("userEmail") String userEmail) {
//        System.err.println("Error occurred for user " + userEmail + ": " + throwable.getMessage());
//        // Handle any errors that occur during the WebSocket session
//    }
//
//    /**
//     * This method sends a message to the specified user's WebSocket session, if the session is open.
//     *
//     * @param userEmail The user's email
//     * @param message   The message to be sent
//     */
//    public static void sendMessage(String userEmail, String message) {
//        Session session = userSessions.get(userEmail);
//        if (session != null && session.isOpen()) {
//            try {
//                session.getBasicRemote().sendText(message);
//            } catch (IOException e) {
//                System.err.println("Error sending message to user " + userEmail + ": " + e.getMessage());
//
//            }
//        }
//    }
//}

package database.Signin;

import database.FriendRequest.FriendRequestRepository;
import database.Friends.Friend;
import database.Friends.FriendRepository;
import database.Users.User;
import database.Users.UserRepository;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
@ServerEndpoint(value = "/signin/{userEmail}")
public class SigninSocket {
    private static Map<String, Session> userSessions = new HashMap<>();

    private static UserRepository userRepository;


    private static SigninRepository signinRepository;


    private static FriendRepository friendRepository;

    @Autowired
    public void setUserRepository(UserRepository repo) {
        userRepository = repo;  // we are setting the static variable
    }

    @Autowired
    public void setSigninRepository(SigninRepository repo){
        signinRepository = repo;
    }

    @Autowired
    public void setFriendRepository(FriendRepository repo){
        friendRepository = repo;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userEmail") String userEmail) {
        userSessions.put(userEmail, session);
        User user = userRepository.findByUserEmail(userEmail);
        if (user != null && !user.isActive()) {
            user.setActive(true);
            userRepository.save(user);
        }

        Signin existingSignin = signinRepository.findTopByUserOrderByLastSignInTimestampDesc(user);
        Signin newSignin;
        if (existingSignin != null) {
            newSignin = existingSignin;
            newSignin.updateSignInInfo();
        } else {
            newSignin = new Signin(user);
            newSignin.setSignInCount(1);
            newSignin.updateSignInInfo();
        }
        signinRepository.save(newSignin);

        System.out.println("User connected: " + userEmail);
    }

    @OnMessage
    public void onMessage(Session session, String message, @PathParam("userEmail") String userEmail) {
        Map<String, Object> response = new HashMap<>();

        User user = userRepository.findByUserEmail(userEmail);
        List<Friend> friendships = friendRepository.findByUser1OrUser2(user, user);

        if (message.equals("getfriend")) {
            List<User> friendList = friendships.stream()
                    .map(friendship -> friendship.getUser1().equals(user) ? friendship.getUser2() : friendship.getUser1())
                    .distinct()
                    .collect(Collectors.toList());

            response.put("success", true);
            response.put("friends", friendList);

            // Send the response back to the client
            sendMessage(userEmail, createJsonString(response));
        }
    }

    private String createJsonString(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append("\"").append(entry.getKey()).append("\":");
            Object value = entry.getValue();

            if (value instanceof String) {
                sb.append("\"").append(value).append("\"");
            } else if (value instanceof List) {
                sb.append("[");
                List<?> list = (List<?>) value;
                for (int i = 0; i < list.size(); i++) {
                    Object item = list.get(i);
                    if (item instanceof User) {
                        sb.append(createUserJsonString((User) item));
                    } else {
                        sb.append(createJsonString((Map<String, Object>) item));
                    }
                    if (i < list.size() - 1) {
                        sb.append(",");
                    }
                }
                sb.append("]");
            } else {
                sb.append(value);
            }

            sb.append(",");
        }

        if (sb.charAt(sb.length() - 1) == ',') {
            sb.setLength(sb.length() - 1);
        }

        sb.append("}");
        return sb.toString();
    }

    private String createUserJsonString(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"name\":\"").append(user.getName()).append("\",");
        sb.append("\"email\":\"").append(user.getUserEmail()).append("\",");
        sb.append("\"active\":\"").append(user.isActive()).append("\"");
        sb.append("}");
        return sb.toString();
    }

    @OnClose
    public void onClose(Session session, @PathParam("userEmail") String userEmail) {
        User closeUserSession = userRepository.findByUserEmail(userEmail);
        if (closeUserSession != null) {
            closeUserSession.setActive(false);
            userRepository.save(closeUserSession);
        }
        userSessions.remove(userEmail);

        System.out.println("User disconnected: " + userEmail);
    }

    @OnError
    public void onError(Session session, Throwable throwable, @PathParam("userEmail") String userEmail) {
        System.err.println("Error occurred for user " + userEmail + ": " + throwable.getMessage());
    }

    public static void sendMessage(String userEmail, String message) {
        Session session = userSessions.get(userEmail);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                System.err.println("Error sending message to user " + userEmail + ": " + e.getMessage());
            }
        }
    }
}