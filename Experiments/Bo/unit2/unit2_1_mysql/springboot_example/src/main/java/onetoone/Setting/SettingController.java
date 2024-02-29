package onetoone.Setting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SettingController {
    @Autowired
    SettingRepository settingRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/settings")
    List<Setting> getAllSetting(){
        return settingRepository.findAll();
    }

    @GetMapping(path = "/settings/{id}")
    Setting getSettingById(@PathVariable int id){
        return settingRepository.findById(id);
    }

    @PostMapping(path = "/settings")
    String createSetting(@RequestBody Setting Setting){
        if (Setting == null)
            return failure;
        settingRepository.save(Setting);
        return success;
    }

    @PutMapping(path = "/settings/{id}")
    Setting updateSetting(@PathVariable int id, @RequestBody Setting request){
        Setting setting = settingRepository.findById(id);
        if(setting == null)
            return null;
        settingRepository.save(request);
        return settingRepository.findById(id);
    }

    @DeleteMapping(path = "/settings/{id}")
    String deleteSetting(@PathVariable int id){
        settingRepository.deleteById(id);
        return success;
    }

}
