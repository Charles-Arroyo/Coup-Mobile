package onetoone.Friends;
import onetoone.Profiles.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface FriendRepository extends JpaRepository<Friend, Long>{
    Friend findById(int id);



    @Transactional
    void deleteById(int id);
}
