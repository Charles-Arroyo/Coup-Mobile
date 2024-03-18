package onetoone.Users;

import onetoone.Friends.Friend;
import onetoone.Friends.FriendRepository;
import onetoone.Profiles.Profile;
import onetoone.Profiles.ProfileRepository;
import onetoone.Setting.Setting;
import onetoone.Setting.SettingRepository;
import onetoone.game.Game;
import onetoone.game.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    ProfileRepository profileRepository; //Creating a repository(mySQL of profiles)

    @Autowired
    FriendRepository friendRepository; // //Creating a repository(mySQL of Friends)

    @Autowired
    SettingRepository settingRepository;
    @Autowired
    GameRepository gameRepository;

    private String success = "{\"success\":true}"; //Sends a JSON boolean object named success

    private String failure = "{\"message\":\"failure\"}"; //Sends a JSON String object named message


    /**
     * Gets all users in the user repo
     * @return
     */
    @GetMapping(path = "/users")
    List<User> getAllUsers() {
        return userRepository.findAll();

    }



    /**
     * Gets a user based on unique ID
     * @param id
     * @return
     */
    @GetMapping(path = "/users/{id}")
    User getUserById(@PathVariable int id) {
        return userRepository.findById(id);
    }



    @PostMapping(path = "/signup")
    public String signUp(@RequestBody User user) {
        // Check if a user with the provided email already exists
        User foundUser = userRepository.findByEmailId(user.getEmailId());

//        // If a user is found, and the passwords match, it means they are already signed up
//        if (foundUser != null) {
//            // User already exists
//            return failure;
//        } else {

            Setting newSetting = new Setting(); // Assume default properties are set in the constructor
            Game newGame = new Game();
            // Initialize newGame properties...
            gameRepository.save(newGame);
            settingRepository.save(newSetting);

            user.setGaming(newGame); // Assuming setUser correctly sets up the relationship
            user.setSetting(newSetting);
            // Initialize other newUser properties...
            userRepository.save(user);


            // Return a success response
            return success;
        //}
    }


    @PostMapping(path = "/addFriend")
    String addFriend(@RequestBody User user) {
        User foundUser = userRepository.findByEmailId(user.getEmailId()); // Creates a user object
            
            return success;

    }


    /**
     * Checks the repo, and allows user to sign in
     * @param user
     * @return
     */
    @PostMapping(path = "/signin")
    public String signIn(@RequestBody User user) { //sends a request body of password & username
        User foundUser = userRepository.findByEmailId(user.getEmailId()); // Creates a user object with the users email passed in
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

    /**
     * This assigns a profile object to a user.
     * @param userId
     * @param profileId
     * @return
     */
    @PutMapping("/users/{userId}/profiles/{profileId}")
    String assignProfileToUser(@PathVariable int userId,@PathVariable int profileId){
        User user = userRepository.findById(userId);
        Profile profile = profileRepository.findById(profileId);
        if(user == null || profile == null) {
            return failure;
        }else {
            profile.setUser(user);
            user.setProfile(profile);
            userRepository.save(user);
            return success;
        }
    }

    /**
     * This assigns friend to a user
     * @param userId
     * @param friendId
     * @return
     */

        @PutMapping("/users/{userId}/friends/{friendId}")
        String assignFriendToUser(@PathVariable int userId,@PathVariable int friendId) {
            User user = userRepository.findById(userId);
            Friend friend = friendRepository.findById(friendId);
            if (user == null || friend == null){
                return failure;
            }else
            {
                friend.setUser(user);
                user.addFriends(friend);
                userRepository.save(user);
                return success;
            }



    }


    /**
     * Deletes a user, can be used in the user setting ss
     * @param id
     * @return
     */
    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
        return success;
    }

    // Endpoint to get a user's ID based on their email
    @GetMapping("/getId/{email}")
    public int getUserIdByEmail(@PathVariable String email) {
        User user = userRepository.findByEmailId(email);
        if (user != null) {
            return user.getSetting().getId();
        } else {
            return -1;
        }
    }

}
