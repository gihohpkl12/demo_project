package movie.web.demo.security;

import lombok.RequiredArgsConstructor;
import movie.web.demo.filter.AuthenticationFilter;
import movie.web.demo.handler.CustomAccessDeniedHandler;
import movie.web.demo.handler.CustomAuthenticationFailHandler;
import movie.web.demo.service.authorize.AuthorizationManagerMaker;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationFilter authenticationFilter;

    private final AuthorizationManagerMaker authorizationManagerMaker;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;

//    private final CustomMultipartFilter multipartFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.cors(c -> c.disable());
        http.httpBasic(basic -> basic.disable());
        http.formLogin(form -> form.disable());


//        http.addFilterBefore(new MultipartFilter(), SecurityContextPersistenceFilter.class);
        http.headers(head -> head.frameOptions(option ->option.sameOrigin()));
        http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));

        http.authorizeHttpRequests(request -> request.anyRequest()
                .access(authorizationManagerMaker.getCustomAuthorizationManager()));

//        authenticationEntryPoint 인증 실패
//        accessDeniedHandler 인가 실패
        http.exceptionHandling(handler -> handler.accessDeniedHandler(customAccessDeniedHandler));
        http.exceptionHandling(handler -> handler.authenticationEntryPoint(new CustomAuthenticationFailHandler()));




//        http.authorizeHttpRequests(request -> request.requestMatchers("/test")
//                .hasRole("USER"));
//
//        http.authorizeHttpRequests(request -> request.requestMatchers("/", "sign-up", "/error", "sign-in")
//                .permitAll()
//                .anyRequest()
//                .authenticated());

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
//        http.addFilterAfter(multipartFilter, FilterSecurityInterceptor.class);


        return http.build();

    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers("/fonts/**")
                .requestMatchers("/js/**")
                .requestMatchers("/css/**")
                .requestMatchers("/photo_upload/**")
                .requestMatchers("/img/**");
    }
}
