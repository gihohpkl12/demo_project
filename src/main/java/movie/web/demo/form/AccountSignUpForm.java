package movie.web.demo.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AccountSignUpForm {

    @NotBlank(message = "nickname을 입력해주세요")
    private String nickname;

    @NotBlank(message = "password를 입력해주세요")
    private String password;

    @NotBlank(message = "password를 반복해서 입력해주세요")
    private String repeatPassword;

    @NotBlank(message = "email을 입력해주세요")
    private String email;

    private String type;

}
