package com.yoti.app.oauthdemojwt.content_cloud.impl;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.yoti.app.content_cloud.model.*;
import com.yoti.app.content_cloud.service.BinInteractions;
import com.yoti.app.content_cloud.service.InsertObject;
import com.yoti.app.content_cloud.service.RetrieveObject;
import com.yoti.app.guice_binding.*;
import com.yoti.app.oauthdemojwt.content_cloud.ContentCloudOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
@Slf4j
public class ContentCloudOperationImpl implements ContentCloudOperations {

    private InsertObject insertObject;
    private RetrieveObject retrieveObject;
    private BinInteractions binInteractions;

    @PostConstruct
    public void init() {
        log.info("creating instance of impls in the post constructor");
        Injector injector = Guice.createInjector(new Module[]{new BinObjectModule(), new InsertObjectModule(),
                new InsertProtoAdapterModule(), new PayloadConverterModule(), new RetrieveObjectModule()});
        insertObject = injector.getInstance(InsertObject.class);
        retrieveObject = injector.getInstance(RetrieveObject.class);
        binInteractions = injector.getInstance(BinInteractions.class);
    }

    @PreDestroy
    public void destroy() {
        insertObject = null;
        retrieveObject = null;
        binInteractions = null;
    }


    @Override
    public InsertMessageResponse insertObject(final InsertMessageRequest insertMessageRequest) {
        return insertObject.insertObjectToCloud(insertMessageRequest);
    }

    @Override
    public RetrieveMessageResponse retrieveObject(final RetrieveMessageRequest retrieveMessageRequest) {
        return retrieveObject.fetchRecordsFromCloud(retrieveMessageRequest);
    }

    @Override
    public Boolean moveObjectToBin(final BinRequest binRequest) {
        return binInteractions.moveObjectToBin(binRequest);
    }

    @Override
    public Boolean restoreObjectFromBin(final BinRequest binRequest) {
        return binInteractions.restoreObjectFromBin(binRequest);
    }

    @Override
    public Boolean emptyBin(final BinRequest binRequest) {
        return binInteractions.emptyBin(binRequest);
    }

    @Override
    public Boolean removeBinnedObjectFromBin(final BinRequest binRequest) {
        return binInteractions.removeBinnedObjectFromBin(binRequest);
    }


}
