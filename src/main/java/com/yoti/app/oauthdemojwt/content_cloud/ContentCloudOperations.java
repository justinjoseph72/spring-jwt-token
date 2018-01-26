package com.yoti.app.oauthdemojwt.content_cloud;

import com.yoti.app.content_cloud.model.*;

public interface ContentCloudOperations {

    InsertMessageResponse insertObject(final InsertMessageRequest insertMessageRequest);
    RetrieveMessageResponse retrieveObject(final RetrieveMessageRequest retrieveMessageRequest);
    Boolean moveObjectToBin(final BinRequest binRequest);
    Boolean restoreObjectFromBin(final BinRequest binRequest);
    Boolean emptyBin(final BinRequest binRequest);
    Boolean removeBinnedObjectFromBin(final BinRequest binRequest);

}
