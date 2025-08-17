package com.menzo.Identity_Service.Config;

import com.menzo.Identity_Service.Entity.Token;
import com.menzo.Identity_Service.Repository.TokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomLogoutHandler implements LogoutHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomLogoutHandler.class);

    @Autowired
    TokenRepository tokenRepo;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication){
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            logger.warn("No cookies found in logout request");
            return;
        }
        for(Cookie cookie : cookies){
            if ("JWT".equals(cookie.getName())){
                String token = cookie.getValue();

                tokenRepo.findByToken(token).ifPresent(savedToken -> {
                    try {
                        savedToken.setLoggedOut(true);
                        tokenRepo.save(savedToken);
                        logger.info("Token marked as logged out: {}", token);
                    } catch (Exception e) {
                        logger.error("Failed to mark token as logged out", e);
                    }
                });

                Cookie clearedJwtCookie = new Cookie("JWT", null);
                clearedJwtCookie.setHttpOnly(true);
                clearedJwtCookie.setSecure(false);
                clearedJwtCookie.setPath("/");
                clearedJwtCookie.setMaxAge(0);
                response.addCookie(clearedJwtCookie);

                logger.info("Cookie created with JWT - null.");
                break;
            }
        }

        try{
            response.sendRedirect("http://localhost:8080/index");
        } catch (IOException e){
            e.printStackTrace();
            logger.error("Logout redirect failed", e);
            throw new RuntimeException("Logout redirect failed.", e);
        }
    }

}
