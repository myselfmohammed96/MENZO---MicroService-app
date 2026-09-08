package com.menzo.Communication_Service.Email.Service;

import com.menzo.Communication_Service.Email.Dto.EmailRequest;
import com.menzo.Communication_Service.Email.Enum.EmailPurpose;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${email.context.app-name}")
    private String appName;

    @Value("${email.context.otp-validity-minutes}")
    private String otpValidityMinutes;

    @Value("${email.display-name.sign-in-otp}")
    private String signInOtpEmailDisplayName;

    private final SpringTemplateEngine templateEngine;

    public EmailService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }


    /*
     *
     *   Send OTP email
     *
     */
    public void sendSignInOtpEmail(EmailRequest emailRequest, String otp) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail, signInOtpEmailDisplayName);       //  displayName
        helper.setTo(emailRequest.getTo());
        helper.setSubject(emailRequest.getSubject());
        helper.setText(
                getSignInOtpEmailTemplate(otp, emailRequest.getUserName()),
                true        //  true -> HTML email
        );

        mailSender.send(message);
    }


    //  get sign-in OTP email template
    private String getSignInOtpEmailTemplate(String otp, String userName) {
        Context context = new Context();

        context.setVariable("appName", appName);
        context.setVariable("actionName", EmailPurpose.SIGN_IN.name().toLowerCase().replace('_', '-'));
        context.setVariable("userName", userName);
        context.setVariable("otpValidityMinutes", otpValidityMinutes);
        context.setVariable("otpCode", otp);
        context.setVariable("supportLink", "");
        context.setVariable("currentYear", LocalDate.now().getYear());

        return templateEngine.process("otp-email", context);
    }

}
