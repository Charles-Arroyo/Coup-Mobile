package onetoone;

import onetoone.Players.Player;
import onetoone.Players.PlayerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import onetoone.Accounts.Account;
import onetoone.Accounts.AccountRepository;


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
    CommandLineRunner initUser(AccountRepository accountRepository, PlayerRepository playerRepository) {
        return args ->
        {

            Account account1 = new Account("Cfarroyo", "cfarroyo@iastate.edu");
            Player player1 = new Player(001,1,200,"Pro","2026");

            Account account2 = new Account("boOo", "BoOo@iastate.edu");
            Player player2 = new Player(002,1,200,"Free","n/a");
            account1.setPlayer(player1);
            account2.setPlayer(player2);
            accountRepository.save(account1);
            accountRepository.save(account2);
        };
    }

}
