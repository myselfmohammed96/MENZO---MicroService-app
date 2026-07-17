package com.menzo.Identity_Service.JWT.Controller;

import com.menzo.Identity_Service.Dto.EmailDto;
import com.menzo.Identity_Service.JWT.Dto.TokenDto;
import com.menzo.Identity_Service.JWT.Dto.TokenMinimalDto;
import com.menzo.Identity_Service.JWT.Service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class JwtRestController {

    @Autowired
    private JwtService jwtService;



    /*
     *
     *  Get JWT object by JWT token
     *
     */
    @GetMapping("get-by-token")
    public ResponseEntity<TokenDto> getByToken(@RequestParam String token){
        TokenDto tokenDto = jwtService.getByToken(token);
        return ResponseEntity.ok(tokenDto);
    }



    /*
     *
     *  Generate JWT for user email
     *
     */
    @PostMapping("/get-token")
    public ResponseEntity<TokenMinimalDto> getToken(@RequestBody EmailDto userEmail) {
        if(userEmail == null || userEmail.getEmail().isEmpty()) {
            throw new IllegalArgumentException("User email empty");
        }
        String token = jwtService.generateToken(userEmail.getEmail());

        return ResponseEntity.ok(new TokenMinimalDto(token));
    }
}
