package onetoone.Friends;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import onetoone.Friends.Friend;
import onetoone.Friends.FriendRepository;
import onetoone.Users.User;
import onetoone.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FriendController {

    @Autowired
    FriendRepository friendRepository; // Adjusted to use FriendRepository
    UserRepository userRepository;
    private String success = "{\"success\":true}"; //Sends a JSON boolean object named success

    private String failure = "{\"fail\":false}"; //Sends a JSON String object named message

    @GetMapping(path = "/friends")
    List<Friend> getAllFriends(){
        return friendRepository.findAll();

    }





    /**
     * Returns all the friends of a specfic user.
     * @param friendEmail1
     * @return
     */

//    @GetMapping(path = "/getFriends/{friendEmail1}")
//    public ResponseEntity<List<Friend>> getFriendsByEmail(@PathVariable String friendEmail1) {
//        List<Friend> friends = friendRepository.findByFriendEmail1(friendEmail1);
//        if (friends.isEmpty()) {
//            return ResponseEntity.notFound().build(); // Returns a 404 if no friends are found
//        }
//        return ResponseEntity.ok(friends);
//    }

    @DeleteMapping("/deleteFriend/{friendEmail1}/{friendEmail2}")
   String deleteFriendRelationship(@PathVariable String friendEmail1, @PathVariable String friendEmail2){
        List<Friend> friends = friendRepository.findByFriendEmail1AndFriendEmail2(friendEmail1,friendEmail2);
        if (friends.isEmpty()) {
            return failure; // Returns a 404 if no friends are found
        }
      for(Friend friend : friends){
          friendRepository.deleteById(friend.getId());
      }
        return success;
    }


    /**
     *
     *
     *
     *
     * Helpful code below
     *
     *
     *
     *
     */


    /**
     * returns json object of friends
     */
    @GetMapping(path = "/getFriends/{friendEmail1}")
    public ResponseEntity<Map<String, List<Friend>>> getFriendsByEmail(@PathVariable String friendEmail1) {
        List<Friend> friends = friendRepository.findByFriendEmail1(friendEmail1);
        if (friends.isEmpty()) {
            return ResponseEntity.notFound().build(); // Returns a 404 if no friends are found
        }
        Map<String, List<Friend>> response = new HashMap<>();
        response.put("friend", friends);
        return ResponseEntity.ok(response);
    }

    /**
     * This one returns all the pending friends
     * @param friendEmail1
     * @return
     */

    @GetMapping(path = "/getFriends/{friendEmail1}")
    public ResponseEntity<Map<String, List<Friend>>> getPendingFriendsByEmail(@PathVariable String friendEmail1) {
        List<Friend> friends = friendRepository.findByFriendEmail1(friendEmail1);
        if (friends.isEmpty()) {
            return ResponseEntity.notFound().build(); // Returns a 404 if no friends are found
        }
        Map<String, List<Friend>> response = new HashMap<>();
        response.put("friend", friends);
        return ResponseEntity.ok(response);
    }


    /**
     * Special code below for sending an String array list of friends
     *
     */
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

}

