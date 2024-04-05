package database.Ranking;

import database.Users.User;
import database.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RankingController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RankingRepository rankingRepository;

    @GetMapping(path = "/getListUserRanking")
    public List<User> rankingList() {
        // Retrieve the ranking with the highest ID (assuming there's only one ranking)
        Ranking ranking = rankingRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("No ranking found"));

        // Retrieve all users from the UserRepository
        List<User> allUsers = userRepository.findAll();

        // Add all users to the ranking
        allUsers.forEach(ranking::addUser);

        // Retrieve the updated list of users in the ranking
        List<User> rankedUsers = ranking.getUsers();

        return rankedUsers;
    }
}