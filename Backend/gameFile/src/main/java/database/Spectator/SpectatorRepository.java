package database.Spectator;

import database.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpectatorRepository extends JpaRepository<Spectator, Long> {
    // Additional custom methods can be defined here if necessary

    Spectator findByUser(User user);

    void deleteByUserId(int id);
}
