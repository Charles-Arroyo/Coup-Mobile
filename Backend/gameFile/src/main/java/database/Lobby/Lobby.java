package database.Lobby;

import database.Users.User;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "lobbies")
public class Lobby {
    @Id
    @GeneratedValue
    private int id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "lobby_id") // Adjust the column name as needed
    private List<User> userList;  //Array List of users, specifically user emails

    @Column
    private boolean isPrivate; //Private lobby (Implement Later)




    @Column
    private boolean isFull;

    public Lobby() {
        userList = new ArrayList<>();
        isFull = false;
    }

    public void addUser(User user) {
        if(userList.size() <=5){
            userList.add(user);
        }else{
            setFull(true);
        }
    }

    public void removeUser(User user){
        userList.remove(user);
    }

    public List<User> getUserArraylist() {
        return userList;
    }


    public void sortUsersByEmail() {
        // Sort userArrayList by email
        userList.sort(Comparator.comparing(User::getUserEmail));
    }

    public User findUserByEmailBinarySearch(String email) {
        int index = Collections.binarySearch(userList, new User(email,null,null), Comparator.comparing(User::getUserEmail));
        if (index >= 0) {
            return userList.get(index);
        } else {
            return null; // User not found
        }
    }
    public String findUserByEmail(String email) {
        for (User user : userList) {
            if (user.getUserEmail().equals(email)) {
                return user.getUserEmail(); // User found
            }
        }
        return null; // User not found
    }

    public User findUserEmail(String email) {
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
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public boolean isEmpty(){
        return userList.isEmpty();
    }
}
