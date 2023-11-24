package movie.web.demo.service.authorize;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import movie.web.demo.security.CustomAuthorizationManager;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

/**
 * 인가와 관련된 클래스를 관리
 */
@Service
@RequiredArgsConstructor
@DependsOn("commonAuthorizeService")
public class AuthorizationManagerMaker {

    private final AuthorizeService commonAuthorizeService;
    private CustomAuthorizationManager customAuthorizationManager;
    public CustomAuthorizationManager getCustomAuthorizationManager() {
        return customAuthorizationManager;
    }

    @PostConstruct
    public void init() {
        customAuthorizationManager = new CustomAuthorizationManager();
        customAuthorizationManager.setPassRequests(commonAuthorizeService.getPassAuthorizationMatcher());
        customAuthorizationManager.setAuthorizationMap(commonAuthorizeService.getAuthorizationMap());
        customAuthorizationManager.setRoleHierarchy(commonAuthorizeService.getRoleHierarchy());
    }

    /**
     * url에 변동사항(추가, 삭제, 수정)이 발생했을 때, 변동 사항을 반영.
     */
    public void reflectUpdateUrl() {
        this.customAuthorizationManager.setPassRequests(commonAuthorizeService.getPassAuthorizationMatcher());
        this.customAuthorizationManager.setAuthorizationMap(commonAuthorizeService.getAuthorizationMap());
    }
}
