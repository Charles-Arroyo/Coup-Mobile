package database.Spectator;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import database.Lobby.Lobby;
import database.Ranking.Ranking;
import database.Users.User;
import database.Users.UserRepository;
import jakarta.persistence.*;
import database.Stats.Stat;
import jakarta.persistence.*;
import database.FriendRequest.FriendRequest;
import lombok.Singular;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Objects;

@Entity
public class Spectator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private Lobby watchingLobby;

    @Column
    private boolean isActive;

    @Column
    private LocalDateTime joinTime;

    @Column
    private LocalDateTime leaveTime;

    public Spectator(User user) {
        this.user = user;
        this.isActive = true;
    }

    public Spectator() {
        // Default constructor
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }

    public void setActive(boolean  isActive){this.isActive = isActive;}

    public boolean getActive(){return isActive;}


    public void joinLobby(Lobby lobby) {
        this.watchingLobby = lobby;
        this.joinTime = LocalDateTime.now();
        this.isActive = true;
    }

    public void leaveLobby() {
        this.leaveTime = LocalDateTime.now();
        this.isActive = false;
    }


}
