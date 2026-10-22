package com.menzo.User_Service.Feign;

import com.menzo.User_Service.User.Profile.Credentials.Dto.HashDto;
import com.menzo.User_Service.User.Profile.Credentials.Dto.VerifyHashDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class AuthFeignTest {

    @Autowired
    private AuthFeign authFeign;


    @Test
    public void hashPasswordTest1() {
        ResponseEntity<HashDto> response = authFeign.hashPassword(new HashDto("Hello"));
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Password hash: " + response.getBody());
        }
    }

    @Test
    public void verifyPasswordTest1() {
        ResponseEntity<Boolean> response = authFeign.verifyPassword(new VerifyHashDto("Hello", "$2a$10$ap8GpnfArNuj/b37fPQUFu.axvsc/JWGRq5LeQniDZkI9Hy4s2jAS"));
        if (!response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Invalid response");
            return;
        }

        if (response.getBody() == null) {
            System.out.println("Null response");
        } else {
            System.out.println("Password match: " + response.getBody());
        }
    }

}