package movie.web.demo.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NicknameChangeForm {

    @NotBlank(message = "현재 nickname을 입력해주세요")
    private String curNickname;

    @NotBlank(message = "새로운 nickname을 입력해주세요")
    private String newNickname;

    @NotBlank(message = "password를 입력해주세요")
    private String password;
}
