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


    public Lobby(String user1, String user2, String user3, String user4, boolean isPrivate) {
        this.user1 = user1;
        this.user2 = user2;
        this.user3 = user3;
        this.user4 = user4;
        this.isPrivate = isPrivate;
    }

    public Lobby(){}

    public String getUser1() {
        return user1;
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

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }




}
