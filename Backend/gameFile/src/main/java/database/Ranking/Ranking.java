package database.Ranking;

import database.Stats.Stat;
import database.Users.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
public class Ranking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private int points;


    @OneToMany(mappedBy = "ranking", cascade = CascadeType.ALL)
    @OrderBy("points DESC")
//    @Transient
    private List<User> users = new ArrayList<>();



    public Ranking(String name, int points) {
        this.name = name;
        this.points = points;
    }

    public Ranking() {
    }

    // Getters and setters for id and name

    public void addUser(User user) {
        users.removeIf(u -> u.getId() == user.getId()); // Ensure no duplicates
        users.add(user);
        user.setRanking(this); // Assuming User has a setRanking method for the bidirectional relationship
    }



    public void removeUser(User user) {
        // Remove the user from this ranking
        users.remove(user);
        user.setRanking(null);
        user.setPoints(0);
    }

    public void setName(String name){this.name = name;}

    public String getName(){
        return name;
    }

    public void setPoints(int points) {
        this.points = (points != 0) ? points : 0;
    }

    public int getPoints(){
        return points;
    }

    public List<User> getUsers() {
        return users;
    }
}