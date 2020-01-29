package com.dev.zaidi.Utils;

import com.dev.zaidi.Models.NetworkAnalytics;
import com.dev.zaidi.Models.SyncDownResponse;

public interface SyncChangesCallback {
    void onSucces(SyncDownResponse response, NetworkAnalytics analytics);

    void onError(String error, NetworkAnalytics analytics);
}
