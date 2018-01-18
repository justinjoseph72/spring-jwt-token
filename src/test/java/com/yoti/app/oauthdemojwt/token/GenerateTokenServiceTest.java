package com.yoti.app.oauthdemojwt.token;

import com.yoti.app.oauthdemojwt.service.GenerateTokenService;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GenerateTokenServiceTest {

    @Autowired
    GenerateTokenService generateTokenService;

    @Autowired
    UserDetailsService userDetailsService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private UserDetails user;

    @Before
    public void init() {
        user = userDetailsService.loadUserByUsername("john.doe");
    }


    @Test
    public void testTokenGenerationWithoutUserDetails() {
        Assert.assertNotNull(user);
        expectedException.expect(AccessDeniedException.class);
        generateTokenService.generateToken(null);
    }

    @Test
    public void testTokenGenerationWithUser() {
        Assert.assertNotNull(user);
        String jwtToken = generateTokenService.generateToken(user);
        Assert.assertThat(jwtToken, Matchers.notNullValue());
    }
}
