package database.Admin;

import database.Friends.Friend;
import database.Friends.FriendRepository;

import database.Chat.MessageRepository;
import database.Lobby.LobbyRepository;
import database.Ranking.Ranking;
import database.Ranking.RankingRepository;
import database.Signin.Signin;
import database.Signin.SigninRepository;
import database.Stats.Stat;
import database.Stats.StatRepository;
import database.Ranking.RankingRepository;
import database.Users.UserRepository;
import database.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.security.PublicKey;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AdminController {

    @Autowired
    private UserRepository userRepository; //Creating a repository(mySQL of users)


    @Autowired
    private MessageRepository messageRepository; //Creating a repository(mySQL of users)

    @Autowired
    private FriendRepository friendRepository; // //Creating a repository(mySQL of Friends)

    @Autowired
    private LobbyRepository lobbyRepository; // //Creating a repository(mySQL of lobbies)

    @Autowired
    private SigninRepository signinRepository;

    @Autowired
    private StatRepository statRepository;

    @Autowired
    private RankingRepository rankingRepository;

    private String success = "{\"success\":true}"; //Sends a JSON boolean object named success

    private String failure = "{\"fail\":false}"; //Sends a JSON String object named message

    /**
     * Gets all users in the user repo
     *
     * @return
     */
    @GetMapping(path = "/users")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }


    /**
     * This will allow the admin to find the user
     * Individually
     * @param userEmail
     * @return
     */
    @GetMapping("/getId/{userEmail}")
    public User getUserByEmail(@PathVariable String userEmail) {
        User user = userRepository.findByUserEmail(userEmail);
        if (user != null) {
            return user;
        } else {
            return null;
        }
    }


    /**
     *  This will allow the admin to look at
     *  all the user's friend
     * @param userEmail
     * @return
     */
    @GetMapping(path = "/getFriends/{userEmail}")
    public ResponseEntity<Map<String, Object>> getFriendsByUserId(@PathVariable String userEmail) {
        Map<String, Object> response = new HashMap<>();

        User user = userRepository.findByUserEmail(userEmail);

        if (user == null) {
            response.put("success", false);
            response.put("message", "User does not exist");
            return ResponseEntity.badRequest().body(response);
        }

        List<Friend> friendships = friendRepository.findByUser1OrUser2(user, user);

        List<Map<String, String>> friendList = friendships.stream()
                .map(friendship -> {
                    User friend = friendship.getUser1().equals(user) ? friendship.getUser2() : friendship.getUser1();
                    Map<String, String> friendData = new HashMap<>();
                    friendData.put("name", friend.getName());
                    friendData.put("email", friend.getUserEmail());
                    return friendData;
                })
                .distinct() // Add this line to remove duplicates
                .collect(Collectors.toList());

        response.put("success", true);
        response.put("friends", friendList);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/getuser")


    /**
     * This will allow the Admin
     * to delete user from the repository
     * @param userEmail
     * @return
     */
    @DeleteMapping(path = "/deleteUser/{userEmail}")
    public ResponseEntity<String> deleteUser(@PathVariable String userEmail) {
        if(userRepository.findByUserEmail(userEmail) != null){
            User user = userRepository.findByUserEmail(userEmail);

            userRepository.delete(user);
            return ResponseEntity.ok("{\"success\":true}");
        }else{
            return ResponseEntity.ok("user not found.");
        }
    }

}
