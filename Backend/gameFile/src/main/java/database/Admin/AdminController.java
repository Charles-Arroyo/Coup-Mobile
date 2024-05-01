package database.Admin;

import database.Friends.Friend;
import database.Friends.FriendRepository;

import database.Chat.MessageRepository;
import database.Lobby.LobbyRepository;
import database.Ranking.Ranking;
import database.Ranking.RankingRepository;
import database.Signin.Signin;
import database.Signin.SigninRepository;
import database.Spectator.SpectatorRepository;
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

    @Autowired
    private SpectatorRepository spectatorRepository;

    private String success = "{\"success\":true}"; //Sends a JSON boolean object named success

    private String failure = "{\"fail\":false}"; //Sends a JSON String object named message

    /**
     * Gets all users in the user repo
     *
     * @return
     */
    @GetMapping(path = "/users")
    public Map<String, Object> getAllUsers() {
        List<User> users = userRepository.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        return response;
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
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



    /**
     * This will allow the Admin
     * to delete user from the repository
     * @param userEmail
     * @return
     */
    @Transactional
    @DeleteMapping(path = "/deleteUser/{userEmail}")
    public ResponseEntity<?> deleteUser(@PathVariable String userEmail) {
        User user = userRepository.findByUserEmail(userEmail);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"User not found\"}");
        }

        // Remove the user from the lobby
        user.setLobby(null);

        // Remove the user from the ranking
        user.setRanking(null);


        // Delete the associated stat record
        Stat stat = user.getStat();
        if (stat != null) {
            statRepository.delete(stat);
        }

        signinRepository.deleteSigninByUser(user);
        spectatorRepository.deleteByUserId(user.getId());

        // Delete the user
        userRepository.delete(user);

        return ResponseEntity.ok("{\"success\":true}");
    }
    /**
     * This will allow the Admin to reset
     * the score
     * @param userEmail
     * @return
     */
    @PutMapping(path = "/resetScore/{userEmail}")
    public ResponseEntity<?> resetScore(@PathVariable String userEmail) {
        User user = userRepository.findByUserEmail(userEmail);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"User not found\"}");
        }

        user.setPoints(0);
        userRepository.save(user);
        return ResponseEntity.ok("{\"success\":true}");
    }

    /**
     * Get a list of user's signin
     *
     * @param userEmail
     * @return
     */
    @GetMapping(path = "/getUserLogs/{userEmail}")
    public ResponseEntity<Map<String, Object>> getSignInLog(@PathVariable String userEmail) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUserEmail(userEmail));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Signin> signInLogs = signinRepository.findByUser(user);

            List<Map<String, Object>> signInLogList = new ArrayList<>();
            for (Signin signIn : signInLogs) {
                Map<String, Object> signInLog = new HashMap<>();
                signInLog.put("id", signIn.getId());
                signInLog.put("userId", signIn.getUser().getId());
                signInLog.put("lastSignInTimestamp", signIn.getLastSignInTimestamp());
                signInLog.put("signInCount", signIn.getSignInCount());
                signInLog.put("lastSignOutTimestamp", signIn.getLastSignOutTimestamp());
                signInLogList.add(signInLog);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("userId", user.getId());
            response.put("signInLogs", signInLogList);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping(path = "/globalStat")
    public ResponseEntity<?> globalStat(){
        User user;
        int allUsers = userRepository.findAll().size();
        int i = 0;
        int active = 0;
        int notActive = 0;

        while(i < allUsers) {
            user = userRepository.findAll().get(i);
            if(user.isActive()){
                active++;
            }else{
                notActive++;
            }
            i++;
        }
        Map<String, Object> response = new HashMap<>();
        response.put("total_players", allUsers);
        response.put("active_players", active);
        response.put("not_active_players", notActive);
        return ResponseEntity.ok(response);

    }


}
