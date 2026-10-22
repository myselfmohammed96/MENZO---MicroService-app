package com.menzo.Identity_Service.Password.Controller;

import static org.junit.jupiter.api.Assertions.*;

import com.menzo.Identity_Service.Password.Dto.HashDto;
import com.menzo.Identity_Service.Password.Dto.VerifyHashDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class PasswordRestControllerTest {

    @Autowired
    private PasswordRestController passwordRestController;


    @Test
    public void hashMethodsTest1() {
        ResponseEntity<?> passwordHash = passwordRestController.hashPassword(new HashDto("Hello"));
        ResponseEntity<?> otpHash = passwordRestController.hashOtp(new HashDto("535353"));

        if (passwordHash.hasBody()) {
            HashDto resultHash = (HashDto) passwordHash.getBody();
            assertNotNull(resultHash);
            assertFalse(resultHash.getString().isEmpty());

            System.out.println("Password hash: " + resultHash);
        }

        if (otpHash.hasBody()) {
            HashDto resultHash = (HashDto) otpHash.getBody();
            assertNotNull(resultHash);
            assertFalse(resultHash.getString().isEmpty());

            System.out.println("OTP hash: " + resultHash);
        }
    }

    @Test
    public void verifyHashMethodsTest1() {
        ResponseEntity<?> passwordMatches = passwordRestController.verifyPassword(new VerifyHashDto("Hello", "$2a$10$TKO5yuTjnm0vQFQNSqwXO.eQBVKvfkfqIMQVgbB.9g4N06xZqSP9C"));
        ResponseEntity<?> otpMatches = passwordRestController.verifyOtp(new VerifyHashDto("535353", "$2a$10$n9I3GTUjiDcCf7SoLzTqaeA7VRWYAlT.7WwZU0MVl8oF1ifTQQ3/G"));

        if (passwordMatches.hasBody()) {
            Boolean result = (Boolean) passwordMatches.getBody();
            assertNotNull(result);
            assertInstanceOf(Boolean.class, result);

            System.out.println("Password matches: " + result);
        }

        if (otpMatches.hasBody()) {
            Boolean result = (Boolean) otpMatches.getBody();
            assertNotNull(result);
            assertInstanceOf(Boolean.class, result);

            System.out.println("OTP matches: " + result);
        }
    }

}