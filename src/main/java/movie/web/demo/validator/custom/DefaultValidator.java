package movie.web.demo.validator.custom;

import movie.web.demo.domain.url.Url;
import movie.web.demo.exception.RoleException;

import java.util.Optional;

public class DefaultValidator implements CustomValidator {
    public boolean isExist(Optional<?> t) {
        return t.isPresent();
    }

    @Override
    public boolean updateCheck(Optional<?> t) {
        if (isExist(t)) {
            return true;
        }
        return false;
//        throw new RoleException("이미 존재하는 Role입니다");
    }

    @Override
    public boolean deleteCheck(Optional<?> t) {
        if (isExist(t)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean addCheck(Optional<?> t) {
        if (isExist(t)) {
//            throw new RoleException("이미 존재하는 Role입니다");
            return false;
        }
        return true;
    }
}
