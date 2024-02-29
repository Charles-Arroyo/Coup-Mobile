package onetoone.Setting;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import onetoone.Users.User;

@Entity
public class Setting {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )

    private boolean sound;
    private int illumination;


    public Setting(boolean sound, int illumination){
        this.sound = sound;
        this.illumination = illumination;
    }

    public Setting(){

    }

    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public int getIllumination() {
        return illumination;
    }

    public void setIllumination(int illumination) {
        this.illumination = illumination;
    }
}
