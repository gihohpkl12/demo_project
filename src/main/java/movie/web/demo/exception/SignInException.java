package movie.web.demo.exception;

public class SignInException extends RuntimeException {
    public SignInException(String msg) {
        super(msg);
    }
}
