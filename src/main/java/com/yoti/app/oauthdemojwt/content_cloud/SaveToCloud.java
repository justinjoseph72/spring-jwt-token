package com.yoti.app.oauthdemojwt.content_cloud;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.yoti.app.content_cloud.model.InsertMessageRequest;
import com.yoti.app.content_cloud.model.InsertMessageResponse;
import com.yoti.app.content_cloud.service.InsertObject;
import com.yoti.app.guice_binding.InsertObjectModule;
import com.yoti.app.guice_binding.InsertProtoAdapterModule;
import com.yoti.app.oauthdemojwt.domain.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class SaveToCloud {

    private InsertObject insertObject;

    @PostConstruct
    public void init() {
        Injector injector = Guice.createInjector(new Module[]{new InsertObjectModule(), new InsertProtoAdapterModule()});
        insertObject = injector.getInstance(InsertObject.class);
    }

    public void saveObject() {
        log.info("gucie injected");
        Person person = new Person(1, "sd", "sdf", "sdf");

        InsertMessageRequest insertMessageRequest = InsertMessageRequest.builder()
                .cloudId("cloudId")
                .dataGroup("Connection")
                .dataObj(person)
                .requesterPublicKey("public key")
                .encryptionKeyId("keyId")
                .build();

        InsertMessageResponse response = insertObject.insertObjectToCloud(insertMessageRequest);
        log.info("this is Done");


    }


}
