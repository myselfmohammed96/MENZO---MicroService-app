package com.menzo.Communication_Service.Email.Service;

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
        boolean otpSent = emailService.sendSignInOtpEmail(
                "myselfmohammed96@gmail.com",
                String.valueOf(662345)
        );
        System.out.println("OTP sent: " + otpSent);
    }
}