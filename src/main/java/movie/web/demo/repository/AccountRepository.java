package movie.web.demo.repository;

import movie.web.demo.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    public Optional<Account> findByEmail(String email);

    public List<Account> findByEmailOrNickname(String email, String nickname);

    public Optional<Account> findByEmailAndPassword(String email, String password);

    public Optional<Account> findByNickname(String nickname);

    @Query("select a from Account a where a.role = 'ROLE_USER'")
    public List<Account> findAllUser();

    @Query("select a from Account a where a.role <> 'ROLE_USER' ")
    public List<Account> findAllManager();

    @Query("select a from Account a where a.role = :role")
    public List<Account> findByRole(@Param("role") String role);
}
