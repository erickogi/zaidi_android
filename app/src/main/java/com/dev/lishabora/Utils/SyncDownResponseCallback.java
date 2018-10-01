package com.dev.lishabora.Utils;

import com.dev.lishabora.Models.Trader.Data;

public interface SyncDownResponseCallback {
    void response(Data responseModel);

    void response(String error);
}
