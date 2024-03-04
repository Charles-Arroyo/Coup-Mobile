package onetoone.Setting;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

// this will connect the foreign key to the user database

import onetoone.Users.User;

@Entity
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // generating a key for this setting
    private int id;

    private String updateEmail;
    private Boolean soundEffect;
    //this is the sound effect, true or false

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
    // Declares a field of type User. This field represents the user associated with a particular setting.
    // It enables direct access to the User entity associated with this Setting entity in the application code.



    public Setting(String updateEmail){
        this.updateEmail = updateEmail;
    }
    public Setting(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUpdateEmail() {
        return this.updateEmail;
    }

    public void setUpdateEmail(String updateEmail) {
        this.updateEmail = updateEmail;
        if (this.user != null) {
            this.user.setEmailId(updateEmail);
        }
    }


//    public String getUpdateEmail() {
//        return updateEmail;
//    }
//
//    public void setUpdateEmail(String updateEmail) {
//        this.updateEmail = updateEmail;
//    }

//    public Boolean getSoundEffect() {
//        return soundEffect;
//    }
//
//    public void setSoundEffect(Boolean soundEffect) {
//        this.soundEffect = soundEffect;
//    }


    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }



}
