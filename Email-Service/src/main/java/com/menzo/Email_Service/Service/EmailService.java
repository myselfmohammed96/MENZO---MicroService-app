package com.menzo.Email_Service.Service;

import com.menzo.Email_Service.Config.MailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.Instant;
import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender gmailSender;

    @Autowired
    private MailProperties adminMailProperties;

//    private String lastOtp;
//    private Instant otpTimeStamp;

    public void sendOtp() {
        String otp = otpGenerator();

//        this.lastOtp = otp;
//        this.otpTimeStamp = Instant.now();

        mailSender(otp);
    }

    private void mailSender(String payLoad) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(adminMailProperties.getUsername());
        message.setTo("todo@gmail.com");
        message.setText("Your OTP is: " + payLoad);
        message.setSubject("OTP Verification,");

        gmailSender.send(message);
    }

    private String otpGenerator() {
        return String.format("%05d", new Random().nextInt(100000));
    }

}
