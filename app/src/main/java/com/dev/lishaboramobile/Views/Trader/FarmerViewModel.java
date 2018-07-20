package com.dev.lishaboramobile.Views.Trader;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.dev.lishaboramobile.Farmer.Models.FamerModel;
import com.dev.lishaboramobile.Global.Data.Operations.Repo.FarmerRepo;
import com.dev.lishaboramobile.Global.Models.ResponseModel;
import com.dev.lishaboramobile.Global.Network.ApiConstants;
import com.dev.lishaboramobile.Global.Utils.ResponseCallback;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class FarmerViewModel extends AndroidViewModel {
    FarmerController farmerController;
    FarmerRepo farmerRepo;
    LiveData<List<FamerModel>> famerModels;
    Gson gson = new Gson();
    private LiveData<List<FamerModel>> farmers;
    private MutableLiveData updateSuccess;
    private MutableLiveData createSuccess;
    private MutableLiveData editSuccess;


    public FarmerViewModel(@NonNull Application application) {
        super(application);
        farmerController = new FarmerController(application);
        farmerRepo = new FarmerRepo(application);
//        if(farmers==null){
//            farmers=new MutableLiveData();
//        }
//        farmers=farmerRepo.fetchAllData(false);
    }

    public LiveData<List<FamerModel>> getFarmerModels(JSONObject jsonObject, boolean fetchFromOnline) {

        if (this.farmers == null) {
            this.farmers = new MutableLiveData();
            farmers = (farmerRepo.fetchAllData(false));
            farmerController.getResponse(ApiConstants.Companion.getFarmers(), jsonObject, "", responseModel -> {

                if (responseModel.getResultCode() == 1 && responseModel.getData() != null) {
                    JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                    Type listType = new TypeToken<LinkedList<FamerModel>>() {
                    }.getType();
                    farmerRepo.insertMultipleTraders(gson.fromJson(jsonArray, listType));

                }
            });


        }


        return farmers;
    }

    public LiveData<List<FamerModel>> getFramerModelsByRoute(String code) {

        if (this.farmers == null) {
            this.farmers = new MutableLiveData();

            famerModels = farmerRepo.getFramersByRoute(code);

            farmers = (famerModels);


        }


        return farmers;
    }

    public LiveData<List<FamerModel>> getFarmerModelsByNames(String names) {

        if (this.farmers == null) {
            this.farmers = new MutableLiveData();

            famerModels = farmerRepo.searchByNames(names);

            farmers = (famerModels);


        }


        return farmers;
    }

    public void refreshAll(JSONObject jsonObject, boolean fetchFromOnline) {
        if (this.farmers == null) {
            this.farmers = new MutableLiveData();

        }
        if (fetchFromOnline) {
            farmerController.getResponse(ApiConstants.Companion.getTraders(), jsonObject, "", new ResponseCallback() {
                @Override
                public void response(ResponseModel responseModel) {
                    JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                    Type listType = new TypeToken<LinkedList<FamerModel>>() {
                    }.getType();
                    farmerRepo.insertMultipleTraders(gson.fromJson(jsonArray, listType));
                    farmers = (farmerRepo.fetchAllData(false));
                }
            });

        } else {
            famerModels = farmerRepo.fetchAllData(false);

            farmers = (famerModels);

        }
    }

    public LiveData<ResponseModel> updateFarmer(JSONObject jsonObject, boolean updateOnline, FamerModel famerModel) {

        if (this.updateSuccess == null) {

        }
        this.updateSuccess = new MutableLiveData();


        //updateSuccess.setValue(new MutableLiveData<>());

        if (updateOnline) {
            farmerController.getResponse(ApiConstants.Companion.getUpdateTrader(), jsonObject, "", responseModel -> updateSuccess.setValue(responseModel));

        } else {
            farmerRepo.upDateRecord(famerModel);
            //ResponseModel responseModel=new ResponseModel();


        }
        return updateSuccess;

    }

    public LiveData<ResponseModel> createFarmer(JSONObject requestData, boolean createOnline, FamerModel famerModel) {

        if (this.createSuccess == null) {
        }
        this.createSuccess = new MutableLiveData();

        if (createOnline) {
            farmerController.getResponse(ApiConstants.Companion.getCreateTrader(), requestData, "", responseModel -> createSuccess.setValue(responseModel));

        } else {
            farmerRepo.insert(famerModel);

        }
        return createSuccess;
    }


}
