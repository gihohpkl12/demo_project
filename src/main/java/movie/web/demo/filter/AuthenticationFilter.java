package movie.web.demo.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import movie.web.demo.service.authentication.AuthenticationService;
import movie.web.demo.service.token.TokenService;
import movie.web.demo.service.token.redis.TokenManageService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.UUID;

/*
    테스트 목록
    1. 토큰 만료 테스트. 생성을 1초 뒤로
 */
@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;

    private final TokenManageService redisTokenManageService;

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("call: "+request.getRequestURI());
        try {
             if (!authenticationService.authenticate(request, response, tokenService, redisTokenManageService)) {
                 System.out.println("authenticate fail");
             }
        } catch (Exception e) {
            System.out.println("error "+e.getMessage()+ " type "+e.getClass());
        }

        filterChain.doFilter(request, response);
    }
}
