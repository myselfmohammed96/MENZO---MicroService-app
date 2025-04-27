//package com.menzo.Identity_Service.Config;
//
//import com.menzo.Identity_Service.Entity.Token;
//import com.menzo.Identity_Service.Repository.TokenRepository;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.logout.LogoutHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Optional;
//
//@Component
//public class CustomLogoutHandler implements LogoutHandler {
//
//    @Autowired
//    TokenRepository tokenRepo;
//
//    @Override
//    public void logout(HttpServletRequest request,
//                       HttpServletResponse response,
//                       Authentication authentication){
//        Cookie[] cookies = request.getCookies();
//        for(Cookie cookie : cookies){
//            if ("JWT".equals(cookie.getName())){
//                String token = cookie.getValue();
//                Optional<Token> tokenInDB = tokenRepo.findByToken(token);
//
//                Token savedToken = tokenInDB.get();
//                if(savedToken != null){
//                    savedToken.setLoggedOut(true);
//                    tokenRepo.save(savedToken);
//                }
//
//                Cookie jwtCookie = new Cookie("JWT", null);
//                jwtCookie.setHttpOnly(true);
//                jwtCookie.setSecure(false);
//                jwtCookie.setPath("/");
//                jwtCookie.setMaxAge(0);
//                response.addCookie(jwtCookie);
//
//                break;
//            }
//        }
////        try{
////            response.sendRedirect("http://localhost:8080/");
////        } catch (IOException e){
////            e.printStackTrace();
////        }
//    }
//
//}
