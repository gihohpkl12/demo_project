package movie.web.demo.service.authentication.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import movie.web.demo.domain.account.Account;
import movie.web.demo.domain.redis.RefreshToken;
import movie.web.demo.exception.AccountException;
import movie.web.demo.exception.TokenException;
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
    private boolean authenticateByRefreshToken(HttpServletRequest request,HttpServletResponse response, TokenService tokenService, TokenManageService tokenManageService, String accessToken) {
        System.out.println("here10");
        String refreshToken = tokenService.getToken(request, "refresh_token");
        if (refreshToken != null) {
            Optional<RefreshToken> getRefreshToken = tokenManageService.getRefreshToken(refreshToken);
            Account extractAccount = tokenService.extractToken(refreshToken);
            if (!getRefreshToken.isPresent()) {
                return false;
            }

            if (!getRefreshToken.get().getAccessToken().equals(accessToken)) {
                tokenManageService.deleteRefreshToken(getRefreshToken.get().getRefreshToken());
                CookieUtil cookieUtil = new CookieUtil();
                cookieUtil.deleteCookie(response, "refresh_token");
                cookieUtil.deleteCookie(response, "access_token");
                return false;
            }

            if(!getRefreshToken.get().getEmail().equals(extractAccount.getEmail()) ||
                    !getRefreshToken.get().getRole().equals(extractAccount.getRole())) {
                return false;
            }

            return true;
        }

        return false;
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
        String accessToken = tokenService.getToken(request, "access_token");
        if (accessToken != null) {
            if (isLogout(accessToken, tokenManageService)) {
                return false;
            }

            try {
                Account extractAccount = tokenService.extractToken(accessToken);
                setSecurityAuthenticationToken(extractAccount);
                return true;
            } catch (ExpiredJwtException e) {
                if (authenticateByRefreshToken(request, response, tokenService, tokenManageService, accessToken)) {
                    String refreshToken = tokenService.getToken(request, "refresh_token");
                    CookieUtil cookieUtil = new CookieUtil();
                    String newAccessToken = tokenService.createAccessTokenByRefreshToken(tokenService.getToken(request, "refresh_token"));
                    cookieUtil.setCookie(response, "access_token", newAccessToken);
                    tokenManageService.updateRefreshToken(newAccessToken, refreshToken);
                    setSecurityAuthenticationToken(tokenService.extractToken(newAccessToken));
                    return true;
                }
                return false;
            } catch (TokenException e) {
                System.out.println("토큰이 없음");
                return false;
            } catch (AccountException | SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
                System.out.println("잘못된 토큰");
                return false;
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }
}
/*

 */
