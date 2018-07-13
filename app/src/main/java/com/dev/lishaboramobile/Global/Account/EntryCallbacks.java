package com.dev.lishaboramobile.Global.Account;

public interface EntryCallbacks {
    void success(EntryModel entryModel);

    void error(String response);

    void startProgressDialog();

    void stopProgressDialog();

    void updateProgressDialog(String message);

}
