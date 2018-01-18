package com.yoti.app.oauthdemojwt.authentication;

import com.yoti.app.oauthdemojwt.constants.ApiUrlConstants;
import com.yoti.app.oauthdemojwt.service.RetrieveDataFromTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

    private final String TOKEN_HEADER;

    @Autowired
    private final RetrieveDataFromTokenService retrieveDataFromTokenService;

    @Autowired
    private final UserDetailsService userDetailsService;

    @Override
    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        log.info("the path from the filter is {}", httpRequest.getServletPath());
        String url = ApiUrlConstants.AUTH_BASE_URL + ApiUrlConstants.LOGIN_ENDPOINT;
        if (!url.equals(httpRequest.getServletPath())) {
            String authToken = httpRequest.getHeaders(TOKEN_HEADER).nextElement();
            String username = retrieveDataFromTokenService.getUserNameFromToken(authToken);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (retrieveDataFromTokenService.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

        }
        chain.doFilter(req, res);
    }
}
