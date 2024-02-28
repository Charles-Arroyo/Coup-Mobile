package onetoone.Players;
import java.util.List;

import onetoone.Players.Player;
import onetoone.Players.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {

    @Autowired
    PlayerRepository playerRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/players")
    List<Player> getAllPlayers(){
        return playerRepository.findAll();
    }

    @GetMapping(path = "/players/{id}")
    Player getPlayerByid(@PathVariable int id){
        return playerRepository.findById(id);
    }

    @PostMapping(path = "/players")
    String createPlayer(@RequestBody Player player){
        if (player == null)
            return failure;
        playerRepository.save(player);
        return success;
    }

    @PutMapping(path = "/players/{id}")
    Player updatePlayer(@PathVariable int id, @RequestBody Player request){
        Player player = playerRepository.findById(id);
        if(player == null)
            return null;
        playerRepository.save(request);
        return playerRepository.findById(id);
    }

    @DeleteMapping(path = "/players/{id}")
    String deletePlayer(@PathVariable int id){
        playerRepository.deleteById(id);
        return success;
    }
}
