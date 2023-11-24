package movie.web.demo.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import feign.form.FormProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaverAccountInfoRequestResult {

    @JsonProperty("resultcode")
    private String resultCode;

    private String message;

    private Response response;

    static public class Response {
        private String id;
        private String email;

        public String getId() {
            return this.id;
        }

        public String getEmail() {
            return this.email;
        }

    }
}
