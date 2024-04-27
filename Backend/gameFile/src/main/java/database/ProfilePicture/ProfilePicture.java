package database.ProfilePicture;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import database.Lobby.Lobby;
import database.Ranking.Ranking;
import database.Users.User;
import database.Users.UserRepository;
import jakarta.persistence.*;
import database.Stats.Stat;
import jakarta.persistence.*;
import database.FriendRequest.FriendRequest;
import lombok.Singular;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Objects;

@Entity
public class ProfilePicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    private byte[] data;
    //The picture data is stored as a byte array in the database using the @Lob annotation,
    //which allows storing large objects.



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
