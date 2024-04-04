package database.Ranking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RankingRepository extends JpaRepository<Ranking, Long> {

    // Find a ranking by its name
    Optional<Ranking> findByName(String name);

    // Find the ranking with the highest ID (assuming ID is auto-incremented)
    Optional<Ranking> findTopByOrderByIdDesc();

    // Find all rankings ordered by ID in descending order
    List<Ranking> findAllByOrderByIdDesc();

    // Delete a ranking by its name
    @Transactional
    void deleteByName(String name);

    // Check if a ranking with a specific name exists
    boolean existsByName(String name);
}