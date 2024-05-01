package database.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 * 
 * @author Vivek Bengre
 * 
 */ 

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(int id);

    User findByUserEmail(String userEmail);

    User findByName(String name);

    @Query("SELECT u FROM User u")
    List<User> findAllWithoutProfilePicture();




    @Transactional
    void deleteById(int id);
}
