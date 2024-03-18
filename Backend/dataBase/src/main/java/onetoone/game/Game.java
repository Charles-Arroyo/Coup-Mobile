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

    private int gameWon = 0;
    private int gameLost = 0;

    private int gamePlayed = 0;

    private String gameResult;


    public Game(String gameResult){
        this.gameResult = gameResult;

    }

    public Game(){
    }


    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }


    public int getGameWon() {
        return gameWon;
    }

//    public void setGameWon(Long gameWon) {
//        this.gameWon = gameWon;
//    }
// Constructors, getters, and setters
    public void incrementGameWon() {
        this.gameWon += 1;
    }
    // Constructors, getters, and setters
    public void incrementGameLost() {
        this.gameLost += 1;
    }

    public void setGameResult(long gameLost, long gameWon) {
        this.gameResult = String.valueOf(gameLost + gameWon);
    }

    public String getGameResult(){
        return gameResult;
    }


    public int getGameLost() {
        return gameLost;
    }


    public int getGamePlayed() {
        return gamePlayed;
    }

    public void setGamePlayed(int gamePlayed) {
        this.gamePlayed = gamePlayed;
    }

    public void addGamePlayed(){
        this.gamePlayed++; // Directly increment gamePlayed for each game result processed
    }

}
