package database.Game;

public class Player {

    String userEmail;
    String cardOne;
    String cardTwo;
    int coins;
    Boolean turn;

    boolean callBluff;

    Player targetPlayer;

    String currentMove;

    int turnNumber;

    public Player(String userEmail, int coins, boolean turn) {
        this.userEmail = userEmail;
        this.coins = 2;
        this.turn = turn;
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
    public void coup(Player player){
        // TODO: Implement this functionality
    }

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
        coins++;
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

    public boolean revealCard(String card,Player player){
        if(cardOne.equals(card) || cardTwo.equals(card)){
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

    public boolean isCallBluff() {
        return callBluff;
    }

    public void setCallBluff(boolean callBluff) {
        this.callBluff = callBluff;
    }

    public String getCurrentMove() {
        return currentMove;
    }

    public void setCurrentMove(String currentMove) {
        this.currentMove = currentMove;
    }
    /*
     * Getters and Setters
     */


}
