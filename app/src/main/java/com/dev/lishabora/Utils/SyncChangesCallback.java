package com.dev.lishabora.Utils;

import com.dev.lishabora.Models.NetworkAnalytics;
import com.dev.lishabora.Models.SyncDownResponse;

public interface SyncChangesCallback {
    void onSucces(SyncDownResponse response, NetworkAnalytics analytics);

    void onError(String error, NetworkAnalytics analytics);
}
