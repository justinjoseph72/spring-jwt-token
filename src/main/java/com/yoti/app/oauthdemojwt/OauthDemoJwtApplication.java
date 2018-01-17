package com.yoti.app.oauthdemojwt;

import com.yoti.app.oauthdemojwt.service.impl.AppUserDetailService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootApplication
public class OauthDemoJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(OauthDemoJwtApplication.class, args);
	}


	@Bean
	public UserDetailsService userDetailsService(){
		return new AppUserDetailService();
	}
}
