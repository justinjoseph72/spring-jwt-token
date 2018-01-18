package com.yoti.app.oauthdemojwt.token;

import com.yoti.app.oauthdemojwt.service.GenerateTokenService;
import com.yoti.app.oauthdemojwt.service.RetrieveDataFromTokenService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RetrieveAndValidateTokenTest {

    @Autowired
    private GenerateTokenService generateTokenService;

    @Autowired
    private RetrieveDataFromTokenService retrieveDataFromTokenService;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    Clock clock;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private UserDetails user;
    private String userName;
    private String tokenForUser;

    @Before
    public void init() {
        userName = "john.doe";
        user = userDetailsService.loadUserByUsername(userName);
        tokenForUser = generateTokenService.generateToken(user);
    }

    @Test
    public void testRetrievedTokenBelongsToTheUser() {
        String userNameFromToken = retrieveDataFromTokenService.getUserNameFromToken(tokenForUser);
        Assert.assertEquals(userName, userNameFromToken);
    }

    @Test
    public void testTwoTokensForASingleUser() {
        String token1ForUser = generateTokenService.generateToken(user);
        String token2ForUser = generateTokenService.generateToken(user);
        Assert.assertNotEquals(token1ForUser,token2ForUser);
        String userNameFromToken1 = retrieveDataFromTokenService.getUserNameFromToken(token1ForUser);
        String userNameFromToken2 = retrieveDataFromTokenService.getUserNameFromToken(token2ForUser);
        Assert.assertEquals(userNameFromToken1,userNameFromToken2);
    }

    @Test
    public void testRetrievedTokenNotExpired() {
        Date expirationDate = retrieveDataFromTokenService.getExpirationDateFromToken(tokenForUser);
        Date currentDate = Date.from(clock.instant());
        Assert.assertTrue(expirationDate.after(currentDate));
    }

    @Test
    public void testRetrievedTokenExpired() {
        Date expirationDate = retrieveDataFromTokenService.getExpirationDateFromToken(tokenForUser);
        Instant instant = clock.instant();
        instant = instant.plus(10, ChronoUnit.MINUTES);
        Date currentDate = Date.from(instant);
        Assert.assertTrue(currentDate.after(expirationDate));
    }

}
