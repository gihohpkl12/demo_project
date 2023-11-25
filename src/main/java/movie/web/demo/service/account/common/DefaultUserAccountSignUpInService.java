package movie.web.demo.service.account.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import movie.web.demo.util.PasswordEncodeManager;
import movie.web.demo.domain.account.Account;
import movie.web.demo.exception.AccountException;
import movie.web.demo.form.AccountSignInForm;
import movie.web.demo.form.AccountSignUpForm;
import movie.web.demo.repository.AccountRepository;
import movie.web.demo.service.account.UserAccountService;
import movie.web.demo.service.account.UserAccountSignUpInService;
import movie.web.demo.service.token.redis.TokenManageService;
import movie.web.demo.util.CookieUtil;
import movie.web.demo.service.token.TokenService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 회원 가입, 로그인 관련 기능
 */
@Service
@RequiredArgsConstructor
public class DefaultUserAccountSignUpInService implements UserAccountSignUpInService {

    private final AccountRepository accountRepository;

    /**
     * 계정 등록
     * @param accountSignUpForm
     */
    public void registerAccount(AccountSignUpForm accountSignUpForm) {
        try {
            if (validateAccountSignUp(accountSignUpForm)) {
                Account account = createAccount(accountSignUpForm);
                accountRepository.save(account);
            }
        } catch (AccountException e) {
            throw new AccountException(e.getMessage());
        }
    }

    /**
     * 계정 등록 가능 여부 확인
     * @param accountSignUpForm
     * @return
     */
    private boolean validateAccountSignUp(AccountSignUpForm accountSignUpForm) {
        List<Account> account = accountRepository.findByEmailOrNickname(accountSignUpForm.getEmail(), accountSignUpForm.getNickname());

        if (account.size() != 0) {
            StringBuilder sb = new StringBuilder();
            for (Account temp : account) {
                if (temp.getEmail().equals(accountSignUpForm.getEmail())) {
                    sb.append("이미 존재하는 이메일입니다.");
                }

                if (temp.getNickname().equals(accountSignUpForm.getNickname())) {
                    if (sb.length() != 0) {
                        sb.append("\n");
                    }
                    sb.append("이미 존재하는 닉네임입니다.");
                }
            }

            throw new AccountException(sb.toString());
        }

        return true;
    }

    /**
     * account 객체 생성
     * @param accountSignUpForm
     * @return
     */
    private Account createAccount(AccountSignUpForm accountSignUpForm) {
        Account account = new Account();
        account.setEmail(accountSignUpForm.getEmail());
        account.setNickname(accountSignUpForm.getNickname());
        account.setRole("ROLE_USER");
        account.setPassword(PasswordEncodeManager.encode(accountSignUpForm.getPassword()));
        account.setJoinDate(LocalDateTime.now());
        account.setLastPasswordChangeDate(account.getJoinDate());
        account.setType(accountSignUpForm.getType());

        return account;
    }

    /**
     * 로그아웃
     * @param tokenManageService
     * @param request
     * @param response
     * @param tokenService
     */
    @Override
    public void logout(TokenManageService tokenManageService, HttpServletRequest request, HttpServletResponse response, TokenService tokenService) {
        if (!UserAccountService.isLogin()) {
            throw new AccountException("로그인 되어있지 않습니다");
        }
        String accessToken = tokenService.getToken(request, "access_token");
        String refreshToken = tokenService.getToken(request, "refresh_token");
        tokenManageService.saveLogoutToken(accessToken);
        tokenManageService.deleteRefreshToken(refreshToken);

        CookieUtil cookieUtil = new CookieUtil();
        cookieUtil.deleteCookie(response, "access_token");
        cookieUtil.deleteCookie(response, "refresh_token");

        SecurityContextHolder.clearContext();

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    @Override
    public Optional<Account> findAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    /**
     * 일반 로그인
     * @param accountSignInForm
     * @param tokenService
     * @param response
     * @param tokenManageService
     */
    @Override
    public void login(AccountSignInForm accountSignInForm, TokenService tokenService, HttpServletResponse response, TokenManageService tokenManageService) {
        Optional<Account> account = findAccountByEmail(accountSignInForm.getEmail());
        String msg = "아이디 혹은 비밀번호를 확인해주세요";

        if (!account.isPresent()) {
            throw new AccountException(msg);
        }

        if (!PasswordEncodeManager.match(accountSignInForm.getPassword(), account.get().getPassword())) {
            throw new AccountException(msg);
        }

        String[] tokens = setCookie(tokenService, account, response);
        saveLoginInfo(tokenManageService, tokens[1], account.get());
    }

    /**
     * OAuth 로그인
     * @param email
     * @param tokenService
     * @param response
     * @param tokenManageService
     */
    @Override
    public void login(String email, TokenService tokenService, HttpServletResponse response, TokenManageService tokenManageService) {
        Optional<Account> account = findAccountByEmail(email);

        if (!account.isPresent()) {
            throw new AccountException("해당 계정으로 가입된 계정이 없습니다");
        }

        if (!account.get().getType().equals("naver")) {
            throw new AccountException("계정 종류가 다릅니다.");
        }
        String[] tokens = setCookie(tokenService, account, response);
        saveLoginInfo(tokenManageService, tokens[1], account.get());
    }

    /**
     * 로그인 후에 refresh token을 redis에 저장
     * @param tokenManageService
     * @param token
     * @param account
     */
    private void saveLoginInfo(TokenManageService tokenManageService, String token, Account account) {
        tokenManageService.saveRefreshToken(token, account);
    }

    /**
     * 로그인 토큰을 생성 및 쿠키에 저장
     * @param tokenService
     * @param account
     * @param response
     * @return
     */
    private String[] setCookie(TokenService tokenService, Optional<Account> account, HttpServletResponse response) {
        CookieUtil cookieUtil = new CookieUtil();

        String accessToken = tokenService.createAccessToken(account.get().getEmail(), account.get().getNickname(), account.get().getRole(), account.get().getId());
        String refreshToken = tokenService.createRefreshToken(account.get().getEmail(), account.get().getNickname(), account.get().getRole(), account.get().getId());

        cookieUtil.setCookie(response, cookieUtil.createCookie("access_token", accessToken));
        cookieUtil.setCookie(response, cookieUtil.createCookie("refresh_token", refreshToken, 60 * 60 * 24 * 7));

        return new String[] {accessToken, refreshToken};
    }

    /**
     * Mypage에서 계정 정보 호출할 때, 로그인 계정의 정보로 가져옴.
     * @return
     */
    public Account getLoginAccount() {
        String loginEmail = UserAccountService.getLoginAccountEmail();
        if (loginEmail == null) {
            throw new AccountException("로그인 해주시기 바랍니다");
        }

        Optional<Account> account = accountRepository.findByEmail(loginEmail);
        if (account.isPresent()) {
            return account.get();
        } else {
            throw new AccountException("계정 관련 오류입니다");
        }
    }
}
