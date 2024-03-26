package database.Users;

import database.Friends.Friend;
import database.Friends.FriendRepository;
import database.Chat.MessageRepository;
import database.game.Game;
import database.game.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

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
    GameRepository gameRepository;

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

    @GetMapping("/getId/{email}")
    public int getUserByEmail(@PathVariable String email) {
        Optional<User> user = userRepository.findByUserEmail(email);
        if (user != null) {
            return user.get().getId();
        } else {
            return -1;
        }
    }


    /**
     * Creates a user, need to account for same emails
     * @param user
     * @return
     */
    @PostMapping(path = "/signup")
    String signUp(@RequestBody User user) {
        if (user != null) { //user is not null
            Game newGame = new Game();
            // Initialize newGame properties...
            gameRepository.save(newGame);
//            settingRepository.save(newSetting);
            user.setGaming(newGame); // Assuming setUser correctly sets up the relationship
//            user.setSetting(newSetting);
            // Initialize other newUser properties...
            userRepository.save(user);
            return success;
        }else{ //Null
            return failure; //Return a Failure
        }
    }

    /**
     * Checks the repo, and allows user to sign in
     * @param user
     * @return
     */
    @PostMapping(path = "/signin")
    public String signIn(@RequestBody User user) { //sends a request body of password & username
        Optional<User> foundUser = userRepository.findByUserEmail(user.getUserEmail()); // Creates a user object with the users email passed in
        if (foundUser.isPresent() && foundUser.get().getPassword().equals(user.getPassword())) {
            User actualUser = foundUser.get();
            actualUser.setIsOnline(true);
            userRepository.save(actualUser); // Save the updated user back to the database
            return success;
        } else {
            return invalidSignIn;
        }
    }


    @PostMapping(path = "/createFriend")
    String createFriendRelationship(@RequestBody Friend friend){ //creating table
        Optional<User> user1 = userRepository.findByUserEmail(friend.getFriendEmail1()); // Creates temp a user object with the first email passed in
        Optional<User> user2 = userRepository.findByUserEmail(friend.getFriendEmail2()); // Creates second temp user object with second email
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

    /**
     * This method finds an existing user, and updates to change username/password. This can be used for
     * a user settings
     * @param id
     * @param request
     * @return
     */
    @PutMapping("/users/{id}")
    User updateUser(@PathVariable int id, @RequestBody User request){
        User user = userRepository.findById(id);
        if(user == null)
            return null;
        userRepository.save(request);
        return userRepository.findById(id);
    }

    @PutMapping("/email/change/{userEmail}")
    @Transactional
    public ResponseEntity<String> changeUserEmail(@PathVariable String userEmail, @RequestBody User updateRequest) {
        if (updateRequest.getUserEmail() == null || updateRequest.getUserEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("{\"message\":\"Invalid email\"}");
        }

        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User email not found"));

        user.setUserEmail(updateRequest.getUserEmail()); // Assuming setUserEmail is the correct method to update the email
        userRepository.save(user);

        return ResponseEntity.ok("{\"message\":\"Email updated successfully\"}");
    }

    @PutMapping("/password/change/{userEmail}")
    @Transactional
    public ResponseEntity<String> changeUserPassword(@PathVariable String userEmail, @RequestBody User passwordUpdateRequest) {
        if (passwordUpdateRequest.getPassword() == null || passwordUpdateRequest.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("{\"message\":\"Invalid password\"}");
        }

        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User email not found"));

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
}
