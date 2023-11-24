package movie.web.demo.service.account.admin;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import movie.web.demo.domain.account.Account;
import movie.web.demo.domain.role.Role;
import movie.web.demo.exception.AccountException;
import movie.web.demo.form.AccountDeleteByAdminForm;
import movie.web.demo.form.AccountRoleUpdateForm;
import movie.web.demo.form.AccountSignUpForm;
import movie.web.demo.repository.AccountRepository;
import movie.web.demo.service.role.RoleService;
import movie.web.demo.util.PasswordEncodeManager;
import movie.web.demo.validator.custom.AccountValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultAdminAccountService implements AdminAccountService {

    private final AccountRepository accountRepository;

    private final AccountValidator accountValidator = new AccountValidator();

    /**
     * 계정 삭제
     * @param accountDeleteByAdminForm
     */
    public void deleteAccount(AccountDeleteByAdminForm accountDeleteByAdminForm) {
        Optional<Account> account = accountRepository.findById(accountDeleteByAdminForm.getId());
        if (!accountValidator.deleteCheck(account)) {
            throw new AccountException("존재하지 않는 권한 입니다");
        }
        accountRepository.delete(account.get());
    }

    /**
     * 계정 권한 업데이트
     * @param accountRoleUpdateForm
     * @param roleService
     */
    @Transactional
    public void updateAccountRole(AccountRoleUpdateForm accountRoleUpdateForm, RoleService roleService) {
        Optional<Account> account = accountRepository.findById(accountRoleUpdateForm.getId());
        Optional<Role> role = roleService.findRoleById(accountRoleUpdateForm.getRole());
        if (accountValidator.updateCheck(account, role)) {
            account.get().setRole(role.get().getRole());
        }
    }

    /**
     * Manager 이상 권한을 갖는 모든 계정을 찾음
     * @return
     */
    public List<Account> getAllManagerAccount() {
        return accountRepository.findAllManager();
    }
}
