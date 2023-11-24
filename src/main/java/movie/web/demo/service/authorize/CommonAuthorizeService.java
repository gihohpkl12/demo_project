package movie.web.demo.service.authorize;

import lombok.RequiredArgsConstructor;
import movie.web.demo.domain.url.Url;
import movie.web.demo.service.url.UrlService;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 인가에 필요한 데이터(RequestMatcher - url, role) 관련 서비스
 */
@Service
@RequiredArgsConstructor
public class CommonAuthorizeService implements AuthorizeService {

    private final UrlService defaultUrlService;

    private final RoleHierarchy roleHierarchy;

    /**
     * 인가가 필요한 url - role 생성
     * @return
     */
    @Override
    public LinkedHashMap<RequestMatcher, String> getAuthorizationMap() {
        LinkedHashMap<RequestMatcher, String> result = new LinkedHashMap<>();
        List<Url> urls = defaultUrlService.getAuthenticationUrls();
        LinkedHashMap<String, List<Url>> authorizationMap = makeRoleAndPathMap(urls);

        for (String role : authorizationMap.keySet()) {
            RequestMatcher requestMatcher = makeRequestMatcher(authorizationMap.get(role));
            result.put(requestMatcher, role);
        }

        return result;
    }

    /**
     * 인가가 필요 없는 url 생성
     * @return
     */
    @Override
    public RequestMatcher getPassAuthorizationMatcher() {
        List<Url> urls = defaultUrlService.getPassAuthenticationUrls();
        return makeRequestMatcher(urls);
    }


    /**
     * 익명 사용자만 접근이 가능한 url(회원가입, 로그인 등)
     * @return
     */
    @Override
    public RequestMatcher getAnonymousUrl() {
        return makeRequestMatcher(defaultUrlService.getSignInUrl());
    }

    private RequestMatcher makeRequestMatcher(List<Url> urls) {
        RequestMatcher[] matchers = new RequestMatcher[urls.size()];
        int index = 0;
        for (Url url : urls) {
            matchers[index++] = new AntPathRequestMatcher(url.getPath());
        }

        return new OrRequestMatcher(matchers);
    }

    private LinkedHashMap<String, List<Url>> makeRoleAndPathMap(List<Url> urls) {
        LinkedHashMap<String, List<Url>> roleAndPath = new LinkedHashMap<>();

        for (Url url : urls) {
            String role = url.getRole().getRole();

            if (!roleAndPath.containsKey(role)) {
                roleAndPath.put(role, new ArrayList<>());
            }

            roleAndPath.get(role).add(url);
        }

        return roleAndPath;
    }

    public RoleHierarchy getRoleHierarchy() {
        return this.roleHierarchy;
    }
}
