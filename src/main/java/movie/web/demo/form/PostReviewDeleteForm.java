package movie.web.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostReviewDeleteForm {

    @NotNull(message = "내부 오류")
    private Long id;

    @NotNull(message = "내부 오류")
    private Long boardId;

    @NotBlank(message = "내부 오류")
    private String nickname;

    @NotBlank(message = "내부 오류")
    private String boardName;

    @NotNull(message = "내부 오류")
    private Long postMetaDataId;

    @NotNull(message = "내부 오류")
    private Long accountId;

    @NotNull(message = "내부 오류")
    private int page;

    @NotNull(message = "내부 오류")
    private int size;
}
