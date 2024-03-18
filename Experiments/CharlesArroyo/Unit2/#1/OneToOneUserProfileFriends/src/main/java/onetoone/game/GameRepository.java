package onetoone.game;

import onetoone.Setting.Setting;
import onetoone.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long>{

    Optional<Setting> findByUserId(int userId); // Assumes that the User entity's primary key is of type Integer.



    @Transactional
    void deleteById(int id);
}
