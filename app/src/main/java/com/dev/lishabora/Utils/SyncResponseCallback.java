package com.dev.lishabora.Utils;

import com.dev.lishabora.Models.NetworkAnalytics;
import com.dev.lishabora.Models.SyncResponseModel;

public interface SyncResponseCallback {
    void response(SyncResponseModel responseModel, NetworkAnalytics analytics);

    void response(String error, NetworkAnalytics analytics);
}
