package movie.web.demo.security;

import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 인가 수행.
 */
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private RequestMatcher passRequests;

    private RoleHierarchy roleHierarchy;

    private Map<RequestMatcher, String> authorizationMap;

    public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }

    public void setPassRequests(RequestMatcher requestMatcher) {
        this.passRequests = requestMatcher;
    }

    public void setAuthorizationMap(Map<RequestMatcher, String>  authorizationMap) {
        this.authorizationMap = authorizationMap;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        System.out.println("===========================================");
        System.out.println("call : "+ object.getRequest().getRequestURI());

        if (passRequests.matcher(object.getRequest()).isMatch()) {
            System.out.println("pass url "+object.getRequest().getRequestURI());
            return new AuthorizationDecision(true);
        }

        for(RequestMatcher matcher : authorizationMap.keySet()) {
            if(matcher.matcher(object.getRequest()).isMatch()) {
                String requiredRole = authorizationMap.get(matcher);
                System.out.println("required role: "+requiredRole);
                if(isGranted(authentication.get(), requiredRole)) {
                    System.out.println("granted");
                    System.out.println("===========================================");
                    return new AuthorizationDecision(true);
                } else {
                    return new AuthorizationDecision(false);
                }
            }
        }
        System.out.println("===========================================");
        return new AuthorizationDecision(false);
    }

    private boolean isGranted(Authentication authentication, String authority) {
        return authentication != null && isAuthorized(authentication, authority);
    }

    private boolean isAuthorized(Authentication authentication, String authority) {

        for (GrantedAuthority grantedAuthority : getGrantedAuthorities(authentication)) {
            if (authority.equals(grantedAuthority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(Authentication authentication) {
        return this.roleHierarchy.getReachableGrantedAuthorities(authentication.getAuthorities());
    }
}
