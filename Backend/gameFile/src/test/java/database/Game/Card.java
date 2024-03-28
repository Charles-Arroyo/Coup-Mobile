package database.Game;

public class Card {
    private String name;

    public Card(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}
