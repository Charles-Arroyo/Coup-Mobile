package database.Lobby;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "lobbies")
public class Lobby {
    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = true)
    private String user1;

    @Column(nullable = true)
    private String user2;

    @Column(nullable = true)
    private String user3;

    @Column(nullable = true)
    private String user4;

    @Column
    private boolean isPrivate;


    @Column
    private boolean isFull;




    public Lobby(String user1, String user2, String user3, String user4, boolean isPrivate, boolean isFull) {
        this.user1 = user1;
        this.user2 = user2;
        this.user3 = user3;
        this.user4 = user4;
        this.isPrivate = isPrivate;
        isFull = false;
    }

    public Lobby(){}

    public String getUser1() {
        return user1;
    }

    public int getId(){
        return id;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public String getUser3() {
        return user3;
    }

    public void setUser3(String user3) {
        this.user3 = user3;
    }

    public String getUser4() {
        return user4;
    }

    public void setUser4(String user4) {
        isFull = true;
        this.user4 = user4;
    }

    public Boolean addUser(String userName){
        if(user1 == null){
            setUser1(userName);
            return true;
        } else if (user2 == null) {
            setUser2(userName);
            return true;
        }else if (user3 == null) {
            setUser3(userName);
            return true;
        }else if (user4 == null) {
            setUser4(userName);
            return true;
        }else{
            return false;
        }
    }



    public boolean removeUser(String userName) {
        if (user1 != null && user1.equals(userName)) {
            user1 = null;
            return true;
        } else if (user2 != null && user2.equals(userName)) {
            user2 = null;
            return true;
        } else if (user3 != null && user3.equals(userName)) {
            user3 = null;
            return true;
        } else if (user4 != null && user4.equals(userName)) {
            user4 = null;
            return true;
        } else {
            return false;
        }
    }


    public boolean isEmpty() {
        return user1 == null && user2 == null && user3 == null && user4 == null;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }
}
