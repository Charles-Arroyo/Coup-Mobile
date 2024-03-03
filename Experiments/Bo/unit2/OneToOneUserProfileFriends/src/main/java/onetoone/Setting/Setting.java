package onetoone.Setting;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import jakarta.persistence.JoinColumn;
// this will connect the foreign key to the user database

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    // This annotation indicates a one-to-one relationship between the Setting entity and another entity (User).
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    // Specifies the foreign key column (user_id) in the Setting table that references the primary key (id) of the User table.
    private User user;
    // Declares a field of type User. This field represents the user associated with a particular setting.
    // It enables direct access to the User entity associated with this Setting entity in the application code.


    public Setting(Boolean soundEffect,String updateEmail){
        this.soundEffect = soundEffect;
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
        return updateEmail;
    }

    public void setUpdateEmail(String updateEmail) {
        this.updateEmail = updateEmail;
    }

    public Boolean getSoundEffect() {
        return soundEffect;
    }

    public void setSoundEffect(Boolean soundEffect) {
        this.soundEffect = soundEffect;
    }

    public void setUser(User user){
        this.user = user;
    }
}
