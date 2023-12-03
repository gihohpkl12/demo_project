package movie.web.demo.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

    public Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 24 * 7);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public Cookie createCookie(String key, String value, int period) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(period);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public void setCookie(HttpServletResponse response, String key, String value) {
        response.addCookie(createCookie(key, value));
    }

    public void setCookie(HttpServletResponse response, Cookie cookie) {
        response.addCookie(cookie);
    }

    public void deleteCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = createCookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
