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
        if (!users.contains(user)) {
            users.add(user);
            user.setRanking(this);
            users.sort(Comparator.comparingInt(this::calculateUserPoints).reversed());
        }
    }

    private int calculateUserPoints(User user) {
        Stat userStat = user.getStat();
        if (userStat != null) {
            return (userStat.getGameWon() * 10) - (userStat.getGameLost() * 2);
        }
        return 0;
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