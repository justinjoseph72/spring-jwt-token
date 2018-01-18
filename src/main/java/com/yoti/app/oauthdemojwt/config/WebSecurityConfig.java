package com.yoti.app.oauthdemojwt.config;

import com.yoti.app.oauthdemojwt.authentication.AuthenticationTokenFilter;
import com.yoti.app.oauthdemojwt.authentication.EntryPointUnauthorizedHandler;
import com.yoti.app.oauthdemojwt.constants.ApiUrlConstants;
import com.yoti.app.oauthdemojwt.service.RetrieveDataFromTokenService;
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

    @Autowired
    private UserDetailsService userDetailsService;


    @Autowired
    private RetrieveDataFromTokenService retrieveDataFromTokenService;

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
                .and()
                .authorizeRequests()
                .antMatchers(ApiUrlConstants.AUTH_BASE_URL + ApiUrlConstants.LOGIN_ENDPOINT)
                .permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated();

        http.addFilterBefore(getFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    private Filter getFilter() throws Exception {
        AuthenticationTokenFilter filter = new AuthenticationTokenFilter(TOKEN_HEADER, retrieveDataFromTokenService, userDetailsService);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }


}
