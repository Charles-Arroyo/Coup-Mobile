package database.ProfilePicture;

import database.Users.User;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilePictureRepository extends JpaRepository<ProfilePicture,Long> {

    ProfilePicture findProfileByUser(User user);
}
