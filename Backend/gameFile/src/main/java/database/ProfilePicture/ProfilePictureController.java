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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfilePictureController {
    @Autowired
    private UserRepository userRepository;

    private String success = "{\"success\":true}"; //Sends a JSON boolean object named success

    private String failure = "{\"fail\":false}"; //Sends a JSON String object named message

    @PutMapping("/api/profilePicture/{userEmail}")
    public String updateProfilePicture(@PathVariable String userEmail, @RequestBody byte[] pictureData) {
        User user = userRepository.findByUserEmail(userEmail);
        if (user == null) {
            return failure;
        }

        ProfilePicture profilePicture = user.getProfilePicture();
        if (profilePicture == null) {
            profilePicture = new ProfilePicture();
            user.setProfilePicture(profilePicture);
        }
        profilePicture.setData(pictureData);
        userRepository.save(user);

        return success;
    }

    @GetMapping("/api/profilePicture/{userEmail}")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String userEmail) {
        User user = userRepository.findByUserEmail(userEmail);
        if (user == null || user.getProfilePicture() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        byte[] pictureData = user.getProfilePicture().getData();
        return ResponseEntity.ok(pictureData);
    }
}