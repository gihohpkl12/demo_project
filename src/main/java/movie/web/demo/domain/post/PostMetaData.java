package movie.web.demo.domain.post;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import movie.web.demo.domain.board.Board;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class PostMetaData {
    @Id @GeneratedValue
    @Column(name = "postMetaData_id")
    private Long id;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String poster;

    @Column(nullable = false)
    private Long accountId;

    private LocalDateTime createDate;

    private Long viewCount = 0l;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
//    @OneToOne(mappedBy = "postMetaData", fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

}
