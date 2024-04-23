package database.Game;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Random;

public class Player {

    String userEmail;
    String cardOne;
    String cardTwo;
    int coins;
    Boolean turn;
    //made public for testing
    public Boolean readyToListen = false;

    String currentMove;

    String playerState; // playerState: wait, turn, contest

    int lives;

    int turnNumber;

    ArrayList<String> playerView;

    public Player(String userEmail, int coins, boolean turn,int lives,String playerState) {
        this.userEmail = userEmail;
        this.coins = 2;
        this.turn = turn;
        this.lives = 2;
        this.playerState = playerState;
    }

    public void setTurnNumber(int number){
        this.turnNumber = number;
    }

    @Override
    public String toString() {
        return "Player{" +
                "userEmail='" + userEmail + '\'' +
                ", cardOne='" + cardOne + '\'' +
                ", cardTwo='" + cardTwo + '\'' +
                ", coins=" + coins +
                ", turn=" + turn +
                ", turnNumber=" + turnNumber +
                '}';
    }



    /*__________________________Player Actions/Outcomes_______________________*/

    /**
     *
     * @param player
     */

    public void action(String action, Player player){
        if(action.contains("Assassinate")){
            assassinate(player);
        }
        if(action.contains("Tax")){
            setCurrentMove("Tax");
            tax(player);
        }

        if(action.contains("Steal")){
            setCurrentMove("Steal");
            steal(player);
        }

        if(action.contains("Income")){
            setCurrentMove("Income");
            income();
        }
        if(action.contains("Coup")){
            setCurrentMove("Coup");
            coup(player);
        }
        if(action.contains("Waiting")){
            setPlayerState("Waiting");
        }
        if(action.contains("Foreign Aid")){
            foreignAid(player);
        }
    }

    public void coup(Player player){
        if(this.coins >= 7){
            loseInfluence(player);
            loseCoins(7);
        }
    }



    /**
     *
     * @param player
     * @param card
     */
    public void callBluff(Player player, String card){
        // TODO: Implement this functionality
    }

    /**
     *
     */
    public void income(){
        addCoins(1);
    }

    /**
     *
     */
    public void foreignAid(Player player){
        player.addCoins(2);
    }

    /**
     *
     */
    public void addCoins(int coins){
        this.coins += coins;
    }

    /**
     *
     *
     */
    public void loseCoins(int lostCoins){
        this.coins -= lostCoins;
    }

    /**
     *
     */
    public String loseInfluence(Player player){
        Random random = new Random();

        if (random.nextBoolean()) {
            String card = player.cardOne;
            player.cardOne = null;
            player.lives--;
            return card;
        } else {
            String card = player.cardTwo;
            player.cardTwo = null;
            player.lives--;
            return card;
        }

    }

    public void gainInfluence(String card, Player player){
        if(cardOne == null){
            player.setCardOne(card);
            player.lives++;
        }else if(cardTwo == null){
            player.setCardTwo(card);
            player.lives++;
        }else{
            System.out.println("PLAYER ALREADY HAS MAX CARDS");
        }
    }
    public String revealCard(String card,Player player){
        if(player.cardOne.contains(card)){
            return cardOne;

        }else if(player.cardTwo.contains(card)){
            return cardTwo;
        }else{

            return player.getUserEmail() + " Was a Liar";
        }
    }




    public String removeCard(String card, Player player){
        if(player.cardOne.contains(card)){
            String cardSave = cardOne;
            cardOne = null;
            return cardSave;
        }else{
            String cardSave = cardTwo;
            cardTwo = null;
            return cardSave;
        }
    }




    /*_______________________End Of Player Actions_______________________*/


    /*__________________________Card Actions_______________________*/



    /*______________________________Duke_______________________*/

    /**
     *
     */
    public void tax(Player player){
        player.addCoins(3);
    }



    /*____________________________End of Duke_______________________*/



    /*______________________________Assassin_______________________*/

    /**
     *
     * @param player
     */
    public void assassinate(Player player){
        if(this.coins >= 3){
            loseInfluence(player);
            this.loseCoins(3);
        }else{
            System.out.println("Not enough Coins");
        }
    }
    /*______________________________End of Assassin_______________________*/


    /*______________________________Captain________________________________*/


    /**
     *
     * @param player
     */

    public void steal(Player player){
        int stealAmount = Math.min(2, player.getCoins());
        player.loseCoins(stealAmount);
        this.addCoins(stealAmount);
    }

    /**
     *
     * @param player
     */
    public void blockSteal(Player player){
        // TODO: Implement this functionality
    }


    /*______________________________End of Captain________________________________*/


    /*______________________________Ambassador________________________________*/

    /*______________________________End of Ambassador_______________________________*/


    /*____________________________________Contessa________________________________*/


    /*______________________________End of Contessa________________________________*/





    /*
     * Getters and Setters
     */
    public String getCardOne() {
        return cardOne;
    }

    public void setCardOne(String cardOne) {
        this.cardOne = cardOne;
    }

    public String getCardTwo() {
        return cardTwo;
    }

    public void setCardTwo(String cardTwo) {
        this.cardTwo = cardTwo;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public Boolean getTurn() {
        return turn;
    }

    public void setTurn(Boolean turn) {
        this.turn = turn;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public String getCurrentMove() {
        return currentMove;
    }

    public void setCurrentMove(String currentMove) {
        this.currentMove = currentMove;
    }

    public String getPlayerState() {
        return playerState;
    }

    public void setPlayerState(String playerState) {
        this.playerState = playerState;
    }

    public String getPlayerView() {
        return playerView.toString();
    }


    public void setPlayerView(ArrayList<String> playerView) {
        this.playerView = playerView;
    }

    /*
     * Getters and Setters
     */

}
