package database.Stats;

import database.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface StatRepository extends JpaRepository<Stat, Integer>{

    Optional<User> findByUserId(int userId); // Assumes that the User entity's primary key is of type Integer.

    Optional<User> deleteByUserId(int userId);

    @Transactional
    void deleteById(int id);
}
