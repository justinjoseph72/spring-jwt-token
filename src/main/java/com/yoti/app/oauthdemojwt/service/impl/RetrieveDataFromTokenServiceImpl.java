package com.yoti.app.oauthdemojwt.service.impl;

import com.yoti.app.oauthdemojwt.service.RetrieveDataFromTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.Clock;
import java.util.Date;
import static com.yoti.app.oauthdemojwt.constants.JwtConstants.*;

@Service
@Slf4j
public class RetrieveDataFromTokenServiceImpl implements RetrieveDataFromTokenService{

    @Value("${com.yoti.token.secret}")
    private String jwtSecret;

    @Autowired
    private Clock clock;

    @Override
    public String getUserNameFromToken(final String token) {
        try {
            final Claims claims = getClaimsFromToken(token);
            return claims.get(SUB_CLAIM).toString();

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
            return createdDate;

        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }

    @Override
    public Date getExpirationDateFromToken(final String token) {
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
                    .setSigningKey(jwtSecret.getBytes(CHAR_SET))
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        } catch (UnsupportedEncodingException e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public Boolean validateToken(final String token, final UserDetails userDetails) {
        final String username = getUserNameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !(this.isTokenExpired(token)));
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = this.getExpirationDateFromToken(token);
        return expiration.before(this.getCurrentDate());
    }

    private Date getCurrentDate() {
        return Date.from(clock.instant().now());
    }
}
