package movie.web.demo.service.token.jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import movie.web.demo.domain.account.Account;
import movie.web.demo.domain.redis.RefreshToken;
import movie.web.demo.exception.AccountException;
import movie.web.demo.exception.TokenException;
import movie.web.demo.service.token.TokenService;
import movie.web.demo.service.token.redis.TokenManageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * JWT 토큰 관련 서비스
 */
@Service
public class JWTTokenService implements TokenService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    private final Long LIMIT_TIME = 1000 * 60 * 60 * 24l;

    /**
     * refresh_token으로 access_token 생성
     * @param refreshToken
     * @return
     */
    @Override
    public String createAccessTokenByRefreshToken(String refreshToken) {
        Account account = extractToken(refreshToken);

        if (account == null) {
            throw new TokenException("refresh token is null");
        }

        return createAccessToken(account.getEmail(), account.getNickname(), account.getRole(), account.getId());
    }
    @Override
    public String createAccessToken(String email, String nickname, String role, Long id) {
        Date start = new Date();
        Date end = new Date(start.getTime() + LIMIT_TIME);

        Claims claims = Jwts.claims()
                .setSubject("access_token")
                .setIssuer("jwt_issuer")
                .setIssuedAt(start)
                .setExpiration(end);

        claims.put("id", id);
        claims.put("email", email);
        claims.put("nickname", nickname);
        claims.put("role", role);

        return Jwts.builder().setHeaderParam("type", "jwt").setClaims(claims).signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    @Override
    public String createRefreshToken(String email, String nickname, String role, Long id) {
        Date start = new Date();
        Date end = new Date(start.getTime() + (LIMIT_TIME * 7));

        Claims claims = Jwts.claims()
                .setSubject("refresh_token")
                .setIssuer("jwt_issuer")
                .setIssuedAt(start)
                .setExpiration(end);

        claims.put("id", id);
        claims.put("email", email);
        claims.put("nickname", nickname);
        claims.put("role", role);

        return Jwts.builder().setHeaderParam("type", "jwt").setClaims(claims).signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    @Override
    public Optional<RefreshToken> findRefreshToken(String refreshToken) {
        return null;
    }

    @Override
    public String getToken(HttpServletRequest request, String tokenName) {
        for(Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(tokenName)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    public Account extractToken(String token) {
        Account result = new Account();
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            Long id = claims.get("id", Long.class);
            String email = claims.get("email", String.class);
            String nickname = claims.get("nickname", String.class);
            String role = claims.get("role", String.class);

            result.setId(id);
            result.setEmail(email);
            result.setNickname(nickname);
            result.setRole(role);

        } catch (TokenException e) {
            System.out.println("토큰이 없음");
            return null;
        } catch (AccountException | SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            System.out.println("잘못된 토큰");
            return null;
        } catch (ExpiredJwtException e) {
            System.out.println("만료된 토큰");
            return null;
        } catch (Exception e) {
            System.out.println("확인이 필요한 예외");
            return null;
        }

        return result;
    }
}
