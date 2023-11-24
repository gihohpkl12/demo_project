package movie.web.demo.service.review;

import movie.web.demo.form.*;
import movie.web.demo.service.post.PostService;

import java.util.List;

public interface ReviewService<T> {

    void addReview(MovieReviewEnrollForm movieReviewEnrollForm);

    void addReview(PostReviewEnrollForm postReviewEnrollForm, PostService postService);

    void deleteReview(MovieReviewDeleteForm movieReviewDeleteForm);

    void deleteReview(PostReviewDeleteForm postReviewDeleteForm);

    List<T> findReviews(MovieDetailForm movieDetailForm);

    void deleteAllReviewOfUser(String nickname, Long id);

    public void updateAllReviewOfUser(String nickname, Long id);
}
