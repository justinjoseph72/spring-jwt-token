package com.yoti.app.oauthdemojwt.config;

import com.yoti.app.oauthdemojwt.content_cloud.SaveToCloud;
import org.junit.Before;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ApplicationConfigs {

    @Bean
    Clock getClock(){
        return Clock.systemUTC();
    }

    @Bean
    SaveToCloud getSavetoCloud(){
        return new SaveToCloud();
    }


}
