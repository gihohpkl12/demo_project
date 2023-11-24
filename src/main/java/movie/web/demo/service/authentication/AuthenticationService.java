package movie.web.demo.service.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import movie.web.demo.domain.account.Account;
import movie.web.demo.service.account.UserAccountService;
import movie.web.demo.service.token.TokenService;
import movie.web.demo.service.token.redis.TokenManageService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface AuthenticationService {

    public final Long LIMIT_TIME = 1000 * 60 * 60 * 24l;

//    public String getAccessToken(HttpServletRequest request);
//
//    public String getRefreshToken(HttpServletRequest request);

//    boolean authenticate(HttpServletRequest request, UserAccountService userAccountService, TokenService tokenService);
    boolean authenticate(HttpServletRequest request, HttpServletResponse response, TokenService tokenService, TokenManageService tokenManageService);

//    boolean authenticateByRefreshToken(HttpServletRequest request, TokenService tokenService);

//    String[] getRefreshTokenInfo(HttpServletRequest request);

    public void setSecurityAuthenticationToken(Account account);

    UsernamePasswordAuthenticationToken createSecurityAuthenticationToken(Account account);
}
