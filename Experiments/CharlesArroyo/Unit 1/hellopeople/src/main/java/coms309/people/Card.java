package coms309.people;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/*
This is a card class, it mimics how a card will work
 */

public class Card {
    private String name;
    public Card(){

    }

    public Card(String name){
        this.name = name;

    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }



    @Override
    public String toString() {
        return name;
    }
    }
