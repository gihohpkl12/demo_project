package movie.web.demo.repository;

import movie.web.demo.domain.review.MovieReview;
import movie.web.demo.domain.review.PostReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovieReviewRepository extends JpaRepository<MovieReview, Long> {

    @Query("select r from MovieReview r where r.docid = :docid and r.movieSeq = :movieSeq and r.title = :title")
    List<MovieReview> findReview(@Param("docid") String docid, @Param("movieSeq") String movieSeq, @Param("title") String title);

    Optional<MovieReview> findById(Long id);

    @Query("select m from MovieReview m where m.accountId = :id")
    List<MovieReview> findAllReviewOfUser(@Param("id") Long id);
}
