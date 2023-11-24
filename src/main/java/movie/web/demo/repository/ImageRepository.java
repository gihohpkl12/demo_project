package movie.web.demo.repository;

import movie.web.demo.domain.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("select i from Image i where i.s3Url in :s3Urls")
    List<Image> findByImage(@Param("s3Urls") List<String> s3Urls);
}
