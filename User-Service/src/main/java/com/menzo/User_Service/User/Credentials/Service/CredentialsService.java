package com.menzo.User_Service.User.Credentials.Service;

import com.menzo.User_Service.Exceptions.PasswordMismatchException;
import com.menzo.User_Service.Feign.AuthFeign;
import com.menzo.User_Service.User.Credentials.Dto.ChangePasswordDto;
import com.menzo.User_Service.User.Credentials.Dto.VerifyPasswordDto;
import com.menzo.User_Service.User.Profile.Entity.User;
import com.menzo.User_Service.User.Profile.Repository.UserRepository;
import com.menzo.User_Service.User.UserRegistration.Service.UserRegistrationService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CredentialsService {

    private static final Logger logger = LoggerFactory.getLogger(CredentialsService.class);

    @Autowired
    private AuthFeign authFeign;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserRegistrationService userRegistrationService;



    /*
    *
    *   Update user password
    *
    */
    public boolean updatePassword(String userEmail,
                                  boolean passwordPresent,
                                  ChangePasswordDto passwordDto) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with Email: " + userEmail));
        try {
            User updatedUser;
            if (passwordPresent) {
                logger.info("Verifying current password to user: {}", userEmail);
                Boolean passwordMatches = authFeign.verifyPassword(new VerifyPasswordDto(passwordDto.getCurrentPassword(), user.getPassword()));
                if (passwordMatches) {
                    logger.info("Current password verification successful.");
                    updatedUser = changePassword(user, passwordDto.getNewPassword());
                } else {
                    logger.error("Current password mismatched.");
                    throw new PasswordMismatchException("Current password didn't match.");
                }
            } else {
                logger.info("Adding new password to user: {}", userEmail);
                updatedUser = changePassword(user, passwordDto.getNewPassword());
            }
            return updatedUser != null;
        } catch (PasswordMismatchException e) {
            throw new PasswordMismatchException(e.getMessage());
        } catch (Exception e) {
            logger.error("Password update failed.");
            throw new RuntimeException("Password update failed", e);
        }
    }


    // Change password
    private User changePassword(User user, String newPassword) {
        try {
            String encodedPassword = userRegistrationService.encodePassword(newPassword);
            user.setPassword(encodedPassword);
            return userRepo.save(user);
        } catch (Exception e) {
            logger.error("Password changing failed.");
            throw new RuntimeException("Password changing failed.", e);
        }
    }

}
