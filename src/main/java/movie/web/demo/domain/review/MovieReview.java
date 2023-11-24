package movie.web.demo.domain.review;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class MovieReview {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, name = "content")
    private String reviewContent;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String docid;

    @Column(nullable = false)
    private String movieSeq;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private Long accountId;

    @Column(nullable = false)
    private LocalDateTime createDate;


}
