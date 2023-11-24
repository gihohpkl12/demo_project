package movie.web.demo.service.movie.kmdb.url_maker;

import java.io.UnsupportedEncodingException;

public interface MovieUrlMaker {
    String getUrlForFindingMovieByTitle(String title) throws UnsupportedEncodingException;

    public String getUrlForFindingMovieByDirector(String keyword) throws UnsupportedEncodingException;

    public String getUrlForFindingMovieByActor(String actor) throws UnsupportedEncodingException;

    String getRecentUrlForFindingMovie();

    String getDefaultUrl();
}
