package database.Users;

import database.Friends.Friend;
import database.Friends.FriendRepository;
import database.FriendRequest.FriendRequest;
import database.Setting.Setting;
import database.Setting.SettingRepository;
import database.Chat.MessageRepository;
import database.game.Game;
import database.game.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
     * Deletes a user, can be used in the user setting
     * @param id
     * @return
     */
    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
        return success;
    }



//    @PostMapping(path = "/acceptFriendOrNot/{yesOrNo}/{userEmail}/{askingPerson}")
//    public Map<String, Object> acceptFriendOrNot(@PathVariable boolean yesOrNo, @PathVariable String userEmail, @PathVariable String askingPerson) {
//        Map<String, Object> response = new HashMap<>();
//        User userAdding = userRepository.findByUserEmail(userEmail);
//        User askingUser = userRepository.findByUserEmail(askingPerson);
//
//        FriendRequest friendRequest = userAdding.getReceivedFriendRequests().stream()
//                .filter(fr -> fr.getRequestingUser().equals(askingUser))
//                .findFirst()
//                .orElse(null);
//
//        if (yesOrNo) {
//            if (friendRepository.findByUser1AndUser2(userAdding, askingUser).isPresent() || friendRepository.existsByUser1AndUser2(userAdding, askingUser)) {
//                response.put("success", false);
//                response.put("message", "Friendship already exists");
//                return response;
//            }
//
//            Friend friend1 = new Friend();
//            friend1.setUser1(userAdding);
//            friend1.setUser2(askingUser);
//            friend1.setAccepted(true);
//
//            Friend friend2 = new Friend();
//            friend2.setUser1(askingUser);
//            friend2.setUser2(userAdding);
//            friend2.setAccepted(true);
//
//            friendRepository.save(friend1);
//            friendRepository.save(friend2);
//
//            if (friendRequest != null) {
//                userAdding.removeFriendRequest(friendRequest);
//                askingUser.removeFriendRequest(friendRequest);
//                friendRequestRepository.delete(friendRequest);
//                userRepository.save(userAdding);
//                userRepository.save(askingUser);
//            }
//
//            response.put("success", true);
//            response.put("message", "Friend request accepted successfully");
//        } else {
//            if (friendRequest != null) {
//                userAdding.removeFriendRequest(friendRequest);
//                askingUser.removeFriendRequest(friendRequest);
//                friendRequestRepository.delete(friendRequest);
//                userRepository.save(userAdding);
//                userRepository.save(askingUser);
//            }
//
//            response.put("success", false);
//            response.put("message", "Friend request rejected");
//        }
//
//        return response;
//    }
//
//    @PostMapping(path = "/sendRequest/{userEmail}/{friendWannaBe}")
//    public Map<String, Object> friendRequest(@PathVariable String userEmail, @PathVariable String friendWannaBe) {
//        Map<String, Object> response = new HashMap<>();
//
//        User friendRequestSender = userRepository.findByUserEmail(userEmail);
//        User friendToBe = userRepository.findByUserEmail(friendWannaBe);
//
//        if (friendToBe == null || friendRequestSender == null) {
//            response.put("success", false);
//            response.put("message", "One or both users do not exist");
//            return response;
//        }
//
//        if (friendRepository.findByUser1AndUser2(friendRequestSender, friendToBe) != null || friendRepository.existsByUser1AndUser2(friendRequestSender,friendToBe)) {
//            response.put("success", false);
//            response.put("message", "Users are already friends");
//            return response;
//        }
//
//        FriendRequest existingRequest = friendToBe.getReceivedFriendRequests().stream()
//                .filter(fr -> fr.getRequestingUser().equals(friendRequestSender))
//                .findFirst()
//                .orElse(null);
//
//        if (existingRequest != null) {
//            response.put("success", false);
//            response.put("message", "Friend request already exists");
//            return response;
//        }
//
//        friendRequestSender.sendFriendRequest(friendToBe);
//        userRepository.save(friendRequestSender);
//
//        response.put("success", true);
//        response.put("message", "Friend request sent successfully");
//        return response;
//    }
//    @GetMapping(path = "/gotFriendRequest/{userEmails}")
//    public Map<String, Object> gotFriendRequest(@PathVariable String userEmails) {
//        List<String> userEmailList = Arrays.asList(userEmails.split(","));
//        Map<String, Object> response = new HashMap<>();
//        List<Map<String, String>> friendRequestList = new ArrayList<>();
//
//        for (String userEmail : userEmailList) {
//            User user = userRepository.findByUserEmail(userEmail);
//            if (user != null) {
//                List<FriendRequest> receivedFriendRequests = user.getReceivedFriendRequests();
//
//                for (FriendRequest friendRequest : receivedFriendRequests) {
//                    User requestingUser = friendRequest.getRequestingUser();
//                    Map<String, String> friendRequestData = new HashMap<>();
//                    friendRequestData.put("email", requestingUser.getUserEmail());
//                    friendRequestData.put("name", requestingUser.getName());
//                    friendRequestList.add(friendRequestData);
//                }
//            }
//        }
//
//        if (friendRequestList.isEmpty()) {
//            response.put("message", "No friend requests found");
//        } else {
//            response.put("requests", friendRequestList);
//        }
//
//        return response;
//    }
}
