package movie.web.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountRoleUpdateForm {

    @NotNull(message = "id가  null입니다")
    private Long id;

    @NotNull(message = "role이  null입니다")
    private Long role;

    @NotBlank(message = "email이 null입니다")
    private String email;
}
