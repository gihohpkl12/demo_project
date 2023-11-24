package movie.web.demo.service.post;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import movie.web.demo.domain.account.Account;
import movie.web.demo.domain.board.Board;
import movie.web.demo.domain.post.Post;
import movie.web.demo.domain.post.PostMetaData;
import movie.web.demo.domain.review.PostReview;
import movie.web.demo.exception.AccountException;
import movie.web.demo.exception.PostException;
import movie.web.demo.form.PostEditForm;
import movie.web.demo.form.PostDeleteForm;
import movie.web.demo.form.PostEnrollForm;
import movie.web.demo.repository.PostMetaDataRepository;
import movie.web.demo.repository.PostRepository;
import movie.web.demo.service.account.UserAccountService;
import movie.web.demo.service.board.BoardService;
import movie.web.demo.service.image.DefaultImageService;
import movie.web.demo.service.image.ImageService;
import movie.web.demo.validator.custom.PostValidator;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultPostService implements PostService {

    private final PostRepository postRepository;

    private final PostMetaDataRepository postMetaDataRepository;

    private final PostValidator postValidator = new PostValidator();

    public boolean userCheck(Long accountId) {
        return UserAccountService.isSameUserCheck(accountId);
    }

    @Transactional
    public void updatePost(PostEditForm postEditForm) {
        Optional<PostMetaData> postMetaData = postMetaDataRepository.findById(postEditForm.getPostMetaDataId());
        if (postValidator.updateCheck(postMetaData, postEditForm.getAccountId())) {
            Post post = postMetaData.get().getPost();
            postMetaData.get().setSubject(postEditForm.getSubject());
            postMetaData.get().setCreateDate(LocalDateTime.now());
            post.setPostContent(postEditForm.getPostContent());
        }
    }

    @Transactional
    public void deletePost(PostDeleteForm postDeleteForm, ImageService defaultImageService, ImageService s3ImageService) {
        Optional<PostMetaData> postMetaData = postMetaDataRepository.findById(postDeleteForm.getPostMetaDataId());
        if (postValidator.deleteCheck(postMetaData, postDeleteForm.getAccountId())) {
            postMetaDataRepository.delete(postMetaData.get());
            postRepository.delete(postMetaData.get().getPost());

            if (defaultImageService.isContainImage(postMetaData.get().getPost().getPostContent())) {
                defaultImageService.deleteImage(postMetaData.get().getPost().getPostContent());
                s3ImageService.deleteImage(postMetaData.get().getPost().getPostContent());
            }
        }
    }

    @Override
    public Post findPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (!post.isPresent()) {
            return null;
        }
        return post.get();
    }

    @Override
    public Post findPostByMetaDataWithReviews(PostMetaData postMetaData) {
        Optional<Post> post = postRepository.findPostByIdWithAllReviews(postMetaData);
        if (!post.isPresent()) {
            return null;
        }

        return post.get();
    }

    @Transactional
    public PostMetaData fidnPostMetaDataById(Long id) {
        Optional<PostMetaData> postMetaData = postMetaDataRepository.findById(id);
        postMetaData.get().setViewCount(postMetaData.get().getViewCount()+1);
        return postMetaData.get();
    }

    @Override
    public List<PostMetaData> findPostMetaDataByBoard(Board board, Pageable pageable) {
        return postMetaDataRepository.findPostMetaDataListByBoard(board, pageable);
    }

    @Override
    public void addPost(PostEnrollForm postEnrollForm, BoardService boardService, ImageService imageService) {
        if(postValidator.addCheck(postEnrollForm.getAccountId())) {
            Post post = createPost(postEnrollForm);
            PostMetaData postMetaData = createPostMetaData(postEnrollForm, boardService.getBoard(postEnrollForm.getBoardId()));

            postMetaData.setPost(post);
            post.setPostMetaData(postMetaData);

            postMetaDataRepository.save(postMetaData);
            postRepository.save(post);

            if (imageService.isContainImage(postEnrollForm.getPostContent())) {
                imageService.imageUpload(postEnrollForm.getPostContent());
            }
        }
//        if (!UserAccountService.isSameUserCheck(postEnrollForm.getAccountId())) {
//            throw new AccountException("로그인 후에 해주시기 바랍니다");
//        }
    }

    private Post createPost(PostEnrollForm postEnrollForm) {
        Post post = new Post();
        post.setPostContent(postEnrollForm.getPostContent());

        return post;
    }

    private PostMetaData createPostMetaData(PostEnrollForm postEnrollForm, Board board) {
        PostMetaData postMetaData = new PostMetaData();
        postMetaData.setCreateDate(LocalDateTime.now());
        postMetaData.setSubject(postEnrollForm.getSubject());
        postMetaData.setPoster(postEnrollForm.getPoster());
        postMetaData.setViewCount(0l);
        postMetaData.setBoard(board);
        postMetaData.setAccountId(postEnrollForm.getAccountId());

        return postMetaData;
    }

    /**
     * 회원이 탈퇴했을 때, 해당 회원이 작성한 모든 Post를 삭제
     * @param nickname
     * @param id
     */
    @Transactional
    public void deleteAllPostOfUser(String nickname, Long id) {
        List<PostMetaData> postMetaDataList = postMetaDataRepository.findAllPostMetaDataOfUser(id);

        for (PostMetaData postMetaData : postMetaDataList) {
            Post post = postMetaData.getPost();
            postRepository.delete(post);
            postMetaDataRepository.delete(postMetaData);
        }
    }

    /**
     * 게시판이 삭제될 때, 해당 게시판에 모든 게시글이 삭제.
     * @param board
     */
    @Transactional
    public void deleteAllPostOfBoard(Board board) {
        List<PostMetaData> postMetaDataList = postMetaDataRepository.findPostMetaDataListByBoard(board);
        for (PostMetaData postMetaData : postMetaDataList) {
            postRepository.delete(postMetaData.getPost());
            postMetaDataRepository.delete(postMetaData);
        }
    }

    /**
     * 회원이 닉네임을 변경했을 때, 해당 회원의 모든 게시글에 작성자 변경
     * @param nickname
     * @param id
     */
    @Transactional
    @Override
    public void updateAllPostOfUser(String nickname, Long id) {
        List<PostMetaData> postMetaDataList = postMetaDataRepository.findAllPostMetaDataOfUser(id);
        for (PostMetaData postMetaData : postMetaDataList) {
            postMetaData.setPoster(nickname);
        }
    }
}
