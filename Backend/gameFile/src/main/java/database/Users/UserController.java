package database.Users;

import database.Friends.Friend;
import database.Friends.FriendRepository;
<<<<<<< HEAD
=======
import database.FriendRequest.FriendRequest;
import database.Setting.Setting;
import database.Setting.SettingRepository;
>>>>>>> bo_featuresBranch
import database.Chat.MessageRepository;
import database.Stats.Stat;
import database.Stats.StatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

<<<<<<< HEAD
import java.util.List;
import java.util.Optional;
=======
import java.util.*;
>>>>>>> bo_featuresBranch

/**
 * 
 * @author Charles Arroyo
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

//    @Autowired
//    SettingRepository settingRepository; // //Creating a repository(mySQL of Friends)

    @Autowired
    StatRepository statRepository;

    private String success = "{\"success\":true}"; //Sends a JSON boolean object named success

    private String failure = "{\"fail\":false}"; //Sends a JSON String object named message

    private String invalidSignIn = "Wrong SignId"; // This will return a string that alert the user that they dont have the right user.


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
//            settingRepository.save(newSetting);
            user.setStat(newStat); // Assuming setUser correctly sets up the relationship
//            user.setSetting(newSetting);
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

<<<<<<< HEAD
    @PostMapping(path = "/createFriend")
    String createFriendRelationship(@RequestBody Friend friend){ //creating table
        User user1 = userRepository.findByUserEmail(friend.getFriendEmail1()); // Creates temp a user object with the first email passed in
        User user2 = userRepository.findByUserEmail(friend.getFriendEmail2()); // Creates second temp user object with second email
//        if((user1.getUserEmail() == null || user2.getUserEmail() == null)){ //makes sure repo is not null
//            return failure;
//        }
        if(friendRepository.friendshipExistsByUserEmails(friend.getFriendEmail1(),friend.getFriendEmail2())){ //Makes sure FriendShip repo does not have it
            return "Friendship exists";
        }
        friend.setAcceptance(true); // Put this in a seperate method
        friendRepository.save(friend);
        return success;
    }
=======
>>>>>>> bo_featuresBranch

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

<<<<<<< HEAD
    @PutMapping("/email/change/{userEmail}")
    @Transactional
    public ResponseEntity<String> changeUserEmail(@PathVariable String userEmail, @RequestBody User updateRequest) {
        if (updateRequest.getUserEmail() == null || updateRequest.getUserEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("{\"message\":\"Invalid email\"}");
        }

        User user = userRepository.findByUserEmail(userEmail);
        user.setUserEmail(updateRequest.getUserEmail()); // Assuming setUserEmail is the correct method to update the email
        userRepository.save(user);

        return ResponseEntity.ok("{\"message\":\"Email updated successfully\"}");
    }
    }

    @PutMapping("/password/change/{userEmail}")
    @Transactional
    public ResponseEntity<String> changeUserPassword(@PathVariable String userEmail, @RequestBody User passwordUpdateRequest) {
        if (passwordUpdateRequest.getPassword() == null || passwordUpdateRequest.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("{\"message\":\"Invalid password\"}");
        }

        User user = userRepository.findByUserEmail(userEmail);

        // Ensure the user is authorized to change the password here
        // This might include verifying the old password, a security question, or an authentication token.

        user.setPassword(passwordUpdateRequest.getPassword()); // Directly set the new passwords
        userRepository.save(user);

        return ResponseEntity.ok("{\"message\":\"Password updated successfully\"}");
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


    //done
    @GetMapping(path = "/gotFriendRequest/{userEmail}")
    public String gotFriendRequest(@PathVariable String userEmail) {
        User user = userRepository.findByUserEmail(userEmail);
        if (user != null && user.IsFriendRequest()) {
            return user.friendRequestPersonName();
        } else {
            return failure;
        }
    }
    

=======
>>>>>>> bo_featuresBranch

}
