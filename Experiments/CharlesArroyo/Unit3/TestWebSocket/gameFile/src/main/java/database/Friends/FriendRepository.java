package database.Friends;
import database.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface FriendRepository extends JpaRepository<Friend, Long>{
    Friend findById(int id);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Friend f WHERE f.friendEmail1 = :friendEmail1 AND f.friendEmail2 = :friendEmail2")
    boolean friendshipExistsByUserEmails(@Param("friendEmail1") String friendEmail1, @Param("friendEmail2") String friendEmail2);

    List<Friend> findByFriendEmail1(String friendEmail1);

    

    List<Friend> findByFriendEmail1AndFriendEmail2(String email1, String email2);







    @Transactional
    void deleteById(int id);
}
