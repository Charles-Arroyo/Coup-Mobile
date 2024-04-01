package database.Users;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import database.Lobby.Lobby;
import jakarta.persistence.*;
import database.Stats.Stat;

import java.util.Objects;

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

    @Column(unique = true)
    private String userEmail;
    private boolean ifActive;

    private String password;

    private boolean isOnline;




    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User)
     * cascade is responsible propagating all changes, even to children of the class Eg: changes made to laptop within a user object will be reflected
     * in the database (more info : https://www.baeldung.com/jpa-cascade-types)
     * @JoinColumn defines the ownership of the foreign key i.e. the user table will have a field called laptop_id
     */



    @OneToOne
    @JoinColumn(name = "stat_id")
    @JsonManagedReference
    private Stat stat;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lobby_id")
    private Lobby lobby;




    public User(String name, String userEmail /* int id*/ ,String password /*int UniqueID*/) {
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

    public void setIsOnline(boolean isOnline){
        this.isOnline = isOnline;
    }

    public boolean getIsOnline(){
        return isOnline;
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", ifActive=" + ifActive +
                ", isOnline=" + isOnline +
                '}';
    }

    public void setUpdateEmail(String updateEmail) {
        this.userEmail = updateEmail;
        if (this.userEmail != null) {
            this.setUserEmail(updateEmail);
        }
    }

    public void setUpdatePassword(String updatePassword) {
        this.password = updatePassword;
        if (this.password != null) {
            this.setPassword(updatePassword);
        }
    }

//    public void setSetting(Setting setting) {
//        this.setting = setting;
//        setting.setUser(this); // Ensure the bidirectional link is established
//    }

    public Stat getStat(){
        return stat;
    }
    public void setStat(Stat stat) {
        this.stat = stat;
        stat.setUser(this); // Ensure the bidirectional link is established
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(userEmail, user.userEmail); // Assuming userEmail is a unique identifier.
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEmail);
    }

}



