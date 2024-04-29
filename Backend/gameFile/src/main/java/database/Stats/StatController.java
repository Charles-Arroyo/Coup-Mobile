package database.Stats;


import database.Ranking.Ranking;
import database.Ranking.RankingController;
import database.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import database.Users.UserRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class StatController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StatRepository statRepository;

    private String success = "{\"success\":true}"; //Sends a JSON boolean object named success

    private String failure = "{\"message\":\"failure\"}"; //Sends a JSON String object named message

    @Autowired
    private RankingController rankingController;


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


//        @GetMapping(path = "/getStats/{id}")
//        public Stat list(@PathVariable int id) {
//            //ID
//            //Game
//            //Print Game
//            User user = userRepository.findById(id);
//            Stat userStat = user.getStat();
//            return userStat;
//
//        }

    @GetMapping(path = "/getStats/{userEmail}")
    public Map<String, Object> list(@PathVariable String userEmail) {
        User user = userRepository.findByUserEmail(userEmail);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Stat userStat = user.getStat();

        if (userStat == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User statistics not found");
        }

        // Update the user's points based on their wins and losses
        user.setPoints(calculateUserPoints(user));

        // Update the ranking
        Ranking ranking = rankingController.getOrCreateAndUpdateRanking();

        // Find the user's rank in the ranking list
        int rank = calculateRank(user, ranking);

        // Calculate the user's score
        int score = user.getPoints();

        Map<String, Object> response = new HashMap<>();
        response.put("wins", userStat.getGameWon());
        response.put("loses", userStat.getGameLost());
        response.put("rank", rank);
        response.put("score", score);

        return response;
    }

    /**
     * this is to calculatethe user points
     * @param user
     * @return
     */
    private int calculateUserPoints(User user) {
        Stat userStat = user.getStat();
        if (userStat != null) {
            return Math.max((userStat.getGameWon() * 10) - (userStat.getGameLost() * 2), 0);
        }
        return 0;
    }

    /**
     * this is to calculate the user rank
     * @param user
     * @param ranking
     * @return
     */
    private int calculateRank(User user, Ranking ranking) {
        List<User> users = ranking.getUsers();
        int rank = 1;
        for (User u : users) {
            if (u.getId() == user.getId()) {
                break;
            }
            rank++;
        }
        return rank;
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
