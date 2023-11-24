package movie.web.demo.service.account;

import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import movie.web.demo.feign.NaverOAuthClient;
import movie.web.demo.form.NaverAccountInfoRequestResult;
import movie.web.demo.form.NaverOAuthAccessTokenRequestResult;
import org.springframework.cloud.openfeign.support.SpringMvcContract;

public interface OAuthUserAccountService<T> extends UserAccountService {

    String getOAuthAuthenticationRequestUrl(String work);
    String getUserEmail(String code);

}
