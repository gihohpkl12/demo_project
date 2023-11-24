package movie.web.demo.form;

import feign.form.FormProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaverOAuthAccessTokenRequestForm {
    private String code;
    private String state;
    @FormProperty("grant_type")
    private String grantType;
    @FormProperty("client_id")
    private String clientId;
    @FormProperty("client_secret")
    private String clientSecret;
    @FormProperty("refresh_token")
    private String refreshToken;
    @FormProperty("access_token")
    private String accessToken;
    @FormProperty("service_provider")
    private String serviceProvider;
}

/*
grant_type
client_id
client_secret
refresh_token
access_token
service_provider
 */
