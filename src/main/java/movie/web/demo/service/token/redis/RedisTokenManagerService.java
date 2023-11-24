package movie.web.demo.service.token.redis;

import lombok.RequiredArgsConstructor;
import movie.web.demo.domain.account.Account;
import movie.web.demo.domain.redis.LogoutToken;
import movie.web.demo.domain.redis.PasswordFindingToken;
import movie.web.demo.domain.redis.RefreshToken;
import movie.web.demo.exception.TokenException;
import movie.web.demo.repository.LogoutTokenRepository;
import movie.web.demo.repository.PasswordFindingRepository;
import movie.web.demo.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Redis로 관리 하는 토큰 관련 서비스
 * (refresh 토큰, logout 토큰, 비밀번호 찾기용 토큰)
 */
@Service
@RequiredArgsConstructor
public class RedisTokenManagerService implements TokenManageService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final LogoutTokenRepository logoutTokenRepository;

    private final PasswordFindingRepository passwordFindingRepository;

    @Override
    public boolean isExistLogoutToken(String token) {
        Optional<LogoutToken> logoutToken = logoutTokenRepository.findById(token);
        return logoutToken.isPresent();
    }

    @Override
    public boolean isExistRefreshToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(token);
        return refreshToken.isPresent();
    }

    public Optional<RefreshToken> getRefreshToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(token);
        return refreshToken;
    }
    @Override
    public void saveRefreshToken(String token, Account account) {
        refreshTokenRepository.save(createRefreshToken(token, account));
    }
    @Override
    public void saveLogoutToken(String token) {
        logoutTokenRepository.save(createLogoutToken(token));
    }

    @Override
    public void deleteRefreshToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(token);
        if (!refreshToken.isPresent()) {
            throw new TokenException("refresh token이 없습니다");
        }
        refreshTokenRepository.delete(refreshToken.get());
    }

    private LogoutToken createLogoutToken(String token) {
        LogoutToken logoutToken = new LogoutToken();
        logoutToken.setLogoutToken(token);
        return logoutToken;
    }

    private RefreshToken createRefreshToken(String token, Account account) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(token);
        refreshToken.setNickname(account.getNickname());
        refreshToken.setRole(account.getRole());
        refreshToken.setEmail(account.getEmail());
        return  refreshToken;
    }

    public String savePasswordFindingToken(String email) {
        PasswordFindingToken passwordFindingToken = createPasswordFindingToken(email);
        passwordFindingRepository.save(passwordFindingToken);
        return passwordFindingToken.getPasswordToken();
    }
    public boolean validatePasswordFindingToken(String token, String email) {
        Optional<PasswordFindingToken> passwordFindingToken = passwordFindingRepository.findById(token);
        if (passwordFindingToken.isPresent()) {
            if (passwordFindingToken.get().getEmail().equals(email)) {
                deletePasswordFindingToken(passwordFindingToken.get());
                return true;
            }
        }
        return false;
    }

    private void deletePasswordFindingToken(PasswordFindingToken passwordFindingToken) {
        passwordFindingRepository.delete(passwordFindingToken);
    }
    private PasswordFindingToken createPasswordFindingToken(String email) {
        return new PasswordFindingToken(createUUID(), email);
    }

    private String createUUID() {
        return UUID.randomUUID().toString();
    }

}
