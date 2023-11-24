package movie.web.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class UrlDeleteForm {

    @NotNull(message = "id가 null입니다")
    private Long id;

    @NotBlank(message = "path가 null입니다")
    private String path;

    private String role;
}
