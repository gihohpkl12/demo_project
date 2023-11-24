package movie.web.demo.controller;

import lombok.RequiredArgsConstructor;
import movie.web.demo.domain.board.Board;
import movie.web.demo.domain.post.PostMetaData;
import movie.web.demo.exception.BoardException;
import movie.web.demo.service.board.BoardService;
import movie.web.demo.service.post.PostService;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final PostService defaultPostService;

    private final BoardService defaultBoardService;
    private final String BOARD_PAGE = "board";
    private final String REDIRECT_MAIN_PAGE = "redirect:/";

    /**
     * =========================================================================================================
     * 게시판
     */
    @GetMapping("/board")
    public String boardPage(@RequestParam("id") Long id, @RequestParam("boardName") String boardName,  Model model, RedirectAttributes redirectAttributes, Pageable pageable) {
        try {
            Board board = new Board();
            board.setBoardName(boardName);
            board.setId(id);

            List<PostMetaData> postMetadata = defaultPostService.findPostMetaDataByBoard(board, pageable);

            model.addAttribute("boards", defaultBoardService.getAllBoard());
            model.addAttribute("board", board);
            model.addAttribute("postMetadata", postMetadata);
            model.addAttribute("alarmCount", 1);
            model.addAttribute("next", postMetadata.size() == pageable.getPageSize() ? true : false);
            model.addAttribute("page", pageable.getPageNumber());
            model.addAttribute("size", 10);
            return BOARD_PAGE;
        } catch (BoardException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_MAIN_PAGE;
        }
    }
}
