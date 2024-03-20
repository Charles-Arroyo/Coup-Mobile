package onetoone.Setting;

import onetoone.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import onetoone.Users.UserRepository;
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


    @PutMapping(path = "/changeEmail/{userId}")
    @Transactional
    public String changeEmail(@PathVariable Long userId, @RequestBody Setting updatedSetting) {

        if (updatedSetting.getUpdateEmail() == null) {

            return "empty";
        }

        Setting setting = settingRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Setting not found"));

        User user = setting.getUser();
        if (user == null) {
            return "null user";
        }

        user.setEmailId(updatedSetting.getUpdateEmail());
        userRepository.save(user);

        return success;
    }

    @PutMapping(path = "/changePass/{userId}")
    @Transactional
    public ResponseEntity<String> changePassword(@PathVariable Long userId, @RequestBody Setting updatedSetting) {
        if (updatedSetting.getUpdatePassword() == null) {
            return ResponseEntity.badRequest().body("{\"message\":\"Invalid password\"}");
        }

        Setting setting = settingRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Setting not found"));

        User user = setting.getUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"User not found\"}");
        }

        // Ensure the user is authorized to change the password here

        user.setPassword(updatedSetting.getUpdatePassword());
        userRepository.save(user);

        return ResponseEntity.ok("{\"success\":true}");
    }



}
