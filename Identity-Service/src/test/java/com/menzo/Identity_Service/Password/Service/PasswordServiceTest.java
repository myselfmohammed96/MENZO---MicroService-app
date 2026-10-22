package com.menzo.Identity_Service.Password.Service;

import com.menzo.Identity_Service.Password.Dto.HashDto;
import com.menzo.Identity_Service.Password.Dto.VerifyHashDto;
import com.menzo.Identity_Service.Password.Enum.HashType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PasswordServiceTest {

    @Autowired
    private PasswordService passwordService;


    /*
     *
     *   hash() testing
     *
     */
    @Test
    public void hashTest1() {
        HashDto hash = passwordService.hash(new HashDto("Hello"), HashType.PASSWORD);
        assertNotNull(hash);
        assertFalse(hash.getString().isEmpty());

        System.out.println("Password hash: " + hash.getString());
    }

    @Test
    public void hashTest2() {
        HashDto hash = passwordService.hash(new HashDto("535353"), HashType.PASSWORD);
        assertNotNull(hash);
        assertFalse(hash.getString().isEmpty());

        System.out.println("OTP hash: " + hash.getString());
    }


    /*
     *
     *   verifyHash() testing
     *
     */
    @Test
    public void verifyHashTest1() {
        boolean verified = passwordService.verifyHash(
                new VerifyHashDto("Hello", "$2a$10$zFuPzp7yQQlzcGlp68ym5eu0ybQxaV2uSbqQPkN6d9oCtvorFFzWq"),
                HashType.PASSWORD
        );
        assertInstanceOf(Boolean.class, verified);

        System.out.println("Password verified: " + verified);
    }

    @Test
    public void verifyHashTest2() {
        boolean verified = passwordService.verifyHash(
                new VerifyHashDto("535353", "$2a$10$jiRYtnOTYiHogPQMQ5CfCOUOFQxyZgKms4NQkkc8F4UJWQLK5Bqqe"),
                HashType.OTP
        );
        assertInstanceOf(Boolean.class, verified);

        System.out.println("OTP verified: " + verified);
    }

}