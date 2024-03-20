package database;


import database.Friends.Friend;
import database.Friends.FriendRepository;
import database.Users.User;
import database.Users.UserRepository;
import org.hibernate.Hibernate;
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

    /**
     *
     * Creates a commandLine runner to enter dummy data into the database
     * As mentioned in User.java just associating the Laptop object with the User will save it into the database because of the CascadeType
     */
    @Bean
    CommandLineRunner initUser(UserRepository userRepository, FriendRepository friendRepository) {
        return args ->
        {

//            User user1 = userRepository.findByEmailId("ThisIsATest"); // Finds User based on Email
//            User user2 = userRepository.findByEmailId("e"); // Finds User Based on Email
////
////                //Create two friend objects.
//                Friend user1Friend = new Friend(user2.getEmailId());
////                Friend user2Friend = new Friend(user1.getEmailId());
////
////                // Add friends to users
//                user1.addFriends(user1Friend);
////                user2.addFriends(user2Friend);
////
////                // Save changes to the database
////                userRepository.save(user1);
////                userRepository.save(user2);


        };
    }

}
