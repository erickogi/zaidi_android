package com.dev.zaidi.Utils;

import com.dev.zaidi.Models.NetworkAnalytics;
import com.dev.zaidi.Models.ResponseModel;
import com.dev.zaidi.Models.ResponseObject;

public interface ResponseCallback {
    void response(ResponseModel responseModel, NetworkAnalytics analytics);

    void response(ResponseObject responseModel, NetworkAnalytics analytics);
}
