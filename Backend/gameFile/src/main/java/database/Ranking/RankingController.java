package database.Ranking;

import database.Stats.Stat;
import database.Users.User;
import database.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class RankingController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RankingRepository rankingRepository;

//    @GetMapping(path = "/getListUserRanking")
//    public Map<String, Object> rankingList() {
//        // Retrieve the ranking with the highest ID (assuming there's only one ranking)
//        Ranking ranking;
////        if (rankingRepository.count() == 0) {
////            // If no ranking is found, create a new ranking
////            ranking = new Ranking();
////            ranking.setPoints(0);
////            rankingRepository.save(ranking);
////        } else {
//            ranking = rankingRepository.findTopByOrderByIdDesc()
//                    .orElseThrow(() -> new RuntimeException("No ranking found"));
////        }
//
//        // Retrieve all users from the UserRepository
//        List<User> allUsers = userRepository.findAll();
//
//        // Add all users to the ranking
//        allUsers.forEach(ranking::addUser);
//
//        // Set points for each user in the ranking
//        allUsers.forEach(user -> ranking.setPoints(user.getPoints()));
//
//        // Set name for each user in the ranking
//        allUsers.forEach(user -> ranking.setName(user.getName()));
//
//        // Save the updated ranking
//        rankingRepository.save(ranking);
//
//        // Retrieve the updated list of users in the ranking
//        List<User> rankedUsers = ranking.getUsers();
//
//        // Create a list of maps to store user's rank, username, and score
//        List<Map<String, Object>> rankings = new ArrayList<>();
//        int rank = 1;
//        for (User user : rankedUsers) {
//            Map<String, Object> userRanking = new HashMap<>();
//            userRanking.put("rank", rank++);
//            userRanking.put("username", user.getName());
//            userRanking.put("score", user.getPoints());
//            rankings.add(userRanking);
//        }
//
//        // Create a map to hold the rankings data
//        Map<String, Object> response = new HashMap<>();
//        response.put("rankings", rankings);
//
//        return response;
//    }

    @GetMapping(path = "/getListUserRanking")
    public ResponseEntity<Map<String, Object>> getRankingList() {
        Ranking ranking = getOrCreateAndUpdateRanking(); // Fetches or updates the ranking as needed

        // Create a list from the ranking's users, sorted by points in descending order
        List<User> sortedUsers = ranking.getUsers().stream()
                .sorted(Comparator.comparingInt(User::getPoints).reversed())
                .collect(Collectors.toList());

        List<Map<String, Object>> rankings = new ArrayList<>();
        int rank = 1; // Initialize rank
        for (int i = 0; i < sortedUsers.size(); i++) {
            // Check if the current user (not the first one) has the same points as the previous one to handle ties
            if (i > 0 && sortedUsers.get(i).getPoints() == sortedUsers.get(i - 1).getPoints()) {
                rank--; // Decrement rank to keep the same rank value for tied users
            }

            Map<String, Object> userRanking = Map.of(
                    "rank", rank,
                    "username", sortedUsers.get(i).getName(),
                    "score", sortedUsers.get(i).getPoints());
            rankings.add(userRanking);

            rank++; // Prepare the next rank value
        }

        Map<String, Object> response = new HashMap<>();
        response.put("rankings", rankings);
        return ResponseEntity.ok(response);
    }


    @Transactional
    protected Ranking getOrCreateAndUpdateRanking() {
        // Retrieve the latest ranking, or create a new one if none exists
        Ranking ranking = rankingRepository.findTopByOrderByIdDesc()
                .orElseGet(() -> new Ranking()); // Assuming constructor with name

        List<User> allUsers = userRepository.findAll();

        // Update user points before adding them to the ranking
        allUsers.forEach(user -> {
            if (user.getStat() != null) {
                user.setPoints(calculateUserPoints(user));
            }
        });

        // Clear current users and re-add to manage ranking consistently
        if (ranking.getUsers() != null) {
            ranking.getUsers().clear();
            allUsers.stream().filter(Objects::nonNull).forEach(ranking::addUser);
        }

        // Save the updated ranking
        return rankingRepository.save(ranking);
    }

    private int calculateUserPoints(User user) {
        Stat userStat = user.getStat();
        if (userStat != null) {
            int gameWon = userStat.getGameWon() != null ? userStat.getGameWon() : 0;
            int gameLost = userStat.getGameLost() != null ? userStat.getGameLost() : 0;
            return Math.max((gameWon * 10) - (gameLost * 2), 0);
        }
        return 0;
    }

}