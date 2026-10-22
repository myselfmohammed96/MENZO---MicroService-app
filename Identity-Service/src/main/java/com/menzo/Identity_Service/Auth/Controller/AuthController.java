package com.menzo.Identity_Service.Auth.Controller;

import com.menzo.Identity_Service.Auth.Dto.*;
import com.menzo.Identity_Service.Auth.Service.GoogleTokenVerifierService;
import com.menzo.Identity_Service.Auth.Service.IdentityAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private static final String JSON = MediaType.APPLICATION_JSON_VALUE;

    private final IdentityAuthService authService;
    private final GoogleTokenVerifierService googleVerifier;

    @PostMapping(value = "/login", consumes = JSON, produces = JSON)
    public ResponseEntity<ApiResponse<IdentityAuthData>> login(@Valid @RequestBody IdentityLoginRequest request) {
        return session(authService.login(request), "AUTHENTICATED", "Authenticated successfully.");
    }

    @PostMapping(value = "/token", consumes = JSON, produces = JSON)
    public ResponseEntity<ApiResponse<IdentityAuthData>> token(@Valid @RequestBody IssueTokenRequest request) {
        return session(authService.issueToken(request), "TOKEN_ISSUED", "Session issued successfully.");
    }

    @PostMapping(value = "/google/verify", consumes = JSON, produces = JSON)
    public ResponseEntity<ApiResponse<GoogleProfile>> verifyGoogle(@Valid @RequestBody GoogleVerifyRequest request) {
        GoogleProfile profile = googleVerifier.verify(request.idToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .body(ApiResponse.success("GOOGLE_TOKEN_VERIFIED", "Google token verified.", profile));
    }

    private ResponseEntity<ApiResponse<IdentityAuthData>> session(IdentityAuthService.IssuedSession session, String code, String message) {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, session.setCookie())
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .body(ApiResponse.success(code, message, session.data()));
    }
}


//package com.menzo.Identity_Service.Controller;
//
//import com.menzo.Identity_Service.Auth.Dto.Authentication.LoginCredentials;
//import com.menzo.Identity_Service.Service.AuthService;
//import com.menzo.Identity_Service.OAuth.Service.OAuthService;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    @Autowired
//    AuthService authService;
//
//    @Autowired
//    OAuthService oAuthService;
//
//
//
//    //    End point for submitting the login form
//    @PostMapping("/login")
//    public String loginUser(@ModelAttribute LoginCredentials loginCred, HttpServletResponse response) throws Exception{
//        System.out.println(loginCred.getEmail() + " #.# " + loginCred.getPassword());
//        Cookie cookie = authService.loginUser(loginCred);
//        response.addCookie(cookie);
//        String redirectUrl = UriComponentsBuilder
//                .fromUriString("http://localhost:8080")
//                .pathSegment("index")
//                .toUriString();
//        return "redirect:" + redirectUrl;
//    }
//}
