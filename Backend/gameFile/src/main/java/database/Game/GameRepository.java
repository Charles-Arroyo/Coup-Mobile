package database.Game;

import database.Chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Message, Long> {

}
