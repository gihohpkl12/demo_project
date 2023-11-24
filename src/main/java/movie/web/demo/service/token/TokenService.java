package movie.web.demo.service.token;

import jakarta.servlet.http.HttpServletRequest;
import movie.web.demo.domain.account.Account;
import movie.web.demo.domain.redis.RefreshToken;
import movie.web.demo.service.token.redis.TokenManageService;

import java.util.Optional;

public interface TokenService {

    String createAccessToken(String email, String nickname, String role, Long id);

    String createRefreshToken(String email, String nickname, String role, Long id);

    Optional<RefreshToken> findRefreshToken(String refreshToken);

    String getToken(HttpServletRequest request, String tokenName);

    Account extractToken(String token);

    String createAccessTokenByRefreshToken(String refreshToken);

}
