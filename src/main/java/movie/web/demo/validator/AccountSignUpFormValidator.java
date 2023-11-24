package movie.web.demo.validator;

import movie.web.demo.form.AccountSignUpForm;
import movie.web.demo.service.account.UserAccountService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AccountSignUpFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AccountSignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountSignUpForm accountSignUpForm = (AccountSignUpForm) target;

        if (!UserAccountService.checkEmailPattern(accountSignUpForm.getEmail())) {
            errors.reject("message", "이메일 형식 에러");
        }

        if (!UserAccountService.checkPasswordPattern(accountSignUpForm.getPassword())) {
            errors.reject("message", "패스워드 형식 에러");
        }

        if (!accountSignUpForm.getPassword().equals(accountSignUpForm.getRepeatPassword())) {
            errors.reject("message", "패스워드 반복 에러");
        }
    }
}
