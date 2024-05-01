package database.Chat;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import database.Friends.FriendRepository;
import database.Users.User;
import database.Users.UserRepository;
import database.Websocketconfig.WebsocketConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller      // this is needed for this to be an endpoint to springboot
@ServerEndpoint(value = "/chat/{username}")  // this is Websocket url
public class ChatSocket {

	// cannot autowire static directly (instead we do it by the below
	// method
	private static MessageRepository msgRepo;

	private static UserRepository userRepository;

	private static FriendRepository friendRepository;


	/*
	 * Grabs the MessageRepository singleton from the Spring Application
	 * Context.  This works because of the @Controller annotation on this
	 * class and because the variable is declared as static.
	 * There are other ways to set this. However, this approach is
	 * easiest.
	 */
	@Autowired
	public void setMessageRepository(MessageRepository repo) {
		msgRepo = repo;  // we are setting the static variable
	}
	@Autowired
	public void setUserRepository(UserRepository repo) {
		userRepository = repo;  // we are setting the static variable
	}

	@Autowired
	public void setUserRepository(FriendRepository repo) {
		friendRepository = repo;  // we are setting the static variable
	}

	// Store all socket session and their corresponding username.
	private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
	private static Map<String, Session> usernameSessionMap = new Hashtable<>();

	private final Logger logger = LoggerFactory.getLogger(ChatSocket.class);

	@OnOpen
	public void onOpen(Session session, @PathParam("username") String username)
			throws IOException {

		if(userRepository.findByUserEmail(username) != null){ // Code checks to make sure username is in repo
			logger.info("Entered into Open");

			// store connecting user information
			sessionUsernameMap.put(session, username);
			usernameSessionMap.put(username, session);

			//Send chat history to the newly connected user
			sendMessageToPArticularUser(username, getChatHistory());

			// broadcast that new user joined
//			String message = "User:" + username + " has Joined the Chat";
//			broadcast(message);
//
//			Message joinMessage = new Message(username, "has Joined the Chat");
//			msgRepo.save(joinMessage);

		}else {
			// If user is not found in the database, close the connection with a reason
			String message = "User:" + username + " Is not in DB";
			broadcast(message);
		}
	}


	@OnMessage
	public void messageFriends(Session session, String message) throws IOException {
		// Handle new messages
		logger.info("Entered into Message: Got Message:" + message);
		String username = sessionUsernameMap.get(session);
		User sendingUser = userRepository.findByUserEmail(username);

		// Direct message to a user using the format "@username <message>"
		if (message.startsWith("@")) {
			String destUsername = message.split(" ")[0].substring(1);
			User userDest = userRepository.findByUserEmail(destUsername);

			// Check if username and destUsername are friends
			if (friendRepository.existsByUser1AndUser2(sendingUser, userDest) || friendRepository.existsByUser1AndUser2(userDest, sendingUser)) {
				// send the message to the sender and receiver if they are friends
				sendMessageToPArticularUser(destUsername, "[DM] " + username + ": " + message);
				sendMessageToPArticularUser(username, "[DM] " + username + ": " + message);
			} else {
				// If they are not friends, send a message only to the sender that the action is not allowed
				sendMessageToPArticularUser(username, "You can only DM your friends.");
			}
		}
		else { // broadcast
			broadcast(username + ": " + message);
		}

		// Saving chat history to repository might be conditional based on your requirements
		msgRepo.save(new Message(username, message));
	}


	@OnClose
	public void onClose(Session session) throws IOException {
		logger.info("Entered into Close");

		// remove the user connection information
		String username = sessionUsernameMap.get(session);

		sessionUsernameMap.remove(session);
		usernameSessionMap.remove(username);

		// broadcase that the user disconnected
		String message = username + " disconnected";
		broadcast(message);
	}


	@OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
		logger.info("Entered into Error");
		throwable.printStackTrace();
	}


	private void sendMessageToPArticularUser(String username, String message) {
		try {
			usernameSessionMap.get(username).getBasicRemote().sendText(message);
		}
		catch (IOException e) {
			logger.info("Exception: " + e.getMessage().toString());
			e.printStackTrace();
		}
	}


	private void broadcast(String message) {
		sessionUsernameMap.forEach((session, username) -> {
			try {
				session.getBasicRemote().sendText(message);
			}
			catch (IOException e) {
				logger.info("Exception: " + e.getMessage().toString());
				e.printStackTrace();
			}

		});

	}


	// Gets the Chat history from the repository
	private String getChatHistory() {
		List<Message> messages = msgRepo.findAll();

		// convert the list to a string
		StringBuilder sb = new StringBuilder();
		if(messages != null && messages.size() != 0) {
			for (Message message : messages) {
				sb.append(message.getUserName() + ": " + message.getContent() + "\n");
			}
		}
		return sb.toString();
	}

} // end of Class
