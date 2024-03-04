package onetoone.Setting;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import onetoone.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import onetoone.Users.UserRepository;
import onetoone.Users.User;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class SettingController {

    @Autowired
    SettingRepository settingRepository;
    @Autowired
    UserRepository userRepository;

    private String success = "{\"success\":true}";
    private String failure = "{\"message\":\"failure\"}";

    public SettingController(SettingRepository settingRepository){
        this.settingRepository = settingRepository;
    }



//
//    @GetMapping(path = "/changeEmail")
////    public ResponseEntity<String> getSetting(@PathVariable("id") Long settingId) {
////        // Fetch the setting from the database using the provided ID
////        Optional<Setting> setting = settingRepository.findById(settingId);
////
////        return setting.map(s -> ResponseEntity.ok(s.toString()))
////                .orElseGet(() -> ResponseEntity.notFound().build());
////    }
//    List<Setting> getAllSetting(){
//        return settingRepository.findAll();
//    }
//
//    @PostMapping(path = "/changeEmail")
//    String addEmail(@RequestBody Setting setting){
//        if (setting == null){
//            return failure;
//        }
//        // Check if the email already exists
//        Optional<Setting> existingSetting = settingRepository.findByupdateEmail(setting.getUpdateEmail());
//        if(existingSetting.isPresent()){
//            return "Email already exists."; // Email exists, so return an appropriate message or handle as desired.
//        } else {
//            settingRepository.save(setting);
//            return "success"; // Again, consider defining what `success` string is.
//        }
//    }

//    @Transactional // Marks the method to be executed within a transactional context.
//    public void updateUserEmailFromSetting(Long settingId) {
//        // Find the Setting entity by its ID. If not found, throw an EntityNotFoundException.
//        Setting setting = settingRepository.findById(settingId)
//                .orElseThrow(() -> new EntityNotFoundException("Setting not found"));
//
//        // Check if the Setting entity contains a non-null updateEmail value.
//        if (setting.getUpdateEmail() != null) {
//            // Retrieve the User entity associated with this Setting.
//            User user = setting.getUser(); // Assuming there's a getUser() in Setting that returns the associated User.
//
//            // Update the User's email with the new email from Setting.
//            user.setEmailId(setting.getUpdateEmail());
//
//            // Save the updated User entity back to the database.
//            userRepository.save(user);
//        }
//    }



//    public Setting getSettingForUser(int userId) {
//        return settingRepository.findByUserId(userId)
//                .orElseThrow(() -> new EntityNotFoundException("Setting not found for user: " + userId));
//    }



//    @PutMapping(path = "/changeEmail")
//    String changeEmail(@RequestBody Setting setting){
////        Setting setting = userRespository.
////       Assuming the Setting entity has a method to get the associated User
//                    User user = setting.getUser();
//
//                    // Corrected to getUpdateEmail() to retrieve the email from updatedSetting
//                    if (user != null && setting.getUpdateEmail() != null) {
//                        // Corrected to use getUpdateEmail() for fetching the new email
//                        user.setEmailId(setting.getUpdateEmail());
//
//                        // Save the updated User entity
//                        userRepository.save(user);
//
//                        return success;
//                    } else {
//                        return failure;
//                    }
////        if (setting == null)
////            return failure;
////        settingRepository.save(setting);
////        return success;
//    }



//    @PutMapping(path = "/changeEmail")
//    @Transactional
//    public ResponseEntity<?> changeEmail(@RequestBody Setting setting) {
//        if (setting == null || setting.getUpdateEmail() == null) {
//            return ResponseEntity.badRequest().body("Invalid request data");
//        }
//
//        User user = setting.getUser();
//        if (user == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        user.setEmailId(setting.getUpdateEmail());
//        userRepository.save(user);
//
//        return ResponseEntity.ok("Email updated successfully");
//    }

    @PutMapping(path = "/changeEmail/{settingId}")
    @Transactional
    public ResponseEntity<?> changeEmail(@PathVariable Long settingId, @RequestBody Setting updatedSetting) {
        if (updatedSetting.getUpdateEmail() == null) {
            return ResponseEntity.badRequest().body("Invalid request data");
        }

        Setting setting = settingRepository.findById(settingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Setting not found"));

        User user = setting.getUser();
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setEmailId(updatedSetting.getUpdateEmail());
        userRepository.save(user);

        return ResponseEntity.ok("Email updated successfully");
    }




}
