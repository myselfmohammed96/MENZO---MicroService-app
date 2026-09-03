package com.menzo.Home_Service.UrlConfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.route.user-auth")
public record UserAuthRouteUrls(
        Page page,
        Action action
) {
    public record Page(
            String signIn,
            String login,
            String otpVerification,
            String forgotPassword,
            String resetPassword
    ) {}

    public record Action(
            String signInSubmit,
            String loginSubmit,
            String forgotPasswordSubmit,
            OtpSubmit otpSubmit,
            SocialSignIn socialSignIn
    ) {
        public record OtpSubmit(
                String userSignIn
        ) {}

        public record SocialSignIn(
                String google
        ) {}
    }
}
