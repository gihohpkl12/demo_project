package movie.web.demo.service.authorize;

import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.HashMap;

public interface AuthorizeService {
    HashMap<RequestMatcher, String> getAuthorizationMap();

    RequestMatcher getPassAuthorizationMatcher();

    RoleHierarchy getRoleHierarchy();

    RequestMatcher getAnonymousUrl();
}
