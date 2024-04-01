package database.Users;

import database.Friends.Friend;
import database.Friends.FriendRepository;
import database.Friends.FriendRequest;
import database.Setting.Setting;
import database.Setting.SettingRepository;
import database.Chat.MessageRepository;
import database.game.Game;
import database.game.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
//
//    /**
//     * This method finds an existing user, and updates to change username/password. This can be used for
//     * a user settings
//     * @param id
//     * @param request
//     * @return
//     */
//    @PutMapping("/users/{id}")
//    User updateUser(@PathVariable int id, @RequestBody User request){
//        User user = userRepository.findById(id);
//        if(user == null)
//            return null;
//        userRepository.save(request);
//        return userRepository.findById(id);
//    }

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
//    @PostMapping(path = "/acceptFriendOrNot/{yesOrNo}/{userEmail}/{askingPerson}")
//    String acceptFriendOrNot(@PathVariable boolean yesOrNo,@PathVariable String userEmail,@PathVariable String askingPerson){ //creating table
//    Friend friend = new Friend();
//    User userAdding = userRepository.findByUserEmail(userEmail);
//    if(yesOrNo == true){
//    friend.setFriendEmail1(userAdding.getUserEmail()); //user accepting
//    friend.setFriendEmail2(askingPerson);//user being accepted;
//
//        if(friendRepository.friendshipExistsByUserEmails(userAdding.getUserEmail(),userAdding.friendRequestPersonName())){ //Makes sure FriendShip repo does not have it
//            return "Friendship exists";
//        }
//        friend.setAcceptance(true); // Put this in a seperate method
//        friendRepository.save(friend);
//        return success;
//    }else{
//        return failure;
//    }
//    }
//
//    //done
//    @PostMapping(path = "/sendRequest/{userEmail}/{friendWannaBe}")
//    public String friendRequest(@PathVariable String userEmail,@PathVariable String friendWannaBe){
//
//        if(userRepository.findByUserEmail(friendWannaBe) == null && userRepository.findByUserEmail(userEmail) == null){ //if both user doesn't exit, return failure
//            return failure;
//        }else { // else return request
//
//
//            User friendToBe = userRepository.findByUserEmail(friendWannaBe);
//            User friendRequestSender = userRepository.findByUserEmail(userEmail);
//
//            if(!friendRepository.friendshipExistsByUserEmails(userEmail, friendWannaBe)
//                   && friendToBe.friendRequestPersonNames() != userEmail)
//            {
//                friendToBe.addFriendRequest(friendRequestSender.getUserEmail());
//                userRepository.save(friendToBe);
//            }else{
//                return failure; // return failure if users are friends
//            }
//
//            return success;
//        }
//    }

//
//    /**
//     * This return a list of people who have sent
//     * friend request to the user
//     * @param userEmail
//     * @return
//     */
//    @GetMapping(path = "/gotFriendRequest/{userEmail}")
//    public String gotFriendRequest(@PathVariable String userEmail) {
//        User user = userRepository.findByUserEmail(userEmail);
//        if (user != null) {
//            return user.friendRequestPersonName();
//        } else {
//            return failure;
//        }
//    }
//

//    @GetMapping(path = "/gotFriendRequest/{userEmails}")
//    List<String> gotFriendRequest(@PathVariable String userEmails) {
//        List<String> userEmailList = Arrays.asList(userEmails.split(","));
//        List<String> friendRequestPersonNames = new ArrayList<>();
//
//        for (String userEmail : userEmailList) {
//            User user = userRepository.findByUserEmail(userEmail); // Assuming this method exists
//            if (user != null) {
//                String friendRequestPersonName = user.friendRequestPersonName();
//                if (friendRequestPersonName != null) {
//                    friendRequestPersonNames.add(friendRequestPersonName);
//                }
//            }
//        }
//
//        if (friendRequestPersonNames.isEmpty()) {
//            return Collections.singletonList("No friend requests found");
//        }
//
//        return friendRequestPersonNames;
//    }

    /**
     *
     * @param yesOrNo
     * @param userEmail
     * @param askingPerson
     * @return
     */
//    @PostMapping(path = "/acceptFriendOrNot/{yesOrNo}/{userEmail}/{askingPerson}")
//    public Map<String, Object> acceptFriendOrNot(@PathVariable boolean yesOrNo, @PathVariable String userEmail, @PathVariable String askingPerson) {
//        Map<String, Object> response = new HashMap<>();
//        Friend friend = new Friend();
//        User userAdding = userRepository.findByUserEmail(userEmail);
//
//        if (yesOrNo) {
//            friend.setFriendEmail1(userAdding.getUserEmail()); // user accepting
//            friend.setFriendEmail2(askingPerson); // user being accepted
//
//            if (friendRepository.friendshipExistsByUserEmails(userAdding.getUserEmail(), askingPerson)) { // Makes sure FriendShip repo does not have it
//                response.put("success", false);
//                response.put("message", "Friendship already exists");
//                return response;
//            }
//            friend.setAcceptance(true);
//            friendRepository.save(friend);
//
//            // Remove the friend request from both users' lists
//            userAdding.removeFriendRequest(askingPerson);
//            User askingUser = userRepository.findByUserEmail(askingPerson);
//            askingUser.removeFriendRequest(userAdding.getUserEmail());
//
//            userRepository.save(userAdding);
//            userRepository.save(askingUser);
//
//            response.put("success", true);
//            response.put("message", "Friend request accepted successfully");
//        } else {
//            // Remove the friend request from both users' lists
//            userAdding.removeFriendRequest(askingPerson);
//            User askingUser = userRepository.findByUserEmail(askingPerson);
//            askingUser.removeFriendRequest(userAdding.getUserEmail());
//
//            userRepository.save(userAdding);
//            userRepository.save(askingUser);
//
//            response.put("success", false);
//            response.put("message", "Friend request rejected");
//        }
//
//        return response;
//    }

    @PostMapping(path = "/acceptFriendOrNot/{yesOrNo}/{userEmail}/{askingPerson}")
    public Map<String, Object> acceptFriendOrNot(@PathVariable boolean yesOrNo, @PathVariable String userEmail, @PathVariable String askingPerson) {
        Map<String, Object> response = new HashMap<>();
        User userAdding = userRepository.findByUserEmail(userEmail);
        User askingUser = userRepository.findByUserEmail(askingPerson);

        if (yesOrNo) {
            if (friendRepository.findByUser1AndUser2(userAdding, askingUser) == null || friendRepository.existsByUser1AndUser2(userAdding,askingUser)) {
                response.put("success", false);
                response.put("message", "Friendship already exists");
                return response;
            }

            Friend friend1 = new Friend();
            friend1.setUser1(userAdding);
            friend1.setUser2(askingUser);
            friend1.setAccepted(true);

            Friend friend2 = new Friend();
            friend2.setUser1(askingUser);
            friend2.setUser2(userAdding);
            friend2.setAccepted(true);

            friendRepository.save(friend1);
            friendRepository.save(friend2);

            FriendRequest friendRequest = userAdding.getReceivedFriendRequests().stream()
                    .filter(fr -> fr.getRequestingUser().equals(askingUser))
                    .findFirst()
                    .orElse(null);

            if (friendRequest != null) {
                userAdding.removeFriendRequest(friendRequest);
                askingUser.removeFriendRequest(friendRequest);
                userRepository.save(userAdding);
                userRepository.save(askingUser);
            }

            response.put("success", true);
            response.put("message", "Friend request accepted successfully");
        } else {
            FriendRequest friendRequest = userAdding.getReceivedFriendRequests().stream()
                    .filter(fr -> fr.getRequestingUser().equals(askingUser))
                    .findFirst()
                    .orElse(null);

            if (friendRequest != null) {
                userAdding.removeFriendRequest(friendRequest);
                askingUser.removeFriendRequest(friendRequest);
                userRepository.save(userAdding);
                userRepository.save(askingUser);
            }

            response.put("success", false);
            response.put("message", "Friend request rejected");
        }

        return response;
    }

    /**
     *
     * @param userEmail
     * @param friendWannaBe
     * @return
     */
//    @PostMapping(path = "/sendRequest/{userEmail}/{friendWannaBe}")
//    public Map<String, Object> friendRequest(@PathVariable String userEmail, @PathVariable String friendWannaBe) {
//        Map<String, Object> response = new HashMap<>();
//
//        if (userRepository.findByUserEmail(friendWannaBe) == null || userRepository.findByUserEmail(userEmail) == null) {
//            response.put("success", false);
//            response.put("message", "One or both users do not exist");
//            return response;
//        }
//
//        User friendToBe = userRepository.findByUserEmail(friendWannaBe);
//        User friendRequestSender = userRepository.findByUserEmail(userEmail);
//
//        Map<String, Object> friendToBeRequests = friendToBe.getFriendRequests();
//        List<String> existingRequests = (List<String>) friendToBeRequests.get("friendRequests");
//
//        if (!friendRepository.friendshipExistsByUserEmails(userEmail, friendWannaBe) && !existingRequests.contains(userEmail)) {
//            friendToBe.addFriendRequest(friendRequestSender);
//            userRepository.save(friendToBe);
//            response.put("success", true);
//            response.put("message", "Friend request sent successfully");
//        } else {
//            response.put("success", false);
//            response.put("message", "Users are already friends or the request already exists");
//        }
//
//        return response;
//    }

    @PostMapping(path = "/sendRequest/{userEmail}/{friendWannaBe}")
    public Map<String, Object> friendRequest(@PathVariable String userEmail, @PathVariable String friendWannaBe) {
        Map<String, Object> response = new HashMap<>();

        User friendRequestSender = userRepository.findByUserEmail(userEmail);
        User friendToBe = userRepository.findByUserEmail(friendWannaBe);

        if (friendToBe == null || friendRequestSender == null) {
            response.put("success", false);
            response.put("message", "One or both users do not exist");
            return response;
        }

        if (friendRepository.findByUser1AndUser2(friendRequestSender, friendToBe) == null || friendRepository.existsByUser1AndUser2(friendRequestSender,friendToBe)) {
            response.put("success", false);
            response.put("message", "Users are already friends");
            return response;
        }

        FriendRequest existingRequest = friendToBe.getReceivedFriendRequests().stream()
                .filter(fr -> fr.getRequestingUser().equals(friendRequestSender))
                .findFirst()
                .orElse(null);

        if (existingRequest != null) {
            response.put("success", false);
            response.put("message", "Friend request already exists");
            return response;
        }

        friendRequestSender.sendFriendRequest(friendToBe);
        userRepository.save(friendRequestSender);

        response.put("success", true);
        response.put("message", "Friend request sent successfully");
        return response;
    }

//    @GetMapping(path = "/gotFriendRequest/{userEmails}")
//    public Map<String, Object> gotFriendRequest(@PathVariable String userEmails) {
//        List<String> userEmailList = Arrays.asList(userEmails.split(","));
//        Map<String, Object> response = new HashMap<>();
//        List<String> friendRequestPersonNames = new ArrayList<>();
//
//        for (String userEmail : userEmailList) {
//            User user = userRepository.findByUserEmail(userEmail); // Assuming this method exists
//            if (user != null) {
//                Map<String, Object> userFriendRequests = user.getFriendRequests();
//                friendRequestPersonNames.addAll((List<String>) userFriendRequests.get("friendRequests"));
//            }
//        }
//
//        if (friendRequestPersonNames.isEmpty()) {
//            response.put("message", "No friend requests found");
//        } else {
//            response.put("Requests", friendRequestPersonNames);
//        }
//
//        return response;
//    }


    /**
     *
     * @param userEmails
     * @return
     */
//    @GetMapping(path = "/gotFriendRequest/{userEmails}")
//    public Map<String, Object> gotFriendRequest(@PathVariable String userEmails) {
//        List<String> userEmailList = Arrays.asList(userEmails.split(","));
//        Map<String, Object> response = new HashMap<>();
//        List<Map<String, String>> friendRequestList = new ArrayList<>();
//
//        for (String userEmail : userEmailList) {
//            User user = userRepository.findByUserEmail(userEmail); // Assuming this method exists
//            if (user != null) {
//                Map<String, Object> userFriendRequests = user.getFriendRequests();
//                List<String> userFriendRequestEmails = (List<String>) userFriendRequests.get("friendRequests");
//
//                for (String friendRequestEmail : userFriendRequestEmails) {
//                    User friendRequestUser = userRepository.findByUserEmail(friendRequestEmail);
//                    if (friendRequestUser != null) {
//                        Map<String, String> friendRequest = new HashMap<>();
//                        friendRequest.put("email", friendRequestEmail);
//                        friendRequest.put("name", friendRequestUser.getName());
//                        friendRequestList.add(friendRequest);
//                    }
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
    @GetMapping(path = "/gotFriendRequest/{userEmails}")
    public Map<String, Object> gotFriendRequest(@PathVariable String userEmails) {
        List<String> userEmailList = Arrays.asList(userEmails.split(","));
        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> friendRequestList = new ArrayList<>();

        for (String userEmail : userEmailList) {
            User user = userRepository.findByUserEmail(userEmail);
            if (user != null) {
                List<FriendRequest> receivedFriendRequests = user.getReceivedFriendRequests();

                for (FriendRequest friendRequest : receivedFriendRequests) {
                    User requestingUser = friendRequest.getRequestingUser();
                    Map<String, String> friendRequestData = new HashMap<>();
                    friendRequestData.put("email", requestingUser.getUserEmail());
                    friendRequestData.put("name", requestingUser.getName());
                    friendRequestList.add(friendRequestData);
                }
            }
        }

        if (friendRequestList.isEmpty()) {
            response.put("message", "No friend requests found");
        } else {
            response.put("requests", friendRequestList);
        }

        return response;
    }
}
