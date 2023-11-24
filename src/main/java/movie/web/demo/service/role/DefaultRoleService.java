package movie.web.demo.service.role;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import movie.web.demo.domain.account.Account;
import movie.web.demo.domain.role.Role;
import movie.web.demo.domain.url.Url;
import movie.web.demo.exception.AccountException;
import movie.web.demo.exception.RoleException;
import movie.web.demo.form.RoleAddForm;
import movie.web.demo.form.RoleUpdateForm;
import movie.web.demo.repository.RoleRepository;
import movie.web.demo.service.account.UserAccountManageService;
import movie.web.demo.service.account.UserAccountService;
import movie.web.demo.service.url.UrlService;
import movie.web.demo.validator.custom.RoleValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultRoleService implements RoleService{

    private final RoleRepository roleRepository;

    private final RoleValidator roleValidator = new RoleValidator();

    /**
     * 권한 계층
     * @return
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchyimpl = new RoleHierarchyImpl();
        String roleHierarchy = makingRoleHierarchy();
        roleHierarchyimpl.setHierarchy(roleHierarchy);
        return roleHierarchyimpl;
    }

    public Optional<Role> findRoleById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (!role.isPresent()) {
            throw new RoleException("존재하지 않는 Role입니다");
        }
        return role;
    }

    public List<Role> getAllRoleOrderNumNotNullSorted() {
        return roleRepository.getAllRoleSortedByOrder();
    }

    public List<Role> getAllRole() {
        return roleRepository.getAllRole();
    }
    private String makingRoleHierarchy() {
        List<Role> roles = getAllRoleOrderNumNotNullSorted();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < roles.size(); i++) {
            sb.append(roles.get(i).getRole());
            if (i != roles.size() -1) {
                sb.append(" > ");
            }
        }
        return sb.toString();
    }

    public void addRole(RoleAddForm roleAddForm) {
        if (roleValidator.addCheck(roleRepository.getRoleByRoleName(roleAddForm.getRole()), roleAddForm.getRole())) {
            roleRepository.save(createRole(roleAddForm));
        }
    }

    private Role createRole(RoleAddForm roleAddForm) {
        Role role = new Role();
        role.setRole(roleAddForm.getRole());
        role.setOrderNum(roleAddForm.getOrderNum());
        return role;
    }

    @Transactional
    public void updateRole(RoleUpdateForm roleUpdateForm) {
        Optional<Role> role = roleRepository.findById(roleUpdateForm.getId());
        Optional<Role> newRole = roleRepository.getRoleByRoleName(roleUpdateForm.getRole());
        if (roleValidator.updateCheck(role, newRole, roleUpdateForm.getRole())) {
            role.get().setOrderNum(roleUpdateForm.getOrderNum());
            role.get().setRole(roleUpdateForm.getRole());
        }
    }

    public void deleteRole(Long id, UserAccountManageService userAccountManageService, UrlService urlService) {
        Optional<Role> role = roleRepository.findById(id);
        if (roleValidator.deleteCheck(role, userAccountManageService, urlService)) {
            roleRepository.delete(role.get());
        }
    }
}
