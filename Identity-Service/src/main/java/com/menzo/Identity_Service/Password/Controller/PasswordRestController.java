package com.menzo.Identity_Service.Password.Controller;

import com.menzo.Identity_Service.Password.Dto.PasswordDto;
import com.menzo.Identity_Service.Password.Dto.VerifyPasswordDto;
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
    *   Encode password
    *
    */
    @PostMapping("/encode-pwd")
    public ResponseEntity<PasswordDto> encodePassword(@RequestBody PasswordDto userPassword){
        PasswordDto encodedPassword = passwordService.encryptPassword(userPassword);
        return ResponseEntity.ok(encodedPassword);
    }



    /*
    *
    *   Verify password
    *
    */
    @PostMapping("verify-password")
    public ResponseEntity<Boolean> verifyPassword(@RequestBody VerifyPasswordDto passwordDto) {
        if (passwordDto == null || passwordDto.getCurrentPassword() == null || passwordDto.getCurrentPassword().isEmpty() ||
                passwordDto.getPasswordInDB() == null || passwordDto.getPasswordInDB().isEmpty()) {
            throw new IllegalArgumentException("Invalid passwordDto");
        }
        boolean matches = passwordService.verifyPassword(passwordDto);
        return ResponseEntity.ok(matches);
    }
}
