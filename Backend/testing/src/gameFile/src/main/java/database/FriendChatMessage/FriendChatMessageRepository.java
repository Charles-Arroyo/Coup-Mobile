package database.FriendChatMessage;

import database.FriendChat.FriendChat;
import database.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendChatMessageRepository extends JpaRepository<FriendChatMessage, Integer> {
    List<FriendChatMessage> findBySenderAndReceiverOrSenderAndReceiverOrderByTimestampAsc(User sender, User receiver1, User receiver2, User sender2);
}