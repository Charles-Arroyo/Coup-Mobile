package database.Friends;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;
import database.Users.User;


/**
 * This class simulates a relationship between a two emails known as a friendship
 */

//@Entity
//
//public class Friend {
//
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Id
//    private int id; // Primary Key, not exposed to users.
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
////    @JsonIgnore
//    private User user;
//
//    private String friendEmail1;
//    private String friendEmail2;
//
//    private boolean acceptance;
//
//    public Friend(String friendEmail1, String friendEmail2) {
//        this.friendEmail1 = friendEmail1;
//        this.friendEmail2 = friendEmail2;
//    }
//
//
//    public Friend() {
//
//    }
//
//    public String getFriendEmail2() {
//        return friendEmail2;
//    }
//
//
//    public String getFriendEmail1() {
//        return friendEmail1;
//    }
//
//    public void setFriendEmail2(String friendEmail2) {
//        this.friendEmail2 = friendEmail2;
//    }
//
//    public void setFriendEmail1(String friendEmail1) {
//        this.friendEmail1 = friendEmail1;
//    }
//
//    public boolean getAcceptance() {
//        return acceptance;
//    }
//
//    public void setAcceptance(boolean acceptance) {
//        this.acceptance = acceptance;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//}


import jakarta.persistence.*;

@Entity
@Table(name = "friend_relationships")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    private boolean accepted;

    // Constructors, getters, and setters
    public Friend(){

    }


    public long getId() {
        return id;
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

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}