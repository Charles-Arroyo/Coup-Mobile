package database.ProfilePicture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import database.Users.User;
import database.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.bind.annotation.*;

import static com.mysql.cj.conf.PropertyKey.logger;

@RestController
public class ProfilePictureController {
    @Autowired
    private UserRepository userRepository;

    private final String SUCCESS_RESPONSE = "{\"success\":true}";
    private final String FAILURE_RESPONSE = "{\"fail\":false}";
    @Autowired
    private ProfilePictureRepository profilePictureRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProfilePictureController.class);

    @Transactional
    @PutMapping("/updateProfile/{userEmail}")
    public ResponseEntity<String> updateProfilePicture(@PathVariable String userEmail, @RequestBody byte[] pictureData) {


        logger.info("Attempting to update profile picture for user: " + userEmail);

        if (pictureData == null || pictureData.length == 0) {
            logger.warn("No picture data provided for user: " + userEmail);
            return ResponseEntity.badRequest().body("{\"message\":\"No picture data provided\"}");
        }

        User user = userRepository.findByUserEmail(userEmail);
        if (user == null) {
            logger.error("User not found: " + userEmail);
            return ResponseEntity.badRequest().body("{\"message\":\"User not found\"}");
        }

        ProfilePicture profilePicture = user.getProfilePicture();
        if (profilePicture == null) {
            profilePicture = new ProfilePicture();
            user.setProfilePicture(profilePicture);
        }

        if (profilePicture.getFilePath() == null) {
            profilePicture.setFilePath("default/path");
        }
        profilePicture.setData(pictureData);
        profilePictureRepository.save(profilePicture);
        userRepository.save(user);
        logger.info("Profile picture updated successfully for user: " + userEmail);

        return ResponseEntity.ok(SUCCESS_RESPONSE);
    }


    @GetMapping("/getProfile/{userEmail}")
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