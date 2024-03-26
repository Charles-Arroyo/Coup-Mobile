package database.Game;

import java.util.List;

public class Game {
    List<Player> players;
    Deck deck;

    String lastCharacterMove;

    public Game(List<Player> players, Deck deck) {
        this.players = players;
        this.deck = deck;
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
