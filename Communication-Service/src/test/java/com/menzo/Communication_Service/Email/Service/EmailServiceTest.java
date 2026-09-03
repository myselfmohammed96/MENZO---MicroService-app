package com.menzo.Communication_Service.Email.Service;

import com.menzo.Communication_Service.Email.Controller.EmailRestController;
import com.menzo.Communication_Service.Email.Dto.EmailRequest;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void testSend() throws MessagingException, UnsupportedEncodingException {
        EmailRequest emailRequest = EmailRequest.builder()
                .to("myselfmohammed96@gmail.com")
                .fromName("LinkedIn Job Alerts bro..")
                .subject("Hi...")
                .body("Hello...")
                .build();
        emailService.send(emailRequest);
    }
}