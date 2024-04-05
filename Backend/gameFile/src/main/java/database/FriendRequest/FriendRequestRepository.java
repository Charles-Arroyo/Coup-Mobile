package database.FriendRequest;

import database.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByRequestedUser(User user);
    List<FriendRequest> findByRequestingUser(User user);
    boolean existsByRequestingUserAndRequestedUser(User requestingUser, User requestedUser);

    FriendRequest findByRequestingUserAndRequestedUser(User requestingUser, User rrequestUser);

    @Transactional
    void deleteByRequestingUserAndRequestedUser(User requestingUser, User requestedUser);
}