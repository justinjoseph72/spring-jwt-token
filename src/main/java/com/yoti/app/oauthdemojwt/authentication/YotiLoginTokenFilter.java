package com.yoti.app.oauthdemojwt.authentication;

import com.yoti.app.oauthdemojwt.service.GenerateTokenService;
import com.yoti.app.oauthdemojwt.service.UserFromYoti;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class YotiLoginTokenFilter extends AbstractAuthenticationProcessingFilter {

    private final GenerateTokenService generateTokenService;

    private final UserDetailsService userDetailsService;
    private final UserFromYoti userFromYoti;

    public YotiLoginTokenFilter(String urlPath, GenerateTokenService generateTokenService, UserDetailsService userDetailsService, UserFromYoti userFromYoti) {
        super(urlPath);
        this.generateTokenService = generateTokenService;
        this.userDetailsService = userDetailsService;
        this.userFromYoti = userFromYoti;
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("from the new filter");
        String token = getTokenFromRequest(request);
        log.info("the token from new filter is {}", token);
        String username = userFromYoti.getUserNameFromYotiToken(token);
        log.info("the user scanned is {}", username);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String jwtToken = generateTokenService.generateToken(userDetails);
        log.info("the jwt token is ", jwtToken);
        Cookie jwtCookie = new Cookie("api-token", jwtToken);
        response.addCookie(jwtCookie);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("john.doe", "jwtpass");
        return getAuthenticationManager().authenticate(authenticationToken);
    }

    private String getTokenFromRequest(final HttpServletRequest request) {
        return request.getParameter("token");
    }
}
