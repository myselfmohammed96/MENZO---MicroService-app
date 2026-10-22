package com.menzo.User_Service.User.Profile.Credentials.Service;

import com.menzo.User_Service.Global.Exception.ExceptionsDep.AuthFeignException;
import com.menzo.User_Service.Global.Exception.ExceptionsDep.PasswordMismatchException;
//import com.menzo.User_Service.User.Credentials.Dto.*;
import com.menzo.User_Service.User.Profile.Credentials.Dto.ChangePasswordDto;
import com.menzo.User_Service.User.Profile.Credentials.Dto.EmailDto;
import com.menzo.User_Service.User.Profile.Credentials.Dto.HashDto;
import com.menzo.User_Service.User.Profile.Credentials.Dto.VerifyHashDto;
import com.menzo.User_Service.User.Profile.Entity.User;
import com.menzo.User_Service.User.Profile.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CredentialsService {

    private static final Logger logger = LoggerFactory.getLogger(CredentialsService.class);

//    @Autowired
//    private AuthFeign authFeign;

    @Autowired
    private UserRepository userRepo;


    /*
     *
     *   User email existence check
     *   (verified user)
     *
     */
//    public boolean checkUserEmailExists(EmailDto emailDto) {
//        return userRepo.existsByEmailAndIsVerifiedTrue(emailDto.getEmail());
//    }


    /*
     *
     *   Encode password
     *
     */
//    public String encodePassword(String userPassword) {
//        try {
//            HashDto encodedPassword = null;
//            logger.info("Password encoded");
//            try {
//                ResponseEntity<HashDto> response = authFeign.hashPassword(new HashDto(userPassword));
//                if (response.getStatusCode().is2xxSuccessful()) {
//                    encodedPassword = response.getBody();
//                } else {
//                    throw new RuntimeException("Password hashing failed.");
//                }
//            } catch (AuthFeignException ex) {
//                logger.error("Feign error while encoding password: status = {}, message = {}", ex.getStatus(), ex.getMessage());
//                throw new RuntimeException("Identity service failed to encode password", ex);
//            }
//            if (encodedPassword == null || encodedPassword.getString() == null) {
//                logger.error("Encoded password is null");
//                throw new RuntimeException("Encoded password is null");
//            }
//            return encodedPassword.getString();
//        } catch (Exception e) {
//            logger.error("Password encoding failed: {}", e.getMessage(), e);
//            throw new RuntimeException("Password encoding failed", e);
//        }
//    }


    /*
     *
     *   Update user password
     *
     */
//    public boolean updatePassword(String userEmail,
//                                  boolean passwordPresent,
//                                  ChangePasswordDto passwordDto) {
//        User user = userRepo.findByEmail(userEmail)
//                .orElseThrow(() -> new EntityNotFoundException("User not found with Email: " + userEmail));
//        try {
//            User updatedUser;
//            if (passwordPresent) {
//                logger.info("Verifying current password to user: {}", userEmail);
////                Boolean passwordMatches = authFeign.verifyPassword(new VerifyPasswordDto(passwordDto.getCurrentPassword(), user.getPassword()));
//                ResponseEntity<Boolean> response = authFeign.verifyPassword(new VerifyHashDto(passwordDto.getCurrentPassword(), user.getPassword()));
//                if (response.getStatusCode().is2xxSuccessful() && Boolean.TRUE.equals(response.getBody())) {
//                    logger.info("Current password verification successful.");
//                    updatedUser = changePassword(user, passwordDto.getNewPassword());
//                } else {
//                    logger.error("Current password mismatched.");
//                    throw new PasswordMismatchException("Current password didn't match.");
//                }
//            } else {
//                logger.info("Adding new password to user: {}", userEmail);
//                updatedUser = changePassword(user, passwordDto.getNewPassword());
//            }
//            return updatedUser != null;
//        } catch (PasswordMismatchException e) {
//            throw new PasswordMismatchException(e.getMessage());
//        } catch (Exception e) {
//            logger.error("Password update failed.");
//            throw new RuntimeException("Password update failed", e);
//        }
//    }


    // Change password
//    private User changePassword(User user, String newPassword) {
//        try {
//            String encodedPassword = encodePassword(newPassword);
//            user.setPassword(encodedPassword);
//            return userRepo.save(user);
//        } catch (Exception e) {
//            logger.error("Password changing failed.");
//            throw new RuntimeException("Password changing failed.", e);
//        }
//    }

}
