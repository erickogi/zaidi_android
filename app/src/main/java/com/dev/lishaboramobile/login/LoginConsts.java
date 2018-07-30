package com.dev.lishaboramobile.login;

import com.dev.lishaboramobile.Global.Account.ResponseObject;
import com.dev.lishaboramobile.Global.Models.ResponseModel;

public class LoginConsts {
    private static ResponseObject responseObject;
    private static ResponseModel responseModel;
    private static String phone;


    public static ResponseObject getResponseObject() {
        if (responseObject != null) {
            return responseObject;
        } else {
            return new ResponseObject();
        }
    }

    public static void setResponseObject(ResponseObject responseObject) {

        LoginConsts.responseObject = responseObject;
    }

    public static ResponseModel getResponseModel() {
        if (responseModel != null) {
            return responseModel;
        } else {
            return new ResponseModel();
        }
    }

    public static void setResponseModel(ResponseModel responseModel) {
        LoginConsts.responseModel = responseModel;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        LoginConsts.phone = phone;
    }
}
