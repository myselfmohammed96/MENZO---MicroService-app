package com.menzo.Identity_Service.Auth.Service;

import com.menzo.Identity_Service.Global.Config.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthCookieFactory {

    private final JwtProperties properties;

    public String issue(String jwt) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(properties.cookieName(), jwt)
                .httpOnly(true)
                .secure(properties.cookieSecure())
                .sameSite(properties.cookieSameSite())
                .path(properties.cookiePath())
                .maxAge(properties.ttl());
        if (properties.cookieDomain() != null && !properties.cookieDomain().isBlank()) {
            builder.domain(properties.cookieDomain());
        }
        return builder.build().toString();
    }
}
