package com.dev.lishabora.Utils;

import com.dev.lishabora.Models.SyncDownResponse;

public interface SyncChangesCallback {
    void onSucces(SyncDownResponse response);

    void onError(String error);
}
