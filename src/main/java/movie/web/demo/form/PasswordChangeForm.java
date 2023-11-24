package movie.web.demo.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeForm {

    @NotBlank(message = "email을 입력해주세요")
    private String email;

    @NotBlank(message = "password를 입력해주세요")
    private String password;

    @NotBlank(message = "새로운 password를 반복해서 입력해주세요")
    private String repeatPassword;

    @NotBlank(message = "새로운 password를 입력해주세요")
    private String newPassword;
}
