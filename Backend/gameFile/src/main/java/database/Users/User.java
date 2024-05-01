package database.Users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import database.Lobby.Lobby;
import database.ProfilePicture.ProfilePicture;
import database.Ranking.Ranking;
import database.Theme.Theme;
import jakarta.persistence.*;
import database.Stats.Stat;
import database.FriendRequest.FriendRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Objects;

/**
 *
 * @author Charles Arroyo
 * @author Bo Oo
 *
 */

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "lobby", "ranking", "receivedFriendRequests", "sentFriendRequests"})
public class User {
     /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String userEmail;
//    private boolean ifActive;

    private String password;

    private boolean active;

    private int points;


    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User)
     * cascade is responsible propagating all changes, even to children of the class Eg: changes made to laptop within a user object will be reflected
     * in the database (more info : https://www.baeldung.com/jpa-cascade-types)
     * @JoinColumn defines the ownership of the foreign key i.e. the user table will have a field called laptop_id
     */

    @OneToMany(mappedBy = "requestedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendRequest> receivedFriendRequests = new ArrayList<>();

    @OneToMany(mappedBy = "requestingUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendRequest> sentFriendRequests = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "stat_id")
    @JsonManagedReference
    @JsonIgnore
    private Stat stat;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lobby_id")
    private Lobby lobby;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ranking_id")
    private Ranking ranking;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_picture_id")
    private ProfilePicture profilePicture;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "theme_id")
    private Theme theme;




    public User(String name, String userEmail, int id, String password, int UniqueID) {
        this.name = name;
        this.userEmail = userEmail;
        this.id = id;
        this.password = password;
        points = 0;
        profilePicture = new ProfilePicture();
        theme = new Theme();
        stat = new Stat();
        setStat(stat); // Set the stat object explicitly
    }

    public User() {

    }

    // =============================== Getters and Setters for each field ================================== //

    public int getId(){
        return id;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getUserEmail(){
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }



    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userEmail='" + userEmail + '\'' +
//                ", ifActive=" + ifActive +
                '}';
    }


    public void sendFriendRequest(User targetUser) {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setRequestingUser(this);
        friendRequest.setRequestedUser(targetUser);
        sentFriendRequests.add(friendRequest);
    }

    public void removeFriendRequest(FriendRequest friendRequest) {
        sentFriendRequests.remove(friendRequest);
        receivedFriendRequests.remove(friendRequest);
    }

    public List<FriendRequest> getReceivedFriendRequests() {
        return receivedFriendRequests;
    }

    public List<FriendRequest> getSentFriendRequests() {
        return sentFriendRequests;
    }


    public List<String> getFriendRequestEmails() {
        return receivedFriendRequests.stream()
                .map(fr -> fr.getRequestingUser().getUserEmail())
                .collect(Collectors.toList());
    }

    public Stat getStat(){
        return stat;
    }
    public void setStat(Stat stat) {
        if(stat != null){
        this.stat = stat;
        if((stat.getGameWon() * 10) - (stat.getGameLost() * 2) < 0){
            points = 0;
        }else {
            points = (stat.getGameWon() * 10) - (stat.getGameLost() * 2);
        }
        }
//        stat.setUser(this); // Ensure the bidirectional link is established
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(userEmail, user.userEmail); // Assuming userEmail is a unique identifier.
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEmail);
    }


    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public boolean isActive() {
        return active;
    }

    @Transactional
    public void setActive(boolean active) {
        this.active = active;
    }

    public Ranking getRanking() {
        return ranking;
    }

    public void setRanking(Ranking ranking) {
        this.ranking = ranking;
    }


    public Integer getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }


    public ProfilePicture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(ProfilePicture profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Theme getTheme() {
        return theme;
    }
    public void setTheme(Theme theme) {
        this.theme = theme;
    }
}



