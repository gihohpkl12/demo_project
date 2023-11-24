package movie.web.demo.form;

import feign.form.FormProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaverOAuthAuthenticationRequestForm {

    @FormProperty("response_type")
    private String responseType;
    @FormProperty("client_id")
    private String clientId;
    @FormProperty("redirect_uri")
    private String redirectUri;
    private String state;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("?response_type=")
                .append(responseType)
                .append("&client_id=")
                .append(clientId)
                .append("&state=")
                .append(state)
                .append("&redirect_uri=")
                .append(redirectUri);

        return sb.toString();
    }

}
