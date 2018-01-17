package com.yoti.app.oauthdemojwt.service.impl;

import com.yoti.app.oauthdemojwt.service.TokenService;
import io.jsonwebtoken.Claims;
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

@Service
@Slf4j
public class TokenServiceImpl implements TokenService {

    private static String SUB_CLAIM = "sub";
    private static String AUDIENCE_CLAIM = "audience";
    private static String CREATED_CLAIM = "created";
    private static String AUDIENCE = "client";

    @Autowired
    private Clock clock;

    @Value("${com.yoti.token.secret}")
    private String jwtSecret;

    @Value("${com.yoti.token.expiration}")
    private Long expiration;


    @Override
    public String getUserNameFromToken(final String token) {
        try {
            final Claims claims = getClaimsFromToken(token);
            return claims.getSubject();

        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }


    @Override
    public Date getCreatedDateFromToken(final String token) {
        try {
            final Claims claims = getClaimsFromToken(token);
            Date createdDate = (Date) claims.get(CREATED_CLAIM);
            return claims.getExpiration();

        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }

    @Override
    public Date getExpirateionDataFromToken(final String token) {
        try {
            final Claims claims = getClaimsFromToken(token);
            return claims.getExpiration();

        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }

    private Claims getClaimsFromToken(final String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes("UTF-8"))
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        } catch (UnsupportedEncodingException e) {
            log.info(e.getMessage());
            return null;
        }
    }

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

    @Override
    public Boolean validateToken(final String token, final UserDetails userDetails) {
        final String username = getUserNameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !(this.isTokenExpired(token)));
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = this.getExpirateionDataFromToken(token);
        return expiration.before(this.getCurrentDate());
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
                    .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes("UTF-8"))
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
        Instant expirationIntant = instant.plus(expiration, ChronoUnit.SECONDS);
        return Date.from(expirationIntant);
    }


}
