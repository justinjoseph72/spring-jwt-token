package com.yoti.app.oauthdemojwt.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class YotiAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        log.info("In the provider");
        return authentication;
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
