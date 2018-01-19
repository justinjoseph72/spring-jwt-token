package com.yoti.app.oauthdemojwt.config;

import com.yoti.app.oauthdemojwt.authentication.AuthenticationTokenFilter;
import com.yoti.app.oauthdemojwt.authentication.ConnectionsLogoutHadler;
import com.yoti.app.oauthdemojwt.authentication.EntryPointUnauthorizedHandler;
import com.yoti.app.oauthdemojwt.authentication.YotiLoginTokenFilter;
import com.yoti.app.oauthdemojwt.constants.ApiUrlConstants;
import com.yoti.app.oauthdemojwt.service.GenerateTokenService;
import com.yoti.app.oauthdemojwt.service.RetrieveDataFromTokenService;
import com.yoti.app.oauthdemojwt.service.UserFromYoti;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Value("${security.signing-key}")
    private String signingKey;

    @Value("${com.yoti.token-header-name}")
    private String TOKEN_HEADER;

    @Value("${security.encoding-strength}")
    private Integer encodingStrength;

    @Value("${security.security-realm}")
    private String securityRealm;

    private static final String LOGOUT = "/logout";

    @Autowired
    private UserDetailsService userDetailsService;


    @Autowired
    private RetrieveDataFromTokenService retrieveDataFromTokenService;

    @Autowired
    GenerateTokenService generateTokenService;

    @Autowired
    UserFromYoti userFromYoti;

    @Autowired
    private EntryPointUnauthorizedHandler unauthorizedHandler;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new ShaPasswordEncoder((encodingStrength)));
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable()
                .formLogin().disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().logout().clearAuthentication(true).invalidateHttpSession(true).logoutUrl(LOGOUT)
                .logoutSuccessHandler(logoutSuccessHandler()).permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated();


        http.addFilterBefore(getApiFilter(), UsernamePasswordAuthenticationFilter.class);

        //added this filter to capture the yoti token and redirect to another site
        // we will not need to use the AuthenticationController in this case
        http.addFilterBefore(getAuthFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    private LogoutSuccessHandler logoutSuccessHandler() {
        return new ConnectionsLogoutHadler();
    }

    private Filter getApiFilter() throws Exception {
        AuthenticationTokenFilter filter = new AuthenticationTokenFilter(TOKEN_HEADER, retrieveDataFromTokenService, userDetailsService);
        filter.setAuthenticationManager(authenticationManager());
        filter.setFilterProcessesUrl(ApiUrlConstants.API_BASE_URL);
        return filter;
    }

    private Filter getAuthFilter() throws Exception {
        YotiLoginTokenFilter filter = new YotiLoginTokenFilter(ApiUrlConstants.AUTH_BASE_URL + ApiUrlConstants.LOGIN_ENDPOINT,
                generateTokenService, userDetailsService, userFromYoti);
        filter.setAuthenticationSuccessHandler(successHandler());
        filter.setAuthenticationFailureHandler(failureHandler());
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    private AuthenticationFailureHandler failureHandler() {
        final SimpleUrlAuthenticationFailureHandler handler = new SimpleUrlAuthenticationFailureHandler();
        handler.setDefaultFailureUrl("http://localhost:8082/ui/fail");
        return handler;
    }

    private AuthenticationSuccessHandler successHandler() {
        final SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
        handler.setAlwaysUseDefaultTargetUrl(true);
        handler.setDefaultTargetUrl("http://localhost:8082/ui/secure-page");
        return handler;
    }


}
