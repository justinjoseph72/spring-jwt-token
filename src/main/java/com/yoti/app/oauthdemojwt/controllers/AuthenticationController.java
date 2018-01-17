package com.yoti.app.oauthdemojwt.controllers;

import com.google.common.base.Strings;
import com.yoti.app.oauthdemojwt.constants.ApiUrlConstants;
import com.yoti.app.oauthdemojwt.service.TokenService;
import com.yoti.app.oauthdemojwt.service.UserFromYoti;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrlConstants.AUTH_BASE_URL)
@Slf4j
public class AuthenticationController {

    @Value("${com.yoti.token-header-name}")
    private String TOKEN_HEADER;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserFromYoti userFromYoti;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserDetailsService userDetailsService;

    @GetMapping(ApiUrlConstants.LOGIN_ENDPOINT)
    public ResponseEntity authenticationUser(@RequestParam(ApiUrlConstants.TOKEN_REQ_PARAM) String token) {
        if (Strings.isNullOrEmpty(token)) {
            return ResponseEntity.badRequest().build();
        }
        String username = userFromYoti.getUserNameFromYotiToken(token);
        log.info("the user scanned is {}", username);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),"jwtpass"
        ));
        HttpHeaders headers = new HttpHeaders();
        headers.add(TOKEN_HEADER, tokenService.generateToken(userDetails));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity(headers, HttpStatus.OK);
    }
}
