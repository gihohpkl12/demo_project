package movie.web.demo.service.board;

import movie.web.demo.domain.board.Board;
import movie.web.demo.form.BoardAddForm;
import movie.web.demo.form.BoardDeleteForm;
import movie.web.demo.form.BoardUpdateForm;
import movie.web.demo.service.post.PostService;

import java.util.List;

public interface BoardService {

    public Board getBoard(String boardName);

    public Board getBoard(Long id);

    public List<Board> getAllBoard();

    public void deleteBoard(BoardDeleteForm boardDeleteForm, PostService postService);

    public void addBoard(BoardAddForm boardAddForm);

    public void updateBoard(BoardUpdateForm boardUpdateForm);
}
