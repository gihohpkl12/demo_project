package movie.web.demo.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class MailForm {

    private String email;
    private String subject;
    private String message;
}
