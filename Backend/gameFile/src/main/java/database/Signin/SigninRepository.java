package database.Signin;

import database.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SigninRepository extends JpaRepository<Signin, Long> {

    // Find the most recent sign-in record for a given user
    Signin findTopByUserOrderByLastSignInTimestampDesc(User user);

    // Find all sign-in records for a given user
    List<Signin> findByUser(User user);

    // Find sign-in records for a given user within a specific time range
    List<Signin> findByUserAndLastSignInTimestampBetween(User user, LocalDateTime startDateTime, LocalDateTime endDateTime);

    // Find sign-in records for a given user with sign-in count greater than a specified value
    List<Signin> findByUserAndSignInCountGreaterThan(User user, int signInCount);

    // Custom query to find the total number of sign-ins for a given user
    @Query("SELECT COUNT(s) FROM Signin s WHERE s.user = :user")
    long countSigninsByUser(@Param("user") User user);

    // Custom query to find the average sign-in count per day for a given user
    @Query("SELECT AVG(s.signInCount) FROM Signin s WHERE s.user = :user AND s.lastSignInTimestamp >= :startDate AND s.lastSignInTimestamp < :endDate")
    double getAverageSigninCountPerDay(@Param("user") User user, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    Optional<Signin> deleteSigninByUser(User user);

}