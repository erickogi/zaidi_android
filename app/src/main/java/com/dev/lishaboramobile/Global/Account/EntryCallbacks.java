package com.dev.lishaboramobile.Global.Account;

public interface EntryCallbacks {
    void success(ResponseObject responseObject);

    void error(String response);

    void startProgressDialog();

    void stopProgressDialog();

    void updateProgressDialog(String message);

}
