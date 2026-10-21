package com.menzo.Communication_Service.Otp.Service;

import com.menzo.Communication_Service.Global.Config.OtpProperties;
import com.menzo.Communication_Service.Global.Enum.Purpose;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HexFormat;

@Component
public class OtpUtil {

    private final SecureRandom secureRandom = new SecureRandom();

    private static final String ALGORITHM = "HmacSHA256";

    private final SecretKeySpec key;


    public OtpUtil(OtpProperties properties) {
        this.key = new SecretKeySpec(properties.hmacSecret().getBytes(StandardCharsets.UTF_8), ALGORITHM);
    }

    //  random 6-digit OTP generator
    public String generateOtp() {
        return String.format("%06d", secureRandom.nextInt(1_000_000));
    }

    public String hashOtp(String email, Purpose purpose, String otp) {
        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(key);
            byte[] digest = mac.doFinal((email + ":" + purpose.name() + ":" + otp).getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("HMAC-SHA256 unavailable", e);
        }
    }

    public boolean otpMatches(String email, Purpose purpose, String otp, String expectedHash) {
        return MessageDigest.isEqual(
                hashOtp(email, purpose, otp).getBytes(StandardCharsets.UTF_8),
                expectedHash.getBytes(StandardCharsets.UTF_8)
        );
    }

}
