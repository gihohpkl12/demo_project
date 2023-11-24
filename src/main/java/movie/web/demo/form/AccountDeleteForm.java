package movie.web.demo.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDeleteForm {

    private Long id;

    @NotBlank(message = "email을 입력해주세요")
    private String email;

    @NotBlank(message = "password를 입력해주세요")
    private String password;
}
