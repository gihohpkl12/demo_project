package movie.web.demo.service.review;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import movie.web.demo.domain.review.MovieReview;
import movie.web.demo.exception.ReviewException;
import movie.web.demo.form.*;
import movie.web.demo.repository.MovieReviewRepository;
import movie.web.demo.service.account.UserAccountService;
import movie.web.demo.service.post.PostService;
import movie.web.demo.validator.custom.ReviewValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieReviewService implements ReviewService<MovieReview> {

    private final MovieReviewRepository movieReviewRepository;

    private final ReviewValidator reviewValidator = new ReviewValidator();

    @Override
    public void addReview(PostReviewEnrollForm postReviewEnrollForm, PostService postService) {

    }

    @Override
    public void deleteReview(PostReviewDeleteForm postReviewDeleteForm) {}

    @Override
    public void addReview(MovieReviewEnrollForm movieReviewEnrollForm) {
        if (reviewValidator.addCheck(movieReviewEnrollForm.getAccountId(), movieReviewEnrollForm.getReviewContent().length())) {
            System.out.println("review length !! "+movieReviewEnrollForm.getReviewContent().length());
            movieReviewRepository.save(createReview(movieReviewEnrollForm));
        }

    }

    private boolean validateBeforeEnrollReview(MovieReviewEnrollForm movieReviewEnrollForm) {
        if (!UserAccountService.isSameUserCheck(movieReviewEnrollForm.getAccountId())) {
            return false;
        }
        return true;
    }

    private MovieReview createReview(MovieReviewEnrollForm movieReviewEnrollForm) {
        MovieReview movieReview = new MovieReview();
        movieReview.setDocid(movieReviewEnrollForm.getDocid());
        movieReview.setMovieSeq(movieReviewEnrollForm.getMovieSeq());
        movieReview.setTitle(movieReviewEnrollForm.getTitle());
        movieReview.setReviewContent(movieReviewEnrollForm.getReviewContent());
        movieReview.setCreateDate(LocalDateTime.now());
        movieReview.setNickname(movieReviewEnrollForm.getNickname());
        movieReview.setAccountId(movieReviewEnrollForm.getAccountId());
        return movieReview;
    }

    @Override
    public void deleteReview(MovieReviewDeleteForm movieReviewDeleteForm) {
        Optional<MovieReview> review = movieReviewRepository.findById(movieReviewDeleteForm.getId());
        if (reviewValidator.deleteCheck(review, movieReviewDeleteForm.getAccountId())) {
            movieReviewRepository.delete(review.get());
        }
    }

    private MovieReview validateBeforeReviewDelete(MovieReviewDeleteForm movieReviewDeleteForm) {
        if (!UserAccountService.isSameUserCheck(movieReviewDeleteForm.getAccountId())) {
            throw new ReviewException("리뷰 작성자만 삭제할 수 있습니다");
        }

        Optional<MovieReview> review = movieReviewRepository.findById(movieReviewDeleteForm.getId());
        if (!review.isPresent()) {
            throw new ReviewException("존재하지 않는 리뷰입니다");
        }

        if (!review.get().getNickname().equals(movieReviewDeleteForm.getNickname())) {
            throw new ReviewException("삭제하려는 리뷰의 계정 정보가 일치하지 않습니다");
        }

        return review.get();
    }

    @Override
    public List<MovieReview> findReviews(MovieDetailForm movieDetailForm) {
        return movieReviewRepository.findReview(movieDetailForm.getDocid(), movieDetailForm.getMovieSeq(), movieDetailForm.getTitle());
    }

    @Override
    @Transactional
    public void deleteAllReviewOfUser(String nickname, Long id) {
        List<MovieReview> reviews = movieReviewRepository.findAllReviewOfUser(id);
        for (MovieReview review : reviews) {
           movieReviewRepository.delete(review);
        }
    }

    @Transactional
    @Override
    public void updateAllReviewOfUser(String nickname, Long id) {
        List<MovieReview> reviews = movieReviewRepository.findAllReviewOfUser(id);
        for (MovieReview review : reviews) {
            review.setNickname(nickname);
        }
    }
}
