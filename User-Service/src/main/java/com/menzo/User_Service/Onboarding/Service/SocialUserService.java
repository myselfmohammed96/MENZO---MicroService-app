package com.menzo.User_Service.Onboarding.Service;

import com.menzo.User_Service.Global.Exception.ErrorCode;
import com.menzo.User_Service.Global.Exception.BusinessException;
import com.menzo.User_Service.Global.Util.MaskingUtil;
import com.menzo.User_Service.User.Profile.Entity.User;
import com.menzo.User_Service.User.Profile.Enum.Gender;
import com.menzo.User_Service.User.Profile.Enum.UserTypes;
import com.menzo.User_Service.User.Profile.Repository.UserRepository;
import com.menzo.User_Service.Global.FeignClient.Dto.GoogleProfile;
import com.menzo.User_Service.Onboarding.Enum.UserRegistrationSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialUserService {

    private static final int NAME_MAX = 50;
    private static final int URL_MAX = 255;

    private final UserRepository userRepo;
    private final CustomerProvisioningService provisioningService;
    private final InputSanitizer sanitizer;

    public record Result(User user, boolean created) {
    }

    @Transactional
    public Result findOrCreateGoogleUser(GoogleProfile profile) {
        String email = sanitizer.normalizeEmail(profile.email());
        String masked = MaskingUtil.maskEmail(email);

        Optional<User> existing = userRepo.findByEmailIgnoreCase(email);
        if (existing.isPresent()) {
            User user = existing.get();
            if (!user.isActive()) {
                log.warn("Google login rejected, account disabled: {}", masked);
                throw new BusinessException(ErrorCode.ACCOUNT_DISABLED, "This account has been disabled. Please contact support.");
            }
            if (!user.isVerified()) {
                // Google proved ownership of the email, so the pending signup can be completed.
                // Drop any password set at signup: it may have been chosen by someone who never owned this email
                // (pre-registration account takeover). The real owner can set a new one via "forgot password".
                user.setVerified(true);
                user.setPassword(null);
                user = userRepo.save(user);
                log.info("Pending signup completed through Google for {}", masked);
            }
            provisioningService.provision(user);
            return new Result(user, false);
        }

        User user = new User();
        user.setFirstName(firstName(profile, email));
        user.setLastName(abbreviate(sanitizer.cleanText(profile.familyName()), NAME_MAX));
        user.setEmail(email);
        user.setGender(Gender.NOT_SPECIFIED);                          // TODO: add this constant (Google doesn't provide gender)
        user.setUserType(UserTypes.CUSTOMER);                          // TODO: match your enum constant
        user.setUserRegistrationSource(UserRegistrationSource.GOOGLE); // TODO: match your enum constant
        user.setProfileUrl(safePicture(profile.picture()));
        user.setActive(true);
        user.setVerified(true);                                        // Google already verified the email
        user = userRepo.saveAndFlush(user);
        provisioningService.provision(user);
        log.info("New user created via Google, userId={}, email={}", user.getUserId(), masked);
        return new Result(user, true);
    }

    private String firstName(GoogleProfile profile, String email) {
        String given = abbreviate(sanitizer.cleanText(profile.givenName()), NAME_MAX);
        return (given == null || given.isBlank()) ? abbreviate(email.substring(0, email.indexOf('@')), NAME_MAX) : given;
    }

    private String safePicture(String url) {
        return (url != null && url.startsWith("https://") && url.length() <= URL_MAX) ? url : null;
    }

    private String abbreviate(String value, int max) {
        return value == null || value.length() <= max ? value : value.substring(0, max);
    }
}
