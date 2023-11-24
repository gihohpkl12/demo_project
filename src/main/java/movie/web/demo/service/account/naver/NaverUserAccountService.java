package movie.web.demo.service.account.naver;

import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import movie.web.demo.domain.account.Account;
import movie.web.demo.feign.NaverOAuthClient;
import movie.web.demo.form.*;
import movie.web.demo.repository.AccountRepository;
import movie.web.demo.service.account.OAuthUserAccountService;
import movie.web.demo.service.token.TokenService;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 네이버 계정으로 로그인, 회원가입
 */
@Service
@RequiredArgsConstructor
public class NaverUserAccountService implements OAuthUserAccountService {

    private final AccountRepository accountRepository;
    private final String naverAuthenticationRequestUrl = "https://nid.naver.com/oauth2.0/authorize";
    private final String naverAccessTokenRequestUrl = "https://nid.naver.com/oauth2.0/token";
    private final String naverUserInfoRequestUrl = "https://openapi.naver.com/v1/nid/me";
    private final String naverSignInRedirectUrl = "http://localhost:8080/naver/sign-in/callback";
    private final String naverSignUpRedirectUrl = "http://localhost:8080/naver/sign-up/callback";
    private final String clientId = "UMKT4dIs6FAj23xkYaC1";
    private final String secret = "fwBz2BIrLy";

    private final Encoder encoder;

    private final Decoder decoder;

    @Override
    public Optional<Account> findAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    /**
     * naver api를 쓰기 위한 feign client
     * @param url
     * @return
     */
    private NaverOAuthClient getClient(String url) {
        NaverOAuthClient naverOAuthClient = Feign.builder()
                .contract(new SpringMvcContract())
                .encoder(encoder)
                .decoder(decoder)
                .target(NaverOAuthClient.class, url);

        return naverOAuthClient;
    }

    /**
     * 네이버 oauth 인증 요청용 url 생성
     * @param work
     * @return
     */
    @Override
    public String getOAuthAuthenticationRequestUrl(String work) {
        return naverAuthenticationRequestUrl + makeAuthenticationRequestForm(work).toString();
    }

    /**
     * 네이버 oauth 인증 요청을 위한 form create 메소드
     * @param work
     * @return
     */
    private NaverOAuthAuthenticationRequestForm makeAuthenticationRequestForm(String work) {
        NaverOAuthAuthenticationRequestForm naverOAuthAuthenticationRequestForm = new NaverOAuthAuthenticationRequestForm();
        if (work.equals("sign-in")) {
            naverOAuthAuthenticationRequestForm.setRedirectUri(naverSignInRedirectUrl);
        } else {
            naverOAuthAuthenticationRequestForm.setRedirectUri(naverSignUpRedirectUrl);
        }
        naverOAuthAuthenticationRequestForm.setClientId(clientId);
        naverOAuthAuthenticationRequestForm.setResponseType("code");
        naverOAuthAuthenticationRequestForm.setState("11111111");

        return naverOAuthAuthenticationRequestForm;
    }

    /**
     * 네이버 로그인에서 네이버 email 가져오기.
     * @param code
     * @return
     */
    @Override
    public String getUserEmail(String code) {
        NaverOAuthAccessTokenRequestResult naverOAuthAccessTokenRequestResult = getClient(naverAccessTokenRequestUrl).accessTokenRequest(makeNaverAccessTokenRequestForm(code));
        NaverAccountInfoRequestResult result = getClient(naverUserInfoRequestUrl).getUserInfo("Bearer "+naverOAuthAccessTokenRequestResult.getAccessToken());
        return result.getResponse().getEmail();
    }

    /**
     * 네이버 access 토큰 생성 요청용 form create 메소드
     * @param code
     * @return
     */
    private NaverOAuthAccessTokenRequestForm makeNaverAccessTokenRequestForm(String code) {
        NaverOAuthAccessTokenRequestForm naverOAuthAccessTokenRequestForm = new NaverOAuthAccessTokenRequestForm();
        naverOAuthAccessTokenRequestForm.setGrantType("authorization_code");
        naverOAuthAccessTokenRequestForm.setClientId(clientId);
        naverOAuthAccessTokenRequestForm.setClientSecret(secret);
        naverOAuthAccessTokenRequestForm.setCode(code);
        naverOAuthAccessTokenRequestForm.setState("11111111");

        return naverOAuthAccessTokenRequestForm;
    }
}
