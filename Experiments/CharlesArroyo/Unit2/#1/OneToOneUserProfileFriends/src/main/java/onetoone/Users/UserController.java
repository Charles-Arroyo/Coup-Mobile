package onetoone.Users;

import onetoone.Friends.Friend;
import onetoone.Friends.FriendRepository;
import onetoone.Setting.Setting;
import onetoone.Setting.SettingRepository;
import onetoone.game.Game;
import onetoone.game.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;

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
    FriendRepository friendRepository; // //Creating a repository(mySQL of Friends)

    @Autowired
    SettingRepository settingRepository; // //Creating a repository(mySQL of Friends)

    @Autowired
    GameRepository gameRepository;

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
     * Gets a user based on unique ID
     *
     * @param id
     * @return
     */
    @GetMapping(path = "/users/{id}")
    User getUserById(@PathVariable int id) {
        return userRepository.findById(id);
    }

    @GetMapping("/getUser/{email}")
    public int getUserByEmail(@PathVariable String email) {
        User user = userRepository.findByUserEmail(email);
        if (user != null) {
            return user.getId();
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
            Setting newSetting = new Setting(); // Assume default properties are set in the constructor
            Game newGame = new Game();
            // Initialize newGame properties...
            gameRepository.save(newGame);
            settingRepository.save(newSetting);
            user.setGaming(newGame); // Assuming setUser correctly sets up the relationship
            user.setSetting(newSetting);
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
        User foundUser = userRepository.findByUserEmail(user.getUserEmail()); // Creates a user object with the users email passed in
        if (foundUser != null && foundUser.getPassword().equals(user.getPassword())) {
            return success;
        }else{
            return failure;
        }
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
        friend.setAcceptance(true);
        friendRepository.save(friend);
        return success;
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
