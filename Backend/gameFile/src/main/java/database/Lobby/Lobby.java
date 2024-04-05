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

//    @OneToOne
//    @JoinColumn(name = "user_id")
//    @JsonBackReference
//    private User user;

    @Column
    private boolean isPrivate; //Private lobby (Implement Later)


    @Column
    private boolean isFull;

    public Lobby() {
        userList = new ArrayList<>();
        isFull = false;
    }

    @Transactional
    public void addUser(User user) {
        if(userList.size() < 4){
            userList.add(user);
        }else{
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


    public User finUserbyEmail(String email) {
        for (User user : userList) {
            if (user.getUserEmail().equals(email)) {
                return user; // User found
            }
        }
        return null; // User not found
    }

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



}
