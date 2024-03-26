package database.Game;

public class Player {

    String userEmail;
    String cardOne;
    String cardTwo;
    int coins;
    Boolean turn;

    public Player(String userEmail, String cardOne, String cardTwo, int coins, boolean turn) {
        this.userEmail = userEmail;
        this.cardOne = cardOne;
        this.cardTwo = cardTwo;
        this.coins = 2;
        this.turn = turn;
    }

    /*__________________________Player Actions/Outcomes_______________________*/

    /**
     *
     * @param player
     */
    public void coup(Player player){
        // TODO: Implement this functionality
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
        // TODO: Implement this functionality
    }

    /**
     *
     */
    public void foreignAid(){
        // TODO: Implement this functionality
    }

    /**
     *
     */
    public void addCoins(int coins){
        // TODO: Implement this functionality
    }

    /**
     *
     *
     */
    public void loseCoins(int coins){
        // TODO: Implement this functionality
    }

    /**
     *
     */
    public void loseInfluence(){
        // TODO: Implement this functionality
    }

    public void gainInfluence(){
        // TODO: Implement this functionality
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
        // TODO: Implement this functionality
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

    /*
     * Getters and Setters
     */


}
