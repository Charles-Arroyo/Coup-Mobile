package onetoone.game;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import onetoone.Friends.Friend;
import onetoone.Profiles.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import onetoone.Setting.Setting;

import onetoone.Users.User;


@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;

    private Long gameWon;
    private Long game;
    private Long gameLost;

    public Game(){
    }

    public 


}
