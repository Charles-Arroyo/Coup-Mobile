package onetoone.Card;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardController {

    @Autowired
    CardRepository cardRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/cards")
    List<Card> getAllCard(){
        return cardRepository.findAll();
    }

    @GetMapping(path = "/cards/{id}")
    Card getCardById(@PathVariable int id){
        return cardRepository.findById(id);
    }

    @PostMapping(path = "/cards")
    String createCard(@RequestBody Card Card){
        if (Card == null)
            return failure;
        cardRepository.save(Card);
        return success;
    }

    @PutMapping(path = "/card/{id}")
    Card updateCard(@PathVariable int id, @RequestBody Card request){
        Card card = cardRepository.findById(id);
        if(card == null)
            return null;
        cardRepository.save(request);
        return cardRepository.findById(id);
    }

    @DeleteMapping(path = "/card/{id}")
    String deleteCard(@PathVariable int id){
        cardRepository.deleteById(id);
        return success;
    }
}
