package movie.web.demo.form;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
public class PostReviewEnrollForm {

    @NotNull(message = "내부 오류")
    private Long id;

    @NotBlank(message = "리뷰 내용을 입력해주세요")
    private String postReviewContent;

    @NotBlank(message = "내부 오류")
    private String nickname;

    private LocalDateTime createDate;

    @NotNull(message = "내부 오류")
    private Long boardId;

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
