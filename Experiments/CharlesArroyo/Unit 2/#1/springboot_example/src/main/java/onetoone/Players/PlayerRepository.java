package onetoone.Players;

import onetoone.Players.Player;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PlayerRepository extends JpaRepository<Player, Long>{

    Player findById(int id);

    @Transactional
    void deleteById(int id);
}
