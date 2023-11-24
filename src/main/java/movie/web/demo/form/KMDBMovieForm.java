package movie.web.demo.form;

import feign.form.FormProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KMDBMovieForm {
    private String title;
    private String docid;
    private String movieSeq;
    private String titleEng;
    private String prodYear;
    private String genre;
    private String ratingDate;
    private String ratingGrade;
    private String nation;
    private String plot;
    private String runtime;
    private String company;
    private List<String> posters;
    private List<String> directors;
    private List<String> actors;
}
