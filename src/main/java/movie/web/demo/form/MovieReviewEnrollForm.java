package movie.web.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class MovieReviewEnrollForm {

    @NotBlank(message = "내용을 입력해주세요")
    private String reviewContent;

    @NotBlank(message = "내부 오류")
    private String title;

    @NotBlank(message = "내부 오류")
    private String docid;

    @NotBlank(message = "내부 오류")
    private String movieSeq;

    @NotBlank(message = "내부 오류")
    private String nickname;

    @NotNull(message = "내부 오류")
    private Long accountId;


    private LocalDateTime createDate;
}
