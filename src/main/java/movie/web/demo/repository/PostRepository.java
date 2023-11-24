package movie.web.demo.repository;

import jakarta.transaction.Transactional;
import movie.web.demo.domain.post.Post;
import movie.web.demo.domain.post.PostMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p where p.postMetaData = :postMetaData")
    Optional<Post> findPostByMetaData(@Param("postMetaData") PostMetaData postMetaData);

    @Transactional
    @Query("select p from Post p where p.id = :id")
    Optional<Post> findPostById(@Param("id") Long id);

    @Query("select p from Post p left join fetch p.reviews where p.postMetaData = :postMetaData")
    Optional<Post> findPostByIdWithAllReviews(@Param("postMetaData") PostMetaData postMetaData);
}
