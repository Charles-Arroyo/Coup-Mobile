package database.Game;

import java.util.Random;

public class Player {

    String userEmail;
    String cardOne;
    String cardTwo;
    int coins;
    Boolean turn;

    int lives;

    int turnNumber;

    public Player(String userEmail, int coins, boolean turn,int lives) {
        this.userEmail = userEmail;
        this.coins = 2;
        this.turn = turn;
        this.lives = 2;
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
        if(action.equals("Assassinate")){
            assassinate(player);
        }
        if(action.equals("Tax")){
            tax();
        }

        if(action.equals("Steal")){
            steal(player);
        }

        if(action.equals("Income")){
            income();
        }
        if(action.equals("Coup")){
            coup(player);
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
    public void foreignAid(){
        addCoins(2);
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
    public void loseInfluence(Player player){
        Random random = new Random();
        if (random.nextBoolean()) {
            player.cardOne = null;
            player.lives--;
        } else {
            player.cardTwo = null;
            player.lives--;
        }

    }

    public void gainInfluence(String card){
        if(cardOne == null){
            setCardOne(card);
            this.lives++;
        }else if(cardTwo == null){
            setCardTwo(card);
            this.lives++;
        }else{
            System.out.println("PLAYER ALREADY HAS MAX CARDS");
        }
    }
    public boolean revealCard(String card,Player player){
        if(player.cardOne.equals(card) || player.cardTwo.equals(card)){
            return true;
        }else{
            return false;
        }
    }




    /*_______________________End Of Player Actions_______________________*/


    /*__________________________Card Actions_______________________*/



    /*______________________________Duke_______________________*/

    /**
     *
     */
    public void tax(){
        // TODO: add bluffing check
        addCoins(3);
        setTurn(false);
    }

    /**
     *
     */
    public void blockForeignAid(Player player){
        // TODO: Implement this functionality
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
            loseCoins(3);
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
        // TODO: Implement this functionality
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

    /**
     *
     * @param player
     */
    public void blockAssassinate(Player player){
        // TODO: Implement this functionality
    }


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

    /*
     * Getters and Setters
     */

}
