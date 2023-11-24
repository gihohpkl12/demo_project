package movie.web.demo.repository;

import movie.web.demo.domain.redis.LogoutToken;
import movie.web.demo.domain.redis.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LogoutTokenRepository extends CrudRepository<LogoutToken, String> {
    Optional<LogoutToken> findByLogoutToken(String logoutToken);
}
