package database.Signin;

import database.Users.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Signin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime lastSignInTimestamp;

    private LocalDateTime lastSignOutTimestamp;

    private int signInCount;

    public Signin() {
    }

    public Signin(User user) {
        this.user = user;
        this.signInCount = 0;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getLastSignInTimestamp() {
        return lastSignInTimestamp;
    }

    public void setLastSignInTimestamp(LocalDateTime lastSignInTimestamp) {
        this.lastSignInTimestamp = lastSignInTimestamp;
    }



    public LocalDateTime getLastSignOutTimestamp() {
        return lastSignOutTimestamp;
    }

    public void setLastSignOutTimestamp(LocalDateTime lastSignOutTimestamp) {
        this.lastSignOutTimestamp = lastSignOutTimestamp;
    }

    // Methods to update sign-in information

    public void updateSignInInfo() {
        this.lastSignInTimestamp = LocalDateTime.now();
        this.signInCount++;
    }

    public void updateSignOutInfo() {
        this.lastSignOutTimestamp = LocalDateTime.now();
    }

    public int getSignInCount() {
        return signInCount;
    }

    public void setSignInCount(int signInCount) {
        this.signInCount = signInCount;
    }
}