package com.dev.zaidi.Utils;

import com.dev.zaidi.Models.NetworkAnalytics;
import com.dev.zaidi.Models.Trader.Data;

public interface SyncDownResponseCallback {
    void response(Data responseModel, NetworkAnalytics analytics);

    void response(String error, NetworkAnalytics analytics);
}
