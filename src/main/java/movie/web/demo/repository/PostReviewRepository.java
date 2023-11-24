package movie.web.demo.repository;

import movie.web.demo.domain.review.PostReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostReviewRepository extends JpaRepository<PostReview, Long> {

    @Query("select p from PostReview p where p.accountId = :id")
    List<PostReview> findAllReviewOfUser(@Param("id") Long id);
}
