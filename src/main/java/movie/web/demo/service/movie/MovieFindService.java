package movie.web.demo.service.movie;

import com.fasterxml.jackson.core.JsonProcessingException;
import movie.web.demo.form.KMDBMovieForm;
import movie.web.demo.form.MovieDetailForm;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface MovieFindService {

    List<KMDBMovieForm> findByTitle(String title) throws UnsupportedEncodingException;

    List<KMDBMovieForm> getRecentMovie() throws UnsupportedEncodingException;

    KMDBMovieForm findMovie(MovieDetailForm movieDetailForm) throws UnsupportedEncodingException;

    List<KMDBMovieForm> findMovie(String keyword);

}
