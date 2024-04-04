package database.Ranking;

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

    @OneToMany(mappedBy = "ranking", cascade = CascadeType.ALL)
    @OrderBy("points DESC")
    private List<User> users = new ArrayList<>();

    public Ranking(String name) {
        this.name = name;
    }

    public Ranking() {
    }

    // Getters and setters for id and name


    public void addUser(User user) {
        // Remove the user from their current ranking if they have one
        if (user.getRanking() != null) {
            user.getRanking().removeUser(user);
        }

        // Add the user to this ranking
        users.add(user);
        user.setRanking(this);

        // Sort the users based on their points
        users.sort(Comparator.comparingInt(User::getPoints).reversed());
    }

    public void removeUser(User user) {
        // Remove the user from this ranking
        users.remove(user);
        user.setRanking(null);
    }
    public List<User> getUsers() {
        return users;
    }
}