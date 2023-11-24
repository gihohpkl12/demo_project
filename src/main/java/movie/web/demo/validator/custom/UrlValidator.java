package movie.web.demo.validator.custom;

import movie.web.demo.domain.url.Url;
import movie.web.demo.exception.UrlException;

import java.util.Optional;

public class UrlValidator extends DefaultValidator {

    public boolean updateCheck(Optional<?> t, Optional<?> t2) {
        if (!updateCheck(t)) {
            throw new UrlException("존재하지 않는 url입니다");
        }

        if (isExist(t2)) {
            Url preUrl = (Url) t.get();
            Url newUrl = (Url) t2.get();

            if (!preUrl.getPath().equals(newUrl.getPath())) {
                System.out.println("here this message ");
                throw new UrlException("이미 존재하는 url입니다");
            }
        }
        return true;
    }
}