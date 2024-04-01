package database.Friends;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import database.Friends.Friend;
import database.Friends.FriendRepository;
import database.Users.User;
import database.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FriendController {

    @Autowired
    FriendRepository friendRepository; // Adjusted to use FriendRepository
    @Autowired
    UserRepository userRepository;
    private String success = "{\"success\":true}"; //Sends a JSON boolean object named success

    private String failure = "{\"fail\":false}"; //Sends a JSON String object named message

    @GetMapping(path = "/friends")
    List<Friend> getAllFriends(){
        return friendRepository.findAll();
    }

//    @GetMapping(path = "/getAcceptedFriends/{friendEmail1}")
//    public ResponseEntity<Map<String, List<Friend>>> getAcceptedFriendsByEmail(@PathVariable String friendEmail1) {
//        // Fetch friends from the repository based on the provided email
//        List<Friend> friends = friendRepository.findByFriendEmail1(friendEmail1);
//
//        // Filter friends who have accepted the request (accepted = true)
//        List<Friend> acceptedFriends = friends.stream()
//                .filter(friend -> friend.isAccepted()) // Assuming 'accepted' is a boolean field
//                .collect(Collectors.toList());
//
//        // If no accepted friends are found, return 404 Not Found response
//        if (acceptedFriends.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        // Construct response containing accepted friends
//        Map<String, List<Friend>> response = new HashMap<>();
//        response.put("friend", acceptedFriends);
//
//        // Return 200 OK response with the map of accepted friends
//        return ResponseEntity.ok(response);
//    }


//    /**
//     * Returns JSON object of friends who have not accepted the request.
//     */
//
//    @GetMapping(path = "/getPendingFriends/{friendEmail1}")
//    public ResponseEntity<Map<String, List<Friend>>> getPendingFriendsByEmail(@PathVariable String friendEmail1) {
//        // Fetch friends from the repository based on the provided email
//        List<Friend> friends = friendRepository.findByFriendEmail1(friendEmail1);
//
//        // Filter friends who have accepted the request (accepted = true)
//        List<Friend> acceptedFriends = friends.stream()
//                .filter(friend -> !friend.isAccepted()) // Assuming 'accepted' is a boolean field
//                .collect(Collectors.toList());
//
//        // If no accepted friends are found, return 404 Not Found response
//        if (acceptedFriends.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        // Construct response containing accepted friends
//        Map<String, List<Friend>> response = new HashMap<>();
//        response.put("friend", acceptedFriends);
//
//        // Return 200 OK response with the map of accepted friends
//        return ResponseEntity.ok(response);
//    }






//    /**
//     * Returns all the friends of a specfic user.
//     * @param friendEmail1
//     * @return
//     */

//    @GetMapping(path = "/getFriends/{friendEmail1}")
//    public ResponseEntity<List<Friend>> getFriendsByEmail(@PathVariable String friendEmail1) {
//        List<Friend> friends = friendRepository.findByFriendEmail1(friendEmail1);
//        if (friends.isEmpty()) {
//            return ResponseEntity.notFound().build(); // Returns a 404 if no friends are found
//        }
//        return ResponseEntity.ok(friends);
//    }

//    @DeleteMapping("/deleteFriend/{friendEmail1}/{friendEmail2}")
//   String deleteFriendRelationship(@PathVariable String friendEmail1, @PathVariable String friendEmail2){
//        User user1 = userRepository.findByUserEmail(friendEmail1);
//        User user2 = userRepository.findByUserEmail(friendEmail2);
//        List<Friend> friends = friendRepository.findByUser1AndUser2(user1,user2);
//        if (friends.isEmpty()) {
//            return failure; // Returns a 404 if no friends are found
//        }
//      for(Friend friend : friends){
//          friendRepository.deleteById(friend.getId());
//      }
//        return success;
//    }
//
//
//    /**
//     *
//     *
//     *
//     *
//     * Helpful code below
//     *
//     *
//     *
//     *
//     */
//
//
//    /**
//     * returns json object of friends
//     */
//    @GetMapping(path = "/getFriend/{friendEmail1}")
//    public ResponseEntity<Map<String, List<Friend>>> getFriendsByEmail(@PathVariable String friendEmail1) {
//        User userEmail = userRepository.findByUserEmail(friendEmail1);
//        List<Friend> friends = friendRepository.findByUser1_UserEmail(userEmail);
//        if (friends.isEmpty()) {
//            return ResponseEntity.notFound().build(); // Returns a 404 if no friends are found
//        }
//        Map<String, List<Friend>> response = new HashMap<>();
//        response.put("friend", friends);
//        return ResponseEntity.ok(response);
//    }

//    /**
//     * Special code below for sending an String array list of friends
//     *
//     */
//    @GetMapping(path = "/getFriends/{friendEmail1}")
//    public ResponseEntity<List<String>> getFriendsByEmail(@PathVariable String friendEmail1) {
//        List<String> listOfFriends = new ArrayList<>();
//
//        List<Friend> friends = friendRepository.findByFriendEmail1(friendEmail1);
//        for (Friend f : friends) {
//            listOfFriends.add(f.getFriendEmail2());
//        }
//
//
//        return ResponseEntity.ok(listOfFriends);
//    }

    @DeleteMapping("/deleteFriend/{userEmail}/{userEmail2}")
    public ResponseEntity<Map<String, Object>> deleteFriendRelationship(@PathVariable String userEmail, @PathVariable String userEmail2) {
        Map<String, Object> response = new HashMap<>();

        User user1 = userRepository.findByUserEmail(userEmail);
        User user2 = userRepository.findByUserEmail(userEmail2);

        if (user1 == null || user2 == null) {
            response.put("success", false);
            response.put("message", "One or both users do not exist");
            return ResponseEntity.badRequest().body(response);
        }

        Friend friendship1 = friendRepository.findByUser1AndUser2(user1, user2);
        Friend friendship2 = friendRepository.findByUser1AndUser2(user2, user1);

        if (friendship1 == null && friendship2 == null) {
            response.put("success", false);
            response.put("message", "Friendship does not exist");
            return ResponseEntity.badRequest().body(response);
        }

        if (friendship1 != null) {
            friendRepository.delete(friendship1);
        }

        if (friendship2 != null) {
            friendRepository.delete(friendship2);
        }

        response.put("success", true);
        response.put("message", "Friendship deleted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/getFriends/{userEmail}")
    public ResponseEntity<Map<String, Object>> getFriendsByUserId(@PathVariable String userEmail) {
        Map<String, Object> response = new HashMap<>();

        User user = userRepository.findByUserEmail(userEmail);

        if (user == null) {
            response.put("success", false);
            response.put("message", "User does not exist");
            return ResponseEntity.badRequest().body(response);
        }

        List<Friend> friendships = friendRepository.findByUser1OrUser2(user,user);

        List<Map<String, String>> friendList = friendships.stream()
                .map(friendship -> {
                    User friend = friendship.getUser1().equals(user) ? friendship.getUser2() : friendship.getUser1();
                    Map<String, String> friendData = new HashMap<>();
//                    friendData.put("userId", friend.getId());
                    friendData.put("name", friend.getName());
                    friendData.put("email", friend.getUserEmail());
                    return friendData;
                })
                .collect(Collectors.toList());

        response.put("success", true);
        response.put("friends", friendList);
        return ResponseEntity.ok(response);
    }


}

