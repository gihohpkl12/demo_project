package movie.web.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDeleteForm {

    @NotNull(message = "내부 오류")
    private Long postMetaDataId;

    @NotBlank(message = "내부 오류")
    private String nickname;

    @NotNull(message = "내부 오류")
    private Long boardId;

    @NotBlank(message = "내부 오류")
    private String boardName;

    @NotNull(message = "내부 오류")
    private Long accountId;

    @NotNull(message = "내부 오류")
    private int page;

    @NotNull(message = "내부 오류")
    private int size;
}
