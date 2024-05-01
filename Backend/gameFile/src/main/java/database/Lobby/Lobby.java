package database.Lobby;

import com.fasterxml.jackson.annotation.JsonBackReference;
import database.Users.User;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.util.*;

@Entity
@Table(name = "lobbies")
public class Lobby {
    @Id
    @GeneratedValue
    private int id;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "lobby_id") // Adjust the column name as needed
    private List<User> userList;  //List of users, specifically user emails.


    //this is for the users to be turned into
    //spectators if the lobby is full
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "spectator_lobby_id")
    private List<User> spectators;

//    @OneToOne
//    @JoinColumn(name = "user_id")
//    @JsonBackReference
//    private User user;

    @Column
    private boolean isPrivate; //Private lobby (Implement Later)


    @Column
    private boolean isFull;


    //This is used to determine if the user game
    //have started or not to determine if the joining
    //user is going to be a spectator
    private boolean gameStarted = false;

    public Lobby() {
        userList = new ArrayList<>();
        isFull = false;
        spectators = new ArrayList<>();
    }

//    @Transactional
//    public void addUser(User user) {
//        if(userList.size() < 4){
//            userList.add(user);
//        }else{
//            setFull(true);
//        }
//    }

    @Transactional
    public void addUser(User user) {
        if (userList.size() < 4 && !userList.contains(user)) { //checks if there is any user to not add same user again
            userList.add(user);
        } else {
            setFull(true);

        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Lobby{");
        sb.append("id=").append(id);
        sb.append(", isPrivate=").append(isPrivate);
        sb.append(", isFull=").append(isFull);
        sb.append(", users=[");
        if (userList != null && !userList.isEmpty()) {
            for (User user : userList) {
                sb.append(user.toString()); // Assuming User class has a sensible toString method.
                sb.append(", "); // To separate users
            }
            sb.setLength(sb.length() - 2); // Remove the last comma and space
        }
        sb.append("]");
        sb.append('}');
        return sb.toString();
    }



    public String getUsers() {
        if (userList != null && !userList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (User user : userList) {
                sb.append(user.getUserEmail()); // Append user email directly
                sb.append(", "); // To separate emails
            }
            sb.setLength(sb.length() - 2); // Remove the last comma and space
            return sb.toString();
        } else {
            return "No users in lobby";
        }
    }

    public void removeUser(User user){
        if (userList.contains(user)) {
            userList.remove(user);
        } else {
            System.out.println("User not found in the lobby.");
        }
    }

    public List<User> getUserArraylist() {
        return userList;
    }


    public void sortUsersByEmail() {
        // Sort userArrayList by email
        userList.sort(Comparator.comparing(User::getUserEmail));
    }


    public String findUserByEmail(String email) {
        for (User user : userList) {
            if (user.getUserEmail().equals(email)) {
                return user.getUserEmail(); // User found
            }
        }
        return null; // User not found
    }

    public int getId(){
        return id;
    }


//   public String getAllUserEmails() {
//    StringBuilder emails = new StringBuilder();
//    for (User user : userList) {
//        emails.append(user.getUserEmail()).append(", ");
//    }
//    if (emails.length() > 0) {
//        emails.setLength(emails.length() - 2); // Remove the last comma and space
//    }
//    return emails.toString();
//}


//    public User finUserbyEmail(String email) {
//        for (User user : userList) {
//            if (user.getUserEmail().equals(email)) {
//                return user; // User found
//            }
//        }
//        return null; // User not found
//    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isFull() {
        if(getUserArraylist().size() == 4){
            isFull = true;
            return isFull;
        }else{
            isFull = false;
            return isFull;
        }
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public boolean isEmpty(){
        return userList.isEmpty();
    }

    //Checker to see if the game started yet or not
    public boolean hasGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    //This will allow the user to be turned into spectators

    public void addSpectator(User user) {
        spectators.add(user);
    }

    public boolean removeSpectator(User user) {
        return spectators.remove(user);
    }

    public List<User> getSpectators() {
        return spectators;
    }


}
