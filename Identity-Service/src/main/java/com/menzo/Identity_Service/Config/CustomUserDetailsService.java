package com.menzo.Identity_Service.Config;

import com.menzo.Identity_Service.Dto.EmailDto;
import com.menzo.Identity_Service.Dto.User;
import com.menzo.Identity_Service.Feign.UserFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserFeign authUserFeign;

    public CustomUserDetailsService(UserFeign authUserFeign){
        this.authUserFeign = authUserFeign;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        logger.info("Fetching user credentials for email: {}", userEmail);
        User credential = authUserFeign.getUserbyUserEmail(new EmailDto(userEmail));
        credential.display();   // *** check ***
        if (credential == null){
            throw new UsernameNotFoundException("User not found with Email: " + userEmail);
        }
        if (!credential.isActive()) {
            throw new DisabledException("You've been blocked. Your account is inactive.");
        }
        logger.info("Returning UserDetails object.");
        return new CustomUserDetails(credential);
    }
}
