package movie.web.demo.service.mail;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import movie.web.demo.form.MailForm;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class DefaultMailService implements MailService{

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    @Override
    public void sendMail(String email, String token) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MailForm mailForm = createMailForm(email, token);
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(mailForm.getEmail());
            mimeMessageHelper.setSubject(mailForm.getSubject());
            mimeMessageHelper.setText(mailForm.getMessage(), true);

            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            System.out.println("mail error "+e.getMessage());
        }
    }

    private MailForm createMailForm(String email, String token) {
        Context context = new Context();
        context.setVariable("token", token);
        context.setVariable("email", email);
        String message = templateEngine.process("mail-form", context);

        MailForm mailForm = new MailForm();
        mailForm.setEmail(email);
        mailForm.setSubject("비밀번호 찾기");
        mailForm.setMessage(message);

        return mailForm;
    }

}
