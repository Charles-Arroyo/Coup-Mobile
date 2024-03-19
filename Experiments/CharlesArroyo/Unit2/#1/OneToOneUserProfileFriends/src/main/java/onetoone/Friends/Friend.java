package onetoone.Friends;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;
import onetoone.Users.User;

/**
 * This class simulates a relationship between a two emails known as a friendship
 */

@Entity

public class Friend {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id; // Primary Key, not exposed to users.

    private String friendEmail1;
    private String friendEmail2;

    private boolean acceptance;

    public Friend(String friendEmail1, String friendEmail2) {
        this.friendEmail1 = friendEmail1;
        this.friendEmail2 = friendEmail2;

    }


    public Friend() {

    }

    public String getFriendEmail2() {
        return friendEmail2;
    }


    public String getFriendEmail1() {
        return friendEmail1;
    }

    public void setFriendEmail2(String friendEmail2) {
        this.friendEmail2 = friendEmail2;
    }

    public void setFriendEmail1(String friendEmail1) {
        this.friendEmail1 = friendEmail1;
    }

    public boolean getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(boolean acceptance) {
        this.acceptance = acceptance;
    }

    @ManyToOne
    @JoinColumn(name = "friendId")
    @JsonIgnore
    private User user;


    public int getId() {
        return id;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
