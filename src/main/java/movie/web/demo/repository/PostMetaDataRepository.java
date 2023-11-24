package movie.web.demo.repository;

import jakarta.transaction.Transactional;
import movie.web.demo.domain.board.Board;
import movie.web.demo.domain.post.PostMetaData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostMetaDataRepository extends JpaRepository<PostMetaData, Long> {

    @Query("select p from PostMetaData p join fetch p.post where p.subject = :sub")
    Optional<PostMetaData> findBySubject(@Param("sub")String sub);

    @Query("select p from PostMetaData p join fetch p.post where p.board = :board")
    List<PostMetaData> findPostMetaDataListByBoard(@Param("board") Board board, Pageable pageable);

    @Query("select p from PostMetaData p join fetch p.post where p.board = :board")
    List<PostMetaData> findPostMetaDataListByBoard(@Param("board") Board board);

    @Query("select p from PostMetaData p where p.id = :id")
    Optional<PostMetaData> findById(@Param("id")Long id);

    @Query("select p from PostMetaData p join fetch p.post where p.accountId = :id")
    List<PostMetaData> findAllPostMetaDataOfUser(@Param("id") Long id);
}
