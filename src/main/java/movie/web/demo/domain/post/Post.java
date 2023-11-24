package movie.web.demo.domain.post;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import movie.web.demo.domain.review.PostReview;

import java.util.List;

@Entity
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @Column(columnDefinition="TEXT")
    private String postContent;

    @OneToOne(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PostMetaData postMetaData;

    @OneToMany(mappedBy ="post", fetch = FetchType.LAZY)
    private List<PostReview> reviews;
}
