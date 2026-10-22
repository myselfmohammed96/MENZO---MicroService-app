package com.menzo.User_Service.Onboarding.Controller;

//import com.menzo.User_Service.Global.Dto.Response.ApiResponse;
//import com.menzo.User_Service.Global.Dto.Response.CredentialsResponse;
import com.menzo.User_Service.Global.Exception.ErrorCode;
import com.menzo.User_Service.Global.Exception.BusinessException;
import com.menzo.User_Service.Onboarding.Dto.Response.ApiResponse;
import com.menzo.User_Service.Onboarding.Dto.Response.CredentialsResponse;
import com.menzo.User_Service.User.Profile.Entity.User;
import com.menzo.User_Service.User.Profile.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("internal/v1/users")
@RequiredArgsConstructor
public class InternalUserRestController {

    private final UserRepository userRepo;

    @GetMapping("/credentials")
    public ResponseEntity<ApiResponse<CredentialsResponse>> credentials(@RequestParam("email") String email) {
        if (email == null || email.isBlank() || email.length() > 100) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, "A valid email is required.");
        }
        User user = userRepo.findByEmailIgnoreCase(email.trim().toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "User not found."));
        CredentialsResponse data = new CredentialsResponse(
                String.valueOf(user.getUserId()), user.getEmail(), user.getPassword(),
                user.getUserType().name(), user.isActive(), user.isVerified(),
                user.getUserRegistrationSource().name());
        log.debug("Credentials served to identity-service for userId={}", user.getUserId());
        return ResponseEntity.ok(ApiResponse.success("CREDENTIALS_FOUND", "OK", data));
    }
}
