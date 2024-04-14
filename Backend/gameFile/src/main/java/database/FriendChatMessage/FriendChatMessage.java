package database.FriendChatMessage;

import database.FriendChat.FriendChat;
import database.Users.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class FriendChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "friend_chat_id")
    private FriendChat friendChat;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;


    @Column(name = "content")
    private String content;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    public FriendChatMessage() {
        this.timestamp = LocalDateTime.now();
    }

    // Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public FriendChat getFriendChat() {
        return friendChat;
    }

    public void setFriendChat(FriendChat friendChat) {
        this.friendChat = friendChat;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}