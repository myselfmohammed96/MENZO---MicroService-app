package com.menzo.Communication_Service.Email.Service;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

//import com.menzo.Communication_Service.Email.Dto.EmailDto;
import com.menzo.Communication_Service.Global.Enum.Purpose;
//import com.menzo.Communication_Service.Global.Feign.UserFeign;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;

@SpringBootTest
class EmailServiceTest {

    @MockBean
    private JavaMailSender mailSender;

    @Autowired
    private EmailService emailService;

//    @MockBean
//    private UserFeign userFeign;


//    @Test
//    void sendOtpEmailTest1() {
//        when(userFeign.getUsername(any(EmailDto.class)))
//                .thenReturn(ResponseEntity.ok("Dominic Reyes"));
//        when(mailSender.createMimeMessage())
//                .thenReturn(new MimeMessage(
//                        (jakarta.mail.Session) null
//                ));
//
//        boolean result = emailService.sendOtpEmail(
//                "123456",
//                "dominic.reyes@example.com",
//                Purpose.USER_SIGN_IN
//        );
//
//        assertTrue(result);
//        verify(mailSender).send(any(MimeMessage.class));
//    }

//    @Test
//    void getOtpEmailTemplateTest1() throws Exception {
//        Method method = emailService.getClass().getDeclaredMethod(
//                "getOtpEmailTemplate",
//                String.class,
//                String.class,
//                Purpose.class
//        );
//        method.setAccessible(true);
//
//        String result = (String) method.invoke(
//                emailService, "123456", "Dominic Reyes", Purpose.USER_SIGN_IN);
//
//        assertNotNull(result);
//        assertTrue(result.contains("123456"));
//        assertTrue(result.contains("Dominic Reyes"));
//    }

//    @Test
//    void getUsernameTest1() throws Exception {
//        when(userFeign.getUsername(any(EmailDto.class)))
//                .thenReturn(ResponseEntity.ok("Dominic Reyes"));
//
//        Method method = emailService.getClass().getDeclaredMethod("getUsername", String.class);
//        method.setAccessible(true);
//        String result = (String) method.invoke(emailService, "dominic.reyes@example.com");
//
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//        System.out.println(result);
//    }

    //    @Test
//    public void testSendSignInOtpEmail() throws MessagingException, UnsupportedEncodingException {
//        boolean otpSent = emailService.sendSignInOtpEmail(
//                "myselfmohammed96@gmail.com",
//                String.valueOf(662345)
//        );
//        System.out.println("OTP sent: " + otpSent);
//    }

//    @Test
//    void sendOtpEmail1() {
//        boolean otpSent = emailService.sendOtpEmail(
//                "123456",
//                "dominic.reyes@example.com",
//                Purpose.USER_SIGN_IN
//        );
//        System.out.println("OTP sent: " + otpSent);
//    }
}