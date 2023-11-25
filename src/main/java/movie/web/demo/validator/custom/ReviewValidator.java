package movie.web.demo.validator.custom;

import movie.web.demo.domain.post.Post;
import movie.web.demo.exception.*;
import movie.web.demo.service.account.UserAccountService;

import java.util.Optional;

public class ReviewValidator extends DefaultValidator {
    public boolean deleteCheck(Optional<?> t, Long id) {
        if (!super.deleteCheck(t)) {
            throw new ReviewException("해당 리뷰가 존재하지 않습니다");
        }
        userCheck(id);
        return true;

    }

    public boolean addCheck(Post post, Long id, int length) {
        userCheck(id);
        if (post == null) {
            throw new PostException("해당 게시글이 존재하지 않습니다");
        }

        if (length > 200) {
            throw new ReviewException("댓글 길이가 200자를 초과합니다");
        }

        return true;
    }

    public boolean addCheck(Long id, int length) {
        if (length > 200) {
            throw new ReviewException("댓글 길이가 200자를 초과합니다");
        }

        return userCheck(id);
    }

    private boolean userCheck(Long id) {
        if (!UserAccountService.isSameUserCheck(id)) {
            throw new AccountException("리뷰 작성자 정보와 현재 계정 정보가 일치하지 않습니다");
        }
        return true;
    }
}
