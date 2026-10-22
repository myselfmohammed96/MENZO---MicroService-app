package com.menzo.User_Service.User.Profile.Repository;

import com.menzo.User_Service.User.Profile.Entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepo;


    /*
     *
     *  findByEmail(String email) tests
     *
     */
    @Test
    public void testFindByEmail1() {
        Optional<User> userOpt = userRepo.findByEmail("abc@email.com");
        if (userOpt.isPresent()) {
            System.out.println("User: " + userOpt.get());
        } else {
            System.out.println("No user found.");
        }
    }

    @Test
    public void testFindByEmail2() {
        Optional<User> userOpt = userRepo.findByEmail("elias.wren@example.com");
        if (userOpt.isPresent()) {
            System.out.println("User: " + userOpt.get());
        } else {
            System.out.println("No user found.");
        }
    }


    /*
     *
     *  existsByEmailAndIsVerifiedTrue(String email) tests
     *
     */
    @Test
    public void testExistsByEmailAndIsVerifiedTrue1() {
        System.out.println("Email exists: " + userRepo.existsByEmailAndIsVerifiedTrue("abc@email.com"));
    }

    @Test
    public void testExistsByEmailAndIsVerifiedTrue2() {
        System.out.println("Email exists: " + userRepo.existsByEmailAndIsVerifiedTrue("elias.wren@example.com"));
    }
}