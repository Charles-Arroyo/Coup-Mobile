package onetoone.Profiles;

import java.util.List;

import onetoone.Friends.Friend;
import onetoone.Friends.FriendRepository;
import onetoone.Profiles.Profile;
import onetoone.Profiles.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Vivek Bengre
 *
 */

@RestController
public class ProfileController {

    @Autowired
    ProfileRepository profileRepository;



    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/Profiles")
    List<Profile> getAllProfiles(){
        return profileRepository.findAll();
    }

    @GetMapping(path = "/Profiles/{id}")
    Profile getProfileById(@PathVariable int id){
        return profileRepository.findById(id);
    }

    @PostMapping(path = "/Profiles")
    String createProfile(@RequestBody Profile profile){
        if (profile == null)
            return failure;
        profileRepository.save(profile);
        return success;
    }

    @PutMapping(path = "/Profiles/{id}")
    Profile updateProfile(@PathVariable int id, @RequestBody Profile request){
        Profile profile = profileRepository.findById(id);
        if(profile == null)
            return null;
        profile.setGamerTag(request.getGamerTag());
        profile.setLevel(request.getLevel());
        profile.setXP(request.getXP());
        profileRepository.save(request);
        return profileRepository.findById(id);
    }

    @DeleteMapping(path = "/Profiles/{id}")
    String deleteProfile(@PathVariable int id){
            profileRepository.deleteById(id);
            return success;
    }
}
