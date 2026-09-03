package com.menzo.Home_Service.Controller;

import com.menzo.Home_Service.Dto.LoginCredentials;
import com.menzo.Home_Service.Dto.User;
import com.menzo.Home_Service.Enum.Gender;
import com.menzo.Home_Service.UrlConfig.StoreFrontRouteUrls;
import com.menzo.Home_Service.UrlConfig.UserAuthApiUrls;
import com.menzo.Home_Service.UrlConfig.UserAuthRouteUrls;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    private final UserAuthRouteUrls userAuthRouteUrls;

    private final UserAuthApiUrls userAuthApiUrls;

    private final StoreFrontRouteUrls storeFrontRouteUrls;

    public AuthController(UserAuthRouteUrls userAuthRouteUrls,
                          UserAuthApiUrls userAuthApiUrls,
                          StoreFrontRouteUrls storeFrontRouteUrls) {
        this.userAuthRouteUrls = userAuthRouteUrls;
        this.userAuthApiUrls = userAuthApiUrls;
        this.storeFrontRouteUrls = storeFrontRouteUrls;
    }


    /*
     *
     *   Get sign-in page
     *   ## Check if the redirect url is present in the url configs or not..
     *
     */
    @GetMapping("/sign-in")
    public String getSignInForm(@RequestParam(value = "redirectUrl", required = false) String redirectUrl,
                                Model model) {
        Map<String, String> pageUrls = new HashMap<>();

        if (redirectUrl != null && redirectUrl.startsWith("/") && !redirectUrl.startsWith("//")) {
            pageUrls.put("redirectUrl", redirectUrl);
        } else {
            pageUrls.put("redirectUrl", storeFrontRouteUrls.page().index());
        }

        pageUrls.put("indexPage", storeFrontRouteUrls.page().index());
        pageUrls.put("loginPage", userAuthRouteUrls.page().login());
        pageUrls.put("otpVerificationPage", userAuthRouteUrls.page().otpVerification());
        pageUrls.put("signInFormSubmit", userAuthRouteUrls.action().signInSubmit());
        pageUrls.put("googleSignIn", userAuthRouteUrls.action().socialSignIn().google());
        pageUrls.put("checkEmailExistence", userAuthApiUrls.checkEmailExistence());

        model.addAttribute("pageUrls", pageUrls);
        model.addAttribute("genders", Gender.values());
        model.addAttribute("user", new User());

        return "auth-template/sign-in.html";
    }


    /*
     *
     *   Get login page
     *
     */
    @GetMapping("/login")
    public String getLoginForm(@RequestParam(value = "redirectUrl", required = false) String redirectUrl,
                               Model model) {
        Map<String, String> pageUrls = new HashMap<>();

        if (redirectUrl != null && redirectUrl.startsWith("/") && !redirectUrl.startsWith("//")) {
            pageUrls.put("redirectUrl", redirectUrl);
        } else {
            pageUrls.put("redirectUrl", storeFrontRouteUrls.page().index());
        }

        pageUrls.put("indexPage", storeFrontRouteUrls.page().index());
        pageUrls.put("signInPage", userAuthRouteUrls.page().signIn());
        pageUrls.put("forgotPasswordPage", userAuthRouteUrls.page().forgotPassword());
        pageUrls.put("loginFormSubmit", userAuthRouteUrls.action().loginSubmit());
        pageUrls.put("googleSignIn", userAuthRouteUrls.action().socialSignIn().google());

        model.addAttribute("pageUrls", pageUrls);
        model.addAttribute("loginCred", new LoginCredentials());

        return "auth-template/log-in.html";
    }


    /*
     *
     *   Get otp verification page
     *
     */
    @GetMapping("/otp-verification")
    public String getOtpVerificationPage(@RequestParam(value = "redirectUrl", required = false) String redirectUrl,
                                         Model model) {
        Map<String, String> pageUrls = new HashMap<>();

        if (redirectUrl != null && redirectUrl.startsWith("/") && !redirectUrl.startsWith("//")) {
            pageUrls.put("redirectUrl", redirectUrl);
        } else {
            pageUrls.put("redirectUrl", storeFrontRouteUrls.page().index());
        }

        pageUrls.put("indexPage", storeFrontRouteUrls.page().index());
        pageUrls.put("otpSubmit", userAuthRouteUrls.action().otpSubmit().userSignIn());

        model.addAttribute("pageUrls", pageUrls);

        return "auth-template/otp-verification.html";
    }


    /*
     *
     *   Get forgot password page
     *
     */
    @GetMapping("/forgot-password")
    public String getForgotPasswordPage(@RequestParam(value = "redirectUrl", required = false) String redirectUrl,
                                        Model model) {
        Map<String, String> pageUrls = new HashMap<>();

        if (redirectUrl != null && redirectUrl.startsWith("/") && !redirectUrl.startsWith("//")) {
            pageUrls.put("redirectUrl", redirectUrl);
        } else {
            pageUrls.put("redirectUrl", storeFrontRouteUrls.page().index());
        }

        pageUrls.put("indexPage", storeFrontRouteUrls.page().index());
        pageUrls.put("signInPage", userAuthRouteUrls.page().signIn());
        pageUrls.put("forgotPasswordSubmit", userAuthRouteUrls.action().forgotPasswordSubmit());

        model.addAttribute("pageUrls", pageUrls);

        return "auth-template/forgot-password.html";
    }


    /*
     *
     *   Get reset password page
     *
     */
    @GetMapping("/reset-password")
    public String getResetPasswordPage() {
        return "auth-template/reset-password.html";
    }

}
