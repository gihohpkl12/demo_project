package movie.web.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleAddForm {

    @NotBlank(message = "role이 null입니다")
    private String role;

    @NotNull(message = "order가 null입니다")
    private Long orderNum;
}
