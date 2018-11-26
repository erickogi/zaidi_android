package com.dev.lishabora.Utils;

public interface ApproveListener {
    void onComplete();

    void onStart();

    void onProgress(int progress);

    void onError(String error);

}
