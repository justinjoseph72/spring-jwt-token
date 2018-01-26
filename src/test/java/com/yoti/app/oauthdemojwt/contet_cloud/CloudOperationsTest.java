package com.yoti.app.oauthdemojwt.contet_cloud;

import com.yoti.app.content_cloud.model.InsertMessageRequest;
import com.yoti.app.content_cloud.model.InsertMessageResponse;
import com.yoti.app.oauthdemojwt.content_cloud.ContentCloudOperations;
import com.yoti.app.oauthdemojwt.domain.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CloudOperationsTest {

    @Autowired
    private ContentCloudOperations contentCloudOperations;

    @Test
    public void testInsertObject(){
        InsertMessageResponse messageResponse = contentCloudOperations.insertObject(getInsertMessageRequest());
    }

    private InsertMessageRequest<Person> getInsertMessageRequest(){
        Person person = new Person(1, "sd", "sdf", "sdf");

        InsertMessageRequest insertMessageRequest = InsertMessageRequest.builder()
                .cloudId("cloudId")
                .dataGroup("Connection")
                .dataObj(person)
                .requesterPublicKey("public key")
                .encryptionKeyId("keyId")
                .build();
        return insertMessageRequest;
    }
}
