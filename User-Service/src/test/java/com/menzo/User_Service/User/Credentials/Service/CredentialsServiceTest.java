package com.menzo.User_Service.User.Credentials.Service;

import com.menzo.User_Service.User.Profile.Credentials.Dto.EmailDto;
import com.menzo.User_Service.User.Profile.Credentials.Service.CredentialsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CredentialsServiceTest {

    @Autowired
    private CredentialsService credentialsService;


    @Test
    public void testCheckUserEmailExists1() {
        System.out.println("Email exists: " + credentialsService.checkUserEmailExists(new EmailDto("abc@email.com")));
    }

    @Test
    public void testCheckUserEmailExists2() {
        System.out.println("Email exists: " + credentialsService.checkUserEmailExists(new EmailDto("elias.wren@example.com")));
    }
}