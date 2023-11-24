package movie.web.demo.service.board;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import movie.web.demo.domain.board.Board;
import movie.web.demo.exception.BoardException;
import movie.web.demo.form.BoardAddForm;
import movie.web.demo.form.BoardDeleteForm;
import movie.web.demo.form.BoardUpdateForm;
import movie.web.demo.repository.BoardRepository;
import movie.web.demo.service.post.PostService;
import movie.web.demo.validator.custom.BoardValidator;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultBoardService implements BoardService {

    private final BoardRepository boardRepository;

    private final BoardValidator boardValidator = new BoardValidator();

    @Override
    public Board getBoard(String boardName) {
        Optional<Board> board = boardRepository.findBoard(boardName);
        if (board.isPresent()) {
            return board.get();
        }

        return null;
    }

    @Override
    @Transactional
    public Board getBoard(Long id) {
        Optional<Board> board = boardRepository.findById(id);
        if (!board.isPresent()) {
            throw new BoardException("해당 board가 없습니다");
        }

        return board.get();
    }

    /**
     * 모든 게시판 조회.
     * 게시판은 변동사항이 적고, 동일하게 조회될 일이 많기 때문에 캐시로 저장.
     * @return
     */
    @Cacheable("boards")
    public List<Board> getAllBoard() {
        List<Board> boards = boardRepository.getAllBoard();
        if (boards == null) {
            return new ArrayList<Board>();
        }

        return boards;
    }

    /**
     * 게시판 업데이트.
     * 게시판이 업데이트 되면 기존에 캐시된 게시판 정보를 삭제함.
     * @param boardUpdateForm
     */
    @CacheEvict(value = "boards", allEntries = true)
    @Transactional
    public void updateBoard(BoardUpdateForm boardUpdateForm) {
        Optional<Board> board = boardRepository.findById(boardUpdateForm.getId());
        Optional<Board> newBoard = boardRepository.findBoard(boardUpdateForm.getBoardName());
        if (boardValidator.updateCheck(board, newBoard)) {
            board.get().setBoardName(boardUpdateForm.getBoardName());
        }
    }

    /**
     * 게시판 추가.
     * 게시판이 추가되면 기존에 캐시된 게시판 정보를 삭제함
     * @param boardAddForm
     */
    @CacheEvict(value = "boards", allEntries = true)
    public void addBoard(BoardAddForm boardAddForm) {
        Optional<Board> board = boardRepository.findBoard(boardAddForm.getBoardName());
        if (!boardValidator.addCheck(board)) {
            throw new BoardException("이미 존재하는 Board입니다");
        }

        boardRepository.save(createBoard(boardAddForm));
    }

    private Board createBoard(BoardAddForm boardAddForm) {
        Board board = new Board();
        board.setBoardName(boardAddForm.getBoardName());
        return board;
    }

    /**
     * 게시판 삭제
     * 게시판이 추가되면 기존에 캐시된 게시판 정보를 삭제함
     */
    @CacheEvict(value = "boards", allEntries = true)
    @Transactional
    public void deleteBoard(BoardDeleteForm boardDeleteForm, PostService postService) {
        Optional<Board> board = boardRepository.findById(boardDeleteForm.getId());
        if (!boardValidator.deleteCheck(board)) {
            throw new BoardException("존재하지 않는 Board입니다");
        }

        if (board.get().getPostMetaDataList().size() > 0) {
            postService.deleteAllPostOfBoard(board.get());
        }

        boardRepository.delete(board.get());
    }
}
