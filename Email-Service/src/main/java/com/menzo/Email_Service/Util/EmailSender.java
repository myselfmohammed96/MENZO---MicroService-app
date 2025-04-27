package com.menzo.Email_Service.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendOtpEmail(String toEmail, String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP code");
        message.setText("Your OTP is: " + otp);
        javaMailSender.send(message);
    }
}
