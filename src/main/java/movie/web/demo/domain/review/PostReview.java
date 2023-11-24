package movie.web.demo.domain.review;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import movie.web.demo.domain.post.Post;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class PostReview {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, name = "content")
    private String reviewContent;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private LocalDateTime createDate;

    @Column(nullable = false)
    private Long boardId;

    @Column(nullable = false)
    private Long accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Post post;

}
