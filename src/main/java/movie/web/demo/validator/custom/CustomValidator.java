package movie.web.demo.validator.custom;

import java.util.Optional;

public interface CustomValidator {

    public boolean updateCheck(Optional<? extends Object> t);

    public boolean deleteCheck(Optional<? extends Object> t);

    public boolean addCheck(Optional<? extends Object> t);
}
