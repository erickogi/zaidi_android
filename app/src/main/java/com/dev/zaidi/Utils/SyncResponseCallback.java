package com.dev.zaidi.Utils;

import com.dev.zaidi.Models.NetworkAnalytics;
import com.dev.zaidi.Models.SyncResponseModel;

public interface SyncResponseCallback {
    void response(SyncResponseModel responseModel, NetworkAnalytics analytics);

    void response(String error, NetworkAnalytics analytics);
}
