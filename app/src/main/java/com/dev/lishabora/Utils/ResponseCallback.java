package com.dev.lishabora.Utils;

import com.dev.lishabora.Models.ResponseModel;
import com.dev.lishabora.Models.ResponseObject;

public interface ResponseCallback {
    void response(ResponseModel responseModel);

    void response(ResponseObject responseModel);
}
