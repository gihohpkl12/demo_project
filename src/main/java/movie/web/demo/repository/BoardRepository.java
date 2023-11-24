package movie.web.demo.repository;

import movie.web.demo.domain.board.Board;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select DISTINCT b from Board b join fetch b.postMetaDataList where b.boardName = :boardName")
    Optional<Board> findBoard(@Param("boardName") String boardName);


    @Query("select b from Board b left join fetch b.postMetaDataList")
    List<Board> findBoard2();

    @Query("select b from Board b")
    List<Board> getAllBoard();
}
