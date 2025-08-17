package com.menzo.Identity_Service.Controller;

import com.menzo.Identity_Service.Dto.PasswordDto;
import com.menzo.Identity_Service.Dto.TokenDto;
import com.menzo.Identity_Service.Entity.Token;
import com.menzo.Identity_Service.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthRestController {

    @Autowired
    AuthService authService;

//    To encode user password - for new user registration

    @PostMapping("/encode-pwd")
    public ResponseEntity<PasswordDto> encodePassword(@RequestBody PasswordDto userPassword){
        PasswordDto encodedPassword = authService.encryptPassword(userPassword);
        return ResponseEntity.ok(encodedPassword);
    }

    @GetMapping("get-by-token")
    public ResponseEntity<TokenDto> getByToken(@RequestParam String token){
        TokenDto tokenDto = authService.getByToken(token);
        return ResponseEntity.ok(tokenDto);
    }

}
