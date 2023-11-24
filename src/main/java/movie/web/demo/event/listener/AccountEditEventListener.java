package movie.web.demo.event.listener;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import movie.web.demo.event.AccountDeleteEvent;
import movie.web.demo.event.AccountNicknameUpdateEvent;
import movie.web.demo.service.post.PostService;
import movie.web.demo.service.review.ReviewService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 회원 탈퇴, 수정 이벤트 발생 시 처리해야 하는 로직 수행.
 * (댓글, 게시글 삭제 혹은 작성자 변경)
 */
@Component
@RequiredArgsConstructor
public class AccountEditEventListener implements AccountEventListener {

    private final ReviewService movieReviewService;

    private final ReviewService postReviewService;

    private final PostService defaultPostService;

    @Override
    @EventListener
    @Transactional
    public void listen(AccountDeleteEvent accountDeleteEvent) {
        movieReviewService.deleteAllReviewOfUser(accountDeleteEvent.getNickname(), accountDeleteEvent.getId());
        postReviewService.deleteAllReviewOfUser(accountDeleteEvent.getNickname(), accountDeleteEvent.getId());
        defaultPostService.deleteAllPostOfUser(accountDeleteEvent.getNickname(), accountDeleteEvent.getId());
    }
    @Override
    @EventListener
    @Transactional
    public void listen(AccountNicknameUpdateEvent accountNicknameUpdateEvent) {
        movieReviewService.updateAllReviewOfUser(accountNicknameUpdateEvent.getNickname(), accountNicknameUpdateEvent.getId());
        postReviewService.updateAllReviewOfUser(accountNicknameUpdateEvent.getNickname(), accountNicknameUpdateEvent.getId());
        defaultPostService.updateAllPostOfUser(accountNicknameUpdateEvent.getNickname(), accountNicknameUpdateEvent.getId());
    }
}
