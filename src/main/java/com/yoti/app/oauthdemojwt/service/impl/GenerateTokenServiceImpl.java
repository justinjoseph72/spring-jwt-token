package com.yoti.app.oauthdemojwt.service.impl;

import com.yoti.app.oauthdemojwt.service.GenerateTokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.yoti.app.oauthdemojwt.constants.JwtConstants.*;

@Service
@Slf4j
public class GenerateTokenServiceImpl implements GenerateTokenService {

    @Autowired
    private Clock clock;

    @Value("${com.yoti.token.secret}")
    private String jwtSecret;

    @Value("${com.yoti.token.expiration}")
    private Long expiration;


    @Override
    public String generateToken(final UserDetails userDetails) {
        if (userDetails == null) {
            throw new AccessDeniedException("Please login with Yoti to generate jwtToken");
        }
        Map<String, Object> claims = getClaimsForUserDetails(userDetails);
        String jwtToken = generateToken(claims);
        log.info("the genreated jwt jwtToken is {}", jwtToken);
        return jwtToken;
    }

    private Map<String, Object> getClaimsForUserDetails(final UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(SUB_CLAIM, userDetails.getUsername());
        claims.put(AUDIENCE_CLAIM, AUDIENCE);
        claims.put(CREATED_CLAIM, getCurrentDate());
        return claims;
    }


    private Date getCurrentDate() {
        return Date.from(clock.instant().now());
    }

    private String generateToken(final Map<String, Object> claims) {
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(getExpirationDate())
                    .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes(CHAR_SET))
                    .compact();
        } catch (UnsupportedEncodingException e) {
            log.warn(e.getMessage());
            return Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(getExpirationDate())
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();
        }
    }

    private Date getExpirationDate() {
        Instant instant = clock.instant().now();
        Instant expirationIntant = instant.plus(expiration, ChronoUnit.MINUTES);
        return Date.from(expirationIntant);
    }


}
