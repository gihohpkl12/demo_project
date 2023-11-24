package movie.web.demo.validator.custom;

import movie.web.demo.domain.account.Account;
import movie.web.demo.exception.AccountException;
import movie.web.demo.exception.RoleException;
import movie.web.demo.service.account.UserAccountService;
import movie.web.demo.util.PasswordEncodeManager;

import java.util.List;
import java.util.Optional;

public class AccountValidator extends DefaultValidator {

    public boolean deleteCheck(Optional<?> t, String email, String password) {
        if (!deleteCheck(t)) {
            throw new AccountException("해당 계정이 없습니다");
        }

        if (!UserAccountService.getLoginAccountEmail().equals(email)) {
            throw new AccountException("로그인한 계정과 삭제하려는 계정의 정보가 일치하지 않습니다");
        }

        Account account = (Account) t.get();
        if (!PasswordEncodeManager.match(password, account.getPassword())) {
            throw new AccountException("계정 정보를 확인해주시기 바랍니다");
        }

        return true;
    }

    // 닉네임 변경
    public boolean updateCheck(Optional<?> t, Optional<?> t2, String nickname, String password) {
        if (!updateCheck(t)) {
            throw new AccountException("해당 계정이 존재하지 않습니다");
        }

        if (!UserAccountService.getLoginAccountNickname().equals(nickname)) {
            throw new AccountException("로그인한 계정과 요청한 계정의 정보가 다릅니다");
        }

        Account account = (Account) t.get();
        if (!PasswordEncodeManager.match(password, account.getPassword())){
            throw new AccountException("계정 정보가 맞지 않습니다");
        }

        if (isExist(t2)) {
            throw new AccountException("이미 사용하고 있는 닉네임입니다");
        }

        return true;
    }

    // 패스워드 변경
    public boolean updateCheck(Optional<?> t, String password) {
        if (!updateCheck(t)) {
            throw new AccountException("존재하지 않는 계정입니다");
        }

        Account account = (Account) t.get();
        if (!PasswordEncodeManager.match(password, account.getPassword())){
            throw new AccountException("계정 정보가 맞지 않습니다");
        }

        return true;
    }

    // 권한 변경
    public boolean updateCheck(Optional<?> t, Optional<?> role) {
        if (!updateCheck(t)) {
            throw new AccountException("존재하지 않는 계정입니다");
        }

        if (!isExist(role)) {
            throw new RoleException("존재하지 않는 권한입니다");
        }

        return true;
    }

    public boolean addCheck(List<?> accounts) {
        if (accounts.size() > 0) {
            throw new AccountException("이미 존재하는 계정 정보입니다");
        }
        return true;
    }
}
