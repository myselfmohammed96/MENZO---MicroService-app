package com.menzo.Identity_Service.Authentication.Service;

import com.menzo.Identity_Service.Authentication.Dto.LoginCredentials;
import com.menzo.Identity_Service.JWT.Service.JwtService;
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
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;



    /*
    *
    *   User authentication (login)
    *   authentication, token generation, cookie building
    *
    */
    public Cookie loginUser(LoginCredentials loginCred) {
        try {
            logger.info("Authenticating user: {}", loginCred.getEmail());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginCred.getEmail(),
                            loginCred.getPassword()
                    )
            );
            if (!authentication.isAuthenticated()) {
                throw new BadCredentialsException("Invalid credentials");
            }
            String token = jwtService.generateToken(loginCred.getEmail());
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



    /*
    *
    *   Create JWT Cookie
    *   (HttpOnly)
    *
    */
    public Cookie createCookie(String token) {
        Cookie cookie = new Cookie("JWT", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(10 * 24 * 60 * 60);
        return cookie;
    }

}
