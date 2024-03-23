package database.game;


import database.Friends.Friend;
import database.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import database.Users.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
public class GameController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GameRepository gameRepository;

    private String success = "{\"success\":true}"; //Sends a JSON boolean object named success

    private String failure = "{\"message\":\"failure\"}"; //Sends a JSON String object named message


//    @GetMapping(path = "/gameTotal/{userId}")
//    public String getGameTotalScore(@PathVariable Long userId) {
//        Optional<Game> gameOpt = gameRepository.findById(userId);
//        if (gameOpt.isPresent()) {
//            Game game = gameOpt.get();
//            // Assuming the Game entity has a method to get the total score
//            String totalScore = game.getGameResult();
//            return totalScore;
//        } else {
//            return failure;
//        }
//    }


//
    // Endpoint to increment the win counter for a user's game
    @PutMapping(path = "/gameTotal/{id}")
    public String gameWon(@RequestBody Game gameResult,@PathVariable int id) {

        User user = userRepository.findById(id);
        if (user == null){
            return failure;
        }
        Game game = user.getGame(); // Assuming User has a direct association with Game

        // Increment the number of games played
        game.addGamePlayed();
        if(gameResult.getGameResult().equals("Win")){
            game.incrementGameWon(); // Method to increment the win counter
            gameRepository.save(game);
            return success;

        }

        if(gameResult.getGameResult().equals("Loss")){
            game.incrementGameLost(); // Method to increment the loss counter
            gameRepository.save(game);
            return success;

        }
        gameRepository.save(game); // Save the updated game statistics
        return failure;
    }


        @GetMapping(path = "/getStats/{id}")
        public Game list(@PathVariable int id) {
            //ID
            //Game
            //Print Game
            User user = userRepository.findById(id);
            Game userGame = user.getGame();
            return userGame;

        }

//
//    @PostMapping(path = "/gameTotal/{id}")
//    public ResponseEntity<?> gameWon(@RequestBody String gameResult, @PathVariable Long id) {
//        Optional<User> userOpt = userRepository.findById(id);
//
//        if (!userOpt.isPresent()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"User not found\"}");
//        }
//
//        User user = userOpt.get();
//        Game game = user.getGame(); // Assuming User has a direct association with Game
//
//        if (game == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"Game not found for user\"}");
//        }
//
//        if ("win".equals(gameResult)) {
//            game.incrementGameWon(); // Method to increment the win counter
//            gameRepository.save(game);
//            return ResponseEntity.ok("{\"message\":\"Game win incremented successfully\"}");
//        } else if ("loss".equals(gameResult)) {
//            game.incrementGameLost(); // Method to increment the loss counter
//            gameRepository.save(game);
//            return ResponseEntity.ok("{\"message\":\"Game loss incremented successfully\"}");
//        } else {
//            return ResponseEntity.badRequest().body("{\"message\":\"Invalid game result\"}");
//        }
//    }


}
