package onetoone.game;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import onetoone.Friends.Friend;
import onetoone.Profiles.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import onetoone.Setting.Setting;

import onetoone.Users.User;


@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    private Long gameWon;
    private Long game;
    private Long gameLost;

    public Game(){
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }


    public Long getGameWon() {
        return gameWon;
    }

    public void setGameWon(Long gameWon) {
        this.gameWon = gameWon;
    }

    public Long getGame() {
        return game = gameLost + gameWon;
    }

//    public void setGame(Long game) {
//        this.game = game;
//    }

    public Long getGameLost() {
        return gameLost;
    }

    public void setGameLost(Long gameLost) {
        this.gameLost = gameLost;
    }




}
