package movie.web.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import movie.web.demo.util.BindingResultUtil;
import movie.web.demo.exception.AccountException;
import movie.web.demo.exception.ReviewException;
import movie.web.demo.exception.PostReviewException;
import movie.web.demo.form.MovieReviewDeleteForm;
import movie.web.demo.form.MovieReviewEnrollForm;
import movie.web.demo.form.PostReviewDeleteForm;
import movie.web.demo.form.PostReviewEnrollForm;
import movie.web.demo.service.post.PostService;
import movie.web.demo.service.review.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final String REDIRECT_POST_PAGE = "redirect:/post";
    private final String REDIRECT_DETAIL_PAGE = "redirect:/details";

    private final ReviewService postReviewService;

    private final ReviewService movieReviewService;

    private final PostService defaultPostService;

    /**
     * =========================================================================================================
     * 게시글 리뷰 작성
     */
    @PostMapping("/post-review-enroll")
    public String enrollPostReview(@Validated PostReviewEnrollForm postReviewEnrollForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", BindingResultUtil.getError(bindingResult));
            return REDIRECT_POST_PAGE + createQueryAfterPostReviewEvent(postReviewEnrollForm.getBoardName(), postReviewEnrollForm.getBoardId(), postReviewEnrollForm.getPostMetaDataId()) + makePaginationQuery(postReviewEnrollForm.getPage(), postReviewEnrollForm.getSize());
        }

        try {
            postReviewService.addReview(postReviewEnrollForm, defaultPostService);
            redirectAttributes.addFlashAttribute("message", "리뷰가 등록됐습니다");
            return REDIRECT_POST_PAGE + createQueryAfterPostReviewEvent(postReviewEnrollForm.getBoardName(), postReviewEnrollForm.getBoardId(), postReviewEnrollForm.getPostMetaDataId())+makePaginationQuery(postReviewEnrollForm.getPage(), postReviewEnrollForm.getSize()) + makePaginationQuery(postReviewEnrollForm.getPage(), postReviewEnrollForm.getSize());
        } catch (AccountException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_POST_PAGE + createQueryAfterPostReviewEvent(postReviewEnrollForm.getBoardName(), postReviewEnrollForm.getBoardId(), postReviewEnrollForm.getPostMetaDataId()) + makePaginationQuery(postReviewEnrollForm.getPage(), postReviewEnrollForm.getSize());
        } catch (PostReviewException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_POST_PAGE + createQueryAfterPostReviewEvent(postReviewEnrollForm.getBoardName(), postReviewEnrollForm.getBoardId(), postReviewEnrollForm.getPostMetaDataId());
        }
    }

    /**
     * =========================================================================================================
     * 게시글 리뷰 삭제
     */
    @PostMapping("/post-review-delete")
    private String deletePostReview(@Validated PostReviewDeleteForm postReviewDeleteForm, BindingResult bindingResult ,RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", BindingResultUtil.getError(bindingResult));
            return REDIRECT_POST_PAGE + createQueryAfterPostReviewEvent(postReviewDeleteForm.getBoardName(), postReviewDeleteForm.getBoardId(), postReviewDeleteForm.getPostMetaDataId()) + makePaginationQuery(postReviewDeleteForm.getPage(), postReviewDeleteForm.getSize());
        }

        try {
            postReviewService.deleteReview(postReviewDeleteForm);
            redirectAttributes.addFlashAttribute("message", "리뷰가 삭제되었습니다");
            return REDIRECT_POST_PAGE + createQueryAfterPostReviewEvent(postReviewDeleteForm.getBoardName(), postReviewDeleteForm.getBoardId(), postReviewDeleteForm.getPostMetaDataId()) + makePaginationQuery(postReviewDeleteForm.getPage(), postReviewDeleteForm.getSize());
        } catch (AccountException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_POST_PAGE + createQueryAfterPostReviewEvent(postReviewDeleteForm.getBoardName(), postReviewDeleteForm.getBoardId(), postReviewDeleteForm.getPostMetaDataId()) + makePaginationQuery(postReviewDeleteForm.getPage(), postReviewDeleteForm.getSize());
        } catch (PostReviewException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_POST_PAGE + createQueryAfterPostReviewEvent(postReviewDeleteForm.getBoardName(), postReviewDeleteForm.getBoardId(), postReviewDeleteForm.getPostMetaDataId()) + makePaginationQuery(postReviewDeleteForm.getPage(), postReviewDeleteForm.getSize());
        }
    }

    /**
     * =========================================================================================================
     * 영화 리뷰 작성
     */
    @PostMapping("/movie-review-enroll")
    public String enrollReview(@Validated MovieReviewEnrollForm movieReviewEnrollForm, BindingResult bindingResult, HttpServletRequest request, RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", BindingResultUtil.getError(bindingResult));
            return REDIRECT_DETAIL_PAGE+createQueryAfterPostReviewEvent(movieReviewEnrollForm.getTitle(), movieReviewEnrollForm.getDocid(), movieReviewEnrollForm.getMovieSeq());
        }

        try {
            movieReviewService.addReview(movieReviewEnrollForm);
            redirectAttributes.addFlashAttribute("message", "리뷰가 등록됐습니다");
            return REDIRECT_DETAIL_PAGE+createQueryAfterPostReviewEvent(movieReviewEnrollForm.getTitle(), movieReviewEnrollForm.getDocid(), movieReviewEnrollForm.getMovieSeq());
        } catch (AccountException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_DETAIL_PAGE+createQueryAfterPostReviewEvent(movieReviewEnrollForm.getTitle(), movieReviewEnrollForm.getDocid(), movieReviewEnrollForm.getMovieSeq());
        } catch (ReviewException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_DETAIL_PAGE+createQueryAfterPostReviewEvent(movieReviewEnrollForm.getTitle(), movieReviewEnrollForm.getDocid(), movieReviewEnrollForm.getMovieSeq());
        }
    }

    /**
     * =========================================================================================================
     * 영화 리뷰 삭제
     */
    @PostMapping("movie-review-delete")
    public String deleteReview(@Validated MovieReviewDeleteForm movieReviewDeleteForm, BindingResult bindingResult, HttpServletRequest request, RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", BindingResultUtil.getError(bindingResult));
            return REDIRECT_DETAIL_PAGE+createQueryAfterPostReviewEvent(movieReviewDeleteForm.getTitle(), movieReviewDeleteForm.getDocid(), movieReviewDeleteForm.getMovieSeq());
        }

        try {
            movieReviewService.deleteReview(movieReviewDeleteForm);
            redirectAttributes.addFlashAttribute("message", "삭제되었습니다");
            return REDIRECT_DETAIL_PAGE+createQueryAfterPostReviewEvent(movieReviewDeleteForm.getTitle(), movieReviewDeleteForm.getDocid(), movieReviewDeleteForm.getMovieSeq());
        } catch (AccountException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_DETAIL_PAGE+createQueryAfterPostReviewEvent(movieReviewDeleteForm.getTitle(), movieReviewDeleteForm.getDocid(), movieReviewDeleteForm.getMovieSeq());
        } catch (ReviewException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_DETAIL_PAGE+createQueryAfterPostReviewEvent(movieReviewDeleteForm.getTitle(), movieReviewDeleteForm.getDocid(), movieReviewDeleteForm.getMovieSeq());
        }
    }

    /**
     * =========================================================================================================
     * 영화, 게시글 작성 이후 redirect 쿼리 파라미터 생성
     */
    private String createQueryAfterPostReviewEvent(String boardName, Long boardId, Long postMetaDataId) {
        StringBuilder sb = new StringBuilder();
        sb.append("?id=")
                .append(postMetaDataId.toString())
                .append("&boardName=")
                .append(boardName)
                .append("&boardId=")
                .append(boardId.toString());

        return sb.toString();
    }

    private String createQueryAfterPostReviewEvent(String title, String docid, String movieSeq) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append("?title=")
                .append(URLEncoder.encode(title, "UTF-8"))
                .append("&docid=")
                .append(docid)
                .append("&movieSeq=")
                .append(movieSeq);

        return sb.toString();
    }

    /**
     * =========================================================================================================
     * pagination 쿼리 생성
     */
    private String makePaginationQuery(int page, int size) {
        StringBuilder sb = new StringBuilder();
        sb.append("&size=")
                .append(Integer.toString(size))
                .append("&page=")
                .append(Integer.toString(page))
                .append("&sort=createDate,desc");
        return sb.toString();
    }
}
