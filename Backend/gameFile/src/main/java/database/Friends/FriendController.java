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



    @GetMapping(path = "/getPersonalFriends/{userEmail}")
    public ResponseEntity<Map<String, Object>> getFriendsByUserId(@PathVariable String userEmail) {
        Map<String, Object> response = new HashMap<>();

        User user = userRepository.findByUserEmail(userEmail);

        if (user == null) {
            response.put("success", false);
            response.put("message", "User does not exist");
            return ResponseEntity.badRequest().body(response);
        }

        List<Friend> friendships = friendRepository.findByUser1OrUser2(user, user);

        List<Map<String, String>> friendList = friendships.stream()
                .map(friendship -> {
                    User friend = friendship.getUser1().equals(user) ? friendship.getUser2() : friendship.getUser1();
                    Map<String, String> friendData = new HashMap<>();
                    friendData.put("name", friend.getName());
                    friendData.put("email", friend.getUserEmail());
                    return friendData;
                })
                .distinct() // Add this line to remove duplicates
                .collect(Collectors.toList());

        response.put("success", true);
        response.put("friends", friendList);
        return ResponseEntity.ok(response);
    }


}

