package com.menzo.Identity_Service.Global.Security;

import com.menzo.Identity_Service.Auth.Dto.CredentialsResponse;
import com.menzo.Identity_Service.Auth.Service.CredentialsGateway;
import com.menzo.Identity_Service.Global.Exception.SocialLoginOnlyException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CredentialsUserDetailsService implements UserDetailsService {

    private final CredentialsGateway credentialsGateway;

    /** Unknown user -> UsernameNotFoundException (Spring turns it into a generic BadCredentials). */
    @Override
    public UserDetails loadUserByUsername(String email) {
        CredentialsResponse c = credentialsGateway.find(email)
                .orElseThrow(() -> new UsernameNotFoundException("No such user"));
        if (c.passwordHash() == null || c.passwordHash().isBlank()) {
            throw new SocialLoginOnlyException();
        }
        return toAuthUser(c);
    }

    public static AuthUser toAuthUser(CredentialsResponse c) {
        return new AuthUser(c.userId(), c.email(), c.passwordHash(), c.role(), c.active(), c.verified());
    }
}
