package onetoone.Setting;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import onetoone.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import onetoone.Users.UserRepository;
import onetoone.Users.User;



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

    @GetMapping(path = "/sound")
    public ResponseEntity<Boolean> getSetting() {
        // Fetch the setting from the database. This assumes there's only one setting entity.
        Optional<Setting> setting = settingRepository.findById(1L); // Example: using a static ID

        if(setting.isPresent()) {
            return ResponseEntity.ok(setting.get().getSoundEffect()); // Assuming Setting has a getSound() method
        } else {
            // Default value if no setting is found
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping(path = "/sound")
    String makeSound(@RequestBody Setting setting){
        if (setting == null)
            return failure;
        settingRepository.save(setting);
        return success;
    }

    @PutMapping(path = "/sound")
    String changeSound(@RequestBody Setting setting){
//        Setting setting = userRespository.
        if (setting == null)
            return failure;
        settingRepository.save(setting);
        return success;
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



    public Setting getSettingForUser(int userId) {
        return settingRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Setting not found for user: " + userId));
    }



//    @PutMapping(path = "/changeEmail")
//    String changeEmail(@RequestBody Setting setting){
////        Setting setting = userRespository.
//        if (setting == null)
//            return failure;
//        settingRepository.save(setting);
//        return success;
//    }


    @PutMapping(path = "/changeEmail/{ch}")
    public String changeEmail(@PathVariable("ch") Long settingId, @RequestBody Setting updatedSetting) {
        return settingRepository.findById(settingId).map(setting -> {
            // Assuming the Setting entity has a method to get the associated User
            User user = setting.getUser();
            if (user != null && updatedSetting.getUpdateEmail() != null) {
                // Update the User's email with the new email from the Setting
                user.setEmailId(updatedSetting.getUpdateEmail());
                // Save the updated User entity
                userRepository.save(user);
                return success;
            } else {
                return failure;
            }
        }).orElse(failure); // Return failure if the setting with the specified ID is not found
    }




}
