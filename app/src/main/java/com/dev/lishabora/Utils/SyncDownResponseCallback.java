package com.dev.lishabora.Utils;

import com.dev.lishabora.Models.NetworkAnalytics;
import com.dev.lishabora.Models.Trader.Data;

public interface SyncDownResponseCallback {
    void response(Data responseModel, NetworkAnalytics analytics);

    void response(String error, NetworkAnalytics analytics);
}
