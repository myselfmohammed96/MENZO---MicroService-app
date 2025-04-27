package com.menzo.Identity_Service.Config;

import com.menzo.Identity_Service.Dto.EmailDto;
import com.menzo.Identity_Service.Dto.User;
import com.menzo.Identity_Service.Feign.UserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserFeign authUserFeign;

    public CustomUserDetailsService(UserFeign authUserFeign){
        this.authUserFeign = authUserFeign;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail)throws UsernameNotFoundException {
        User credential = authUserFeign.getUserbyUserEmail(new EmailDto(userEmail));
        if (credential == null){
            throw new UsernameNotFoundException("User not found with Email: " + userEmail);
        }
        return new CustomUserDetails(credential);
    }
}
