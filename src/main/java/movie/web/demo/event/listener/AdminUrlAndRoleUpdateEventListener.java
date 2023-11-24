package movie.web.demo.event.listener;

import lombok.RequiredArgsConstructor;
import movie.web.demo.event.RoleUpdateEvent;
import movie.web.demo.event.UrlUpdateEvent;
import movie.web.demo.event.listener.AdminEventListener;
import movie.web.demo.service.authorize.AuthorizationManagerMaker;
import movie.web.demo.service.role.RoleService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.stereotype.Component;

/**
 * 관리자 페이지에서 role, url에 변동 사항이 생겼을 때
 * 인증 및 인가에 해당 변동 사항을 반영.
 */
@Component
@RequiredArgsConstructor
public class AdminUrlAndRoleUpdateEventListener implements AdminEventListener {

    private final AuthorizationManagerMaker authorizationManagerMaker;

    private final ApplicationContext applicationContext;

    private final RoleService defaultRoleService;

    @Override
    @EventListener
    public void Listen(UrlUpdateEvent urlUpdateEvent) {
        authorizationManagerMaker.reflectUpdateUrl();
    }

    @Override
    @EventListener
    public void Listen(RoleUpdateEvent roleUpdateEvent) {
        RoleHierarchy roleHierarchy = (RoleHierarchy) applicationContext.getBean("roleHierarchy");
        roleHierarchy = defaultRoleService.roleHierarchy();
        authorizationManagerMaker.getCustomAuthorizationManager().setRoleHierarchy(roleHierarchy);
    }
}
