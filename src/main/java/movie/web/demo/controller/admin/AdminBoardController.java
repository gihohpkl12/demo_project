package movie.web.demo.controller.admin;

import lombok.RequiredArgsConstructor;
import movie.web.demo.domain.board.Board;
import movie.web.demo.exception.BoardException;
import movie.web.demo.form.BoardAddForm;
import movie.web.demo.form.BoardDeleteForm;
import movie.web.demo.form.BoardUpdateForm;
import movie.web.demo.service.board.BoardService;
import movie.web.demo.service.post.PostService;
import movie.web.demo.util.BindingResultUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/board")
@RequiredArgsConstructor
public class AdminBoardController {

    private final BoardService defaultBoardService;

    private final PostService defaultPostService;

    private final String BOARD_MANAGE_MAIN_PAGE = "/admin/board-manage-main";

    private final String BOARD_UPDATE_PAGE = "/admin/board-update";

    private final String BOARD_ADD_PAGE = "/admin/board-add";

    private final String REIDRECT_BOARD_MAIN_PAGE = "redirect:/admin/board";

    /**
     * =================================================================================================
     * 게시판 관리 페이지
     */
    @GetMapping
    public String boardManagePage(Model model) {
        List<Board> boards = defaultBoardService.getAllBoard();
        model.addAttribute("boards", boards);

        return BOARD_MANAGE_MAIN_PAGE;
    }

    /**
     * =================================================================================================
     * 게시판 업데이트
     */
    @GetMapping("board-update")
    public String boardUpdatePage(Long id, Model model) {
        if (id == null) {
            model.addAttribute("close_message", "id가 null입니다");
        }

        try {
            Board board = defaultBoardService.getBoard(id);
            model.addAttribute("board", board);
            return BOARD_UPDATE_PAGE;
        } catch (BoardException e) {
            model.addAttribute("close_message", e.getMessage());
            return BOARD_UPDATE_PAGE;
        }
    }

    @PostMapping("/board-update")
    @ResponseBody
    public String updateBoard(@Validated @RequestBody BoardUpdateForm boardUpdateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return BindingResultUtil.getError(bindingResult);
        }

        try {
            defaultBoardService.updateBoard(boardUpdateForm);
            return "수정 되었습니다";
        } catch (BoardException e) {
            return e.getMessage();
        }
    }

    /**
     * =================================================================================================
     * 게시판 추가
     */

    @GetMapping("/board-add")
    public String boardAddPage() {
        return BOARD_ADD_PAGE;
    }

    @PostMapping("/board-add")
    @ResponseBody
    public String addBoard(@Validated @RequestBody BoardAddForm boardAddForm, BindingResult bindingResult) {
        System.out.println(boardAddForm.getBoardName());

        if (bindingResult.hasErrors()) {
            return BindingResultUtil.getError(bindingResult);
        }

        try {
            defaultBoardService.addBoard(boardAddForm);
            return "추가 되었습니다";
        } catch (BoardException e) {
            return e.getMessage();
        }
    }

    /**
     * =================================================================================================
     * 게시판 삭제
     */

    @PostMapping("/board-delete")
    public String deleteBoard(@Validated BoardDeleteForm boardDeleteForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", BindingResultUtil.getError(bindingResult));
            return REIDRECT_BOARD_MAIN_PAGE;
        }

        try {
            defaultBoardService.deleteBoard(boardDeleteForm, defaultPostService);
            redirectAttributes.addFlashAttribute("message", "삭제 되었습니다");
            return REIDRECT_BOARD_MAIN_PAGE;
        } catch (BoardException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REIDRECT_BOARD_MAIN_PAGE;
        }
    }
}
