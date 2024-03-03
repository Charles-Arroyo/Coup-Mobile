package onetoone.Users;

import onetoone.Friends.Friend;
import onetoone.Friends.FriendRepository;
import onetoone.Profiles.Profile;
import onetoone.Profiles.ProfileRepository;
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
    UserRepository userRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    FriendRepository friendRepository;

    private String success = "{\"message\":\"success\"}";

    private String failure = "{\"message\":\"failure\"}";



    @GetMapping(path = "/users")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    User getUserById(@PathVariable int id) {
        return userRepository.findById(id);
    }

    @PostMapping(path = "/users")
    String createUser(@RequestBody User user) {
        if (user == null) {
            return success;
        }
        userRepository.save(user);
        return success;
    }

    @PostMapping(path = "/signin")
    public String signIn(@RequestBody User user) {

        User foundUser = userRepository.findByEmailId(user.getEmailId());
        if (foundUser != null && foundUser.getPassword().equals(user.getPassword())) {
            return success;
        }else{
           return failure;
        }
    }


    @PutMapping("/users/{id}")
    User updateUser(@PathVariable int id, @RequestBody User request){
        User user = userRepository.findById(id);
        if(user == null)
            return null;
        userRepository.save(request);
        return userRepository.findById(id);
    }   
    
    @PutMapping("/users/{userId}/profiles/{profileId}")
    String assignProfileToUser(@PathVariable int userId,@PathVariable int profileId){
        User user = userRepository.findById(userId);
        Profile profile = profileRepository.findById(profileId);
        if(user == null || profile == null)
            return failure;
        profile.setUser(user);
        user.setProfile(profile);
        userRepository.save(user);
        return success;
    }


    @PutMapping("/users/{userId}/friends/{friendId}")
    String assignFriendToUser(@PathVariable int userId,@PathVariable int friendId){
        User user = userRepository.findById(userId);
        Friend friend = friendRepository.findById(friendId);
        if(user == null || friend == null)
            return failure;
        friend.setUser(user);
        user.addFriends(friend);
        userRepository.save(user);
        return success;
    }

    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
        return success;
    }
}
