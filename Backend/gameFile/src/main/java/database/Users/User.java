package database.Users;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import database.Stats.Stat;
import jakarta.persistence.*;
import database.FriendRequest.FriendRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @author Charles Arroyo
 * @author Bo Oo
 * 
 */ 

@Entity
public class User {
     /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(unique = true)
    private String userEmail;
    private boolean ifActive;

    private String password;

    private boolean friendRequest = false;

    private boolean isOnline;




    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User)
     * cascade is responsible propagating all changes, even to children of the class Eg: changes made to laptop within a user object will be reflected
     * in the database (more info : https://www.baeldung.com/jpa-cascade-types)
     * @JoinColumn defines the ownership of the foreign key i.e. the user table will have a field called laptop_id
     */

    @OneToMany(mappedBy = "requestedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendRequest> receivedFriendRequests = new ArrayList<>();

    @OneToMany(mappedBy = "requestingUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendRequest> sentFriendRequests = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "stat_id")
    @JsonManagedReference
    private Stat stat;



    public User(String name, String userEmail,int id ,String password ,int UniqueID) {
        this.name = name;
        this.userEmail = userEmail;
        this.ifActive = true;
        this.id = id;
        this.password = password;
        this.isOnline = false;
    }

    public User() {

    }

    // =============================== Getters and Setters for each field ================================== //

    public int getId(){
        return id;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getUserEmail(){
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public boolean getIsActive(){
        return ifActive;
    }

    public void setIfActive(boolean ifActive){
        this.ifActive = ifActive;
    }

//    public void setIsOnline(boolean isOnline){
//        this.isOnline = isOnline;
//    }
//
//    public boolean getIsOnline(){
//        return isOnline;
//    }



    public void sendFriendRequest(User targetUser) {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setRequestingUser(this);
        friendRequest.setRequestedUser(targetUser);
        sentFriendRequests.add(friendRequest);
    }

    public void removeFriendRequest(FriendRequest friendRequest) {
        sentFriendRequests.remove(friendRequest);
        receivedFriendRequests.remove(friendRequest);
    }

    public List<FriendRequest> getReceivedFriendRequests() {
        return receivedFriendRequests;
    }

    public List<FriendRequest> getSentFriendRequests() {
        return sentFriendRequests;
    }


    public List<String> getFriendRequestEmails() {
        return receivedFriendRequests.stream()
                .map(fr -> fr.getRequestingUser().getUserEmail())
                .collect(Collectors.toList());
    }

    public Stat getStat(){
        return stat;
    }
    public void setStat(Stat stat) {
        this.stat = stat;
        stat.setUser(this); // Ensure the bidirectional link is established
    }
}



