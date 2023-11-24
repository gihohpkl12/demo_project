package movie.web.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostEnrollForm {

    @NotBlank(message = "내용을 입력해주세요")
    private String postContent;

    @NotBlank(message = "제목을 입력해주세요")
    private String subject;

    @NotNull(message = "내부 오류")
    private Long boardId;

    @NotBlank(message = "내부 오류")
    private String poster;

    @NotBlank(message = "내부 오류")
    private String boardName;

    @NotNull(message = "내부 오류")
    private Long accountId;

    @NotNull(message = "내부 오류")
    private int page;

    @NotNull(message = "내부 오류")
    private int size;
}
