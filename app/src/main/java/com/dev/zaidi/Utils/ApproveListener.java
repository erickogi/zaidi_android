package com.dev.zaidi.Utils;

public interface ApproveListener {
    void onComplete();

    void onStart();

    void onProgress(int progress);

    void onError(String error);

}
