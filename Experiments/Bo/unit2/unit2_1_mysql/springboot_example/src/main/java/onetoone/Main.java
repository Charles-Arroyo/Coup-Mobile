package onetoone;

import onetoone.Card.CardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


import onetoone.Users.User;
import onetoone.Users.UserRepository;
import onetoone.Card.Card;
import onetoone.Card.CardRepository;

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
     * @param userRepository repository for the User entity
     * Creates a commandLine runner to enter dummy data into the database
     * As mentioned in User.java just associating the Laptop object with the User will save it into the database because of the CascadeType
     */
    @Bean
    CommandLineRunner initUser(UserRepository userRepository, CardRepository cardRepository) {
        return args -> {

            Card card1 = new Card(1,"Bo", 12, 2);
            Card card2 = new Card(2,"Alice", 10, 3);
            Card card3 = new Card(3,"Charlie", 5, 4);

            cardRepository.save(card1);
            cardRepository.save(card2);
            cardRepository.save(card3);

            User user1 = new User("John", "john@somemail.com");
            User user2 = new User("Jane", "jane@somemail.com");
            User user3 = new User("Justin", "justin@somemail.com");
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);





        };
    }

}
