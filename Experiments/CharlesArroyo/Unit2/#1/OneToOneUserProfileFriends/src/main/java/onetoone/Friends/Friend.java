package onetoone.Friends;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;
import onetoone.Users.User;


@Entity
public class Friend {
    public Friend() {

    }
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id; // Primary Key, not exposed to users.

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
