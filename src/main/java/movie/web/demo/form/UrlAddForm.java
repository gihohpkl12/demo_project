package movie.web.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlAddForm {

    @NotBlank(message = "path가 null입니다")
    private String path;

    @NotNull(message = "권한을 선택해주세요")
    private Long role;

    private Long orderNum;
}
