package database.Admin;
import database.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface AdminRepository extends JpaRepository<User, Integer> {

    User findUserById(int id);

    User findByUserEmail(String email);

}
