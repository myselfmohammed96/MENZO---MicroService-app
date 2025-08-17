package com.menzo.User_Service.Service;

import com.menzo.User_Service.Dto.*;
import com.menzo.User_Service.Entity.User;
import com.menzo.User_Service.Enums.Gender;
import com.menzo.User_Service.Enums.Roles;
import com.menzo.User_Service.Feign.AuthFeign;
import com.menzo.User_Service.Repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AuthFeign authFeign;

    //    Saving new Registered user
    public UserDto saveNewUser(RegNewUser newUser){
        try {
            logger.info("Registering new user with email: {}", newUser.getEmail());
            if (userRepo.existsByEmail(newUser.getEmail())) {
                throw new IllegalArgumentException("Email is already in use.");
            }

            String encodedPassword = encodePassword(newUser.getPassword());
            User user = new User(newUser.getFirstName(), newUser.getLastName(),
                    newUser.getPhoneNumber(), newUser.getEmail(),
                    newUser.getDateOfBirth(), newUser.getGender(), encodedPassword);
            user.setRoles(Roles.USER);
            user.setActive(true);

            User savedUser = userRepo.save(user);
            logger.info("User saved successfully: {}", user.getEmail());

            return new UserDto(savedUser);
        } catch (Exception e) {
            logger.error("Error saving user: {}", e.getMessage(), e);
            throw e;
        }
    }

    private String encodePassword(String userPassword){
        try {
            logger.debug("Encoding password");
            PasswordDto encodedPasswordDto = authFeign.encodePassword(new PasswordDto(userPassword));
            return encodedPasswordDto.getPassword();
        } catch (Exception e) {
            logger.error("Password encoding failed: {}", e.getMessage(), e);
            throw new RuntimeException("Password encoding failed", e);
        }
    }

    public User updateUserActiveStatus(UpdateUserActiveStatusDto statusDto) {
        try {
            User user = userRepo.findById(statusDto.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + statusDto.getUserId()));
            if (user.isActive() != statusDto.isBlock()) {
                throw new RuntimeException("Invalid Input, both user activeStatus & block request should be same.");
            }
            user.setActive(user.isActive() && statusDto.isBlock() ? false : true);
            return userRepo.save(user);
        } catch (Exception e) {
            logger.error("Error updating user active status: {}", e.getMessage(), e);
            throw new RuntimeException("Error updating user activeStatus", e);
        }

    }

//    @PostConstruct
//    public void saveNewUsers() {
//        for(int i=0; i<50; i++) {
//            RegNewUser newUser = new RegNewUser("user ", String.valueOf(i+1), String.valueOf(123456+i),
//                    LocalDate.of(2000, 1, 1), "user"+i+1+"@gmail.com", Gender.MALE,
//                    "user"+String.valueOf(i+1)+"@gmail.com", "user"+String.valueOf(i+1)+"@gmail.com", null);
//            saveNewUser(newUser);
//        }
//    }

}
