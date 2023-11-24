package movie.web.demo.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieDetailForm {
    @NotBlank(message = "title null")
    private String title;
    @NotBlank(message = "movieSeq null")
    private String movieSeq;
    @NotBlank(message = "docid null")
    private String docid;
}
