package com.yoti.app.oauthdemojwt.authentication;

import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomCrsfTokenRepository implements CsrfTokenRepository {

    public static final String XSRF_HEADER_NAME = "X-XSRF-TOKEN";
    public static final String XSRF_TOKEN_COOKIE_NAME = "XSRF-TOKEN";
    private static final String CSRF_QUERY_PARAM_NAME = "_csrf";

    private final CookieCsrfTokenRepository delegate = new CookieCsrfTokenRepository();

    public CustomCrsfTokenRepository(){
        delegate.setCookieHttpOnly(true);
        delegate.setHeaderName(XSRF_HEADER_NAME);
        delegate.setCookieName(XSRF_TOKEN_COOKIE_NAME);
        delegate.setParameterName(CSRF_QUERY_PARAM_NAME);
    }

    @Override
    public CsrfToken generateToken(final HttpServletRequest request) {
        return delegate.generateToken(request);
    }

    @Override
    public void saveToken(final CsrfToken token, final HttpServletRequest request, final HttpServletResponse response) {
        delegate.saveToken(token, request, response);
        response.setHeader(XSRF_HEADER_NAME, nullSafeTokenValue(token));
    }

    @Override
    public CsrfToken loadToken(final HttpServletRequest request) {
        return delegate.loadToken(request);
    }

    private String nullSafeTokenValue(final CsrfToken token) {
        return token == null ? "" : token.getToken();
    }
}
