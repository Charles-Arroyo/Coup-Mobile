package database.FriendChat;

import database.Friends.Friend;
import database.Friends.FriendRepository;
import database.Users.User;
import database.Users.UserRepository;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@ServerEndpoint(value = "/chatFriend/{userId}/{friendId}")
public class FriendChatSocket {
    private static UserRepository userRepository;
    private static FriendRepository friendRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        FriendChatSocket.userRepository = userRepository;
    }

    @Autowired
    public void setFriendRepository(FriendRepository friendRepository) {
        FriendChatSocket.friendRepository = friendRepository;
    }

    private static Map<Integer, Session> sessions = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId, @PathParam("friendId") Integer friendId) {
        sessions.put(userId, session);
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("userId") Integer userId, @PathParam("friendId") Integer friendId) throws IOException {
        User user = userRepository.findById(userId);
        User friend = userRepository.findById(friendId);

        if (user != null && friend != null) {
            // Check if the users are friends
            Friend friendship = friendRepository.findByUser1AndUser2(user, friend);
            if (friendship == null) {
                friendship = friendRepository.findByUser1AndUser2(friend, user);
            }

            if (friendship != null && friendship.isAccepted()) {
                // Save the message to the database
                FriendChatMessage chatMessage = new FriendChatMessage();
                chatMessage.setSender(user);
                chatMessage.setReceiver(friend);
                chatMessage.setContent(message);
                // Save the chat message using a repository or service

                // Send the message to the sender's session
                Session senderSession = sessions.get(userId);
                if (senderSession != null && senderSession.isOpen()) {
                    senderSession.getBasicRemote().sendText("You: " + message);
                }

                // Send the message to the friend's session
                Session friendSession = sessions.get(friendId);
                if (friendSession != null && friendSession.isOpen()) {
                    friendSession.getBasicRemote().sendText(user.getUserEmail() + ": " + message);
                }
            }
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("userId") Integer userId) {
        sessions.remove(userId);
    }
}