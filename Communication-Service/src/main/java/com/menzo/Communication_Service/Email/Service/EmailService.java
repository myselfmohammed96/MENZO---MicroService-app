package com.menzo.Communication_Service.Email.Service;

import com.menzo.Communication_Service.Global.Enum.Purpose;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDate;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${app.mail.from}")
    private String fromEmail;

    @Value("${app.mail.brand-name:MENZO}")
    private String appName;

    @Value("${app.mail.display-name.sign-in-otp}")
    private String signInOtpEmailDisplayName;

    @Value("${app.mail.subject.sign-in-otp}")
    private String signInOtpEmailSubject;

    private final JavaMailSender mailSender;

    private final SpringTemplateEngine templateEngine;


    /*
     *
     *   Send OTP email
     *
     */
    public void sendOtpEmail(String toEmail, String firstName, String otp, Purpose purpose, Duration validity) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, signInOtpEmailDisplayName);
            helper.setTo(toEmail);
            helper.setSubject(signInOtpEmailSubject);
            helper.setText(getOtpEmailTemplate(otp, firstName, purpose, validity), true);     //  true -> HTML email

            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error("Failed to send OTP email to {}", toEmail, e);
        }
    }

    //  get sign-in OTP email template
    private String getOtpEmailTemplate(String otp, String firstName, Purpose purpose, Duration validity) {
        Context context = new Context();

        context.setVariable("appName", appName);
        context.setVariable("actionName", purpose.getDisplayName().toLowerCase());
        context.setVariable("firstName", firstName);
        context.setVariable("otpValidityMinutes", validity.toMinutes());
        context.setVariable("otpCode", otp);
        context.setVariable("supportLink", "");
        context.setVariable("currentYear", LocalDate.now().getYear());

        return templateEngine.process("otp-email", context);
    }

}
