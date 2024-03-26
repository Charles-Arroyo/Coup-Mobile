package database.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    List<Player> players;
    Deck deck;

    String lastCharacterMove;
    Player currentPlayer;



    public Game(List<Player> players) {
        this.players = players;
    }

    public void initGame(String name1,String name2, String name3, String name4){
        // Adds players
        players.add(new Player(name1,2,false));
        players.add(new Player(name2,2,false));
        players.add(new Player(name3,2,false));
        players.add(new Player(name4,2,false));
        int var = 0;
        //Shuffle
        Collections.shuffle(players); //Shuffles Player to allow fair chance for 1st move

        for (int i = 0; i < players.size(); i++) { //Assigns index to player
            Player player = players.get(i);
            player.setTurnNumber(i);
        }

        currentPlayer = players.get(0); //Assigns current player to first player in array.

        System.out.println("The Current Player is: ");
        System.out.print(currentPlayer.toString());

        deck = new Deck(); // Create a Deck Object
        deck.initializeDeck(); //Initialize a deck of 15 Cards. [Duke,Duke,Duke,Captain,Captain...]
        deck.shuffle(); //Shuffle/randomize Array List of Cards
        System.out.println();
        System.out.println();
        System.out.println("The Current Deck is: ");

        System.out.println(deck.toString()); // Print Deck for Testing

       //Now we need the draw card feature.



    }

    public String getLastCharacterMove() {
        return lastCharacterMove;
    }

    public void setLastCharacterMove(String lastCharacterMove) {
        this.lastCharacterMove = lastCharacterMove;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(String playerName) {
        for (Player player : players) {
            if (player.getUserEmail().equals(playerName)) {
                return player; // Return the player if found
            }
        }
        return null; // Return null if player not found
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public String getDeck() {
        return deck.toString();
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }




}
