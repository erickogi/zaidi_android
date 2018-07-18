package com.dev.lishaboramobile.Admin.Callbacks;

import com.dev.lishaboramobile.Global.Models.ResponseModel;

public interface CreateTraderCallbacks {
    void success(ResponseModel responseModel);

    void error(String response);

    void startProgressDialog();

    void stopProgressDialog();

    void updateProgressDialog(String message);

}
