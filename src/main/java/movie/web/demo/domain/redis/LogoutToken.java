package movie.web.demo.domain.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("logoutToken")
@Getter
@Setter
public class LogoutToken {

    @Id
    private String logoutToken;

    @TimeToLive
    private Long expiration = 60 * 60 * 24 * 7l;
}
