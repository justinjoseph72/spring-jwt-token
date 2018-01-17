package com.yoti.app.oauthdemojwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ApplicationConfigs {

    @Bean
    Clock getClock(){
        return Clock.systemUTC();
    }


}
