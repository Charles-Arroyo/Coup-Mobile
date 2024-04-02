package database.FriendRequest;


import database.Chat.MessageRepository;
import database.Friends.Friend;
import database.Friends.FriendRepository;
import database.Users.User;
import database.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class FriendRequestController {

    @Autowired
    UserRepository userRepository; //Creating a repository(mySQL of users)


    @Autowired
    MessageRepository messageRepository; //Creating a repository(mySQL of users)

    @Autowired
    FriendRepository friendRepository; // //Creating a repository(mySQL of Friends)

    @Autowired
    FriendRequestRepository friendRequestRepository;


    @PostMapping(path = "/sendRequest/{userEmail}/{friendWannaBe}")
    public Map<String, Object> friendRequest(@PathVariable String userEmail, @PathVariable String friendWannaBe) {
        Map<String, Object> response = new HashMap<>();

        User friendRequestSender = userRepository.findByUserEmail(userEmail);
        User friendToBe = userRepository.findByUserEmail(friendWannaBe);

        if (friendToBe == null || friendRequestSender == null) {
            response.put("success", false);
            response.put("message", "One or both users do not exist");
            return response;
        }

        if (friendRepository.findByUser1AndUser2(friendRequestSender, friendToBe) != null || friendRepository.existsByUser1AndUser2(friendRequestSender, friendToBe)) {
            response.put("success", false);
            response.put("message", "Users are already friends");
            return response;
        }

        boolean existingRequest = friendRequestRepository.existsByRequestingUserAndRequestedUser(friendRequestSender, friendToBe);

        if (existingRequest) {
            response.put("success", false);
            response.put("message", "Friend request already exists");
            return response;
        }

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setRequestingUser(friendRequestSender);
        friendRequest.setRequestedUser(friendToBe);
        friendRequestRepository.save(friendRequest);

        response.put("success", true);
        response.put("message", "Friend request sent successfully");
        return response;
    }

    @GetMapping(path = "/gotFriendRequest/{userEmails}")
    public Map<String, Object> gotFriendRequest(@PathVariable String userEmails) {
        List<String> userEmailList = Arrays.asList(userEmails.split(","));
        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> friendRequestList = new ArrayList<>();

        for (String userEmail : userEmailList) {
            User user = userRepository.findByUserEmail(userEmail);
            if (user != null) {
                List<FriendRequest> receivedFriendRequests = friendRequestRepository.findByRequestedUser(user);

                for (FriendRequest friendRequest : receivedFriendRequests) {
                    User requestingUser = friendRequest.getRequestingUser();
                    Map<String, String> friendRequestData = new HashMap<>();
                    friendRequestData.put("email", requestingUser.getUserEmail());
                    friendRequestData.put("name", requestingUser.getName());
                    friendRequestList.add(friendRequestData);
                }
            }
        }

        if (friendRequestList.isEmpty()) {
            response.put("message", "No friend requests found");
        } else {
            response.put("requests", friendRequestList);
        }

        return response;
    }



    @PostMapping(path = "/acceptFriendOrNot/{yesOrNo}/{userEmail}/{askingPerson}")
    public Map<String, Object> acceptFriendOrNot(@PathVariable boolean yesOrNo, @PathVariable String userEmail, @PathVariable String askingPerson) {
        Map<String, Object> response = new HashMap<>();
        User userAdding = userRepository.findByUserEmail(userEmail);
        User askingUser = userRepository.findByUserEmail(askingPerson);

        FriendRequest friendRequest = friendRequestRepository.findByRequestingUserAndRequestedUser(askingUser, userAdding);

        if (friendRequest == null) {
            response.put("success", false);
            response.put("message", "Friend request not found");
            return response;
        }

        if (yesOrNo) {
            if (friendRepository.existsByUser1AndUser2(userAdding, askingUser) || friendRepository.existsByUser1AndUser2(askingUser, userAdding)) {
                response.put("success", false);
                response.put("message", "Friendship already exists");
                friendRequestRepository.delete(friendRequest);
                return response;
            }

            Friend friend1 = new Friend();
            friend1.setUser1(userAdding);
            friend1.setUser2(askingUser);
            friend1.setAccepted(true);

            Friend friend2 = new Friend();
            friend2.setUser1(askingUser);
            friend2.setUser2(userAdding);
            friend2.setAccepted(true);

            friendRepository.save(friend1);
            friendRepository.save(friend2);

            friendRequestRepository.delete(friendRequest);

            response.put("success", true);
            response.put("message", "Friend request accepted successfully");
        } else {
            friendRequestRepository.delete(friendRequest);

            response.put("success", true);
            response.put("message", "Friend request rejected");
        }

        return response;
    }
}
