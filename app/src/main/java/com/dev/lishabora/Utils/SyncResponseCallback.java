package com.dev.lishabora.Utils;

import com.dev.lishabora.Models.SyncResponseModel;

public interface SyncResponseCallback {
    void response(SyncResponseModel responseModel);

    void response(String error);
}
