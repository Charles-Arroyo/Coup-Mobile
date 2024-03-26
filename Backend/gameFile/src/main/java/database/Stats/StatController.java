package database.Stats;


import database.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import database.Users.UserRepository;


@RestController
public class StatController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StatRepository statRepository;

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
    public String gameWon(@RequestBody Stat gameResult, @PathVariable int id) {

        User user = userRepository.findById(id);
        if (user == null){
            return failure;
        }

        Stat stat = user.getStat(); // Assuming User has a direct association with Game

        // Increment the number of games played
        stat.addGamePlayed();
        if(gameResult.getGameResult().equals("Win")){
            stat.incrementGameWon(); // Method to increment the win counter
            statRepository.save(stat);
            return success;

        }

        if(gameResult.getGameResult().equals("Loss")){
            stat.incrementGameLost(); // Method to increment the loss counter
            statRepository.save(stat);
            return success;

        }
        statRepository.save(stat); // Save the updated stat statistics
        return failure;
    }


        @GetMapping(path = "/getStats/{id}")
        public Stat list(@PathVariable int id) {
            //ID
            //Game
            //Print Game
            User user = userRepository.findById(id);
            Stat userStat = user.getStat();
            return userStat;

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
