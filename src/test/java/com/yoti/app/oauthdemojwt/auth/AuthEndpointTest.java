package com.yoti.app.oauthdemojwt.auth;

import com.yoti.app.oauthdemojwt.constants.ApiUrlConstants;
import org.assertj.core.util.Strings;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class AuthEndpointTest extends BaseMockMvnTest{

    @Value("${com.yoti.token-header-name}")
    public String TOKEN_HEADER;

    @Test
    public void testAuthLoginWithoutToken() throws Exception {
        getMockMvc().perform(MockMvcRequestBuilders.get(getBaseLoginAuthUrl())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void testAuthLoginWithToken() throws Exception {
        getMockMvc().perform(MockMvcRequestBuilders.get(getAuthLoginUrlWithToken("oljjsdf"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string(TOKEN_HEADER, CoreMatchers.notNullValue()));

    }

    private String getBaseLoginAuthUrl() {
        return ApiUrlConstants.AUTH_BASE_URL+ApiUrlConstants.LOGIN_ENDPOINT;
    }

    private String getAuthLoginUrlWithToken(String token){
        return Strings.concat(getBaseLoginAuthUrl(),"?",ApiUrlConstants.TOKEN_REQ_PARAM,"=",token);
    }

}
