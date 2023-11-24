package movie.web.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import movie.web.demo.form.KMDBMovieForm;
import movie.web.demo.form.MovieDetailForm;
import movie.web.demo.form.MovieReviewDeleteForm;
import movie.web.demo.form.MovieReviewEnrollForm;
import movie.web.demo.service.board.BoardService;
import movie.web.demo.service.movie.MovieFindService;
import movie.web.demo.service.movie.manager.RecentMovieManager;
import movie.web.demo.service.review.ReviewService;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final String MOVIE_LIST_PAGE = "list";
    private final String REDIRECT_MAIN = "redirect:/";

    private final RecentMovieManager recentMovieManager;

    private final MovieFindService kMDBMovieFindService;

    private final ReviewService movieReviewService;

    private final BoardService defaultBoardService;

    /**
     * =========================================================================================================
     * 최신 영화
     */
    @GetMapping("recent-movie")
    public String recentMoviePage(Model model) {
        model.addAttribute("movies", recentMovieManager.getRecentMovie());
        model.addAttribute("title", "Recent Movie");
        model.addAttribute("boards", defaultBoardService.getAllBoard());

        return MOVIE_LIST_PAGE;
    }

    /**
     * =========================================================================================================
     * 영화 상세 페이지
     */
    @GetMapping("/details")
    public String movieDetails(@Validated MovieDetailForm movieDetailForm, BindingResult bindingResult, Model model, RedirectAttributes redirectModel) throws UnsupportedEncodingException {
        KMDBMovieForm kmdbMovieForm = kMDBMovieFindService.findMovie(movieDetailForm);
        model.addAttribute("movie", kmdbMovieForm);
        model.addAttribute("reviewEnrollForm", new MovieReviewEnrollForm());
        model.addAttribute("reviewDeleteForm", new MovieReviewDeleteForm());
        model.addAttribute("reviews", movieReviewService.findReviews(movieDetailForm));
        model.addAttribute("boards", defaultBoardService.getAllBoard());
        return "detail";
    }

    /**
     * =========================================================================================================
     * 영화 검색
     */
    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (keyword == null || keyword.length() == 0) {
            return getPreUrl(request, response);
        }
        List<KMDBMovieForm> result = kMDBMovieFindService.findMovie(keyword);
        model.addAttribute("movies", result);
        model.addAttribute("title", "Search Result: "+keyword);
        model.addAttribute("boards", defaultBoardService.getAllBoard());

        return MOVIE_LIST_PAGE;
    }

    private String getPreUrl(HttpServletRequest request, HttpServletResponse response) {
        String url = REDIRECT_MAIN;

        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            url = "redirect:"+savedRequest.getRedirectUrl();
        }

        return url;
    }
}
