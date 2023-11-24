package movie.web.demo.validator;

import movie.web.demo.form.PasswordChangeForm;
import movie.web.demo.service.account.UserAccountService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PasswordChangeFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(PasswordChangeForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PasswordChangeForm passwordChangeForm = (PasswordChangeForm) target;

        if (!UserAccountService.checkPasswordPattern(passwordChangeForm.getPassword()) ||
                !UserAccountService.checkPasswordPattern(passwordChangeForm.getNewPassword()) ||
                !UserAccountService.checkEmailPattern(passwordChangeForm.getEmail()) ||
                !passwordChangeForm.getNewPassword().equals(passwordChangeForm.getRepeatPassword()) ||
                passwordChangeForm.getNewPassword().equals(passwordChangeForm.getPassword())) {
            errors.reject("message", "입력 정보를 확인하세요");
        }
    }
}
