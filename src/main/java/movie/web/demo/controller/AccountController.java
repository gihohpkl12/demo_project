package movie.web.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import movie.web.demo.service.mail.MailService;
import movie.web.demo.util.BindingResultUtil;
import movie.web.demo.domain.account.Account;
import movie.web.demo.exception.*;
import movie.web.demo.form.*;
import movie.web.demo.service.account.*;
import movie.web.demo.service.authentication.AuthenticationService;
import movie.web.demo.service.board.BoardService;
import movie.web.demo.service.token.redis.TokenManageService;
import movie.web.demo.validator.AccountSignInFormValidator;
import movie.web.demo.validator.AccountSignUpFormValidator;
import movie.web.demo.service.token.TokenService;
import movie.web.demo.validator.PasswordChangeFormValidator;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final String SING_UP_PAGE_NAME = "join";
    private final String SING_IN_PAGE_NAME = "login";
    private final String REDIRECT_MAIN_PAGE = "redirect:/";
    private final String REDIRECT_SIGN_UP_PAGE = "redirect:/sign-up";
    private final String REDIRECT_SIGN_IN_PAGE = "redirect:/sign-in";
    private final String PASSWORD_CHANGE_PAGE = "/password-change";
    private final String NICKNAME_CHANGE_PAGE = "/nickname-change";
    private final String REDIRECT_MYPAGE = "redirect:/mypage";
    private final String REDIRECT_LOGOUT = "redirect:/logout";
    private final String ACCOUNT_DELETE_PAGE = "/account-delete";
    private final String PASSWORD_FIND_PAGE = "/password-find";
    private final String PASSWORD_FIND_INIT_PAGE = "/password-find-init";
    private final String REDIRECT_PASSWORD_FIND = "redirect:/password-find";
    private final String MYPAGE_PAGE= "/profile";

    private final TokenService jWTTokenService;

    private final TokenManageService redisTokenManageService;

    private final OAuthUserAccountService naverUserAccountService;

    private final UserAccountSignUpInService defaultUserAccountSignUpAndInService;

    private final UserAccountManageService defaultUserAccountManageService;

    private final BoardService defaultBoardService;

    private final AuthenticationService jwtAuthenticationService;

    private final MailService defaultMailService;

    /**
     * =========================================================================================================
     * validator
     */
    @InitBinder("accountSignInForm")
    public void accountSignInFormBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new AccountSignInFormValidator());
    }
    @InitBinder("accountSignUpForm")
    public void accountSignUpFormBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new AccountSignUpFormValidator());
    }

    @InitBinder("passwordChangeForm")
    public void passwordChangeFormBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new PasswordChangeFormValidator());
    }

    /**
     * =========================================================================================================
     * OAuth 로그인, 회원 가입
     */
    @GetMapping("/naver/sign-in")
    public String naverSignInPage() {
        return "redirect:"+ naverUserAccountService.getOAuthAuthenticationRequestUrl("sign-in");
    }

    @GetMapping("/naver/sign-in/callback")
    public String naverLoginRequestCallback(NaverOAuthAuthenticationRequestResult naverOAuthAuthenticationRequestResult, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String email = naverUserAccountService.getUserEmail(naverOAuthAuthenticationRequestResult.getCode());
            defaultUserAccountSignUpAndInService.login(email, jWTTokenService, response, redisTokenManageService);
            redirectAttributes.addFlashAttribute("message", "반갑습니다");

            return redirectUrlAfterLoginSuccess(request, response);
        } catch (AccountException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_SIGN_IN_PAGE;
        }
    }

    @GetMapping("/naver/sign-up")
    public String naverSignUpPage() {
        return "redirect:"+ naverUserAccountService.getOAuthAuthenticationRequestUrl("sign-up");
    }

    @GetMapping("/naver/sign-up/callback")
    public String naverUserInfoRequestCallback(NaverOAuthAuthenticationRequestResult naverOAuthAuthenticationRequestResult, RedirectAttributes redirectAttributes) {
        String email = naverUserAccountService.getUserEmail(naverOAuthAuthenticationRequestResult.getCode());
        redirectAttributes.addFlashAttribute("email", email);
        redirectAttributes.addFlashAttribute("type", "naver");

        return REDIRECT_SIGN_UP_PAGE;
    }

    /**
     * =========================================================================================================
     * 기본 로그인, 회원가입
     */
    @GetMapping("/sign-up")
    public String singUpPage() {
        return SING_UP_PAGE_NAME;
    }

    @GetMapping("/sign-in")
    public String signInPage() {
        return SING_IN_PAGE_NAME;
    }
    @PostMapping("/sign-up")
    public String signUp(@Validated @ModelAttribute AccountSignUpForm accountSignUpForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        String errorMessage = BindingResultUtil.getError(bindingResult);
        if (errorMessage == null || !errorMessage.equals("")) {
            redirectAttributes.addFlashAttribute("message", errorMessage);
            return REDIRECT_SIGN_UP_PAGE;
        }

        try {
            defaultUserAccountSignUpAndInService.registerAccount(accountSignUpForm);
            redirectAttributes.addFlashAttribute("message", "가입을 축하드립니다.");
        } catch (AccountException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_SIGN_UP_PAGE;
        }

        return REDIRECT_MAIN_PAGE;
    }

    @PostMapping("/sign-in")
    public String signIn(@Validated @ModelAttribute AccountSignInForm accountSignInForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        String errorMessage = BindingResultUtil.getError(bindingResult);
        if (errorMessage == null || !errorMessage.equals("")) {
            redirectAttributes.addFlashAttribute("message", errorMessage);
            return REDIRECT_SIGN_IN_PAGE;
        }

        try {
            defaultUserAccountSignUpAndInService.login(accountSignInForm, jWTTokenService, response, redisTokenManageService);
            redirectAttributes.addFlashAttribute("message", "반갑습니다");
            return redirectUrlAfterLoginSuccess(request, response);
        } catch (AccountException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_SIGN_IN_PAGE;
        }
    }

    /**
     * =========================================================================================================
     * 로그아웃
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, Model model) {
        try {
            defaultUserAccountSignUpAndInService.logout(redisTokenManageService, request, response, jWTTokenService);
            if (model.getAttribute("message") != null) {
                redirectAttributes.addFlashAttribute("message", model.getAttribute("message"));
            } else {
                redirectAttributes.addFlashAttribute("message", "로그아웃 되었습니다");
            }
        } catch (AccountException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_MAIN_PAGE;
        } catch (TokenException e) {
            redirectAttributes.addFlashAttribute("message", "다시 시도해주시기 바랍니다");
            return REDIRECT_MAIN_PAGE;
        }
        return REDIRECT_MAIN_PAGE;
    }

    private String redirectUrlAfterLoginSuccess(HttpServletRequest request, HttpServletResponse response) {
        String url = REDIRECT_MAIN_PAGE;

        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            url = "redirect:"+savedRequest.getRedirectUrl();
        }

        return url;
    }

    /**
     * =========================================================================================================
     * 마이 페이지
     */
    @GetMapping("/mypage")
    public String myPage(Model model, RedirectAttributes redirectAttributes) {
        try {
            Account account = defaultUserAccountSignUpAndInService.getLoginAccount();
            model.addAttribute("account", account);
            model.addAttribute("passwordChangeAgo", ChronoUnit.DAYS.between(account.getLastPasswordChangeDate().toLocalDate(), LocalDate.now()));
            model.addAttribute("boards", defaultBoardService.getAllBoard());
            return MYPAGE_PAGE;
        } catch (AccountException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_MYPAGE;
        }
    }

    /**
     * =========================================================================================================
     * 패스워드 변경
     */
    @GetMapping("/change-password")
    public String changePasswordPage() {
        return PASSWORD_CHANGE_PAGE;
    }

    @PostMapping("/change-password")
    public String changePassword(RedirectAttributes redirectAttributes, @Validated PasswordChangeForm passwordChangeForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", BindingResultUtil.getError(bindingResult));
            return REDIRECT_MAIN_PAGE;
        }

        try {
            defaultUserAccountManageService.changePassword(passwordChangeForm);
            redirectAttributes.addFlashAttribute("message", "비밀번호가 변경되었습니다");
            return REDIRECT_MAIN_PAGE;
        } catch (AccountException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_MAIN_PAGE;
        } catch (PasswordException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_MAIN_PAGE;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "잠시 후에 다시 시도해주시기 바랍니다");
            return REDIRECT_MAIN_PAGE;
        }
    }

    /**
     * =========================================================================================================
     * 패스워드 찾기
     */
    @GetMapping("password-find-init")
    public String passwordFindInitPage() {
        return PASSWORD_FIND_INIT_PAGE;
    }

    @PostMapping("password-token")
    public String sendPasswordFindingToken(String email, RedirectAttributes redirectAttributes) {
        if (email == null) {
            redirectAttributes.addFlashAttribute("message", "메일을 입력하세요");
            return REDIRECT_MYPAGE;
        }

        try {
            defaultUserAccountManageService.findAccountByEmail(email);
        } catch (AccountException e) {
            redirectAttributes.addFlashAttribute("message", "존재하지 않는 계정입니다");
            return PASSWORD_FIND_INIT_PAGE;
        }

        String token = redisTokenManageService.savePasswordFindingToken(email);
        defaultMailService.sendMail(email, token);
        redirectAttributes.addFlashAttribute("email", email);

        return REDIRECT_PASSWORD_FIND;
    }

    @GetMapping("password-find")
    public String findPasswordPage(Model model) {
        return PASSWORD_FIND_PAGE;
    }

    @PostMapping("password-find")
    public String findPassword(RedirectAttributes redirectAttributes, @Validated PasswordFindingForm passwordFindingForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", BindingResultUtil.getError(bindingResult));
            return REDIRECT_MAIN_PAGE;
        }

        try {
            String tempPassword = defaultUserAccountManageService.findPassword(passwordFindingForm, redisTokenManageService);
            redirectAttributes.addFlashAttribute("message", "비밀번호가 "+tempPassword+" 로 초기화 되었습니다");
            return REDIRECT_MAIN_PAGE;
        } catch (AccountException e ) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_MAIN_PAGE;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "잠시 후에 시도해주시기 바랍니다");
            return REDIRECT_MAIN_PAGE;
        }
    }

    /**
     * =========================================================================================================
     * 닉네임 변경
     */
    @GetMapping("/change-nickname")
    public String changeNicknamePage(Model model) {
        return NICKNAME_CHANGE_PAGE;
    }

    @PostMapping("/change-nickname")
    public String changeNickname(RedirectAttributes redirectAttributes, @Validated NicknameChangeForm nicknameChangeForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", BindingResultUtil.getError(bindingResult));
            return REDIRECT_LOGOUT;
        }

        try {
            defaultUserAccountManageService.changeNickname(nicknameChangeForm);
            redirectAttributes.addFlashAttribute("message", "변경 되었습니다 다시 로그인 해주세요");
            return REDIRECT_LOGOUT;
        } catch (AccountException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_MYPAGE;
        }
    }

    /**
     * =========================================================================================================
     * 계정 탈퇴
     */
    @GetMapping("/delete-account")
    public String deleteAccountPage(Model model) {
        return ACCOUNT_DELETE_PAGE;
    }

    @PostMapping("/delete-account")
    public String deleteAccount(RedirectAttributes redirectAttributes, @Validated AccountDeleteForm accountDeleteForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", BindingResultUtil.getError(bindingResult));
            return REDIRECT_MYPAGE;
        }

        try {
            defaultUserAccountManageService.deleteAccount(accountDeleteForm);
            redirectAttributes.addFlashAttribute("message", "탈퇴 되었습니다");
            return REDIRECT_LOGOUT;
        } catch (AccountException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_MYPAGE;
        }
    }
}
