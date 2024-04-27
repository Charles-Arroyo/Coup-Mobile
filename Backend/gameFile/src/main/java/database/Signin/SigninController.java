package database.Signin;

import database.Users.User;
import database.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class SigninController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SigninRepository signinRepository;

    private String success = "{\"success\":\"true\"}";
    private String failure = "{\"success\":\"false\"}";

    /**
     * Checks the repo, and allows user to sign in
     *
     * @param user
     * @return
     */
    @PostMapping(path = "/signin")
    public String signIn(@RequestBody User user) {
        User foundUser = userRepository.findByUserEmail(user.getUserEmail());

        // Check if the user is an admin
        if (user.getUserEmail().equals("admin") && user.getPassword().equals("admin")) {
            return "{\"success\":\"admin\"}";
        }

        if (foundUser != null && foundUser.getPassword().equals(user.getPassword())) {
            foundUser.setActive(true);
            userRepository.save(foundUser);

            SigninSocket.sendMessage(foundUser.getUserEmail(), "User signed in");

            if (signinRepository.findTopByUserOrderByLastSignInTimestampDesc(foundUser) != null) {
                Signin existingSignin = signinRepository.findTopByUserOrderByLastSignInTimestampDesc(foundUser);

                Signin newSignin = new Signin(foundUser);
                newSignin.setSignInCount(existingSignin.getSignInCount());
                newSignin.updateSignInInfo();
                signinRepository.save(newSignin);
            } else {
                Signin newSignIn = new Signin(foundUser);
                newSignIn.updateSignInInfo();
                signinRepository.save(newSignIn);
            }

            return success;
        } else {
            return failure;
        }
    }



//    /**
//     *
//     * @param userEmail
//     * @return
//     */
//    @GetMapping(path = "/getsignLog/{userEmail}")
//    public ResponseEntity<Map<String, Object>> getSignInLog(@PathVariable String userEmail) {
//        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUserEmail(userEmail));
//        if (optionalUser.isPresent()) {
//            User user = optionalUser.get();
//            List<Signin> signInLogs = signinRepository.findByUser(user);
//
//            List<Map<String, Object>> signInLogList = new ArrayList<>();
//            for (Signin signIn : signInLogs) {
//                Map<String, Object> signInLog = new HashMap<>();
//                signInLog.put("id", signIn.getId());
//                signInLog.put("userId", signIn.getUser().getId());
//                signInLog.put("lastSignInTimestamp", signIn.getLastSignInTimestamp());
//                signInLog.put("signInCount", signIn.getSignInCount());
//                signInLog.put("lastSignOutTimestamp", signIn.getLastSignOutTimestamp());
//                signInLogList.add(signInLog);
//            }
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("userId", user.getId());
//            response.put("signInLogs", signInLogList);
//
//            return ResponseEntity.ok(response);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @GetMapping(path = "/checkUserStatus/{userEmail}")
    public ResponseEntity<Map<String, Object>> checkUserStatus(@PathVariable String userEmail) {
        User user = userRepository.findByUserEmail(userEmail);
        if (user != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("active", user.isActive());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
