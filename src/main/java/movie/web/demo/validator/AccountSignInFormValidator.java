package movie.web.demo.validator;

import movie.web.demo.form.AccountSignInForm;
import movie.web.demo.service.account.UserAccountService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AccountSignInFormValidator implements Validator  {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AccountSignInForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountSignInForm accountSignInForm = (AccountSignInForm) target;

        if (!UserAccountService.checkEmailPattern(accountSignInForm.getEmail())) {
            errors.reject("message", "이메일 형식 에러");
        }

        if (!UserAccountService.checkPasswordPattern(accountSignInForm.getPassword())) {
            errors.reject("message", "패스워드 형식 에러");
        }
    }
}
