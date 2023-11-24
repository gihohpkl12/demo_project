package movie.web.demo.service.account;

import movie.web.demo.domain.account.Account;
import movie.web.demo.form.AccountDeleteForm;
import movie.web.demo.form.NicknameChangeForm;
import movie.web.demo.form.PasswordChangeForm;
import movie.web.demo.form.PasswordFindingForm;
import movie.web.demo.service.authentication.AuthenticationService;
import movie.web.demo.service.token.redis.TokenManageService;

import java.util.List;
import java.util.Optional;

public interface UserAccountManageService extends UserAccountService {

    public List<Account> getAllUser();

    public void changePassword(PasswordChangeForm passwordChangeForm);

    public void changeNickname(NicknameChangeForm nicknameChangeForm);

    public void deleteAccount(AccountDeleteForm accountDeleteForm);

    public String findPassword(PasswordFindingForm passwordFindingForm, TokenManageService tokenManageService);

    public Account findAccountById(Long id);

    public List<Account> findAccountByRole(String role);
}
