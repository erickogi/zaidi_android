package com.dev.lishaboramobile.login.ui.login;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.dev.lishaboramobile.Global.Account.ResponseObject;
import com.dev.lishaboramobile.Global.Models.ResponseModel;
import com.dev.lishaboramobile.Global.Network.ApiConstants;
import com.dev.lishaboramobile.Global.Network.Request;
import com.dev.lishaboramobile.Global.Utils.ResponseCallback;

import org.json.JSONObject;

public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData phoneAuth;
    private MutableLiveData passAuth;
    private MutableLiveData otpRequest;
    private MutableLiveData otpConfirm;
    private MutableLiveData newPassConfirm;

    public LoginViewModel(@NonNull Application application) {
        super(application);

    }
    // TODO: Implement the ViewModel

    public LiveData<ResponseObject> phoneAuth(JSONObject jsonObject) {


        if (this.phoneAuth == null) {
            this.phoneAuth = new MutableLiveData();
        }


        Request.Companion.getResponseSingle(ApiConstants.Companion.getPhoneAuth(), jsonObject, "", new ResponseCallback() {
            @Override
            public void response(ResponseModel responseModel) {


            }

            @Override
            public void response(ResponseObject responseModel) {
                phoneAuth.setValue(responseModel);

            }
        });
        return phoneAuth;
    }

    public LiveData<ResponseObject> passwordAuth(JSONObject jsonObject) {

        if (this.passAuth == null) {
            this.passAuth = new MutableLiveData();
        }
        Request.Companion.getResponseSingle(ApiConstants.Companion.getPasswordAuth(), jsonObject, "", new ResponseCallback() {
            @Override
            public void response(ResponseModel responseModel) {

            }

            @Override
            public void response(ResponseObject responseModel) {
                passAuth.setValue(responseModel);

            }
        });
        return passAuth;
    }

    public LiveData<ResponseObject> otpRequest(JSONObject jsonObject) {

        if (this.otpRequest == null) {
            this.otpRequest = new MutableLiveData();
        }
        Request.Companion.getResponseSingle(ApiConstants.Companion.getPasswordAuth(), jsonObject, "", new ResponseCallback() {
            @Override
            public void response(ResponseModel responseModel) {

            }

            @Override
            public void response(ResponseObject responseModel) {
                otpRequest.setValue(responseModel);

            }
        });
        return otpRequest;
    }

    public LiveData<ResponseObject> otpConfirm(JSONObject jsonObject) {

        if (this.otpConfirm == null) {
            this.otpConfirm = new MutableLiveData();
        }
        Request.Companion.getResponseSingle(ApiConstants.Companion.getOtpRequest(), jsonObject, "", new ResponseCallback() {
            @Override
            public void response(ResponseModel responseModel) {

            }

            @Override
            public void response(ResponseObject responseModel) {
                otpConfirm.setValue(responseModel);

            }
        });
        return otpConfirm;
    }

    public LiveData<ResponseObject> newPassConfirm(JSONObject jsonObject) {

        if (this.newPassConfirm == null) {
            this.newPassConfirm = new MutableLiveData();
        }
        Request.Companion.getResponseSingle(ApiConstants.Companion.getNewPassordConfirm(), jsonObject, "", new ResponseCallback() {
            @Override
            public void response(ResponseModel responseModel) {

            }

            @Override
            public void response(ResponseObject responseModel) {
                newPassConfirm.setValue(responseModel);

            }
        });
        return newPassConfirm;
    }
}
