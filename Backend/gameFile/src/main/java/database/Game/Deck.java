package database.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;



    public Deck() {
        this.cards = new ArrayList<>();
    }

    public void initializeDeck(){
        cards.add(new Card("Duke"));
        cards.add(new Card("Duke"));
        cards.add(new Card("Duke"));
        cards.add(new Card("Captain"));
        cards.add(new Card("Captain"));
        cards.add(new Card("Captain"));
        cards.add(new Card("Ambassador"));
        cards.add(new Card("Ambassador"));
        cards.add(new Card("Ambassador"));
        cards.add(new Card("Contessa"));
        cards.add(new Card("Contessa"));
        cards.add(new Card("Contessa"));
        cards.add(new Card("Assassin"));
        cards.add(new Card("Assassin"));
        cards.add(new Card("Assassin"));

    }
    public void shuffle(){
        Collections.shuffle(cards);
    }

    public String drawCard(){
        Card card = cards.remove(0);
        return card.getName();
    }

    private String swapCard(String cardName){
        cards.add(new Card(cardName));
        Card card = cards.remove(0);
        return card.getName();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Deck: [");
        for (int i = 0; i < cards.size(); i++) {
            sb.append(cards.get(i));
            if (i < cards.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public void addCardToBottomOfDeck(Card card){
        this.cards.add(card);
    }

}
