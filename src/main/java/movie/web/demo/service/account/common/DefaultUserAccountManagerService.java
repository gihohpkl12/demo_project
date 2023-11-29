package movie.web.demo.service.account.common;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import movie.web.demo.event.AccountDeleteEvent;
import movie.web.demo.event.AccountNicknameUpdateEvent;
import movie.web.demo.form.PasswordFindingForm;
import movie.web.demo.service.token.redis.TokenManageService;
import movie.web.demo.util.PasswordEncodeManager;
import movie.web.demo.domain.account.Account;
import movie.web.demo.exception.AccountException;
import movie.web.demo.exception.PasswordException;
import movie.web.demo.form.AccountDeleteForm;
import movie.web.demo.form.NicknameChangeForm;
import movie.web.demo.form.PasswordChangeForm;
import movie.web.demo.repository.AccountRepository;
import movie.web.demo.service.account.UserAccountManageService;
import movie.web.demo.service.account.UserAccountService;
import movie.web.demo.validator.custom.AccountValidator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultUserAccountManagerService implements UserAccountManageService {

    private final AccountRepository accountRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final AccountValidator accountValidator = new AccountValidator();
    @Override
    public Optional<Account> findAccountByEmail(String email) {
        Optional<Account> account = accountRepository.findByEmail(email);
        if (!account.isPresent()) {
            throw new AccountException("해당 계정이 존재하지 않습니다");
        }
        return account;
    }

    @Override
    public Account findAccountById(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        if (!account.isPresent()) {
            throw new AccountException("해당 계정이 존재하지 않습니다");
        }
        return account.get();
    }

    @Transactional
    public void changePassword(PasswordChangeForm passwordChangeForm) {
        Optional<Account> account = accountRepository.findByEmail(passwordChangeForm.getEmail());
        if (accountValidator.updateCheck(account, passwordChangeForm.getPassword())) {
            account.get().setPassword(PasswordEncodeManager.encode(passwordChangeForm.getNewPassword()));
            account.get().setLastPasswordChangeDate(LocalDateTime.now());
        }
    }

    @Transactional
    public void changeNickname(NicknameChangeForm nicknameChangeForm) {
        Optional<Account> account = accountRepository.findByNickname(nicknameChangeForm.getCurNickname());
        Optional<Account> newAccount = findAccountByNickname(nicknameChangeForm.getNewNickname());
        if (accountValidator.updateCheck(account, newAccount, nicknameChangeForm.getCurNickname(), nicknameChangeForm.getPassword())) {
            account.get().setNickname(nicknameChangeForm.getNewNickname());
            applicationEventPublisher.publishEvent(new AccountNicknameUpdateEvent(account.get().getId(), account.get().getNickname()));
        }
    }

    private Optional<Account> findAccountByNickname(String nickname) {
        return accountRepository.findByNickname(nickname);
    }

    public void deleteAccount(AccountDeleteForm accountDeleteForm) {
        Optional<Account> account = accountRepository.findByEmail(accountDeleteForm.getEmail());
        if (accountValidator.deleteCheck(account, accountDeleteForm.getEmail(), accountDeleteForm.getPassword())) {
            accountRepository.delete(account.get());
            applicationEventPublisher.publishEvent(new AccountDeleteEvent(account.get().getId(), account.get().getNickname()));
        }
    }

    @Transactional
    public String findPassword(PasswordFindingForm passwordFindingForm, TokenManageService tokenManageService) {
        if (tokenManageService.validatePasswordFindingToken(passwordFindingForm.getToken(), passwordFindingForm.getEmail())) {
            try {
                return resetPassword(passwordFindingForm);
            } catch (AccountException e) {
                throw new AccountException("계정 정보를 확인하시기 바랍니다");
            }
        } else {
            throw new AccountException("계정 정보를 확인하시기 바랍니다");
        }
    }

    @Transactional
    private String resetPassword(PasswordFindingForm passwordFindingForm) {
        Optional<Account> account = findAccountByEmail(passwordFindingForm.getEmail());
        String resetPassword = Integer.toString((int)(Math.random() * 1000));
        if (account.isPresent()) {
            account.get().setPassword(PasswordEncodeManager.encode(resetPassword));
            return resetPassword;
        } else {
            throw new AccountException("해당 계정이 없습니다");
        }
    }

    public List<Account> getAllUser() {
        return accountRepository.findAllUser();
    }

    public List<Account> findAccountByRole(String role) {
        return accountRepository.findByRole(role);
    }

}
