package com.menzo.User_Service.Onboarding.Service;

import com.menzo.User_Service.Global.Cache.DirectedTo;
import com.menzo.User_Service.Global.Cache.Entity.RedirectUrlTemp;
import com.menzo.User_Service.Global.Cache.Repository.RedirectUrlTempRepository;
import com.menzo.User_Service.Global.Config.SignupProperties;
import com.menzo.User_Service.Global.Util.MaskingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedirectUrlService {

    private final RedirectUrlTempRepository repo;
    private final InputSanitizer sanitizer;
    private final SignupProperties signupProperties;

    /** One pending redirect per email+purpose: a new signup attempt replaces the previous one. */
    @Transactional
    public void save(String email, String redirectUrl, DirectedTo directedTo) {
        repo.deleteByEmailAndDirectedTo(email, directedTo);   // bulk delete runs immediately
        repo.save(RedirectUrlTemp.builder()
                .userEmail(email)
                .redirectUrl(redirectUrl)
                .directedTo(directedTo)
                .build());
        log.info("Stored {} redirect for {}", directedTo, MaskingUtil.maskEmail(email));
    }

    @Transactional
    public String consume(String email, DirectedTo directedTo) {
        String result = signupProperties.defaultRedirectUrl();
        try {
            result = repo.findFirstByUserEmailAndDirectedToOrderByCreatedAtDesc(email, directedTo)
                    .map(RedirectUrlTemp::getRedirectUrl)
                    .filter(sanitizer::isAllowedRedirect)
                    .orElse(result);
            repo.deleteByEmailAndDirectedTo(email, directedTo);
        } catch (RuntimeException ex) {
            log.warn("Could not read/clear stored redirect for {}, using default", MaskingUtil.maskEmail(email), ex);
        }
        return result;
    }
}
