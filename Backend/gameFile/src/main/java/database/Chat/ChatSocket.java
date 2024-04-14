package database.FriendChat;

import database.Friends.Friend;
import database.Friends.FriendRepository;
import database.Users.User;
import database.Users.UserRepository;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Controller
@ServerEndpoint(value = "/chatFriend/{userId}/{friendId}")
public class FriendChatSocket {
	private static UserRepository userRepository;
	private static FriendRepository friendRepository;
	private static FriendChatMessageRepository friendChatMessageRepository;

	@Autowired
	public void setUserRepository(UserRepository repo) {
		userRepository = repo;
	}

	@Autowired
	public void setFriendRepository(FriendRepository repo) {
		friendRepository = repo;
	}

	@Autowired
	public void setFriendChatMessageRepository(FriendChatMessageRepository repo) {
		friendChatMessageRepository = repo;
	}

	private static Map<Session, Long> sessionUserMap = new Hashtable<>();
	private static Map<Long, Session> userSessionMap = new Hashtable<>();

	private final Logger logger = LoggerFactory.getLogger(FriendChatSocket.class);

	@OnOpen
	public void onOpen(Session session, @PathParam("userId") Long userId, @PathParam("friendId") Long friendId) {
		logger.info("Entered into Open");

		sessionUserMap.put(session, userId);
		userSessionMap.put(userId, session);

		// Send chat history to the newly connected user
		sendChatHistory(userId, friendId);
	}

	@OnMessage
	public void onMessage(Session session, String message, @PathParam("userId") Long userId, @PathParam("friendId") Long friendId) throws IOException {
		logger.info("Entered into Message: Got Message:" + message);

		User sender = userRepository.findById(userId).orElse(null);
		User receiver = userRepository.findById(friendId).orElse(null);

		if (sender != null && receiver != null) {
			Friend friendship = friendRepository.findByUser1AndUser2(sender, receiver);
			if (friendship == null) {
				friendship = friendRepository.findByUser1AndUser2(receiver, sender);
			}

			if (friendship != null && friendship.isAccepted()) {
				// Save the message to the repository
				FriendChatMessage chatMessage = new FriendChatMessage();
				chatMessage.setSender(sender);
				chatMessage.setReceiver(receiver);
				chatMessage.setContent(message);
				friendChatMessageRepository.save(chatMessage);

				// Send the message to the sender and receiver
				sendMessageToUser(userId, "[You]: " + message);
				sendMessageToUser(friendId, "[" + sender.getUsername() + "]: " + message);
			}
		}
	}

	@OnClose
	public void onClose(Session session) {
		logger.info("Entered into Close");

		Long userId = sessionUserMap.get(session);
		sessionUserMap.remove(session);
		userSessionMap.remove(userId);
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		logger.info("Entered into Error");
		throwable.printStackTrace();
	}

	private void sendMessageToUser(Long userId, String message) {
		try {
			Session session = userSessionMap.get(userId);
			if (session != null && session.isOpen()) {
				session.getBasicRemote().sendText(message);
			}
		} catch (IOException e) {
			logger.info("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void sendChatHistory(Long userId, Long friendId) {
		List<FriendChatMessage> messages = friendChatMessageRepository.findBySenderIdAndReceiverId(userId, friendId);
		messages.addAll(friendChatMessageRepository.findBySenderIdAndReceiverId(friendId, userId));

		StringBuilder sb = new StringBuilder();
		for (FriendChatMessage message : messages) {
			String sender = message.getSender().getUsername();
			String content = message.getContent();
			sb.append("[").append(sender).append("]: ").append(content).append("\n");
		}

		sendMessageToUser(userId, sb.toString());
	}
}