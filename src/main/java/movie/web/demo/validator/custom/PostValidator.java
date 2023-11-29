package movie.web.demo.validator.custom;

import movie.web.demo.domain.post.Post;
import movie.web.demo.domain.post.PostMetaData;
import movie.web.demo.exception.AccountException;
import movie.web.demo.exception.PostException;
import movie.web.demo.service.account.UserAccountService;

import java.util.Optional;

public class PostValidator extends DefaultValidator {

    public boolean updateCheck(Optional<?> t, Long id) {
        if (!updateCheck(t)) {
            throw new PostException("해당 게시글이 없습니다");
        }

        PostMetaData postMetaData = (PostMetaData) t.get();
        Post post = postMetaData.getPost();
        if (post == null) {
            throw new PostException("해당 게시글이 없습니다");
        }

        if (postMetaData.getAccountId() != id) {
            throw new AccountException("해당 게시글의 작성자만 수정/삭제가 가능합니다");
        }

        if (!userCheck(id)) {
            throw new AccountException("해당 게시글의 작성자만 수정/삭제가 가능합니다");
        }

        return true;
    }

    public boolean deleteCheck(Optional<?> t, Long id) {
        if (!updateCheck(t)) {
            throw new PostException("해당 게시글이 없습니다");
        }

        PostMetaData postMetaData = (PostMetaData) t.get();
        Post post = postMetaData.getPost();
        if (post == null) {
            throw new PostException("해당 게시글이 없습니다");
        }

        if (!postMetaData.getAccountId().equals(id)) {
            throw new AccountException("해당 게시글의 작성자만 수정/삭제가 가능합니다");
        }

        if (!userCheck(id)) {
            throw new AccountException("해당 게시글의 작성자만 수정/삭제가 가능합니다");
        }

        return true;
    }

    public boolean addCheck(Long id) {
        return userCheck(id);
    }

    private boolean userCheck(Long id) {
        return UserAccountService.isSameUserCheck(id);
    }


}
