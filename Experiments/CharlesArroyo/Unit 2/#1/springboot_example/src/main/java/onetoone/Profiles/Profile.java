package onetoone.Profiles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import onetoone.Users.User;

import javax.sound.sampled.Port;

@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String gamerTag;
    private int level;
    private int XP;

    @OneToOne
    @JsonIgnore
    private User user;

    public Profile(String gamerTag, int level, int XP){
        this.gamerTag = gamerTag;
        this.level = level;
        this.XP = XP;
    }

    public Profile(){

    }

    public String getGamerTag() {
        return gamerTag;
    }

    public int getLevel() {
        return level;
    }

    public int getXP() {
        return XP;
    }

    public User getUser() {
        return user;
    }

    public void setGamerTag(String gamerTag) {
        this.gamerTag = gamerTag;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setXP(int XP) {
        this.XP = XP;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
