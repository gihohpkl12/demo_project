package movie.web.demo.service.authentication.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import movie.web.demo.domain.account.Account;
import movie.web.demo.domain.redis.RefreshToken;
import movie.web.demo.service.authentication.AuthenticationService;
import movie.web.demo.service.token.redis.TokenManageService;
import movie.web.demo.util.CookieUtil;
import movie.web.demo.service.token.TokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * JWT 토큰 인증 서비스
 */
@Service
@RequiredArgsConstructor
public class JwtAuthenticationService implements AuthenticationService {


    /**
     * SecurityContext에 저장할 AuthenticationToken 생성
     * @param account
     * @return
     */
    @Override
    public UsernamePasswordAuthenticationToken createSecurityAuthenticationToken(Account account) {
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(new String[] {account.getRole()})
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(account, null, authorities);
    }

    /**
     * SecurityContext에 토큰 저장
     * @param account
     */
    public void setSecurityAuthenticationToken(Account account) {
        SecurityContextHolder.getContext().setAuthentication(createSecurityAuthenticationToken(account));
    }

    /**
     * access_token으로 인증에 실패한 경우 refresh_token으로 인증 시도.
     * @param request
     * @param tokenService
     * @param tokenManageService
     * @return
     */
    private boolean authenticateByRefreshToken(HttpServletRequest request, TokenService tokenService, TokenManageService tokenManageService) {
        String refreshToken = tokenService.getToken(request, "refresh_token");
        if (!tokenManageService.isExistRefreshToken(refreshToken)) {
            return false;
        }

        Account extractAccount = tokenService.extractToken(refreshToken);
        Optional<RefreshToken> getRefreshToken = tokenManageService.getRefreshToken(refreshToken);
        if (extractAccount == null) {
            return false;
        }
        if(!getRefreshToken.isPresent()) {
            return false;
        }

        if(!getRefreshToken.get().getEmail().equals(extractAccount.getEmail()) ||
                !getRefreshToken.get().getRole().equals(extractAccount.getRole())) {
            return false;
        }

        setSecurityAuthenticationToken(extractAccount);

        return true;
    }

    /**
     * 로그아웃 했던 토큰인지 여부 확인
     * @param token
     * @param tokenManageService
     * @return
     */
    private boolean isLogout(String token, TokenManageService tokenManageService) {
        return tokenManageService.isExistLogoutToken(token);
    }

    /**
     * 인증 시도
     * (access_token으로 인증 시도. 실패할 경우 refresh_token으로 인증 시도)
     * @param request
     * @param response
     * @param tokenService
     * @param tokenManageService
     * @return
     */
    public boolean authenticate(HttpServletRequest request, HttpServletResponse response, TokenService tokenService, TokenManageService tokenManageService) {
        boolean result = true;
        Account extractAccount = null;

        String accessToken = tokenService.getToken(request, "access_token");
        if (accessToken != null && isLogout(accessToken, tokenManageService)) {
            return false;
        }

        if (accessToken != null) {
            extractAccount = tokenService.extractToken(accessToken);
            if (extractAccount == null) {
                result = false;
            }
        } else {
            result = false;
        }

        // access_token으로 인증에 실패한 경우
        if (!result) {
            if (authenticateByRefreshToken(request, tokenService, tokenManageService)) {
                CookieUtil cookieUtil = new CookieUtil();
                cookieUtil.setCookie(response, "access_token", tokenService.createAccessTokenByRefreshToken(tokenService.getToken(request, "refresh_token")));
            }
        } else {
            setSecurityAuthenticationToken(extractAccount);
        }

        return result;
    }
}
/*

 */
