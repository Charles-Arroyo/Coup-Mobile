package coms309.people;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController

public class CardController {
    HashMap<String, Card> CardList = new  HashMap<>();
    @GetMapping("/card")
    public  HashMap<String,Card> getAllPersons() {
        return CardList;
    }

    @GetMapping("/card/{name}")
    public Card getPerson(@PathVariable String name) {
        Card p = CardList.get(name);
        return p;
    }

    @PostMapping("/card")
    public  String createcard(@RequestBody Card card) {
        System.out.println(card);
        CardList.put(card.getName(), card);
        return "New card "+ card.getName() + " Saved";
    }



}
