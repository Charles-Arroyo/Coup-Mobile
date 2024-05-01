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
    @Column(length = 25 * 1024 * 1024) // 25 MB
    private byte[] data;
    //The picture data is stored as a byte array in the database using the @Lob annotation,
    //which allows storing large objects.

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column(name = "file_path", nullable = true) // adjust this based on your schema requirements
    private String filePath;

    public ProfilePicture() {
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

    public void setUser(User user) {
        this.user = user;
    }

    public User returnUser() {
        return user;
    }

    // Getter and setter for filePath
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}