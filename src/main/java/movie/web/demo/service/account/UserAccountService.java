package movie.web.demo.service.account;

import movie.web.demo.domain.account.Account;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.regex.Pattern;


public interface UserAccountService {

    Optional<Account> findAccountByEmail(String email);

    static boolean checkEmailPattern(String email) {
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern pattern = Pattern.compile(regex);

        if (pattern.matcher(email).matches()) {
            return true;
        }

        return false;
    }

    static boolean checkPasswordPattern(String password) {
        if(password == null || password.length() > 4 || password.length() == 0) {
            return false;
        }

        String regex = "^[0-9]*$";
        Pattern pattern = Pattern.compile(regex);

        if (!pattern.matcher(password).matches()) {
            return false;
        }

        return true;
    }

    static boolean isLogin() {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken) {
            return true;
        }
        return false;
    }


    static String getLoginAccountEmail() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
            Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return account.getEmail();
        }
        return null;
    }

    static String getLoginAccountRole() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
            Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return account.getRole();
        }
        return null;
    }

    static String getLoginAccountNickname() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
            Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return account.getNickname();
        }
        return null;
    }

//    static Long getLoginAccountId() {
//        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
//            Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            return account.getId();
//        }
//        return null;
//    }

    static boolean isSameUserCheck(Long accountId) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
            Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (account.getId() == null || accountId != accountId) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

}
