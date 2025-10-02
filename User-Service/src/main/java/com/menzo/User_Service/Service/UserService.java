package com.menzo.User_Service.Service;

import com.menzo.User_Service.Dto.*;
import com.menzo.User_Service.Entity.User;
import com.menzo.User_Service.Enums.Roles;
import com.menzo.User_Service.Exceptions.AuthFeignException;
import com.menzo.User_Service.Feign.AuthFeign;
import com.menzo.User_Service.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AuthFeign authFeign;

    //  Register new user
    public Cookie registerNewUser(RegNewUser newUser) {
        UserDto savedUser = saveNewUser(newUser);
        TokenMinimalDto jwtToken = null;
        try {
            jwtToken = authFeign.getToken(new EmailDto(newUser.getEmail()));
        } catch (AuthFeignException ex) {
            logger.error("Feign error while JWT token: status = {}, message = {}", ex.getStatus(), ex.getMessage());
            throw new RuntimeException("Identity service failed while creating JWT token", ex);
        }
        if (jwtToken == null || jwtToken.getToken() == null) {
            logger.error("JWT token is null");
            throw new RuntimeException("JWT token is null");
        }
        return createCookie(jwtToken.getToken());
    }

    //    Saving new Registered user
    private UserDto saveNewUser(RegNewUser newUser){
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
            if (savedUser == null || savedUser.getId() == null) {
                throw new RuntimeException("Failed to save user");
            }
            logger.info("User saved successfully: {}", user.getEmail());
            return new UserDto(savedUser);
        } catch (Exception e) {
            logger.error("Error saving user: {}", e.getMessage(), e);
            throw e;
        }
    }

    private String encodePassword(String userPassword){
        try {
            logger.info("Encoding password");
            PasswordDto encodedPasswordDto = null;
            try {
                encodedPasswordDto = authFeign.encodePassword(new PasswordDto(userPassword));
            } catch (AuthFeignException ex) {
                logger.error("Feign error while encoding password: status = {}, message = {}", ex.getStatus(), ex.getMessage());
                throw new RuntimeException("Identity service failed to encode password", ex);
            }
            if (encodedPasswordDto == null || encodedPasswordDto.getPassword() == null) {
                logger.error("Encoded password is null");
                throw new RuntimeException("Encoded password is null");
            }
            return encodedPasswordDto.getPassword();
        } catch (Exception e) {
            logger.error("Password encoding failed: {}", e.getMessage(), e);
            throw new RuntimeException("Password encoding failed", e);
        }
    }

    public Cookie createCookie(String token) {
        Cookie cookie = new Cookie("JWT", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(10 * 24 * 60 * 60);
        return cookie;
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

    public Long updateUserDetails(String userEmail, UserMinimalDto latestUser) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with Email: " + userEmail));
        user.setFirstName(latestUser.getFirstName() != null && !latestUser.getFirstName().isEmpty() ? latestUser.getFirstName() : user.getFirstName());
        user.setLastName(latestUser.getLastName() != null && !latestUser.getLastName().isEmpty() ? latestUser.getLastName() : user.getLastName());
        user.setPhoneNumber(latestUser.getPhoneNumber() != null && !latestUser.getPhoneNumber().isEmpty() ? latestUser.getPhoneNumber() : user.getPhoneNumber());

        User updatedUser = userRepo.save(user);
        if(latestUser.getFirstName().equals(updatedUser.getFirstName()) ||
                latestUser.getLastName().equals(updatedUser.getLastName()) ||
                latestUser.getPhoneNumber().equals(updatedUser.getPassword())) {
            return updatedUser.getId();
        } else {
            return null;
        }
    }

    public boolean isUserEmailExists(EmailDto emailDto) {
        boolean emailExists = userRepo.existsByEmail(emailDto.getEmail());
        return emailExists;
    }

//    ********* Google OAuth *********

    public UserDto saveGoogleOAuthUser(OAuthUserDto googleUser) {
        if(googleUser == null || googleUser.getEmail() == null) {
            logger.error("Google OAuth user or email is null");
            throw new IllegalArgumentException("Invalid Google OAuth user data");
        }
        try {
            return userRepo.findByEmail(googleUser.getEmail())
                    .map(user -> {
                        logger.info("User already exists with email: {}", googleUser.getEmail());
                        return new UserDto(user);
                    })
                    .orElseGet(() -> {
                        User newUser = new User(googleUser);
                        newUser.setRoles(Roles.USER);
                        newUser.setActive(true);
                        User savedUser = userRepo.save(newUser);
                        logger.info("New Google OAuth user saved: {}", savedUser.getEmail());
                        return new UserDto(savedUser);
                    });
        } catch (DataAccessException e) {
            logger.error("Database error while saving/fetching Google OAuth user: {}", googleUser.getEmail(), e);
            throw new RuntimeException("Database error while processing OAuth login", e);
        }
        catch (Exception e) {
            logger.error("Unexpected error during Google OAuth user save: {}", googleUser.getEmail(), e);
            throw new RuntimeException("Unexpected error during OAuth login", e);
        }

//        boolean existsInUserRepo = userRepo.existsByEmail(googleUser.getEmail());
//
//        if (existsInUserRepo) {
//            User user = userRepo.findByEmail(googleUser.getEmail())
//                    .orElse(null);
//            return new UserDto(user);
//        } else {
//            User savedUser = userRepo.save(new User(googleUser));
//            return new UserDto(savedUser);
//        }
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
