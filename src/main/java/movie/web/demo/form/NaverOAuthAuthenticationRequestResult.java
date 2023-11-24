package movie.web.demo.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaverOAuthAuthenticationRequestResult {
    private String code;
    private String state;
    private String error;
    private String error_description;
}
