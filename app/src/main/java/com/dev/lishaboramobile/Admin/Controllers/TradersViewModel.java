package com.dev.lishaboramobile.Admin.Controllers;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.dev.lishaboramobile.Global.Models.ResponseModel;
import com.dev.lishaboramobile.Global.Network.ApiConstants;

import org.json.JSONObject;

public class TradersViewModel extends AndroidViewModel {
    private TradersController tradersController;
    private MutableLiveData traders;
    private MutableLiveData updateSuccess;
    private MutableLiveData createSuccess;
    private MutableLiveData editSuccess;

    public TradersViewModel(@NonNull Application application) {
        super(application);
        tradersController = new TradersController(application);


    }

    public LiveData<ResponseModel> getTraderModels(JSONObject jsonObject, boolean fetchFromOnline) {

        if (this.traders == null) {
            this.traders = new MutableLiveData();
            if (fetchFromOnline) {
                tradersController.getResponse(ApiConstants.Companion.getTraders(), jsonObject, "", responseModel -> traders.setValue(responseModel));

            } else {

            }


        }


        return traders;
    }

    public void refresh(JSONObject jsonObject, boolean fetchFromOnline) {
        if (this.traders == null) {
            this.traders = new MutableLiveData();

        }
        if (fetchFromOnline) {
            tradersController.getResponse(ApiConstants.Companion.getTraders(), jsonObject, "", responseModel -> traders.setValue(responseModel));

        } else {

        }
    }

    public LiveData<ResponseModel> updateTrader(JSONObject jsonObject, boolean updateOnline) {

        if (this.updateSuccess == null) {

        }
        this.updateSuccess = new MutableLiveData();


        //updateSuccess.setValue(new MutableLiveData<>());

        if (updateOnline) {
            tradersController.getResponse(ApiConstants.Companion.getUpdateTrader(), jsonObject, "", responseModel -> updateSuccess.setValue(responseModel));

        } else {

        }
        return updateSuccess;

    }


    public LiveData<ResponseModel> createTrader(JSONObject requestData, boolean createOnline) {

        if (this.createSuccess == null) {
        }
        this.createSuccess = new MutableLiveData();

        if (createOnline) {
            tradersController.getResponse(ApiConstants.Companion.getCreateTrader(), requestData, "", responseModel -> createSuccess.setValue(responseModel));

        } else {

        }
        return createSuccess;
    }
}
