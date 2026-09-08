package com.menzo.Communication_Service.Email.Service;

import com.menzo.Communication_Service.Email.Dto.EmailDto;
import com.menzo.Communication_Service.Global.Enum.Purpose;
import com.menzo.Communication_Service.Global.Feign.UserFeign;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserFeign userFeign;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${email.context.app-name}")
    private String appName;

    @Value("${email.context.otp-validity-minutes}")
    private String otpValidityMinutes;

    @Value("${email.display-name.sign-in-otp}")
    private String signInOtpEmailDisplayName;

    @Value("${email.subject.sign-in-otp}")
    private String signInOtpEmailSubject;

    private final SpringTemplateEngine templateEngine;

    public EmailService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }


    /*
     *
     *   Send OTP email
     *
     */
    public boolean sendOtpEmail(String otp, String userEmail, Purpose purpose) {
        try {
            String username = getUsername(userEmail);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, signInOtpEmailDisplayName);       //  displayName
            helper.setTo(userEmail);
            helper.setSubject(signInOtpEmailSubject);
            helper.setText(getOtpEmailTemplate(otp, username, purpose), true);     //  true -> HTML email

            mailSender.send(message);
            return true;
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error("Failed to send OTP email to {}", userEmail, e);
            return false;
        }
    }


    //  get sign-in OTP email template
    private String getOtpEmailTemplate(String otp, String userName, Purpose purpose) {
        Context context = new Context();

        context.setVariable("appName", appName);
        context.setVariable("actionName", purpose.getDisplayName().toLowerCase());
        context.setVariable("userName", userName);
        context.setVariable("otpValidityMinutes", otpValidityMinutes);
        context.setVariable("otpCode", otp);
        context.setVariable("supportLink", "");
        context.setVariable("currentYear", LocalDate.now().getYear());

        return templateEngine.process("otp-email", context);
    }


    //  get username by user email
    private String getUsername(String userEmail) {
        ResponseEntity<?> response = userFeign.getUsername(new EmailDto(userEmail));
        if (response == null || response.getBody() == null) {
            throw new RuntimeException("Failed to get username by user email.");
        }
        return response.getBody().toString();
    }

}
