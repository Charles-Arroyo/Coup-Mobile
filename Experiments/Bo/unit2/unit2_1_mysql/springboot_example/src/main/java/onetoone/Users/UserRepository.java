package onetoone.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 
 * @author Vivek Bengre
 * 
 */

public interface UserRepository extends JpaRepository<User, Long> {


    User findById(int id);

    // Removed static keyword and corrected return type to Optional<User>
//    Optional<User> findByUsername(String username);

    @Transactional
    void deleteById(int id);
}
