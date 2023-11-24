package movie.web.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardAddForm {

    @NotBlank(message = "board가 null입니다")
    private String boardName;
}
