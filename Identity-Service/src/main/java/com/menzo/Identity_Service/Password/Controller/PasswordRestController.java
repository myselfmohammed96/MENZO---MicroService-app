package com.menzo.Identity_Service.Password.Controller;

import com.menzo.Identity_Service.Password.Dto.HashDto;
import com.menzo.Identity_Service.Password.Dto.VerifyHashDto;
import com.menzo.Identity_Service.Password.Enum.HashType;
import com.menzo.Identity_Service.Password.Service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class PasswordRestController {

    @Autowired
    private PasswordService passwordService;


    /*
     *
     *   Hash password
     *
     */
    @PostMapping("/encode-pwd")
    public ResponseEntity<HashDto> hashPassword(@RequestBody HashDto password) {
        if (password == null || password.getString() == null || password.getString().isEmpty()) {
            throw new IllegalArgumentException("Invalid password on hashing");
        }
        HashDto passwordHash = passwordService.hash(password, HashType.PASSWORD);
        return ResponseEntity.ok(passwordHash);
    }


    /*
     *
     *   Hash OTP
     *   Used in Communication-service (to hash generated OTP)
     *
     */
    @PostMapping("/hash-otp")
    public ResponseEntity<HashDto> hashOtp(@RequestBody HashDto otp) {
        if (otp == null || otp.getString() == null || otp.getString().isEmpty()) {
            throw new IllegalArgumentException("Invalid OTP on hashing");
        }
        HashDto otpHash = passwordService.hash(otp, HashType.OTP);
        return ResponseEntity.ok(otpHash);
    }


    /*
     *
     *    Verify password hash
     *
     */
    @PostMapping("/verify-pwd")
    public ResponseEntity<Boolean> verifyPassword(@RequestBody VerifyHashDto passwords) {
        if (passwords == null || passwords.getCurrent() == null || passwords.getStoredHash() == null) {
            throw new IllegalArgumentException("Invalid password verification data.");
        }

        boolean matches = passwordService.verifyHash(passwords, HashType.PASSWORD);
        return ResponseEntity.ok(matches);
    }


    /*
     *
     *    Verify password hash
     *
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<Boolean> verifyOtp(@RequestBody VerifyHashDto otps) {
        if (otps == null || otps.getCurrent() == null || otps.getStoredHash() == null) {
            throw new IllegalArgumentException("Invalid OTP verification data.");
        }

        boolean matches = passwordService.verifyHash(otps, HashType.OTP);
        return ResponseEntity.ok(matches);
    }




//    ===========================
    /*
     *
     *   Verify password - DEP
     *
     */
//    @PostMapping("verify-password")
//    public ResponseEntity<Boolean> verifyPassword(@RequestBody VerifyPasswordDto passwordDto) {
//        if (passwordDto == null || passwordDto.getCurrentPassword() == null || passwordDto.getCurrentPassword().isEmpty() ||
//                passwordDto.getPasswordInDB() == null || passwordDto.getPasswordInDB().isEmpty()) {
//            throw new IllegalArgumentException("Invalid passwordDto");
//        }
////        boolean matches = passwordService.verifyPassword(passwordDto);
//        boolean matches = passwordService.verifyHash(
//                new VerifyHashDto(passwordDto.getCurrentPassword(), passwordDto.getPasswordInDB()),
//                HashType.PASSWORD
//        );
//        return ResponseEntity.ok(matches);
//    }


}
