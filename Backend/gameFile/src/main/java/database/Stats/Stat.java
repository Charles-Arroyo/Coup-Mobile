package database.Stats;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import database.Users.User;

@Entity
public class Stat {

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

    @JsonIgnore
    private int coupCount = 0;
    @JsonIgnore
    private int endGameCoinsLeft = 0;
    @JsonIgnore
    private int foreignAid = 0;
    @JsonIgnore
    private int bluff = 0;
    @JsonIgnore
    private int steal = 0;
    @JsonIgnore
    private int tax = 0;
    @JsonIgnore
    private int assassinateCount = 0;


    public Stat(String gameResult) {
        this.gameResult = gameResult;
    }

    public Stat() {
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public int getGameWon() {
        return gameWon;
    }

    public void incrementGameWon() {
        this.gameWon += 1;
    }

    public void incrementGameLost() {
        this.gameLost += 1;
    }

    public void setGameResult(long gameLost, long gameWon) {
        this.gameResult = String.valueOf(gameLost + gameWon);
    }

    public String getGameResult() {
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

    public void addGamePlayed() {
        this.gamePlayed++;
    }

    public int getCoupCount() {return coupCount;}

    public void setCoupCount(int coupCount) {
        this.coupCount = coupCount;
    }

    public int getEndGameCoinsLeft() {return endGameCoinsLeft;}

    public void setEndGameCoinsLeft(int endGameCoinsLeft) {
        this.endGameCoinsLeft = endGameCoinsLeft;
    }

    public int getForeignAid() {return foreignAid;}

    public void setForeignAid(int foreignAid) {
        this.foreignAid = foreignAid;
    }

    public int getBluff() {return bluff;}

    public void setBluff(int bluff) {
        this.bluff = bluff;
    }

    public int getSteal() {return steal;}

    public void setSteal(int steal) {
        this.steal = steal;
    }

    public int getTax() {return tax;}

    public void setTax(int tax) {
        this.tax = tax;
    }

    public int getAssassinateCount(){return assassinateCount;}

    public void setAssassinateCount(int assassinateCount){
        this.assassinateCount = assassinateCount;
    }
}