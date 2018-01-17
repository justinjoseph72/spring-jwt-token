package com.yoti.app.oauthdemojwt.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class YotiAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    private static final String TOKEN_PARAM_NAME = "token";

    public YotiAuthenticationFilter(final String authenicationEntryPoint,AuthenticationManager authenticationManager){
        super(authenicationEntryPoint);
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String token  = obtainToken(request);
        log.info("the token is {}",token);
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("john.doe","jwtpass");
        return getAuthenticationManager().authenticate(authRequest);
    }

    private String obtainToken(final HttpServletRequest request) {
        //FIXME: input sanitise the param (e.g. char matcher whitelist and length).
        return request.getParameter(TOKEN_PARAM_NAME);
    }
}
