package movie.web.demo.domain.redis;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("passwordFindingToken")
@Getter
@Setter
@NoArgsConstructor
public class PasswordFindingToken {

    @Id
    private String passwordToken;

    private String email;

    @TimeToLive
    private Long expiration =  60 * 60 * 24l;

    public PasswordFindingToken(String passwordToken, String email) {
        this.passwordToken = passwordToken;
        this.email = email;
    }
}
