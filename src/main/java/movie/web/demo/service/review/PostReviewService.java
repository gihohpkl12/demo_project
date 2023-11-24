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
        if (reviewValidator.addCheck(post, postReviewEnrollForm.getId())) {
            PostReview postReview = createPostReview(postReviewEnrollForm);
            postReview.setPost(post);
            postReviewRepository.save(postReview);
        }


    }

    private boolean validateBeforeEnrollReview(PostReviewEnrollForm postReviewEnrollForm) {
        if (!UserAccountService.isSameUserCheck(postReviewEnrollForm.getAccountId())) {
            return false;
        }
        return true;
    }

    @Override
    public void deleteReview(PostReviewDeleteForm postReviewDeleteForm) {
        Optional<PostReview> postReview = postReviewRepository.findById(postReviewDeleteForm.getId());
        if (reviewValidator.deleteCheck(postReview)) {
            postReviewRepository.delete(postReview.get());
        }
    }

    private PostReview validateBeforeDeleteReview(PostReviewDeleteForm postReviewDeleteForm) {
        Optional<PostReview> postReview = postReviewRepository.findById(postReviewDeleteForm.getId());
        if (!reviewValidator.deleteCheck(postReview)) {

        }
        if (!UserAccountService.isSameUserCheck(postReviewDeleteForm.getAccountId())) {
            throw new AccountException("리뷰 작성자 정보와 현재 계정 정보가 일치하지 않습니다");
        }


        if (!postReview.isPresent()) {
            throw new PostReviewException("해당 리뷰가 존재하지 않습니다");
        }

        if (!UserAccountService.isSameUserCheck(postReviewDeleteForm.getAccountId())) {
            throw new PostReviewException("리뷰 작성자만 삭제할 수 있습니다");
        }

        return postReview.get();
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
