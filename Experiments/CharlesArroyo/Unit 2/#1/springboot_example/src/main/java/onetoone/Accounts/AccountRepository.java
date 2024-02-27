package onetoone.Accounts;

import onetoone.Laptops.Laptop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findById(int id);

    @Transactional
    void deleteById(int id);
}
