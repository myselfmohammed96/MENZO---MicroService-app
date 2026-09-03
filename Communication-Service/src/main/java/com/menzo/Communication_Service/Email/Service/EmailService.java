package com.menzo.Communication_Service.Email.Service;

import com.menzo.Communication_Service.Email.Config.MailProperties;
import com.menzo.Communication_Service.Email.Dto.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String fromEmail;

//    @Autowired
//    private MailProperties adminMailProperties;

//    private String lastOtp;
//    private Instant otpTimeStamp;


    public void send(EmailRequest emailRequest) throws MessagingException, UnsupportedEncodingException {

//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setTo(emailRequest.getTo());
//        msg.setSubject(emailRequest.getSubject());
//        msg.setText(emailRequest.getBody());
//        mailSender.send(msg);

        System.out.println("Start.. here...");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

//        System.out.println(fromEmail);
//        System.out.println(emailRequest.getFromName());
        //   display name needed...


        helper.setFrom(fromEmail, emailRequest.getFromName());
        helper.setTo(emailRequest.getTo());
        helper.setSubject(emailRequest.getSubject());
        helper.setText(emailRequest.getBody());

        mailSender.send(message);
        System.out.println("End.. here...");
    }

//    public void sendOtp() {
//        String otp = otpGenerator();
//
////        this.lastOtp = otp;
////        this.otpTimeStamp = Instant.now();
//
//        mailSender(otp);
//    }

//    private void mailSender(String payLoad) {
//        SimpleMailMessage message = new SimpleMailMessage();
//
//        message.setFrom(adminMailProperties.getUsername());
//        message.setTo("todo@gmail.com");
//        message.setText("Your OTP is: " + payLoad);
//        message.setSubject("OTP Verification,");
//
//        gmailSender.send(message);
//    }

//    private String otpGenerator() {
//        return String.format("%05d", new Random().nextInt(100000));
//    }

}
