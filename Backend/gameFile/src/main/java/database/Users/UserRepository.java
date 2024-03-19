package database.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(int id);

    User findByUserEmail(String userEmail);
    User findBySettingId(int id);

    @Transactional
    void deleteById(int id);
}
