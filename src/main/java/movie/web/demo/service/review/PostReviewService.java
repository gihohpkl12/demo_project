package movie.web.demo.service.review;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import movie.web.demo.domain.post.Post;
import movie.web.demo.domain.review.PostReview;
import movie.web.demo.exception.AccountException;
import movie.web.demo.exception.PostReviewException;
import movie.web.demo.form.*;
import movie.web.demo.repository.PostReviewRepository;
import movie.web.demo.service.account.UserAccountService;
import movie.web.demo.service.post.PostService;
import movie.web.demo.validator.custom.ReviewValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostReviewService implements ReviewService<PostReview> {

    private final PostReviewRepository postReviewRepository;

    ReviewValidator reviewValidator = new ReviewValidator();

    private PostReview createPostReview(PostReviewEnrollForm postReviewEnrollForm) {
        PostReview postReview = new PostReview();
        postReview.setReviewContent(postReviewEnrollForm.getPostReviewContent());
        postReview.setNickname(postReviewEnrollForm.getNickname());
        postReview.setCreateDate(LocalDateTime.now());
        postReview.setBoardId(postReviewEnrollForm.getBoardId());
        postReview.setAccountId(postReviewEnrollForm.getAccountId());

        return postReview;
    }

    @Override
    public void addReview(PostReviewEnrollForm postReviewEnrollForm, PostService postService) {
        Post post = postService.findPostById(postReviewEnrollForm.getId());
        if (reviewValidator.addCheck(post, postReviewEnrollForm.getAccountId(), postReviewEnrollForm.getPostReviewContent().length())) {
            PostReview postReview = createPostReview(postReviewEnrollForm);
            postReview.setPost(post);
            postReviewRepository.save(postReview);
        }


    }

    @Override
    public void deleteReview(PostReviewDeleteForm postReviewDeleteForm) {
        Optional<PostReview> postReview = postReviewRepository.findById(postReviewDeleteForm.getId());
        if (reviewValidator.deleteCheck(postReview)) {
            postReviewRepository.delete(postReview.get());
        }
    }

    @Override
    public List<PostReview> findReviews(MovieDetailForm movieDetailForm) {
        return null;
    }

    @Override
    @Transactional
    public void deleteAllReviewOfUser(String nickname, Long id) {
        List<PostReview> reviews = postReviewRepository.findAllReviewOfUser(id);

        for (PostReview review : reviews) {
            postReviewRepository.delete(review);
        }
    }

    @Transactional
    @Override
    public void updateAllReviewOfUser(String nickname, Long id) {
        List<PostReview> reviews = postReviewRepository.findAllReviewOfUser(id);
        for (PostReview review : reviews) {
            review.setNickname(nickname);
        }
    }

    @Override
    public void addReview(MovieReviewEnrollForm movieReviewEnrollForm) {}

    @Override
    public void deleteReview(MovieReviewDeleteForm movieReviewDeleteForm) {}
}
