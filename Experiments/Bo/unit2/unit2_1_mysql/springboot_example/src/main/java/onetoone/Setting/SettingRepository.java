package onetoone.Setting;

import onetoone.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SettingRepository extends JpaRepository<Setting, Long>{


    // Custom query method to find a Setting entity based on the user's ID.
    // This method returns an Optional of Setting, which means it can return a Setting object
    // if found, or an empty Optional if no setting is found for the provided userId.
    // The use of Optional is a good practice to avoid null checks and handle the absence of a value in a more functional style.
    Optional<Setting> findByUserId(int userId); // Assumes that the User entity's primary key is of type Integer.

    Optional<Setting> findByupdateEmail(String updateEmail);


    @Transactional
    void deleteById(int id);
}
