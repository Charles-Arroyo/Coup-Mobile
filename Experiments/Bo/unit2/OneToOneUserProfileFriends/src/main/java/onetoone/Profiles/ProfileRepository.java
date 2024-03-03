package onetoone.Profiles;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;



public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findById(int id);

    @Transactional
    void deleteById(int id);
}
