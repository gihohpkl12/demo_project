package movie.web.demo.service.account.admin;

import movie.web.demo.domain.account.Account;
import movie.web.demo.form.AccountDeleteByAdminForm;
import movie.web.demo.form.AccountRoleUpdateForm;
import movie.web.demo.form.AccountSignUpForm;
import movie.web.demo.service.role.RoleService;

import java.util.List;

public interface AdminAccountService {

    public void deleteAccount(AccountDeleteByAdminForm accountDeleteByAdminForm);

    public void updateAccountRole(AccountRoleUpdateForm accountRoleUpdateForm, RoleService roleService);

    public List<Account> getAllManagerAccount();
}
