package database;
import database.Friends.Friend;
import database.Friends.FriendRepository;
import database.Lobby.LobbyRepository;
import database.Users.User;
import database.Users.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import database.Game.Deck;
import database.Game.Game;
import database.Game.Player;

import java.util.ArrayList;
import java.util.List;



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
//        List<Player> players = new ArrayList<>(); // Create an Array list of Players
//        Game game = new Game(players); //Pass in Deck and Array List
//        game.initGame("A","B","C","D"); // Sends four players, see init game method
//        System.out.println(game.toString());
//
//        game.getCurrentPlayer().setCurrentMove("Income");
//        game.setLastCharacterMove("Income");
//        game.turn(game.getCurrentPlayer(),game.getCurrentPlayer().getCurrentMove());
//        System.out.println();
//        System.out.println(game.toString());
//
//    }


}





