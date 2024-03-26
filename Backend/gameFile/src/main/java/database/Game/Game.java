package database.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    List<Player> players;
    Deck deck;

    String lastCharacterMove;
    Player currentPlayer;



    public Game(List<Player> players, Deck deck) {
        this.players = players;
        this.deck = deck;
    }

    public void initGame(){

        players.add(new Player("A",2,false));
        players.add(new Player("B",2,false));
        players.add(new Player("C",2,false));
        players.add(new Player("D",2,false));
        Collections.shuffle(players);
        currentPlayer = players.get(0);
        Deck deck = new Deck();
        deck.initializeDeck();
        deck.shuffle();



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

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }




}
