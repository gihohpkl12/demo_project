package movie.web.demo.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleAddResult {

    private String message;

    public RoleAddResult(String message) {
        this.message = message;
    }
}
