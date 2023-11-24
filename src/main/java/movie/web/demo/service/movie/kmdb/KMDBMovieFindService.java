package movie.web.demo.service.movie.kmdb;

import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import lombok.RequiredArgsConstructor;
import movie.web.demo.form.KMDBMovieForm;
import movie.web.demo.form.MovieDetailForm;
import movie.web.demo.service.movie.kmdb.url_maker.MovieUrlMaker;
import movie.web.demo.feign.KMDBClient;
import movie.web.demo.service.movie.MovieFindService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * KMDB API 영화 검색
 */
@Service
@RequiredArgsConstructor
public class KMDBMovieFindService implements MovieFindService {

    private final Encoder encoder;

    private final Decoder secondDecoder;

    private final MovieUrlMaker movieUrlMaker;
    private KMDBClient getOAuthClient(String url) throws UnsupportedEncodingException {
        KMDBClient kmdbClient = Feign.builder()
                .contract(new SpringMvcContract())
                .encoder(encoder)
                .decoder(secondDecoder)
                .target(KMDBClient.class, url);
        return kmdbClient;
    }

    @Override
    public List<KMDBMovieForm> getRecentMovie() throws UnsupportedEncodingException {
        String url = movieUrlMaker.getRecentUrlForFindingMovie();
        String movies = getOAuthClient(url).findRecentMovie();

        List<KMDBMovieForm> result = removePosterNotExistMovie(convertData(movies));
        return result;
    }

    /**
     * 포스터가 없는 영화는 제거.
     * @param movieList
     * @return
     */
    private List<KMDBMovieForm> removePosterNotExistMovie(List<KMDBMovieForm> movieList) {
        List<KMDBMovieForm> result = new ArrayList<>();
        for (KMDBMovieForm kmdbMovieForm : movieList) {
            if (kmdbMovieForm.getPosters().size() == 1) {
                if (kmdbMovieForm.getPosters().get(0).equals("img/NoData.PNG")) {
                    continue;
                }
            }
            result.add(kmdbMovieForm);
            if (result.size() >= 52) {
                break;
            }
        }
        return result;
    }

    /**
     * 영화 상세 보기
     * @param movieDetailForm
     * @return
     * @throws UnsupportedEncodingException
     */
    @Override
    public KMDBMovieForm findMovie(MovieDetailForm movieDetailForm) throws UnsupportedEncodingException {
        String movies = getOAuthClient(movieUrlMaker.getDefaultUrl()).findMovie(movieDetailForm);
        return convertData(movies).get(0);
    }


    /**
     * 영화 검색.
     * 감독, 배우, 제목으로 검색
     * @param keyword
     * @return
     */
    @Override
    public List<KMDBMovieForm> findMovie(String keyword)  {
        List<KMDBMovieForm> result = new ArrayList<>();

        try {
            KMDBClient directorKmdbClient  = getOAuthClient(movieUrlMaker.getUrlForFindingMovieByDirector(keyword));
            KMDBClient actorKmdbClient  = getOAuthClient(movieUrlMaker.getUrlForFindingMovieByActor(keyword));
            KMDBClient titleKmdbClient  = getOAuthClient(movieUrlMaker.getUrlForFindingMovieByTitle(keyword));

            String directData = directorKmdbClient.findMovieByKeyword();
            String actorData = actorKmdbClient.findMovieByKeyword();
            String titleData = titleKmdbClient.findMovieByTitle();

            result = convertData(titleData);
            integrateList(result, convertData(actorData));
            integrateList(result, convertData(directData));

            return result;
        } catch (Exception e) {
            return result;
        }
    }

    private List<KMDBMovieForm> integrateList(List<KMDBMovieForm> result, List<KMDBMovieForm> addList) {
        for (KMDBMovieForm kmdbMovieForm : addList) {
            result.add(kmdbMovieForm);
        }
        return result;
    }

    @Override
    public List<KMDBMovieForm> findByTitle(String title) throws UnsupportedEncodingException {
        KMDBClient kmdbClient  = getOAuthClient(movieUrlMaker.getUrlForFindingMovieByTitle(title));
        String query = kmdbClient.findMovieByTitle();
        List<KMDBMovieForm> result = convertData(query);
        return result;
    }

    /**
     * 영화 정보 추출
     * @param movies
     * @return
     */
    private List<KMDBMovieForm> convertData(String movies) {
        ArrayList<KMDBMovieForm> results = new ArrayList<>();
        JSONObject totalData = JSONObject.fromObject(movies);
        if (totalData.getInt("TotalCount") == 0) {
            return results;
        }

        JSONArray data = totalData.getJSONArray("Data");
        JSONArray resultData = data.getJSONObject(0).getJSONArray("Result");

        for (int i = 0; i < resultData.size(); i++) {
            KMDBMovieForm movie = new KMDBMovieForm();
            JSONObject getData = resultData.getJSONObject(i);

            movie.setDocid(getData.getString("DOCID"));
            if (movie.getDocid() == null || movie.getDocid().equals("")) {
                movie.setDocid("-");
            }

            movie.setMovieSeq(getData.getString("movieSeq"));
            if (movie.getMovieSeq() == null || movie.getMovieSeq().equals("")) {
                movie.setMovieSeq("-");
            }

            movie.setProdYear(getData.getString("prodYear"));
            if (movie.getProdYear() == null || movie.getProdYear().equals("")) {
                movie.setProdYear("-");
            }

            movie.setTitleEng(getData.getString("titleEng"));
            if (movie.getTitleEng() == null || movie.getTitleEng().equals("")) {
                movie.setTitleEng("-");
            }

            movie.setNation(getData.getString("nation"));
            if (movie.getNation() == null || movie.getNation().equals("")) {
                movie.setNation("-");
            }

            movie.setCompany(getData.getString("company"));
            if (movie.getCompany() == null || movie.getCompany().equals("")) {
                movie.setCompany("-");
            }

            movie.setPlot(getData.getString("plot"));
            if (movie.getPlot() == null || movie.getPlot().equals("")) {
                movie.setPlot("-");
            }

            movie.setRuntime(getData.getString("runtime"));
            if (movie.getRuntime() == null || movie.getRuntime().equals("")) {
                movie.setRuntime("-");
            }

            movie.setGenre(getData.getString("genre").replace(",", ", "));
            if (movie.getGenre() == null || movie.getGenre().equals("")) {
                movie.setGenre("-");
            }

            // 상관 없는 문자열 제거.
            String title = getData.getString("title");
            title = title.replaceAll(" !HS ", "");
            title = title.replaceAll(" !HS", "");
            title = title.replaceAll("!HS ", "");
            title = title.replaceAll(" !HE ", "");
            title = title.replaceAll(" !HE", "");
            title = title.replaceAll("!HE", "");

            if (title.charAt(0) == ' ') {
                title = title.substring(1, title.length());
            }
            if (title.charAt(title.length() - 1) == ' ') {
                title = title.substring(0, title.length() - 1);
            }
            movie.setTitle(title);
            if (movie.getTitle() == null || movie.getTitle().equals("")) {
                movie.setTitle("-");
            }

            ArrayList<String> directors = new ArrayList<>();
            JSONArray directorData = getData.getJSONArray("director");
            for (int j = 0; j < directorData.size(); j++) {
                directors.add(directorData.getJSONObject(j).getString("directorNm"));
            }
            if (directors.size() == 0) {
                directors.add("-");
            }
            movie.setDirectors(directors);

            ArrayList<String> actors = new ArrayList<>();
            JSONArray actorData = getData.getJSONArray("actor");
            for (int j = 0; j < actorData.size(); j++) {
                actors.add(actorData.getJSONObject(j).getString("actorNm"));
            }
            if (actors.size() == 0) {
                actors.add("-");
            }
            movie.setActors(actors);

            JSONArray ratingData = getData.getJSONArray("rating").getJSONArray(1);
            movie.setRatingGrade(ratingData.getJSONObject(0).getString("ratingGrade"));
            movie.setRatingDate(ratingData.getJSONObject(0).getString("ratingDate"));

            if (movie.getRatingDate() == null || movie.getRatingDate().equals("")) {
                movie.setRatingDate("-");
            }

            if (movie.getRatingGrade() == null || movie.getRatingGrade().equals("")) {
                movie.setRatingGrade("-");
            }

            String[] temp = getData.getString("posters").split("\\|");
            ArrayList<String> posters = new ArrayList<>();
            for (int j = 0; j < temp.length; j++) {
                posters.add(temp[j]);
            }

            if (posters.size() == 0) {
                posters.add("img/NoData.PNG");
            } else if (posters.size() == 1 && posters.get(0).equals("")) {
                posters.remove(0);
                posters.add("img/NoData.PNG");
            }
            movie.setPosters(posters);

            results.add(movie);
        }

        return results;
    }
}

