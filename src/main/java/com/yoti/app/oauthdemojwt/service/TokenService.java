package com.yoti.app.oauthdemojwt.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface TokenService {

     String getUserNameFromToken(final String token);
     Date getCreatedDateFromToken(final String token);
     Date getExpirationDateFromToken(final String token);
     String generateToken(UserDetails userDetails);
     Boolean validateToken(String token,UserDetails userDetails);

}
