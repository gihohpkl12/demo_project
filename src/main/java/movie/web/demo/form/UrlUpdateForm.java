package movie.web.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UrlUpdateForm {

    @NotNull(message = "id가 null입니다")
    private Long id;

    @NotBlank(message = "path가 null입니다")
    private String path;

    private Long role;

    private Long orderNum;
}
