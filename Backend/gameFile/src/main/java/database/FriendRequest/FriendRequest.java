package database.FriendRequest;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;
import database.Users.User;
import database.Friends.Friend;
import jakarta.persistence.*;

@Entity
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requesting_user_id", nullable = false)
    private User requestingUser;

    @ManyToOne
    @JoinColumn(name = "requested_user_id", nullable = false)
    private User requestedUser;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public User getRequestingUser() {
        return requestingUser;
    }

    public void setRequestingUser(User requestingUser) {
        this.requestingUser = requestingUser;
    }

    public User getRequestedUser() {
        return requestedUser;
    }

    public void setRequestedUser(User requestedUser) {
        this.requestedUser = requestedUser;
    }
}