package movie.web.demo.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordFindingForm {

    @NotBlank(message = "email을 입력해주세요")
    private String email;

    @NotBlank(message = "메일로 전송한 토큰을 입력해주세요")
    private String token;
}
