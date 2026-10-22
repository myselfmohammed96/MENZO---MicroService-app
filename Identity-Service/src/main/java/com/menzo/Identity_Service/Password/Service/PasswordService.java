package com.menzo.Identity_Service.Password.Service;

import com.menzo.Identity_Service.Password.Dto.HashDto;
import com.menzo.Identity_Service.Password.Dto.PasswordDto;
import com.menzo.Identity_Service.Password.Dto.VerifyHashDto;
import com.menzo.Identity_Service.Password.Dto.VerifyPasswordDto;
import com.menzo.Identity_Service.Password.Enum.HashType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;


    /*
     *
     *   Hashing String
     *
     */
    public HashDto hash(HashDto hashInput, HashType type) {
        if (hashInput == null || hashInput.getString() == null || hashInput.getString().isEmpty()) {
            throw new IllegalArgumentException("Hash input not available.");
        }
        try {
            logger.info(type.equals(HashType.PASSWORD) ? "Hashing password." : "Hashing otp.");
            String hashString = passwordEncoder.encode(hashInput.getString());
            return new HashDto(hashString);
        } catch (Exception e) {
            logger.error("Error while hashing {}", type.name(), e);
            throw new RuntimeException("Failed to hash " + type.name(), e);
        }
    }


    /*
     *
     *   Verify hash
     *
     */
    public boolean verifyHash(VerifyHashDto hashDto, HashType type) {
        try {
            return passwordEncoder.matches(hashDto.getCurrent(), hashDto.getStoredHash());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid " + type.name() + " hash verification.");
            throw new IllegalArgumentException(type.name() + " invalid: " + e);
        } catch (Exception e) {
            throw new RuntimeException(type.name() + " verification failed: " + e);
        }
    }

}




/*
 *
 *   Encrypt password
 *   with the Bcrypt encoder
 *
 */
//    public PasswordDto encryptPassword(PasswordDto password) {
//        if (password == null || password.getPassword() == null) {
//            throw new IllegalArgumentException("Password must not be null");
//        }
//        try {
//            logger.info("Encrypting password");
//            String encodedPasswordString = passwordEncoder.encode(password.getPassword());
//            return new PasswordDto(encodedPasswordString);
//        } catch (Exception e) {
//            logger.error("Error during password encryption", e);
//            throw new RuntimeException("Failed to encrypt password", e);
//        }
//    }


//    public boolean verifyPassword(VerifyPasswordDto passwordDto) {
//        try {
//            return passwordEncoder.matches(passwordDto.getCurrentPassword(), passwordDto.getPasswordInDB());
//        } catch (IllegalArgumentException e) {
//            logger.error("Verification password invalid");
//            throw new IllegalArgumentException("Password invalid: " + e);
//        } catch (Exception e) {
//            throw new RuntimeException("Password verification failed" + e);
//        }
//    }