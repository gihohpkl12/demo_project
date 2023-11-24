package movie.web.demo.controller.admin;

import lombok.RequiredArgsConstructor;
import movie.web.demo.domain.board.Board;
import movie.web.demo.service.board.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminMainController {

    private final BoardService boardService;

    private final String ADMIN_MAIN_PAGE = "/admin/admin-main";

    /**
     * 관리자 메인 페이지
     */
    @GetMapping
    public String adminPage(Model model, @RequestParam(value = "message", required = false) String message) {
        if (message != null) {
            model.addAttribute("message", message);
        }

        model.addAttribute("alarmCount", "0");
        List<Board> boards = boardService.getAllBoard();
        model.addAttribute("boards", boards);

        return ADMIN_MAIN_PAGE;
    }

}
