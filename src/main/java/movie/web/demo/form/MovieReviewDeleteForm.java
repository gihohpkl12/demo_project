package movie.web.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieReviewDeleteForm {
    @NotNull(message = "내부 오류")
    private Long id;

    @NotBlank(message = "내부 오류")
    private String movieSeq;

    @NotBlank(message = "내부 오류")
    private String title;

    @NotBlank(message = "내부 오류")
    private String docid;

    @NotBlank(message = "내부 오류")
    private String nickname;

    @NotNull(message = "내부 오류")
    private Long accountId;

}
