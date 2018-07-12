package com.dev.lishaboramobile.Global.Account;

public interface EntryCallbacks {
    void success(String response, int Usertype, int accountState, Object data);

    void error(String response);

    void startProgressDialog();

    void stopProgressDialog();

    void updateProgressDialog(String message);

}
