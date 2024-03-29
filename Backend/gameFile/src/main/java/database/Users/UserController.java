package database.Users;

import database.Friends.Friend;
import database.Friends.FriendRepository;
import database.Setting.Setting;
import database.Setting.SettingRepository;
import database.Chat.MessageRepository;
import database.game.Game;
import database.game.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    MessageRepository messageRepository; //Creating a repository(mySQL of users)

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

    @GetMapping("/getId/{email}")
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

//    /**
//     *
//     * @param friend
//     * @return
//     */
//    @PostMapping(path = "/createFriend")
//    String createFriendRelationship(@RequestBody Friend friend){ //creating table
//        User user1 = userRepository.findByUserEmail(friend.getFriendEmail1()); // Creates temp a user object with the first email passed in
//        User user2 = userRepository.findByUserEmail(friend.getFriendEmail2()); // Creates second temp user object with second email
////        if((user1.getUserEmail() == null || user2.getUserEmail() == null)){ //makes sure repo is not null
////            return failure;
////        }
//        if(friendRepository.friendshipExistsByUserEmails(friend.getFriendEmail1(),friend.getFriendEmail2())){ //Makes sure FriendShip repo does not have it
//            return "Friendship exists";
//        }
//        friend.setAcceptance(true); // Put this in a seperate method
//        friendRepository.save(friend);
//        return success;
//    }

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

//    /**
//     * This will allow the user to create a friend if the user accepts the accept button
//     * @param friend
//     * @return
//     */
//    @PutMapping(path = "/createFriend")
//    String acceptFriend(@RequestBody Friend friend){ //creating table
//        User user1 = userRepository.findByUserEmail(friend.getFriendEmail1()); // Creates temp a user object with the first email passed in
//        User user2 = userRepository.findByUserEmail(friend.getFriendEmail2()); // Creates second temp user object with second email
////        if((user1.getUserEmail() == null || user2.getUserEmail() == null)){ //makes sure repo is not null
////            return failure;
////        }
//        if(friendRepository.friendshipExistsByUserEmails(friend.getFriendEmail1(),friend.getFriendEmail2())){ //Makes sure FriendShip repo does not have it
//            return "Friendship exists";
//        }
//        friend.setAcceptance(true); // Put this in a seperate method
//        friendRepository.save(friend);
//        return success;
//    }





//    @PostMapping( = "/friendRequest/{userEmail}")
//    public String friendRequest(@PathVariable String userEmail,@RequestBody String friendRequestEmail){
//        User friendRequestSender = userRepository.findByUserEmail(userEmail);
//        User friendToBe;
//        if(userRepository.findByUserEmail(friendRequestEmail) != null){
//            friendToBe = userRepository.findByUserEmail(friendRequestEmail);// check for the person's email;
//        }else {
//            return failure;
//        }
//
//        friendToBe.getFriendRequest(friendRequestSender, true);
//
//        return success;
//    }
    //not done
    @PutMapping(path = "/acceptFriendOrNot/{userEmail}")
    String acceptFriendOrNot(@PathVariable User userEmail,@RequestBody boolean yesOrNo){ //creating table
    Friend friend = null;
    if(yesOrNo == true){
    friend.setFriendEmail1(userEmail.getUserEmail()); //user accepting
    friend.setFriendEmail2(userEmail.friendRequestPersonName());//user being accepted;

    //        if((user1.getUserEmail() == null || user2.getUserEmail() == null)){ //makes sure repo is not null
    //            return failure;
    //        }

        if(friendRepository.friendshipExistsByUserEmails(userEmail.getUserEmail(),userEmail.friendRequestPersonName())){ //Makes sure FriendShip repo does not have it
            return "Friendship exists";
        }
        friend.setAcceptance(true); // Put this in a seperate method
        friendRepository.save(friend);
        return success;
    }else{
        return failure;
    }
    }

    //done
    public String friendRequest( String userEmail,String friendRequestEmail){
        User friendRequestSender = userRepository.findByUserEmail(userEmail);
        User friendToBe;
        if(userRepository.findByUserEmail(friendRequestEmail) != null){
            friendToBe = userRepository.findByUserEmail(friendRequestEmail);// check for the person's email;
        }else {
            return failure;
        }

        friendToBe.getFriendRequest(friendRequestSender, true);

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
    


}
