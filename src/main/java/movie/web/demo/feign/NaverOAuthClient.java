package movie.web.demo.feign;

import feign.Headers;
import movie.web.demo.form.NaverAccountInfoRequestResult;
import movie.web.demo.form.NaverOAuthAccessTokenRequestForm;
import movie.web.demo.form.NaverOAuthAccessTokenRequestResult;
import movie.web.demo.form.NaverOAuthAuthenticationRequestForm;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

public interface NaverOAuthClient {
    @RequestMapping(method = RequestMethod.GET, consumes = APPLICATION_FORM_URLENCODED_VALUE)
    @Headers("Content-Type: application/json")
    void loginRequest(NaverOAuthAuthenticationRequestForm naverOAuthAuthenticationRequestForm);

    @RequestMapping(method = RequestMethod.GET, consumes = {APPLICATION_FORM_URLENCODED_VALUE})
    @Headers("Content-Type: application/json")
    NaverOAuthAccessTokenRequestResult accessTokenRequest(NaverOAuthAccessTokenRequestForm naverOAuthLoginRequestForm);

    @RequestMapping(method = RequestMethod.GET, consumes = {APPLICATION_FORM_URLENCODED_VALUE})
    NaverAccountInfoRequestResult getUserInfo(@RequestHeader("Authorization") String requester);
}
