package database.Theme;

import database.ProfilePicture.ProfilePicture;
import database.Users.User;
import database.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
public class ThemeController {
    @Autowired
    private UserRepository userRepository;

    private String success = "{\"success\":true}"; //Sends a JSON boolean object named success

    private String failure = "{\"fail\":false}"; //Sends a JSON String object named message

    @PutMapping("/Theme/{userEmail}")
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

    @GetMapping("/Theme/{userEmail}")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String userEmail) {
        User user = userRepository.findByUserEmail(userEmail);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        ProfilePicture profilePicture = user.getProfilePicture();
        if (profilePicture == null || profilePicture.getData() == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        byte[] pictureData = profilePicture.getData();
        return ResponseEntity.ok(pictureData);
    }
}