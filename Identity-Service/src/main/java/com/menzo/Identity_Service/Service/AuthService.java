package com.menzo.Identity_Service.Service;

import com.menzo.Identity_Service.Dto.*;
import com.menzo.Identity_Service.Entity.Token;
import com.menzo.Identity_Service.Feign.UserFeign;
import com.menzo.Identity_Service.Repository.TokenRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenRepository tokenRepository;

    //    Encrypt password with the Bcrypt encoder
    public PasswordDto encryptPassword(PasswordDto password) {
        if (password == null || password.getPassword() == null) {
            throw new IllegalArgumentException("Password must not be null");
        }
        try {
            logger.info("Encrypting password");
            String encodedPasswordString = passwordEncoder.encode(password.getPassword());
            return new PasswordDto(encodedPasswordString);
        } catch (Exception e) {
            logger.error("Error during password encryption", e);
            throw new RuntimeException("Failed to encrypt password", e);
        }
    }

    public boolean verifyPassword(VerifyPasswordDto passwordDto) {
        try {
            return passwordEncoder.matches(passwordDto.getCurrentPassword(), passwordDto.getPasswordInDB());
        } catch (IllegalArgumentException e) {
            logger.error("Verification password invalid");
            throw new IllegalArgumentException("Password invalid" + e);
        } catch (Exception e) {
            throw new RuntimeException("Password verification failed" + e);
        }
    }

    //    logging in user - (authentication, token generation, cookie building & return the cookies)
    public Cookie loginUser(LoginCredentials loginCred) {
        try {
            logger.info("Authenticating user: {}", loginCred.getEmail());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginCred.getEmail(), loginCred.getPassword())
            );
            if (!authentication.isAuthenticated()) {
                throw new BadCredentialsException("Invalid credentials");
            }
            String token = generateToken(loginCred.getEmail());
            logger.info("User authenticated successfully: {}", loginCred.getEmail());

            return createCookie(token);

        } catch (DisabledException e) {
            logger.warn("Blocked user tried logging in: {}", loginCred.getEmail());
            throw new RuntimeException(e.getMessage());
        } catch (AuthenticationException e) {
            logger.warn("Authentication failed for: {}", loginCred.getEmail());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during login", e);
            throw new RuntimeException("Login failed", e);
        }
    }

    //    generate new Token for the given userEmail, after revoking the previous tokens
    public String generateToken(String userEmail) {
        logger.info("Generating token for user: {}", userEmail);
        User user = userFeign.getUserbyUserEmail(new EmailDto(userEmail));
        System.out.println("generate Token method:");
        user.display(); //*** check ***
        String token = jwtService.generateToken(userEmail);

        boolean oldTokensRevoked = revokeAllTokensByUser(user);
        if (oldTokensRevoked) {
            tokenRepository.save(new Token(token, false, user.getId()));
        }

        logger.info("Token generated and saved for user ID: {}", user.getId());
        return token;
    }

    //    Revoking the previous tokens
    private boolean revokeAllTokensByUser(User user) {
        List<Token> tokenList = tokenRepository.findAllTokensByUser(user.getId());

        if (tokenList.isEmpty()) {
            logger.info("No active tokens found for user ID: {}", user.getId());
            return true;
        }
        tokenList.stream()
                .filter(t -> !t.isLoggedOut())
                .forEach(t -> t.setLoggedOut(true));
        tokenRepository.saveAll(tokenList);

        logger.info("Revoked {} tokens for user ID: {}", tokenList.size(), user.getId());
        return true;
    }

    //    Creates HttpOnly Secure Cookie
    public Cookie createCookie(String token) {
        Cookie cookie = new Cookie("JWT", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(10 * 24 * 60 * 60);
        return cookie;
    }

    //    Get token entity by token
    public TokenDto getByToken(String token) {
        logger.info("Fetching token entity");
        Token tokenInDB = tokenRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Token not found: " + token));
        UserStatusDto userDto = userFeign.getUserByUserId(tokenInDB.getUserId());
        return new TokenDto(
                tokenInDB.getTokenId(),
                tokenInDB.getToken(),
                tokenInDB.isLoggedOut(),
                tokenInDB.getUserId(),
                userDto.isActive()
        );
    }

}

