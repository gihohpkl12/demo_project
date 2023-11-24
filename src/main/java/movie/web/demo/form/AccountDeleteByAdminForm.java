package movie.web.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDeleteByAdminForm {

    @NotNull(message = "id가 null입니다")
    private Long id;

    @NotBlank(message = "nickname이 null입니다")
    private String nickname;

    @NotBlank(message = "email이 null입니다")
    private String email;
}
