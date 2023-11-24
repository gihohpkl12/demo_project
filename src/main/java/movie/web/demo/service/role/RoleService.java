package movie.web.demo.service.role;

import movie.web.demo.domain.role.Role;
import movie.web.demo.form.RoleAddForm;
import movie.web.demo.form.RoleUpdateForm;
import movie.web.demo.service.account.UserAccountManageService;
import movie.web.demo.service.account.UserAccountService;
import movie.web.demo.service.url.UrlService;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    public List<Role> getAllRoleOrderNumNotNullSorted();
    public RoleHierarchy roleHierarchy();

    public Optional<Role> findRoleById(Long id);

    public List<Role> getAllRole();

    public void addRole(RoleAddForm roleAddForm);

    public void updateRole(RoleUpdateForm roleUpdateForm);
    public void deleteRole(Long id, UserAccountManageService userAccountManageService, UrlService urlService);

}
