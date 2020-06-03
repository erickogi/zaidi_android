package com.dev.zaidi.ViewModels.Login;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.dev.zaidi.Models.NetworkAnalytics;
import com.dev.zaidi.Models.ResponseModel;
import com.dev.zaidi.Models.ResponseObject;
import com.dev.zaidi.Network.ApiConstants;
import com.dev.zaidi.Network.Request;
import com.dev.zaidi.Utils.ResponseCallback;

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


        //this.phoneAuth.setValue(null);
            this.phoneAuth = new MutableLiveData();



        Request.Companion.getResponseSingle(getApplication().getBaseContext(),ApiConstants.Companion.getPhoneAuth(), jsonObject, "", new ResponseCallback() {
            @Override
            public void response(ResponseModel responseModel, NetworkAnalytics analytics) {

                responseModel.setAnalytics(analytics);
                phoneAuth.setValue(responseModel);

            }

            @Override
            public void response(ResponseObject responseModel, NetworkAnalytics analytics) {
                responseModel.setAnalytics(analytics);

                phoneAuth.setValue(responseModel);

            }
        });
        return phoneAuth;
    }

    public LiveData<ResponseObject> passwordAuth(JSONObject jsonObject) {

        // if (this.passAuth == null) {
            this.passAuth = new MutableLiveData();
        // }
        Request.Companion.getResponseSingle(getApplication().getBaseContext(),ApiConstants.Companion.getPasswordAuth(), jsonObject, "", new ResponseCallback() {
            @Override
            public void response(ResponseModel responseModel, NetworkAnalytics analytics) {

            }

            @Override
            public void response(ResponseObject responseModel, NetworkAnalytics analytics) {
                responseModel.setAnalytics(analytics);

                passAuth.setValue(responseModel);

            }
        });
        return passAuth;
    }

    public LiveData<ResponseObject> otpRequest(JSONObject jsonObject) {

//        if (this.otpRequest == null) {
//            this.otpRequest = new MutableLiveData();
//        }
        this.otpRequest = new MutableLiveData();
        Request.Companion.getResponseSingle(getApplication().getBaseContext(),ApiConstants.Companion.getOtpRequest(), jsonObject, "", new ResponseCallback() {
            @Override
            public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                responseModel.setAnalytics(analytics);

                otpRequest.setValue(responseModel);

            }

            @Override
            public void response(ResponseObject responseModel, NetworkAnalytics analytics) {
                responseModel.setAnalytics(analytics);

                otpRequest.setValue(responseModel);

            }
        });
        return otpRequest;
    }

    public LiveData<ResponseObject> otpConfirm(JSONObject jsonObject) {

        if (this.otpConfirm == null) {
            this.otpConfirm = new MutableLiveData();
        }
        Request.Companion.getResponseSingle(getApplication().getBaseContext(),ApiConstants.Companion.getOtpRequest(), jsonObject, "", new ResponseCallback() {
            @Override
            public void response(ResponseModel responseModel, NetworkAnalytics analytics) {

            }

            @Override
            public void response(ResponseObject responseModel, NetworkAnalytics analytics) {
                otpConfirm.setValue(responseModel);

            }
        });
        return otpConfirm;
    }

    public LiveData<ResponseObject> newPassConfirm(JSONObject jsonObject) {

        //  if (this.newPassConfirm == null) {
            this.newPassConfirm = new MutableLiveData();
        //  }
        Request.Companion.getResponseSingle(getApplication().getBaseContext(),ApiConstants.Companion.getNewPassordConfirm(), jsonObject, "", new ResponseCallback() {
            @Override
            public void response(ResponseModel responseModel, NetworkAnalytics analytics) {

            }

            @Override
            public void response(ResponseObject responseModel, NetworkAnalytics analytics) {
                newPassConfirm.setValue(responseModel);

            }
        });
        return newPassConfirm;
    }
}
