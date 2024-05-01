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
import org.springframework.http.MediaType;
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
@RequestMapping("/PFP")
public class ProfilePictureController {
    @Autowired
    private UserRepository userRepository;

    private final String SUCCESS_RESPONSE = "{\"success\":true}";
    private final String FAILURE_RESPONSE = "{\"fail\":false}";

    @PutMapping("/{userEmail}")
    public ResponseEntity<String> updateProfilePicture(@PathVariable String userEmail, @RequestBody byte[] pictureData) {
        if (pictureData == null || pictureData.length == 0) {
            return ResponseEntity.badRequest().body("{\"message\":\"No picture data provided\"}");
        }

        User user = userRepository.findByUserEmail(userEmail);
        if (user == null) {
            return ResponseEntity.badRequest().body("{\"message\":\"User not found\"}");
        }

        ProfilePicture profilePicture = user.getProfilePicture();
        if (profilePicture == null) {
            profilePicture = new ProfilePicture();
            user.setProfilePicture(profilePicture);
        }
        profilePicture.setData(pictureData);
        userRepository.save(user);

        return ResponseEntity.ok(SUCCESS_RESPONSE);
    }

    @GetMapping("/{userEmail}")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String userEmail) {
        User user = userRepository.findByUserEmail(userEmail);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ProfilePicture profilePicture = user.getProfilePicture();
        if (profilePicture == null || profilePicture.getData() == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        byte[] pictureData = profilePicture.getData();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("image/jpeg"))  // Ensure correct media type
                .body(pictureData);
    }
}