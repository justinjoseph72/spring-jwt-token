package com.yoti.app.oauthdemojwt.auth;

import com.yoti.app.oauthdemojwt.authentication.AuthenticationTokenFilter;
import com.yoti.app.oauthdemojwt.service.RetrieveDataFromTokenService;
import org.junit.Before;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class BaseMockMvnTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;



    protected MockMvc getMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(this.restDocumentation))
                //.apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        return mockMvc;
    }

    protected MockMvc getMockMvcWithSpringSecurity() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(this.restDocumentation))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        return mockMvc;
    }
}
