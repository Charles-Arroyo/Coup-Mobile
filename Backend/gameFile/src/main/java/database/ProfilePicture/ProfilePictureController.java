package database.ProfilePicture;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import database.Lobby.Lobby;
import database.Ranking.Ranking;
import database.Users.User;
import database.Users.UserRepository;
import jakarta.persistence.*;
import database.Stats.Stat;
import jakarta.persistence.*;
import database.FriendRequest.FriendRequest;
import lombok.Singular;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfilePictureController {
    @Autowired
    private UserRepository userRepository;

    private String success = "{\"success\":true}"; //Sends a JSON boolean object named success

    private String failure = "{\"fail\":false}"; //Sends a JSON String object named message

    @PutMapping(path = "/userProfileChange/{userEmail}")
    public String changeUserProfile(@PathVariable String userEmail){
        // check if the user exists
        if(userRepository.findByUserEmail(userEmail) == null){
            return failure;
        }
        // find the user
        User user = userRepository.findByUserEmail(userEmail);




        return success;
    }


}
