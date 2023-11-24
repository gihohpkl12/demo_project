package movie.web.demo.service.movie.manager;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import movie.web.demo.form.KMDBMovieForm;
import movie.web.demo.service.movie.MovieFindService;
import movie.web.demo.service.movie.kmdb.KMDBMovieFindService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 최신 영화 관리.
 */
@Service
@RequiredArgsConstructor
public class RecentMovieManager {

    private List<KMDBMovieForm> recentMovie;

    private final MovieFindService kmdbMovieFindService;

    @PostConstruct
    public void init() throws UnsupportedEncodingException {
        setRecentMovie();
    }

    /**
     * 최신 영화는 매일 정각 12시에 저장.
     * @throws UnsupportedEncodingException
     */
    @Scheduled(cron = "0 0 0 * * *")
    synchronized public void setRecentMovie() throws UnsupportedEncodingException {
        this.recentMovie =  kmdbMovieFindService.getRecentMovie();
    }

    public List<KMDBMovieForm> getRecentMovie() {
        return recentMovie;
    }
}
