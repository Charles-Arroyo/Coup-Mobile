package onetoone.Accounts;
import java.util.List;

import onetoone.Laptops.Laptop;
import onetoone.Players.Player;
import onetoone.Players.PlayerRepository;
import onetoone.Users.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;



public class AccountController {

    @Autowired
    AccountRepository accountRepository;
    PlayerRepository playerRepository;

    private final String success = "{\"message\":\"success\"}";
    private final String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/accounts")
    List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    @GetMapping(path = "/accounts/{id}")
    Account getAccountbyID(@PathVariable int id){
        return accountRepository.findById(id);
    }

    @PostMapping(path = "/accounts")
    String creatAccount(@RequestBody Account Account){
        if (Account == null)
            return failure;
        accountRepository.save(Account);
        return success;
    }

    @PutMapping(path = "/accounts/{id}")
    Account updateAccount(@PathVariable int id, @RequestBody Account request){
        Account account = accountRepository.findById(id);
        if(account == null)
            return null;
        accountRepository.save(request);
        return accountRepository.findById(id);
    }

    @DeleteMapping(path = "/accounts/{id}")
    String deleteAccount(@PathVariable int id){
        accountRepository.deleteById(id);
        return success;
    }
    @PutMapping("/accounts/{userId}/players/{playerID}")
    String assignPlayertoAccount(@PathVariable int playerID,@PathVariable int accountID){
        Account account = accountRepository.findById(accountID);
        Player player = playerRepository.findById(playerID);
        if(account == null || player == null)
            return failure;
        player.setAccount(account);
        account.setPlayer(player);
        accountRepository.save(account);
        return success;
    }

}
