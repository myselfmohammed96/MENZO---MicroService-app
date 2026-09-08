package com.menzo.Communication_Service.Email.Service;

import com.menzo.Communication_Service.Email.Dto.EmailRequest;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void testSendSignInOtpEmail() throws MessagingException, UnsupportedEncodingException {
        EmailRequest request = new EmailRequest();

        request.setTo("myselfmohammed96@gmail.com");
        request.setSubject("Sign-in Verification Code");
        request.setUserName("Mohammed");

        emailService.sendSignInOtpEmail(request, String.valueOf(662345));
    }
}