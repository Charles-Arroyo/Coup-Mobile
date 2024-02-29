package onetoone.Card;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import onetoone.Users.User;




@Entity
public class Card {


    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )

    private String CardID;

    private String CardAbility;

    private String CardHolderName;

    private int actionCost;

    private int numOfCards;



    @OneToOne
    @JsonIgnore
    private User user;

    public Card(String CardID,String CardHolderName,int actionCost, int numOfCards){
        this.CardID = CardID;
        this.CardHolderName = CardHolderName;
        this.actionCost = actionCost;
        this.numOfCards = numOfCards;

    }

    public Card(){

    }

    public String getCardID() {
        return CardID;
    }

    public void setCardID(String cardID) {
        CardID = cardID;
    }

    public String getCardAbility() {
        return CardAbility;
    }

    public void setCardAbility(String cardAbility) {
        CardAbility = cardAbility;
    }

    public String getCardHolderName() {
        return CardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        CardHolderName = cardHolderName;
    }

    public int getActionCost() {
        return actionCost;
    }

    public void setActionCost(int actionCost) {
        this.actionCost = actionCost;
    }

    public int getNumOfCards() {
        return numOfCards;
    }

    public void setNumOfCards(int numOfCards) {
        this.numOfCards = numOfCards;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
