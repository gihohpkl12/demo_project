package movie.web.demo.form;

import feign.form.FormProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaverOAuthAccessTokenRequestResult {
    @FormProperty("access_token")
    private String accessToken;
    @FormProperty("refresh_token")
    private String refreshToken;
    @FormProperty("token_type")
    private String tokenType;
    @FormProperty("expires_in")
    private Long expiresIn;



}

/*
access_token
refresh_token
token_type
expires_in
 */

