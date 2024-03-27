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
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


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

    /* This Bean creates the documentation for all the APIs Which is exactly
          what is needed.
        */
    @Bean
    public Docket getAPIdocs() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

}

//import database.Game.Deck;
//import database.Game.Game;
//import database.Game.Player;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Main {
//    public static void main(String[] args) {
//
//
//        Deck deck = new Deck();
//        deck.initializeDeck();
//        deck.shuffle();
//        System.out.println(deck.toString());
//
//        List<Player> players = new ArrayList<>();
//        Game game = new Game(players,deck);
//
//    }
//}


