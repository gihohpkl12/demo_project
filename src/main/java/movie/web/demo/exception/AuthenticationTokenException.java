package movie.web.demo.exception;

public class AuthenticationTokenException extends RuntimeException {

    public AuthenticationTokenException(String msg) {
        super(msg);
    }
}
