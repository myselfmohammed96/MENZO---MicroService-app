package com.menzo.User_Service.Onboarding.Service;

import com.menzo.User_Service.Global.Exception.ErrorCode;
import com.menzo.User_Service.Global.Exception.BusinessException;
import com.menzo.User_Service.Global.Util.MaskingUtil;
import com.menzo.User_Service.User.Profile.Entity.User;
import com.menzo.User_Service.User.Profile.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserActivationService {

    private final UserRepository userRepo;
    private final CustomerProvisioningService provisioningService;

    /** isVerified=true and Customer/Cart/Wishlist in ONE transaction: a user is never "verified but without a cart". */
    @Transactional
    public User activate(String email) {
        User user = userRepo.findByEmailIgnoreCase(email).orElseThrow(() ->
                new BusinessException(ErrorCode.USER_NOT_FOUND, "No signup found for this email. Please register first."));
        if (!user.isVerified()) {
            user.setVerified(true);
            user = userRepo.save(user);
        }
        provisioningService.provision(user);
        log.info("User activated, userId={}, email={}", user.getUserId(), MaskingUtil.maskEmail(email));
        return user;
    }
}
