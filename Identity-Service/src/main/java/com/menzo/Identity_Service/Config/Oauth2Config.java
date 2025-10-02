//package com.menzo.Identity_Service.Config;
//
//import com.menzo.Identity_Service.Service.JwtService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//@Configuration
//public class Oauth2Config {
//
//    @Bean
//    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService(JwtService jwtService) {
//        return userRequest -> {
//            OAuth2User oauthUser = new DefaultOAuth2UserService().loadUser(userRequest);
//
//            String email = oauthUser.getAttribute("email");
//            String name = oauthUser.getAttribute("name");
//
////            User user = userRepo.findByEmail(email).orElseGet(() -> {
////                User newUser = new User();
////                newUser.setEmail(email);
////                newUser.setName(name);
////                newUser.setAuthProvider("GOOGLE");UserRepository userRepo,
////                return userRepo.save(newUser);
////            });
////
////            String jwt = jwtService.generateToken(user);
////            jwtService.addTokenToCookie(jwt);
//            System.out.println(email + " " + name);
//
//            return oauthUser;
//        };
//    }
//}
