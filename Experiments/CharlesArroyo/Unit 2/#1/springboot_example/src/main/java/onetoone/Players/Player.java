package onetoone.Players;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.MapsId;


import com.fasterxml.jackson.annotation.JsonIgnore;
import onetoone.Accounts.Account;

@Entity
public class Player {
    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int profileId;

    private int level;

    private int experiencePoints;

    private String subscription;

    private String subscriptionExpire;

    @OneToOne
    @JsonIgnore
    private Account account;


    public Player(int profileId,int level,int experiencePoints, String subscription,String subscriptionExpire) {
        this.profileId= profileId;

        this.level = level;

        this.experiencePoints = experiencePoints;

        this.subscription = subscription;

        this.subscriptionExpire = subscriptionExpire;
    }

    public Player() {

    }


    public int getProfileId(){
        return this.profileId;
    }

    public void setPr(int profileId){
        this.profileId = profileId;
    }


    public void setLevel(int level){
        this.level = level;
    }
    public String getSubstription(){
        return this.subscription;
    }

    public void setSubscription(String subscription){
        this.subscription = subscription;
    }
    public String setSubscriptionExpire(){
        return this.subscriptionExpire;
    }

    public void getSubscriptionExpire(String subscriptionExpire){
        this.subscriptionExpire = subscriptionExpire;
    }

    public int getExperiencePoints(){
        return this.experiencePoints;
    }

    public void setExperiencePoints(int experiencePoints){
        this.experiencePoints = experiencePoints;
    }

    public void setAccount(Account account){
        this.account = account;

    }



}
