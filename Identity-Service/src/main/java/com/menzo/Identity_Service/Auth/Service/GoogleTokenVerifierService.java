package com.menzo.Identity_Service.Auth.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.menzo.Identity_Service.Auth.Dto.GoogleProfile;
import com.menzo.Identity_Service.Global.Config.GoogleProperties;
import com.menzo.Identity_Service.Global.Exception.AuthException;
import com.menzo.Identity_Service.Global.Exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Slf4j
@Service
public class GoogleTokenVerifierService {
    private static final String INVALID_MESSAGE = "Google sign-in failed. Please try again.";

    private final GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    private final GoogleIdTokenVerifier verifier;

    public GoogleTokenVerifierService(GoogleProperties properties) {
        try {
            this.verifier = new GoogleIdTokenVerifier.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory)
                    .setAudience(List.of(properties.clientId()))
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalStateException("Could not initialise the Google token verifier", e);
        }
    }

    public GoogleProfile verify(String idTokenString) {
        GoogleIdToken token;
        try {
            token = GoogleIdToken.parse(jsonFactory, idTokenString);        // malformed input -> 401, not 503
        } catch (IOException | IllegalArgumentException ex) {
            log.warn("Malformed Google ID token received");
            throw new AuthException(ErrorCode.GOOGLE_TOKEN_INVALID, INVALID_MESSAGE);
        }

        try {
            if (!verifier.verify(token)) {
                log.warn("Google ID token rejected (signature / issuer / audience / expiry)");
                throw new AuthException(ErrorCode.GOOGLE_TOKEN_INVALID, INVALID_MESSAGE);
            }
        } catch (GeneralSecurityException | IOException ex) {               // could not fetch Google's keys: our side/outage
            log.error("Google token verification could not be completed", ex);
            throw new AuthException(ErrorCode.GOOGLE_VERIFICATION_UNAVAILABLE,
                    "Google sign-in is temporarily unavailable. Please try again shortly.");
        }

        GoogleIdToken.Payload p = token.getPayload();
        return new GoogleProfile(
                p.getSubject(),
                p.getEmail(),
                Boolean.TRUE.equals(p.getEmailVerified()),
                (String) p.get("given_name"),
                (String) p.get("family_name"),
                (String) p.get("picture"));
    }
}
