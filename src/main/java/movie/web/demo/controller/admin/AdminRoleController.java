package movie.web.demo.controller.admin;

import lombok.RequiredArgsConstructor;
import movie.web.demo.domain.role.Role;
import movie.web.demo.event.RoleUpdateEvent;
import movie.web.demo.exception.RoleException;
import movie.web.demo.form.RoleAddForm;
import movie.web.demo.form.RoleUpdateForm;
import movie.web.demo.service.account.UserAccountManageService;
import movie.web.demo.service.role.RoleService;
import movie.web.demo.service.url.UrlService;
import movie.web.demo.util.BindingResultUtil;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/role")
@RequiredArgsConstructor
public class AdminRoleController {

    private final RoleService defaultRoleService;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final UserAccountManageService defaultUserAccountManageService;

    private final UrlService defaultUrlService;
    private final String ROLE_MANAGE_PAGE = "/admin/role-manage-main";
    private final String ROLE_UPDATE_PAGE = "/admin/role-update";
    private final String ROLE_ADD_PAGE = "/admin/role-add";
    private final String REDIRECT_ROLE_MANAGE_PAGE = "redirect:/admin/role";

    /**
     * 권한 메인 페이지
     */
    @GetMapping
    public String roleManagePage(Model model) {
        List<Role> roles = defaultRoleService.getAllRole();
        model.addAttribute("roles", roles);
        return ROLE_MANAGE_PAGE;
    }

    /**
     * =================================================================================================
     * 권한 추가
     */
    @GetMapping("/role-add")
    public String roleAddPage() {
        return ROLE_ADD_PAGE;
    }

    @PostMapping("/role-add")
    @ResponseBody
    public String addRole(@Validated @RequestBody RoleAddForm roleAddForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return BindingResultUtil.getError(bindingResult);
        }

        try {
            defaultRoleService.addRole(roleAddForm);
            return "저장됐습니다";
        } catch (RoleException e) {
            return e.getMessage();
        }
    }

    /**
     * =================================================================================================
     * 권한 업데이트
     */
    @GetMapping("role-update")
    public String roleUpdatePage(Long id, Model model) {
        if (id == null) {
            model.addAttribute("close_message", "id가 null입니다");
            return ROLE_UPDATE_PAGE;
        }
        try {
            Optional<Role> role = defaultRoleService.findRoleById(id);
            model.addAttribute("role", role.get());
            return ROLE_UPDATE_PAGE;
        } catch (RoleException e) {
            model.addAttribute("close_message", e.getMessage());
            return ROLE_UPDATE_PAGE;
        }
    }

    @PostMapping("role-update")
    @ResponseBody
    public String roleUpdate(@Validated @RequestBody RoleUpdateForm roleUpdateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return BindingResultUtil.getError(bindingResult);
        try {
            defaultRoleService.updateRole(roleUpdateForm);
            return "수정됐습니다";
        } catch (RoleException e) {
            return e.getMessage();
        }
    }

    /**
     * =================================================================================================
     * 권한 삭제
     */
    @PostMapping("role-delete")
    public String roleDelete(Long id, RedirectAttributes redirectAttributes) {
        if (id == null) {
            redirectAttributes.addFlashAttribute("message", "id가 null입니다");
            return REDIRECT_ROLE_MANAGE_PAGE;
        }

        try {
            defaultRoleService.deleteRole(id, defaultUserAccountManageService, defaultUrlService);
            redirectAttributes.addFlashAttribute("message", "삭제 되었습니다");
            return REDIRECT_ROLE_MANAGE_PAGE;
        } catch (RoleException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_ROLE_MANAGE_PAGE;
        }
    }

    /**
     * =================================================================================================
     * 권한 변경사항 반영
     */
    @GetMapping("/role-reflect")
    public String reflectRole() {
        applicationEventPublisher.publishEvent(new RoleUpdateEvent());
        return REDIRECT_ROLE_MANAGE_PAGE;
    }
}
