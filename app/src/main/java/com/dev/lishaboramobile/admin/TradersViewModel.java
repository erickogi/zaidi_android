package com.dev.lishaboramobile.admin;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.dev.lishaboramobile.Global.Account.ResponseObject;
import com.dev.lishaboramobile.Global.Models.ResponseModel;
import com.dev.lishaboramobile.Global.Network.ApiConstants;
import com.dev.lishaboramobile.Global.Utils.ResponseCallback;

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
                tradersController.getResponse(ApiConstants.Companion.getTraders(), jsonObject, "", new ResponseCallback() {
                    @Override
                    public void response(ResponseModel responseModel) {
                        traders.setValue(responseModel);

                    }

                    @Override
                    public void response(ResponseObject responseModel) {

                    }
                });
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
            tradersController.getResponse(ApiConstants.Companion.getTraders(), jsonObject, "", new ResponseCallback() {
                @Override
                public void response(ResponseModel responseModel) {
                    traders.setValue(responseModel);

                }

                @Override
                public void response(ResponseObject responseModel) {

                }
            });
        } else {

        }
    }

    public LiveData<ResponseModel> updateTrader(JSONObject jsonObject, boolean updateOnline) {

        if (this.updateSuccess == null) {

        }
        this.updateSuccess = new MutableLiveData();


        //updateSuccess.setValue(new MutableLiveData<>());

        if (updateOnline) {
            tradersController.getResponse(ApiConstants.Companion.getUpdateTrader(), jsonObject, "", new ResponseCallback() {
                @Override
                public void response(ResponseModel responseModel) {
                    updateSuccess.setValue(responseModel);

                }

                @Override
                public void response(ResponseObject responseModel) {

                }
            });
        } else {

        }
        return updateSuccess;

    }


    public LiveData<ResponseModel> createTrader(JSONObject requestData, boolean createOnline) {

        if (this.createSuccess == null) {
        }
        this.createSuccess = new MutableLiveData();

        if (createOnline) {
            tradersController.getResponse(ApiConstants.Companion.getCreateTrader(), requestData, "",
                    new ResponseCallback() {
                        @Override
                        public void response(ResponseModel responseModel) {
                            createSuccess.setValue(responseModel);

                        }

                        @Override
                        public void response(ResponseObject responseModel) {

                        }
                    });
        } else {

        }
        return createSuccess;
    }
}
