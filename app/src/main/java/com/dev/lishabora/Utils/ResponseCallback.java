package com.dev.lishabora.Utils;

import com.dev.lishabora.Models.NetworkAnalytics;
import com.dev.lishabora.Models.ResponseModel;
import com.dev.lishabora.Models.ResponseObject;

public interface ResponseCallback {
    void response(ResponseModel responseModel, NetworkAnalytics analytics);

    void response(ResponseObject responseModel, NetworkAnalytics analytics);
}
