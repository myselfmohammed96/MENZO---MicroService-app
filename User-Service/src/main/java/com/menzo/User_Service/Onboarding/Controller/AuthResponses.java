package com.menzo.User_Service.Onboarding.Controller;

//import com.menzo.User_Service.Global.Dto.Response.ApiResponse;
//import com.menzo.User_Service.Global.Dto.Response.AuthResponse;
//import com.menzo.User_Service.Global.Dto.Response.AuthResult;
import com.menzo.User_Service.Onboarding.Dto.Response.ApiResponse;
import com.menzo.User_Service.Onboarding.Dto.Response.AuthResponse;
import com.menzo.User_Service.Onboarding.Dto.Response.AuthResult;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

final class AuthResponses {

    private AuthResponses() {}

    static ResponseEntity<ApiResponse<AuthResponse>> ok(AuthResult result, String code, String message) {
        HttpHeaders headers = new HttpHeaders();
        result.setCookies().forEach(cookie -> headers.add(HttpHeaders.SET_COOKIE, cookie));
        headers.add(HttpHeaders.CACHE_CONTROL, "no-store");     //  No caching auth response
        return ResponseEntity.ok().headers(headers).body(ApiResponse.success(code, message, result.body()));
    }
}
