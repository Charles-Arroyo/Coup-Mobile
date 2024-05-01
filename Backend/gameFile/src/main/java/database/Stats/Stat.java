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
    private int bluff = 0;

    @JsonIgnore
    private int block;

    @JsonIgnore
    private int income;
    @JsonIgnore
    private int moveCount;

    private double incomeAvg;
    private double coupAvg;
    private double bluffAvg;

    private double exchangeAvg;
    private double taxAvg;
    private double assnAvg;
    private double stealAvg;

    private String mostUsedMove;
    private String mostLikelyMove;


    /**
     * Special Moves
     */
    @JsonIgnore
    private int assassinateCount = 0;

    @JsonIgnore
    private int foreignAid = 0;

    @JsonIgnore
    private int steal = 0;
    @JsonIgnore
    private int tax = 0;

    /**
     * Special Moves
     */


    @JsonIgnore
    private int exchange = 0;





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

    public void increaseTax(){
        tax++;
        moveCount++;
    }

    public void increaseBluff(){
        bluff++;
        moveCount++;
    }


    public void increaseBlock(){
        block++;
        moveCount++;
    }

    public void increaseIncome() {
        income++;
        moveCount++;
    }

    public void increaseCoup() {
        coupCount++;
        moveCount++;
    }
    public void setTax(int tax) {
        this.tax = tax;
    }

    public int getAssassinateCount(){return assassinateCount;}

    public void setAssassinateCount(int assassinateCount){
        this.assassinateCount = assassinateCount;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }


    public double getIncomeAvg() {
        return incomeAvg;
    }

    public void setIncomeAvg(double incomeAvg) {
        this.incomeAvg = incomeAvg;
    }

    public double getCoupAvg() {
        return coupAvg;
    }

    public void setCoupAvg(double coupAvg) {
        this.coupAvg = coupAvg;
    }

    public double getBluffAvg() {
        return bluffAvg;
    }

    public void setBluffAvg(double bluffAvg) {
        this.bluffAvg = bluffAvg;
    }

    public double getExchangeAvg() {
        return exchangeAvg;
    }

    public void setExchangeAvg(double exchangeAvg) {
        this.exchangeAvg = exchangeAvg;
    }

    public double getTaxAvg() {
        return taxAvg;
    }

    public void setTaxAvg(double taxAvg) {
        this.taxAvg = taxAvg;
    }

    public double getAssnAvg() {
        return assnAvg;
    }

    public void setAssnAvg(double assnAvg) {
        this.assnAvg = assnAvg;
    }

    public double getStealAvg() {
        return stealAvg;
    }

    public void setStealAvg(double stealAvg) {
        this.stealAvg = stealAvg;
    }

    public int getExchange() {
        return exchange;
    }

    public void setExchange(int exchange) {
        this.exchange = exchange;
    }

    public void incrementSpecialMoves(String move){
        if(move.contains("Exchange")){
            this.exchange++;
        }else if(move.contains("Steal")){
            this.steal++;
        }else if(move.contains("Tax")){
            this.tax++;
        }else if(move.contains("Assassinate")){
            this.assassinateCount++;
        }
    }

    public void setAverages(){
        if (moveCount > 0) {
            this.incomeAvg = (double) income / moveCount;
            this.coupAvg = (double) coupCount / moveCount;
            this.bluffAvg = (double) bluff / moveCount;
            this.exchangeAvg = (double) assassinateCount / moveCount;
            this.stealAvg = (double) steal / moveCount;
            this.taxAvg = (double) tax / moveCount;
            this.assnAvg = (double) assassinateCount / moveCount;
        } else {
            this.incomeAvg = 0;
        }
    }


    public void findMostUsedMove() {
        int maxCount = Math.max(Math.max(Math.max(Math.max(Math.max(Math.max(income, coupCount), bluff), block), steal), tax), assassinateCount);
        if (income == maxCount) {
            mostUsedMove = "Income";
        } else if (coupCount == maxCount) {
            mostUsedMove = "Coup";
        } else if (bluff == maxCount) {
            mostUsedMove = "Bluff";
        } else if (block == maxCount) {
            mostUsedMove = "Block";
        } else if (steal == maxCount) {
            mostUsedMove = "Steal";
        } else if (tax == maxCount) {
            mostUsedMove = "Tax";
        } else if (assassinateCount == maxCount) {
            mostUsedMove = "Assassinate";
        }

        System.out.println("Most Used Move: " + mostUsedMove);
    }

    public void findMostLikelyMove() {
        double maxAvg = Math.max(Math.max(Math.max(Math.max(Math.max(Math.max(incomeAvg, coupAvg), bluffAvg), stealAvg), taxAvg), assnAvg), exchangeAvg);

        if (incomeAvg == maxAvg) {
            mostLikelyMove = "Income";
        } else if (coupAvg == maxAvg) {
            mostLikelyMove = "Coup";
        } else if (bluffAvg == maxAvg) {
            mostLikelyMove = "Bluff";
        } else if (stealAvg == maxAvg) {
            mostLikelyMove = "Steal";
        } else if (taxAvg == maxAvg) {
            mostLikelyMove = "Tax";
        } else if (assnAvg == maxAvg) {
            mostLikelyMove = "Assassinate";
        } else if (exchangeAvg == maxAvg) {
            mostLikelyMove = "Exchange";
        }
        System.out.println("Most Likely Move: " + mostLikelyMove);
    }

}


