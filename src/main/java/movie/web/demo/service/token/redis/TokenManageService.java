package movie.web.demo.service.token.redis;

import movie.web.demo.domain.account.Account;
import movie.web.demo.domain.redis.RefreshToken;

import java.util.Optional;

public interface TokenManageService {
    boolean isExistLogoutToken(String token);

    boolean isExistRefreshToken(String token);

    void saveRefreshToken(String accessToken, String refreshToken, Account account);
    public void updateRefreshToken(String accessToken, String refreshToken);

    void saveLogoutToken(String token);

    void deleteRefreshToken(String token);

    public String savePasswordFindingToken(String email);
    public boolean validatePasswordFindingToken(String token, String email);

    public Optional<RefreshToken> getRefreshToken(String token);

}
