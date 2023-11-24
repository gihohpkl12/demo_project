package movie.web.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import movie.web.demo.domain.board.Board;
import movie.web.demo.service.board.BoardService;
import movie.web.demo.service.movie.MovieFindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainPageController {
    private final String MAIN_PAGE_NAME = "main";

    private final BoardService boardService;

    /**
     * =========================================================================================================
     * 메인 페이지
     */
    @GetMapping("/")
    public String mainPage(Model model, @RequestParam(value = "message", required = false) String message) throws UnsupportedEncodingException, JsonProcessingException {
        if (message != null) {
            model.addAttribute("message", message);
        }

        model.addAttribute("alarmCount", "0");
        List<Board> boards = boardService.getAllBoard();
        model.addAttribute("boards", boards);

        return MAIN_PAGE_NAME;
    }

}
