package onetoone.Users;

import jakarta.persistence.*;
import onetoone.Friends.Friend;
import onetoone.Profiles.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Charles Arroyo
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

    private String userEmail;
    private boolean ifActive;

    private String password;

    private String potentialFriend;

    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User)
     * cascade is responsible propagating all changes, even to children of the class Eg: changes made to laptop within a user object will be reflected
     * in the database (more info : https://www.baeldung.com/jpa-cascade-types)
     * @JoinColumn defines the ownership of the foreign key i.e. the user table will have a field called laptop_id
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private Profile profile;



    public User(String name, String userEmail, int id,String password, int UniqueID, String potentialFriend) {
        this.name = name;
        this.userEmail = userEmail;
        this.ifActive = true;
        this.id = id;
        this.password = password;
        this.potentialFriend = potentialFriend;
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


    public void setId(int id){
        this.id = id;
    }

    public String getPotentialFriend() {
        return potentialFriend;
    }

    public void setPotentialFriend(String potentialFriend) {
        this.potentialFriend = potentialFriend;
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

    public Profile getProfile(){
        return profile;
    }

    public void setProfile(Profile profile){
        this.profile = profile;
    }

}



