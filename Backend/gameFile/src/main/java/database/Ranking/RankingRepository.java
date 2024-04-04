package database.Ranking;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RankingRepository extends JpaRepository<Ranking,Long>{



}
