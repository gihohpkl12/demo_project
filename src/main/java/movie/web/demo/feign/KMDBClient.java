package movie.web.demo.feign;

import feign.Headers;
import movie.web.demo.form.KMDBMovieForm;
import movie.web.demo.form.MovieDetailForm;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

public interface KMDBClient {

//    @RequestMapping(method = RequestMethod.GET,  consumes = APPLICATION_FORM_URLENCODED_VALUE)
//    @Headers("Content-Type: application/json")
//    KMDBMovieForm findMovieByTitle();

    @RequestMapping(method = RequestMethod.GET,  consumes = APPLICATION_FORM_URLENCODED_VALUE)
    @Headers("Content-Type: application/json")
    String findMovieByTitle();

    @RequestMapping(method = RequestMethod.GET,  consumes = APPLICATION_FORM_URLENCODED_VALUE)
    @Headers("Content-Type: application/json")
    String findMovieByKeyword();

    @RequestMapping(method = RequestMethod.GET,  consumes = APPLICATION_FORM_URLENCODED_VALUE)
    @Headers("Content-Type: application/json")
    String findRecentMovie();

    @RequestMapping(method = RequestMethod.GET,  consumes = APPLICATION_FORM_URLENCODED_VALUE)
    @Headers("Content-Type: application/json")
    String findMovie(MovieDetailForm movieDetailForm);



}
