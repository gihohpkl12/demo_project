package movie.web.demo.service.movie.kmdb.url_maker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * KMDB API 영화 검색용 url 생성
 */
@Service
public class KMDBMovieUrlMaker implements MovieUrlMaker {

    @Value("${kmdb.api.url}")
    private String KMDB_URL;

    @Value("${kmdb.api.key}")
    private String KMDB_KEY;

    @Value("${kmdb.api.recent.key}")
    private String KMDB_RECENT_KEY;

    @Value("${kmdb.api.recent.url}")
    private String KMDB_RECENT_URL;

    @Override
    public String getUrlForFindingMovieByTitle(String title) throws UnsupportedEncodingException {
        StringBuilder sb = makingDefaultUrl();
        sb.append("&title=");
        sb.append(URLEncoder.encode(title, "UTF-8"));
        return sb.toString();
    }

    public String getUrlForFindingMovieByDirector(String director) throws UnsupportedEncodingException {
        StringBuilder sb = makingDefaultUrl();
        sb.append("&director=");
        sb.append(URLEncoder.encode(director, "UTF-8"));

        return sb.toString();
    }

    public String getUrlForFindingMovieByActor(String actor) throws UnsupportedEncodingException {
        StringBuilder sb = makingDefaultUrl();
        sb.append("&actor=");
        sb.append(URLEncoder.encode(actor, "UTF-8"));

        return sb.toString();
    }


    @Override
    public String getRecentUrlForFindingMovie() {
        StringBuilder sb = makingDefaultUrl();
        LocalDate localDate = LocalDate.now();
        String start = localDate.minusMonths(2).toString().replace("-", "");
//        String end = localDate.plusMonths(1).toString().replace("-", "");
        sb.append("&releaseDts=")
                .append(start)
                .append("&listCount=100")
                .append("&sort=prodYear");
        return sb.toString();
    }

    @Override
    public String getDefaultUrl() {
        return makingDefaultUrl().toString();
    }

    private StringBuilder makingDefaultUrl() {
        StringBuilder sb =new StringBuilder();
        sb.append(KMDB_URL);
        sb.append("Y&ServiceKey=");
        sb.append(KMDB_KEY);
        return sb;
    }

    private StringBuilder makingRecentMovieUrl() {
        StringBuilder sb =new StringBuilder();
        sb.append(KMDB_RECENT_URL);
        sb.append("Y&ServiceKey=");
        sb.append(KMDB_RECENT_KEY);
        return sb;
    }
}
