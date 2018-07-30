package com.dev.lishaboramobile.admin.ui.admins;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.dev.lishaboramobile.Global.Account.ResponseObject;
import com.dev.lishaboramobile.Global.Data.Operations.Repo.FarmerRepo;
import com.dev.lishaboramobile.Global.Data.Operations.Repo.RoutesRepo;
import com.dev.lishaboramobile.Global.Data.Operations.Repo.TraderRepo;
import com.dev.lishaboramobile.Global.Models.ResponseModel;
import com.dev.lishaboramobile.Global.Network.ApiConstants;
import com.dev.lishaboramobile.Global.Network.Request;
import com.dev.lishaboramobile.Global.Utils.ResponseCallback;

import org.json.JSONObject;

public class TradersProfileViewModel extends AndroidViewModel {
    private TraderRepo traderRepo;
    private RoutesRepo routesRepo;
    private FarmerRepo farmerRepo;


    private MutableLiveData traderRoutes;
    private MutableLiveData traderProducts;


    public TradersProfileViewModel(@NonNull Application application) {
        super(application);
        traderRepo = new TraderRepo(application);
        routesRepo = new RoutesRepo(application);
        farmerRepo = new FarmerRepo(application);
    }
    // TODO: Implement the ViewModel


    public LiveData<ResponseModel> getTraderRoutesModels(JSONObject jsonObject, boolean fetchFromOnline) {

        if (this.traderRoutes == null) {
            this.traderRoutes = new MutableLiveData();
            if (fetchFromOnline) {
                Request.Companion.getResponse(ApiConstants.Companion.getTraderRoutes(), jsonObject, "", new ResponseCallback() {
                            @Override
                            public void response(ResponseModel responseModel) {
                                traderRoutes.postValue(responseModel);
                            }

                            @Override
                            public void response(ResponseObject responseModel) {

                                traderRoutes.postValue(responseModel);
                            }
                        }
                );

            } else {

            }


        }


        return traderRoutes;
    }

    public LiveData<ResponseModel> getTraderProductsModels(JSONObject jsonObject, boolean fetchFromOnline) {

        if (this.traderProducts == null) {
            this.traderProducts = new MutableLiveData();
            if (fetchFromOnline) {
                Request.Companion.getResponse(ApiConstants.Companion.getTraderProducts(), jsonObject, "", new ResponseCallback() {
                            @Override
                            public void response(ResponseModel responseModel) {
                                traderProducts.setValue(responseModel);
                            }

                            @Override
                            public void response(ResponseObject responseModel) {

                                traderProducts.setValue(responseModel);
                            }
                        }
                );

            } else {

            }


        }


        return traderProducts;
    }


}
