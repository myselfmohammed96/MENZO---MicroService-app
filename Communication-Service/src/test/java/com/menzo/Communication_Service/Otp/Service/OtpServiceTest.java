package com.menzo.Communication_Service.Otp.Service;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

//import com.menzo.Communication_Service.Email.Dto.HashDto;
//import com.menzo.Communication_Service.Global.Feign.AuthFeign;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@SpringBootTest
class OtpServiceTest {

    @Autowired
    private OtpService otpService;

    @Test
    void sendNewOtpTest1() {}

    @Test
    void resendOtpTest1() {}

    @Test
    void enforceSendLimitsTest1() {}

    @Test
    void secondsUntil() throws Exception {
        Method method = otpService.getClass()
                .getDeclaredMethod("secondsUntil", LocalDateTime.class, LocalDateTime.class);
        method.setAccessible(true);
        long resultSeconds = (long) method.invoke(otpService, LocalDateTime.now(), LocalDateTime.now().plusMinutes(3));

        System.out.println("Result seconds: " + resultSeconds);
        assertEquals(180L, resultSeconds);
    }

    @Test
    void generateOtpTest1() throws Exception {
        Method method = OtpService.class
                .getDeclaredMethod("generateOtp");
        method.setAccessible(true);
        String result = (String) method.invoke(otpService);

        System.out.println("Result: " + result);
        assertNotNull(result);
        assertTrue(result.matches("\\d{6}"));
    }

//    @Test
//    void hastOtpTest1() throws Exception {
//        when(authFeign.hashOtp(any(HashDto.class)))
//                .thenReturn(ResponseEntity.ok(new HashDto("hashed-value")));
//
//        String hash = otpService.hashOtp("123456");
//
//        assertFalse(hash.isEmpty());
//        assertEquals("hashed-value", hash);
//        System.out.println(hash);
//    }

    //  checks if the otpService exists and if otpService contains the authFeign injected.
    @Test
    void checkInjection() {
        assertNotNull(otpService);
        assertNotNull(ReflectionTestUtils.getField(otpService, "authFeign"));
    }

    //    @Test
//    void hastOtpTest1() throws Exception {
//        when(authFeign.hashOtp(any(HashDto.class)))
//                .thenReturn(ResponseEntity.ok(new HashDto("hashed-value")));
//
//        Method method = OtpService.class
//                .getDeclaredMethod("hashOtp", String.class);
//        method.setAccessible(true);
//        String result = (String) method.invoke(otpService, "123456");
//
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//        System.out.println(result);
//    }

}