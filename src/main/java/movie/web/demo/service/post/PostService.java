package movie.web.demo.service.post;

import movie.web.demo.domain.board.Board;
import movie.web.demo.domain.post.Post;
import movie.web.demo.domain.post.PostMetaData;
import movie.web.demo.form.PostEditForm;
import movie.web.demo.form.PostDeleteForm;
import movie.web.demo.form.PostEnrollForm;
import movie.web.demo.service.board.BoardService;
import movie.web.demo.service.image.ImageService;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    public void updatePost(PostEditForm postEditForm);
    public Post findPostByMetaDataWithReviews(PostMetaData postMetaData);
    public boolean userCheck(Long accountId);
    public void deletePost(PostDeleteForm postDeleteForm, ImageService defaultImageService, ImageService s3ImageService);
    public Post findPostById(Long id);
    public PostMetaData fidnPostMetaDataById(Long id);

    List<PostMetaData> findPostMetaDataByBoard(Board board, Pageable pageable);

    public void addPost(PostEnrollForm postEnrollForm, BoardService boardService, ImageService imageService);

    public void deleteAllPostOfUser(String nickname, Long id);

    public void updateAllPostOfUser(String nickname, Long id);

    public void deleteAllPostOfBoard(Board board);
}
