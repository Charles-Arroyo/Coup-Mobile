package onetoone.Setting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface SettingRepository extends JpaRepository<Setting, Long> {

    Setting findById(int id);

    @Transactional
    void deleteById(int id);
}
