package database.Lobby;

import database.Users.User;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "lobbies")
public class Lobby {
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private User user1;

    @ManyToOne
    private User user2;

    @ManyToOne
    private User user3;

    @ManyToOne
    private User user4;

    @Column
    private boolean isPrivate;


    @Column
    private boolean isFull;




//    public Lobby(/*String user1, String user2, String user3, String user4, boolean isPrivate, boolean isFull*/) {
////        this.user1 = user1;
////        this.user2 = user2;
////        this.user3 = user3;
////        this.user4 = user4;
////        this.isPrivate = isPrivate;
////        isFull = false;
//    }

    public Lobby(){}

    public String getUser1() {
        return user1.getUserEmail();
    }

    public int getId(){
        return id;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        if(user2 == null){
            return "Empty";
        }else{
            return user2.getUserEmail();
        }

    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public String getUser3() {
        if(user3 == null){
            return "Empty";
        }else{
            return user3.getUserEmail();
        }
    }

    public void setUser3(User user3) {
        this.user3 = user3;
    }

    public String getUser4() {
        if(user4 == null){
            return "Empty";
        }else{
            return user4.getUserEmail();
        }
    }

    public void setUser4(User user4) {
        isFull = true;
        this.user4 = user4;
    }

    public Boolean addUser(User userName){
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

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }
}
