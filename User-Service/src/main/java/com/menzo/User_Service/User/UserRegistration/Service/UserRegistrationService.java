package com.menzo.User_Service.User.UserRegistration.Service;

import com.menzo.User_Service.Exceptions.AuthFeignException;
import com.menzo.User_Service.Feign.AuthFeign;
import com.menzo.User_Service.User.Credentials.Dto.EmailDto;
import com.menzo.User_Service.User.Credentials.Service.CredentialsService;
import com.menzo.User_Service.User.UserProfile.Dto.UserDto;
import com.menzo.User_Service.User.UserProfile.Entity.User;
import com.menzo.User_Service.User.UserProfile.Enum.UserTypes;
import com.menzo.User_Service.User.UserProfile.Repository.UserRepository;
import com.menzo.User_Service.User.UserRegistration.Dto.OAuthUserDto;
import com.menzo.User_Service.User.UserRegistration.Dto.RegNewUser;
import com.menzo.User_Service.User.UserRegistration.Dto.TokenMinimalDto;
import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationService.class);

    @Autowired
    private AuthFeign authFeign;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CredentialsService credentialsService;



    /*
     *
     * New User Registration - Customer
     *
     */
    public Cookie registerNewUser(RegNewUser newUser) {
        UserDto savedUser = saveNewUser(newUser);
        TokenMinimalDto jwtToken = null;
        try {
            jwtToken = authFeign.generateToken(new EmailDto(savedUser.getEmail()));
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


    //  Save new Registered user
    private UserDto saveNewUser(RegNewUser newUser) {
        try {
            if (userRepo.existsByEmail(newUser.getEmail())) {
                throw new IllegalArgumentException("Email is already in use.");
            }
            logger.info("New user registration: {}", newUser.getEmail());

            String encodedPassword = credentialsService.encodePassword(newUser.getPassword());
            User user = new User(newUser.getFirstName(),
                    newUser.getLastName(),
                    newUser.getEmail(),
                    newUser.getPhoneNumber(),
                    newUser.getGender(),
                    UserTypes.CUSTOMER,
                    newUser.getDateOfBirth(),
                    encodedPassword
            );
            User savedUser = userRepo.save(user);
            if (savedUser.getUserId() == null) {
                throw new RuntimeException("Failed to save user");
            }
            logger.info("User registered: {}", user.getEmail());
            return new UserDto(savedUser);
        } catch (Exception e) {
            logger.error("Error saving user: {}", e.getMessage(), e);
            throw e;
        }
    }


    //  Create JWT cookie
    private Cookie createCookie(String token) {
        Cookie cookie = new Cookie("JWT", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(10 * 24 * 60 * 60);
        return cookie;
    }



    /*
    *
    *   Get Google OAuth user details
    *   For the user data got from the Google server
    *
    */
    public UserDto saveGoogleOAuthUser(OAuthUserDto googleUser) {
        if (googleUser == null || googleUser.getEmail() == null) {
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
                        newUser.setUserType(UserTypes.CUSTOMER);
                        User savedUser = userRepo.save(newUser);
                        logger.info("New Google OAuth user saved: {}", savedUser.getEmail());
                        return new UserDto(savedUser);
                    });
        } catch (DataAccessException e) {
            logger.error("Database error while saving/fetching Google OAuth user: {}", googleUser.getEmail(), e);
            throw new RuntimeException("Database error while processing OAuth login", e);
        } catch (Exception e) {
            logger.error("Unexpected error during Google OAuth user save: {}", googleUser.getEmail(), e);
            throw new RuntimeException("Unexpected error during OAuth login", e);
        }
    }
}
