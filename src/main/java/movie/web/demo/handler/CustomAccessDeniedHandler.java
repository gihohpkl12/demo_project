package movie.web.demo.handler;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import movie.web.demo.service.authorize.AuthorizeService;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;

/**
 * 인가 실패 handler
 */
@Component
@RequiredArgsConstructor
@DependsOn("commonAuthorizeService")
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final AuthorizeService commonAuthorizeService;

    private RequestMatcher anonymousUrl;

    @PostConstruct
    public void init() {
        this.anonymousUrl = commonAuthorizeService.getAnonymousUrl();
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String url = "/?message=";
        String message = "권한이 없습니다";

        if (anonymousUrl.matcher(request).isMatch()) {
            message = "로그아웃 해주시기 바랍니다";
        }

        response.sendRedirect(url+URLEncoder.encode(message, "UTF-8"));
    }
}
