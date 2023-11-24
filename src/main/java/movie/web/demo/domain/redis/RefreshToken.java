package movie.web.demo.domain.redis;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("refreshToken")
@Getter
@Setter
public class RefreshToken {

    @Id
    private String refreshToken;

    private String nickname;

    private String email;

    private String role;

    @TimeToLive
    private Long expiration =  60 * 60 * 24 * 7l;
}
