package database.Lobby;
import database.Chat.Message;
import database.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LobbyRepository extends JpaRepository<Lobby, Long> {
    Lobby findById(int id);

    void deleteById(int id);

    boolean existsById(int id);
}
