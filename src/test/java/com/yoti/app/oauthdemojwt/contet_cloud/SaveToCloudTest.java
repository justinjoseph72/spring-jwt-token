package com.yoti.app.oauthdemojwt.contet_cloud;

import com.yoti.app.oauthdemojwt.content_cloud.SaveToCloud;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SaveToCloudTest {

    @Autowired
    private SaveToCloud saveToCloud;

    @Test
    public void test1(){
        saveToCloud.saveObject();
    }
}
