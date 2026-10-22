package com.menzo.User_Service.Onboarding.Controller;

//import com.menzo.User_Service.Global.Dto.GoogleLoginRequest;
//import com.menzo.User_Service.Global.Dto.LoginRequest;
//import com.menzo.User_Service.Global.Dto.Response.ApiResponse;
//import com.menzo.User_Service.Global.Dto.Response.AuthResponse;
//import com.menzo.User_Service.Global.Dto.Response.AuthResult;
import com.menzo.User_Service.Onboarding.Dto.GoogleLoginRequest;
import com.menzo.User_Service.Onboarding.Dto.LoginRequest;
import com.menzo.User_Service.Onboarding.Dto.Response.ApiResponse;
import com.menzo.User_Service.Onboarding.Dto.Response.AuthResponse;
import com.menzo.User_Service.Onboarding.Dto.Response.AuthResult;
import com.menzo.User_Service.Onboarding.Service.UserAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserAuthRestController {
    private final UserAuthService authService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResult result = authService.login(request);
        return AuthResponses.ok(result, "LOGIN_SUCCESS", "Logged in successfully.");
    }

    @PostMapping(value = "/google-login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<AuthResponse>> googleLogin(@Valid @RequestBody GoogleLoginRequest request) {
        AuthResult result = authService.googleLogin(request);
        return result.body().newUser()
                ? AuthResponses.ok(result, "GOOGLE_SIGNUP_SUCCESS", "Account created with Google. Welcome aboard!")
                : AuthResponses.ok(result, "GOOGLE_LOGIN_SUCCESS", "Logged in successfully.");
    }
}
