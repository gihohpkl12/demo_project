package movie.web.demo.repository;

import movie.web.demo.domain.redis.LogoutToken;
import movie.web.demo.domain.redis.PasswordFindingToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PasswordFindingRepository extends CrudRepository<PasswordFindingToken, String> {


}
