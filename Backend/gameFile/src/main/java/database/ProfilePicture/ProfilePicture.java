package database.ProfilePicture;

import com.fasterxml.jackson.annotation.JsonBackReference;
import database.Users.User;
import jakarta.persistence.*;

@Entity
public class ProfilePicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    private byte[] data;
    //The picture data is stored as a byte array in the database using the @Lob annotation,
    //which allows storing large objects.

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;


    public ProfilePicture(){

    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
}
