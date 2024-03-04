package onetoone.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(int id);

    User findByEmailId(String emailId);



    User findBySettingId(int id);

    User findByPassword(String password);

    @Transactional
    void deleteById(int id);
}
