package movie.web.demo.controller.admin;

import lombok.RequiredArgsConstructor;
import movie.web.demo.domain.account.Account;
import movie.web.demo.domain.role.Role;
import movie.web.demo.exception.AccountException;
import movie.web.demo.exception.RoleException;
import movie.web.demo.form.AccountDeleteByAdminForm;
import movie.web.demo.form.AccountRoleUpdateForm;
import movie.web.demo.service.account.UserAccountManageService;
import movie.web.demo.service.account.admin.AdminAccountService;
import movie.web.demo.service.role.RoleService;
import movie.web.demo.util.BindingResultUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/account")
@RequiredArgsConstructor
public class AdminAccountController {

    private final UserAccountManageService defaultUserAccountManagerService;

    private final RoleService defaultRoleService;

    private final AdminAccountService defaultAdminAccountService;

    private final String ACCOUNT_UPDATE_PAGE = "/admin/account-update";

    private final String REDIRECT_ACCOUNT_MANAGE_PAGE = "redirect:/admin/account/user-manage";

    private final String REDIRECT_MANAGER_MANAGE_PAGE = "redirect:/admin/account/manager-manage";

    private final String MANAGER_MANAGE_PAGE = "/admin/manager-manage-main";

    private final String USER_MANAGE_PAGE = "/admin/user-manage-main";

    /**
     * 사용자 관리 메인 페이지
     */
    @GetMapping("/user-manage")
    public String userAccountManagePage(Model model) {
        List<Account> accounts = defaultUserAccountManagerService.getAllUser();
        model.addAttribute("users", accounts);
        return USER_MANAGE_PAGE;
    }

    /**
     * 관리자 계정 관리 메인 페이지
     */
    @GetMapping("/manager-manage")
    public String managerAccountManagePage(Model model) {
        List<Account> managers = defaultAdminAccountService.getAllManagerAccount();
        model.addAttribute("managers", managers);
        return MANAGER_MANAGE_PAGE;
    }

    /**
     * =================================================================================================
     * 사용자, 관리자 계정 업데이트
     */
    @GetMapping({"/user-update", "/manager-update"})
    public String userAccountUpdatePage(Long id, Model model) {
        try {
            Account account = defaultUserAccountManagerService.findAccountById(id);
            List<Role> roles = defaultRoleService.getAllRoleOrderNumNotNullSorted();
            model.addAttribute("account", account);
            model.addAttribute("roles", roles);
            return ACCOUNT_UPDATE_PAGE;
        } catch (AccountException e) {
            model.addAttribute("close_message", e.getMessage());
            return ACCOUNT_UPDATE_PAGE;
        }
    }

    @PostMapping({"/user-update", "/manager-update"})
    @ResponseBody
    public String userAccountUpdate(@Validated @RequestBody AccountRoleUpdateForm accountRoleUpdateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return BindingResultUtil.getError(bindingResult);
        }

        try {
            defaultAdminAccountService.updateAccountRole(accountRoleUpdateForm, defaultRoleService);
            return "권한이 변경되었습니다";
        } catch (AccountException e) {
            return e.getMessage();
        } catch (RoleException e) {
            return e.getMessage();
        }
    }

    /**
     * =================================================================================================
     * 사용자, 관리자 계정 삭제
     */
    @PostMapping("/user-delete")
    public String userAccountDelete(@Validated AccountDeleteByAdminForm accountDeleteByAdminForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", BindingResultUtil.getError(bindingResult));
            return REDIRECT_ACCOUNT_MANAGE_PAGE;
        }

        try {
            defaultAdminAccountService.deleteAccount(accountDeleteByAdminForm);
            redirectAttributes.addFlashAttribute("message", "삭제 되었습니다");
            return REDIRECT_ACCOUNT_MANAGE_PAGE;
        } catch (AccountException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_ACCOUNT_MANAGE_PAGE;
        }
    }

    @PostMapping("/manager-delete")
    public String managerAccountDelete(@Validated AccountDeleteByAdminForm accountDeleteByAdminForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", BindingResultUtil.getError(bindingResult));
            return REDIRECT_MANAGER_MANAGE_PAGE;
        }

        try {
            defaultAdminAccountService.deleteAccount(accountDeleteByAdminForm);
            redirectAttributes.addFlashAttribute("message", "삭제 되었습니다");
            return REDIRECT_MANAGER_MANAGE_PAGE;
        } catch (AccountException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_MANAGER_MANAGE_PAGE;
        }
    }
}
