package com.yoti.app.oauthdemojwt.service.impl;

import com.yoti.app.oauthdemojwt.service.UserFromYoti;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserFromYotiImpl implements UserFromYoti {
    @Override
    public String getUserNameFromYotiToken(final String yotiToken) {
        log.info("the token is {}",yotiToken);
        return "john.doe";
    }
}
