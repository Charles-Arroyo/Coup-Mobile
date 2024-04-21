package database.FriendChat;

import database.FriendChatMessage.FriendChatMessage;
import database.Friends.Friend;
import database.Friends.FriendRepository;
import database.Users.User;
import database.Users.UserRepository;
import database.FriendChatMessage.FriendChatMessageRepository;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@ServerEndpoint(value = "/chatFriend/{userId}/{friendId}")
public class FriendChatSocket {
    private static UserRepository userRepository;
    private static FriendRepository friendRepository;
    private static FriendChatMessageRepository friendChatMessageRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        FriendChatSocket.userRepository = userRepository;
    }

    @Autowired
    public void setFriendRepository(FriendRepository friendRepository) {
        FriendChatSocket.friendRepository = friendRepository;
    }

    @Autowired
    public void setFriendChatMessageRepository(FriendChatMessageRepository friendChatMessageRepository) {
        FriendChatSocket.friendChatMessageRepository = friendChatMessageRepository;
    }

    private static Map<Integer, Session> sessions = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId, @PathParam("friendId") Integer friendId) throws IOException {
        sessions.put(userId, session);

        User user = userRepository.findById(userId);
        User friend = userRepository.findById(friendId);

        if (user != null && friend != null) {
            // Check if the users are friends
            Friend friendship = friendRepository.findByUser1AndUser2(user, friend);
            if (friendship == null) {
                friendship = friendRepository.findByUser1AndUser2(friend, user);
            }

            if (friendship != null && friendship.isAccepted()) {
                // Retrieve the chat history from the database
                List<FriendChatMessage> chatHistory = friendChatMessageRepository.findBySenderAndReceiverOrSenderAndReceiverOrderByTimestampAsc(user, friend, friend, user);

                // Send the chat history to the connected client
                for (FriendChatMessage message : chatHistory) {
                    String sender = message.getSender().equals(user) ? "You" : message.getSender().getUserEmail();
                    session.getBasicRemote().sendText(sender + ": " + message.getContent());
                }
            }
        }
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
                friendChatMessageRepository.save(chatMessage);

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