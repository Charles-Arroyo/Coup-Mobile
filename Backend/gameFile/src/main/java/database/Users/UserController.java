package database.Users;

import database.Friends.Friend;
import database.Friends.FriendRepository;

import database.Chat.MessageRepository;
import database.Stats.Stat;
import database.Stats.StatRepository;
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
    UserRepository userRepository; //Creating a repository(mySQL of users)


    @Autowired
    MessageRepository messageRepository; //Creating a repository(mySQL of users)

    @Autowired
    FriendRepository friendRepository; // //Creating a repository(mySQL of Friends)

    @Autowired
    StatRepository statRepository;

    private String success = "{\"success\":true}"; //Sends a JSON boolean object named success

    private String failure = "{\"fail\":false}"; //Sends a JSON String object named message

//    private String invalidSignIn = "Wrong SignId"; // This will return a string that alert the user that they dont have the right user.


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
     * Gets a user based on unique ID
     *
     * @param id
     * @return
     */
    @GetMapping(path = "/users/{id}")
    User getUserById(@PathVariable int id) {
        return userRepository.findById(id);
    }

    @GetMapping("/getId/{userEmail}")
    public int getUserByEmail(@PathVariable String userEmail) {
        User user = userRepository.findByUserEmail(userEmail);
        if (user != null) {
            return user.getId();
        } else {
            return -1;
        }
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
     * Checks the repo, and allows user to sign in
     *
     * @param user
     * @return
     */
    @PostMapping(path = "/signin")
    public String signIn(@RequestBody User user) { //sends a request body of password & username
        User foundUser = userRepository.findByUserEmail(user.getUserEmail()); // Creates a user object with the users email passed in
        if (foundUser != null && foundUser.getPassword().equals(user.getPassword())) {
            return success;
        }else{
            return failure;
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


}
