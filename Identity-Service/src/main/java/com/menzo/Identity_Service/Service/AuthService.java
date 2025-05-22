package com.menzo.Identity_Service.Service;

import com.menzo.Identity_Service.Dto.*;
import com.menzo.Identity_Service.Entity.Token;
import com.menzo.Identity_Service.Feign.UserFeign;
import com.menzo.Identity_Service.Repository.TokenRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    UserFeign userFeign;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    TokenRepository tokenRepository;

//    Encoding password with the Bcrypt encoder

    public String encryptPassword(PasswordDto password) {
        System.out.println("Password from the AuthSErvice layer: " + password.getPassword());
        if (password == null || password.getPassword() == null) {
            throw new IllegalArgumentException("Password must not be null");
        }
        return passwordEncoder.encode(password.getPassword());
    }

//    logging in user - by authentication, token generation, cookie building & returning the cookies

    public Cookie loginUser(LoginCredentials loginCred) {     // HttpServletResponse response
        Authentication authentication = authenticationManager.authenticate(new
                UsernamePasswordAuthenticationToken(loginCred.getEmail(), loginCred.getPassword()));
        if (authentication.isAuthenticated()){
//            User user = userFeign.getUserbyUserEmail(new EmailDto(loginCred.getEmail()));
            String token = generateToken(loginCred.getEmail());
            Cookie cookie = createCookie(token);
            return cookie;
        } else{
            throw new RuntimeException("Invalid access");
        }
    }

//    generate new Token for the given userEmail, after revoking the previous tokens

    public String generateToken(String userEmail){
        User user = userFeign.getUserbyUserEmail(new EmailDto(userEmail));
        String generatedToken = jwtService.generateToken(userEmail);
        boolean oldTokensRevoked = revokeAllTokensByUser(user);
        if (oldTokensRevoked){
            Token token = new Token(generatedToken, false, user.getId());
            tokenRepository.save(token);
        }
        return generatedToken;
    }

//    Revoking the previous tokens

    private boolean revokeAllTokensByUser(User user){
        List<Token> tokenList = tokenRepository.findAllTokensByUser(user.getId());
//        if (tokenList != null && !tokenList.isEmpty()){
        tokenList.forEach(t -> {t.setLoggedOut(true);});
        tokenRepository.saveAll(tokenList);
        return true;
//        }
    }

//    Creates HttpOnly Secure Cookie

    private Cookie createCookie(String token){
        Cookie cookie = new Cookie("JWT", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(30 * 24 * 60 * 60);
        return cookie;
    }

    public Token getByToken(String token) {
        Token tokenInDB = tokenRepository.findByToken(token).orElseThrow(() ->
                new EntityNotFoundException("TokenEntity not found with the token: " + token));
        if (tokenInDB == null){
            throw new RuntimeException("Token entity not found.!");
        }
        return tokenInDB;
    }
}

