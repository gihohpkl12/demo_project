package movie.web.demo.validator.custom;

import movie.web.demo.domain.board.Board;
import movie.web.demo.exception.BoardException;

import java.util.Optional;

public class BoardValidator extends DefaultValidator {

    public boolean updateCheck(Optional<?> t, Optional<?> t2) {
        if (!updateCheck(t)) {
            throw new BoardException("존재하지 않는 게시판 입니다");
        }

        if (isExist(t2)) {
            Board preBoard = (Board) t.get();
            Board newBoard = (Board) t2.get();

            if (!preBoard.getBoardName().equals(newBoard.getBoardName())) {
                throw new BoardException("이미 존재하는 Board 입니다");
            }
        }
        return true;
    }

}
