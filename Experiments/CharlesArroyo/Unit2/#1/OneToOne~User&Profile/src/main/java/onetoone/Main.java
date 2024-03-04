package onetoone;


import onetoone.Profiles.Profile;
import onetoone.Profiles.ProfileRepository;
import onetoone.Users.User;
import onetoone.Users.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



/**
 * 
 * @author Vivek Bengre
 * 
 */ 

@SpringBootApplication
@EnableJpaRepositories
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // Create 3 users with their machines
    /**
     *
     * Creates a commandLine runner to enter dummy data into the database
     * As mentioned in User.java just associating the Laptop object with the User will save it into the database because of the CascadeType
     */
    @Bean
    CommandLineRunner initUser(UserRepository userRepository, ProfileRepository profileRepository) {
        return args ->
        {

            User user1 = new User("Cfarroyo", "cfarroyo@iastate.edu");
            Profile profile1 = new Profile("Chuck",1,200);
            user1.setProfile(profile1);
            userRepository.save(user1);
        };
    }

}
