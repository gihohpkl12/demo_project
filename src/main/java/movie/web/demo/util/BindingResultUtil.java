package movie.web.demo.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

/**
 * BindingResult에서 에러 메시지 추출.
 */
public class BindingResultUtil {

    public static String getError(BindingResult bindingResult) {
        StringBuilder sb = new StringBuilder();

        for (ObjectError error : bindingResult.getAllErrors()) {
            sb.append(error.getDefaultMessage());
            sb.append("\n");
        }
        return sb.toString();
    }
}
