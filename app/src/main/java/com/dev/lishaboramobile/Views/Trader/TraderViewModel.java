package com.dev.lishaboramobile.Views.Trader;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.dev.lishaboramobile.Farmer.Models.FamerModel;
import com.dev.lishaboramobile.Global.Account.ResponseObject;
import com.dev.lishaboramobile.Global.Data.Operations.Repo.FarmerRepo;
import com.dev.lishaboramobile.Global.Models.ResponseModel;
import com.dev.lishaboramobile.Global.Network.ApiConstants;
import com.dev.lishaboramobile.Global.Network.Request;
import com.dev.lishaboramobile.Global.Utils.ResponseCallback;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class TraderViewModel extends AndroidViewModel

{
    FarmerRepo farmerRepo;
    private MutableLiveData farmer;
    private MutableLiveData farmers;
    private MutableLiveData unit;
    private MutableLiveData units;
    private MutableLiveData route;
    private MutableLiveData routes;
    private MutableLiveData cycle;
    private MutableLiveData cycles;


    public TraderViewModel(@NonNull Application application) {
        super(application);
        farmers = new MutableLiveData();
        farmers.setValue(farmerRepo.fetchAllData(false));
    }

    public LiveData<List<FamerModel>> getFarmers() {
        if (farmers == null) {
            farmers = new MutableLiveData();
            farmers.setValue(farmerRepo.fetchAllData(false));
        }

        return farmers;
    }

    public LiveData<List<FamerModel>> getFarmersByName(String names) {
        if (farmers == null) {
            farmers = new MutableLiveData();
            farmers.setValue(farmerRepo.searchByNames(names));
        }

        return farmers;
    }

    public LiveData<List<FamerModel>> getFarmersByMobile(String mobile) {
        if (farmers == null) {
            farmers = new MutableLiveData();
        }

        farmers.setValue(farmerRepo.searchByMobile(mobile));

        return farmers;
    }

    public LiveData<List<FamerModel>> getFarmersByRoute(String route) {
        if (farmers == null) {
            farmers = new MutableLiveData();
        }
        farmers.setValue(farmerRepo.getFramersByRoute(route));


        return farmers;
    }


    public void refreshFarmers(JSONObject jsonObject, boolean fetchFromOnline) {
        if (this.farmers == null) {
            this.farmers = new MutableLiveData();

        }
        if (fetchFromOnline) {

            Gson gson = new Gson();
            Request.Companion.getResponse(ApiConstants.Companion.getFarmers(), jsonObject, "", new ResponseCallback() {
                @Override
                public void response(ResponseModel responseModel) {
                    JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                    Type listType = new TypeToken<LinkedList<FamerModel>>() {
                    }.getType();
                    farmerRepo.insertMultipleTraders(gson.fromJson(jsonArray, listType));
                    farmers.setValue(farmerRepo.fetchAllData(fetchFromOnline));
                }

                @Override
                public void response(ResponseObject responseModel) {

                }
            });

        } else {
            farmer.setValue(farmerRepo.fetchAllData(fetchFromOnline));


        }
    }


}
