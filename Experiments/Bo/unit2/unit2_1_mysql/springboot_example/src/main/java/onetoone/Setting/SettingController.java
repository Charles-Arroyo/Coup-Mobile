package onetoone.Setting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.util.Optional;


@RestController
public class SettingController {

    @Autowired
    SettingRepository settingRepository;

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


    @GetMapping(path = "/changeEmail")
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

    @PostMapping(path = "/changeEmail")
    String addEmail(@RequestBody Setting setting){
        if (setting == null){
            return failure;
        }
        // Check if the email already exists
        Optional<Setting> existingSetting = settingRepository.findByupdateEmail(setting.getUpdateEmail());
        if(existingSetting.isPresent()){
            return "Email already exists."; // Email exists, so return an appropriate message or handle as desired.
        } else {
            settingRepository.save(setting);
            return "success"; // Again, consider defining what `success` string is.
        }
    }

//    @PutMapping(path = "/changeEmail/{ch}")
//    String changeEmail(@RequestBody Setting setting){
////        Setting setting = userRespository.
//        if (setting == null)
//            return failure;
//        settingRepository.save(setting);
//        return success;
//    }



}
