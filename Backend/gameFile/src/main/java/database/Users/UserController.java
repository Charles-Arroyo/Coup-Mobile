package database.Users;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.*;

/**
 * 
 * @author Charles Arroyo
 * @author Bo Oo
 * 
 */ 

@RestController
public class UserController {

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

//    @Autowired
//    SettingRepository settingRepository; // //Creating a repository(mySQL of Friends)

    @Autowired
    private StatRepository statRepository;

    @Autowired
    private RankingRepository rankingRepository;

    private String success = "{\"success\":true}"; //Sends a JSON boolean object named success

    private String failure = "{\"fail\":false}"; //Sends a JSON String object named message

//    private String invalidSignIn = "Wrong SignId"; // This will return a string that alert the user that they dont have the right user.


//    /**
//     * Gets all users in the user repo
//     *
//     * @return
//     */
//    @GetMapping(path = "/users")
//    List<User> getAllUsers() {
//        return userRepository.findAll();
//    }

    /**
     * Gets a user based on unique ID
     *
     * @param id
     * @return
     */
    @GetMapping(path = "/users/{id}")
    User getUserById(@PathVariable int id) {
        return userRepository.findById(id);
    }

//    @GetMapping("/getId/{userEmail}")
//    public int getUserByEmail(@PathVariable String userEmail) {
//        User user = userRepository.findByUserEmail(userEmail);
//        if (user != null) {
//            return user.getId();
//        } else {
//            return -1;
//        }
//    }

    @Transactional
    @DeleteMapping(path = "/deleteLobby/{lobbyId}")
    public boolean deleteLobbby(@PathVariable int lobbyId){
        lobbyRepository.deleteById(lobbyId);
        return true;
    }


    /**
     * Creates a user, need to account for same emails
     *
     * @param user
     * @return
     */
    @PostMapping(path = "/signup")
    String signUp(@RequestBody User user) {
        if (user != null && userRepository.findByUserEmail(user.getUserEmail()) == null) { //user is not null
            Stat newStat = new Stat();
            // Initialize newStat properties...
            statRepository.save(newStat);
            user.setStat(newStat); // Assuming setUser correctly sets up the relationship
            // Initialize other newUser properties...
            userRepository.save(user);
            return success;
        } else { //Null
            return failure; //Return a Failure
        }
    }


    /**
     * Deletes a user, can be used in the user setting
     * @param id
     * @return
     */
    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
        return success;
    }

    /**
     *
     * @param userEmail
     * @return
     */
    @GetMapping(path = "/getUserActive/{userEmail}")
    public ResponseEntity<Map<String, Object>> getUserActivity(@PathVariable String userEmail) {
        User user = userRepository.findByUserEmail(userEmail);

        if (user != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("userEmail", user.getUserEmail());
            response.put("active", user.isActive());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Allows the user to change their email
     * @param userEmail
     * @param updateUserEmail
     * @return
     */
    @PutMapping(path = "/changeEmail/{userEmail}")
    @Transactional
    public String changeEmail(@PathVariable String userEmail, @RequestBody User updateUserEmail) {
        if (updateUserEmail.getUserEmail() == null) {
            return failure;
        }

        User user = userRepository.findByUserEmail(userEmail);

        if (user == null) {
            return failure;
        }else {
            user.setUserEmail(updateUserEmail.getUserEmail());
            userRepository.save(user);

            return success;
        }
    }

    /**
     * This will allow the user to change their password
     * @param userEmail
     * @param updateUserPassword
     * @return
     */

    @PutMapping(path = "/changePass/{userEmail}")
    @Transactional
    public ResponseEntity<String> changePassword(@PathVariable String userEmail, @RequestBody User updateUserPassword) {
        if (updateUserPassword.getPassword() == null) {
            return ResponseEntity.badRequest().body("{\"message\":\"Invalid password\"}");
        }

        User user = userRepository.findByUserEmail(userEmail);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"User not found\"}");
        }else {

            // Ensure the user is authorized to change the password here
            user.setPassword(updateUserPassword.getPassword());
            userRepository.save(user);

            return ResponseEntity.ok("{\"success\":true}");
        }
    }



    @PutMapping(path = "/changeName/{userEmail}")
    @Transactional
    public ResponseEntity<String> changeName(@PathVariable String userEmail, @RequestBody User updateUserName) {
        if (updateUserName.getName() == null) {
            return ResponseEntity.badRequest().body("{\"message\":\"Invalid Name\"}");
        }

        User user = userRepository.findByUserEmail(userEmail);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"User not found\"}");
        }else {

            // Ensure the user is authorized to change the password here
            user.setName(updateUserName.getName());
            userRepository.save(user);

            return ResponseEntity.ok("{\"success\":true}");
        }
    }



}
