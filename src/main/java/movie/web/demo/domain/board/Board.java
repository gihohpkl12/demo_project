package movie.web.demo.domain.board;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import movie.web.demo.domain.post.PostMetaData;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
public class Board implements Serializable {
    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false)
    private String boardName;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List<PostMetaData> postMetaDataList;
}
