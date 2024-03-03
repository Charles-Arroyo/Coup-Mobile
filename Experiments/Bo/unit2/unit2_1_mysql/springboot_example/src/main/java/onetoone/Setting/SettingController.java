package onetoone.Setting;

import java.security.Principal;

import onetoone.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import onetoone.Setting.Setting;

@RestController
public class SettingController {

    @Autowired
    SettingRepository settingRepository;

    private String success = "{\"message\":\"true\"}";
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


    @GetMapping(path = "/Email")
//    public ResponseEntity<String> getSetting(@PathVariable("id") Long settingId) {
//        // Fetch the setting from the database using the provided ID
//        Optional<Setting> setting = settingRepository.findById(settingId);
//
//        return setting.map(s -> ResponseEntity.ok(s.toString()))
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
    List<Setting> getAllSetting(){
        return settingRepository.findAll();
    }

    @PostMapping(path = "/Email")
    String addEmail(@RequestBody Setting setting){
        if (setting == null)
            return failure;
        settingRepository.save(setting);
        return success;
    }

    @PutMapping(path = "/Email")
    String changeEmail(@RequestBody Setting setting){
//        Setting setting = userRespository.
        if (setting == null)
            return failure;
        settingRepository.save(setting);
        return success;
    }



}
