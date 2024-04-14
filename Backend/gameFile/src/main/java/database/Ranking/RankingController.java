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


    @GetMapping(path = "/getListUserRanking")
    public ResponseEntity<Map<String, Object>> getRankingList() {
        Ranking ranking = getOrCreateAndUpdateRanking(); // Fetches or updates the ranking as needed

        List<Map<String, Object>> rankings = new ArrayList<>();

        if (ranking != null && ranking.getUsers() != null) {
            // Create a list from the ranking's users, sorted by points in descending order
            List<User> sortedUsers = ranking.getUsers().stream()
                    .sorted(Comparator.comparingInt(User::getPoints).reversed())
                    .collect(Collectors.toList());

            int rank = 1; // Initialize rank
            for (int i = 0; i < sortedUsers.size(); i++) {
                // Check if the current user (not the first one) has the same points as the previous one to handle ties
                if (i > 0 && sortedUsers.get(i).getPoints() == sortedUsers.get(i - 1).getPoints()) {
                    rank--; // Decrement rank to keep the same rank value for tied users
                }

                User user = sortedUsers.get(i);
                String username = user.getName() != null ? user.getName() : "";
                Integer score = user.getPoints() != null ? user.getPoints() : 0;

                Map<String, Object> userRanking = Map.of(
                        "rank", rank,
                        "username", username,
                        "score", score);
                rankings.add(userRanking);

                rank++; // Prepare the next rank value
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("rankings", rankings);
        return ResponseEntity.ok(response);
    }

    @Transactional
    public Ranking getOrCreateAndUpdateRanking() {
        // Retrieve the latest ranking, or create a new one if none exists
        Ranking ranking = rankingRepository.findTopByOrderByIdDesc()
                .orElseGet(() -> new Ranking()); // Assuming constructor with name

        List<User> allUsers = userRepository.findAll();

        // Update user points before adding them to the ranking
        allUsers.forEach(user -> user.setPoints(calculateUserPoints(user)));

        if (ranking != null && ranking.getUsers() != null) {
            // Clear current users and re-add to manage ranking consistently
            ranking.getUsers().clear();

            // Filter out users with 0 points and either 0 wins or 0 losses
            List<User> filteredUsers = allUsers.stream()
                    .filter(user -> {
                        Stat userStat = user.getStat();
                        return user.getPoints() != 0 || (userStat != null && userStat.getGameWon() != 0 && userStat.getGameLost() != 0);
                    })
                    .collect(Collectors.toList());

            filteredUsers.forEach(ranking::addUser);
        }

        // Save the updated ranking
        return rankingRepository.save(ranking);
    }

    private int calculateUserPoints(User user) {
        Stat userStat = user.getStat();
        if (userStat != null) {
            return Math.max((userStat.getGameWon() * 10) - (userStat.getGameLost() * 2), 0);
        }
        return 0;
    }

}