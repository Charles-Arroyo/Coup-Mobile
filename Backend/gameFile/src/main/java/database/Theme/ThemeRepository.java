package database.Theme;

import database.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeRepository extends JpaRepository<Theme,Long> {
    Theme findByUser(User user);
}
