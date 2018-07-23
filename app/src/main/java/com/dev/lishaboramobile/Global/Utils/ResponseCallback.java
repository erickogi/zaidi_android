package com.dev.lishaboramobile.Global.Utils;

import com.dev.lishaboramobile.Global.Account.ResponseObject;
import com.dev.lishaboramobile.Global.Models.ResponseModel;

public interface ResponseCallback {
    void response(ResponseModel responseModel);

    void response(ResponseObject responseModel);
}
