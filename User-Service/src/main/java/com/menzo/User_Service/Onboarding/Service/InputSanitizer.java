package com.menzo.User_Service.Onboarding.Service;

import com.menzo.User_Service.Global.Config.SignupProperties;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InputSanitizer {

    private static final Pattern ZERO_WIDTH = Pattern.compile("[\\u200B-\\u200D\\u2060\\uFEFF]");
    private static final Pattern CONTROL = Pattern.compile("\\p{Cntrl}");
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private static final Pattern SAFE_PATH = Pattern.compile("^/[A-Za-z0-9\\-._~/?&=%#]*$");

    private final SignupProperties signupProperties;

    public String cleanText(String value) {
        if (value == null) {
            return null;
        }
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFKC);
        normalized = ZERO_WIDTH.matcher(normalized).replaceAll("");
        normalized = CONTROL.matcher(normalized).replaceAll(" ");
        return WHITESPACE.matcher(normalized).replaceAll(" ").strip();
    }

    public String normalizeEmail(String email) {
        return cleanText(email).toLowerCase(Locale.ROOT);
    }

    public String normalizePhone(String phone) {
        String digits = phone.replaceAll("\\D", "");
        return digits.substring(digits.length() - 10);
    }

    public boolean isAllowedRedirect(String url) {
        if (url == null || url.length() > 2048) {
            return false;
        }
        if (url.startsWith("/")) {
            return !url.startsWith("//") && SAFE_PATH.matcher(url).matches();
        }
        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            boolean httpScheme = "https".equalsIgnoreCase(scheme) || "http".equalsIgnoreCase(scheme);
            return httpScheme
                    && uri.getUserInfo() == null
                    && uri.getHost() != null
                    && signupProperties.allowedRedirectHosts().contains(uri.getHost().toLowerCase(Locale.ROOT));
        } catch (URISyntaxException e) {
            return false;
        }
    }

}
