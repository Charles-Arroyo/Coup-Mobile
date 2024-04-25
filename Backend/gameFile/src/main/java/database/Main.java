package database;
import database.Friends.Friend;
import database.Friends.FriendRepository;
import database.Lobby.LobbyRepository;
//import database.Signin.SigninSocketConfigurator;
import database.Ranking.RankingRepository;
import database.Users.User;
import database.Users.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import database.Game.Deck;
import database.Game.Game;
import database.Game.Player;

import static org.springframework.boot.SpringApplication.run;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
//import database.Signin.SigninSocketConfigurator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 *
 * @author Vivek Bengre
 *
 */

@SpringBootApplication
@EnableJpaRepositories
public class Main {

    public static void main(String[] args) {
//        ApplicationContext applicationContext = SpringApplication.run(Main.class, args);
//        SigninSocketConfigurator.setApplicationContext(applicationContext);
        SpringApplication.run(Main.class, args);

        
    }

    /**
     * Creates a commandLine runner to enter dummy data into the database
     * As mentioned in User.java just associating the Laptop object with the User will save it into the database because of the CascadeType
     */
    @Bean
    CommandLineRunner initUser(UserRepository userRepository, FriendRepository friendRepository, LobbyRepository lobbyRepository) {
        return args ->
        {

        };
    }


//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
//                .build();
//    }
//    public static void main(String[] args) {
//
//
//
//
//
//    }


}