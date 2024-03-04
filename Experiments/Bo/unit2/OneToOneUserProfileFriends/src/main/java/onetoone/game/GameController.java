package onetoone.game;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import onetoone.Users.User;
import onetoone.Users.UserRepository;

import java.util.Optional;


@RestController
public class GameController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GameRepository gameRepository;

    private String success = "{\"success\":true}"; //Sends a JSON boolean object named success

    private String failure = "{\"message\":\"failure\"}"; //Sends a JSON String object named message

    @GetMapping(path = "/totalGame/{id}")
    public ResponseEntity<?> getTotalGame(@PathVariable Long id) {
        Optional<Game> game = gameRepository.findById(id); // Assuming you have a gameRepository

        if (!game.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"Game not found\"}");
        }

        return ResponseEntity.ok(game.get().getGame());
    }

    @GetMapping(path = "/totalWonGame/{id}")
    public ResponseEntity<?> totalWonGame(@PathVariable Long id) {
        Optional<Game> game = gameRepository.findById(id); // Assuming you have a gameRepository

        if (!game.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"Game not found\"}");
        }

        return ResponseEntity.ok(game.get().getGameWon());
    }

    @GetMapping(path = "/totalGameLost/{id}")
    public ResponseEntity<?> totalGameLost(@PathVariable Long id) {
        Optional<Game> game = gameRepository.findById(id); // Assuming you have a gameRepository

        if (!game.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"Game not found\"}");
        }

        return ResponseEntity.ok(game.get().getGameLost());
    }




}
