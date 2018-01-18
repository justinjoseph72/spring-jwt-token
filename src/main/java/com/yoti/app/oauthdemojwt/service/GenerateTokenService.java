package com.yoti.app.oauthdemojwt.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface GenerateTokenService {


     String generateToken(UserDetails userDetails);


}
