package onetoone.Accounts;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import onetoone.Laptops.Laptop;
import onetoone.Players.Player;
import onetoone.Users.User;

/**
 *
 * @author Charles Arroyo
 */

@Entity
public class Account {

    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String userName;

    private String email;

    private String password;

    private boolean ifActive;

    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User)
     * @JsonIgnore is to assure that there is no infinite loop while returning either user/laptop objects (laptop->user->laptop->...)
     */

    /*
 Is this below a primary key?
     */
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id")
    private Player player;

    public Account(/*Declare varibles a user would have*/ String userName, String email) {
        this.email = email;
        this.userName = userName;
        this.ifActive = true;
    }

    public Account() {
        //nothing in here
    }

    // =============================== Getters and Setters for each field ================================== //
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getEmail(){
        return userName;
    }

    public void getEmail(String email){
        this.email = email;
    }

    public boolean getIsActive(){
        return ifActive;
    }

    public void setIfActive(boolean ifActive){
        this.ifActive = ifActive;
    }

    public Player getPlayer(){

        return player;
    }

    public void setPlayer(Player player){
        this.player = player;
    }





}