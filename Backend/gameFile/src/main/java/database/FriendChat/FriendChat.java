package database.FriendChat;

import database.FriendChatMessage.FriendChatMessage;
import database.Users.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class FriendChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user1_id")
    private User user1;


    @ManyToOne
    @JoinColumn(name = "user2_id")
    private User user2;


    @OneToMany(mappedBy = "friendChat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendChatMessage> messages = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public FriendChat() {
        this.createdAt = LocalDateTime.now();
    }

    public FriendChat(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public List<FriendChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<FriendChatMessage> messages) {
        this.messages = messages;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Helper methods

    public void addMessage(FriendChatMessage message) {
        messages.add(message);
        message.setFriendChat(this);
    }

    public void removeMessage(FriendChatMessage message) {
        messages.remove(message);
        message.setFriendChat(null);
    }
}