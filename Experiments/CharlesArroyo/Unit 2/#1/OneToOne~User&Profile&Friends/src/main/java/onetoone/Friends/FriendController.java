package onetoone.Friends;

import java.util.List;

import onetoone.Friends.Friend;
import onetoone.Friends.FriendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class FriendController {

    @Autowired
    FriendRepository friendRepository; // Adjusted to use FriendRepository
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/friends")
    List<Friend> getAllFriends(){
        return friendRepository.findAll();
    }

    @GetMapping(path = "/friends/{id}")
    Friend getFriendById(@PathVariable int id){
        return friendRepository.findById(id);
    }

    @PostMapping(path = "/friends")
    String createFriend(@RequestBody Friend friend){
        if (friend == null)
            return failure;
        friendRepository.save(friend);
        return success;
    }

    @PutMapping("/friends/{id}")
    Friend updateFriend(@PathVariable int id, @RequestBody Friend request){
        Friend friend = friendRepository.findById(id);
        if(friend == null)
            return null;
        friendRepository.save(request);
        return friendRepository.findById(id);

    }
}

