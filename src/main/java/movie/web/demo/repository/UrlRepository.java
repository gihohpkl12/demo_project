package movie.web.demo.repository;

import movie.web.demo.domain.role.Role;
import movie.web.demo.domain.url.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {

    @Query("select u from Url u where u.role is null")
    List<Url> getAllNonAuthenticationUrl();

    @Query("select u from Url u left join fetch u.role order by u.id ASC")
    List<Url> findAll();

    @Query("select u from Url u where u.role is not null order by u.orderNum ASC")
    List<Url> getAllAuthenticationUrl();

    @Query("select u from Url u where u.path = :path")
    Optional<Url> findUrlByPath(@Param("path") String path);

    @Query("select u from Url u where u.role = :role")
    List<Url> findUrlByRole(@Param("role") Role role);

//    @Query("select u from Url u where u.role = 'ROLE_ANONYMOUS'")
//    List<Url> getAnonymousUrl();
}
