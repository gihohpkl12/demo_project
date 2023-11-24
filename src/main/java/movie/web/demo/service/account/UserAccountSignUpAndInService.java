package movie.web.demo.service.account;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import movie.web.demo.domain.account.Account;
import movie.web.demo.form.AccountSignInForm;
import movie.web.demo.form.AccountSignUpForm;
import movie.web.demo.service.authentication.AuthenticationService;
import movie.web.demo.service.token.TokenService;
import movie.web.demo.service.token.redis.TokenManageService;

public interface UserAccountSignUpAndInService extends UserAccountService {
    void login(AccountSignInForm accountSignInForm, TokenService tokenService, HttpServletResponse response, TokenManageService tokenManageService);

    void login(String email, TokenService tokenService, HttpServletResponse response, TokenManageService tokenManageService);

    void registerAccount(AccountSignUpForm accountSignUpForm);

    void logout(TokenManageService tokenManageService, HttpServletRequest request, HttpServletResponse response, TokenService tokenService);

    Account getLoginAccount();
}
