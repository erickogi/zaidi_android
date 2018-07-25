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

public class AdminsViewModel extends AndroidViewModel {
    private TraderRepo traderRepo;
    private RoutesRepo routesRepo;
    private FarmerRepo farmerRepo;


    private MutableLiveData traders;
    private MutableLiveData products;
    private MutableLiveData updateSuccess;
    private MutableLiveData updateAdminSuccess;
    private MutableLiveData createSuccess;
    private MutableLiveData createProductSuccess;
    private MutableLiveData editSuccess;


    public AdminsViewModel(@NonNull Application application) {
        super(application);
        traderRepo = new TraderRepo(application);
        routesRepo = new RoutesRepo(application);
        farmerRepo = new FarmerRepo(application);
    }
    // TODO: Implement the ViewModel

    public LiveData<ResponseModel> getTraderModels(JSONObject jsonObject, boolean fetchFromOnline) {

        if (this.traders == null) {
            this.traders = new MutableLiveData();
            if (fetchFromOnline) {
                Request.Companion.getResponse(ApiConstants.Companion.getTraders(), jsonObject, "", new ResponseCallback() {
                            @Override
                            public void response(ResponseModel responseModel) {
                                traders.setValue(responseModel);
                            }

                            @Override
                            public void response(ResponseObject responseModel) {

                                traders.setValue(responseModel);
                            }
                        }
                );

            } else {

            }


        }


        return traders;
    }

    public LiveData<ResponseModel> getProductsModels(JSONObject jsonObject, boolean fetchFromOnline) {

        if (this.products == null) {
            this.products = new MutableLiveData();
            if (fetchFromOnline) {
                Request.Companion.getResponse(ApiConstants.Companion.getProducts(), jsonObject, "", new ResponseCallback() {
                            @Override
                            public void response(ResponseModel responseModel) {
                                products.setValue(responseModel);
                            }

                            @Override
                            public void response(ResponseObject responseModel) {

                                products.setValue(responseModel);
                            }
                        }
                );

            } else {

            }


        }


        return products;
    }


    public void refresh(JSONObject jsonObject, boolean fetchFromOnline) {
        if (this.traders == null) {
            this.traders = new MutableLiveData();

        }
        if (fetchFromOnline) {
            Request.Companion.getResponse(ApiConstants.Companion.getTraders(), jsonObject, "", new ResponseCallback() {
                        @Override
                        public void response(ResponseModel responseModel) {
                            traders.setValue(responseModel);
                        }

                        @Override
                        public void response(ResponseObject responseModel) {
                            traders.setValue(responseModel);

                        }
                    }
            );

        } else {

        }
    }

    public LiveData<ResponseModel> updateTrader(JSONObject jsonObject, boolean updateOnline) {

        if (this.updateSuccess == null) {

        }
        this.updateSuccess = new MutableLiveData();


        //updateSuccess.setValue(new MutableLiveData<>());

        if (updateOnline) {
            Request.Companion.getResponse(ApiConstants.Companion.getUpdateTrader(), jsonObject, "",
                    new ResponseCallback() {
                        @Override
                        public void response(ResponseModel responseModel) {
                            updateSuccess.setValue(responseModel);
                        }

                        @Override
                        public void response(ResponseObject responseModel) {
                            traders.setValue(responseModel);

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
            Request.Companion.getResponse(ApiConstants.Companion.getCreateTrader(), requestData, "", new ResponseCallback() {
                @Override
                public void response(ResponseModel responseModel) {
                    createSuccess.setValue(responseModel);
                }

                @Override
                public void response(ResponseObject responseModel) {
                    traders.setValue(responseModel);

                }
            });

        } else {

        }
        return createSuccess;
    }

    public LiveData<ResponseModel> updateAdmin(JSONObject jsonObject, boolean b) {
        if (this.updateAdminSuccess == null) {

        }
        this.updateAdminSuccess = new MutableLiveData();


        //updateSuccess.setValue(new MutableLiveData<>());

        if (b) {
            Request.Companion.getResponse(ApiConstants.Companion.getUpdateAdmin(), jsonObject, "",
                    new ResponseCallback() {
                        @Override
                        public void response(ResponseModel responseModel) {
                            updateAdminSuccess.setValue(responseModel);
                        }

                        @Override
                        public void response(ResponseObject responseModel) {
                            updateAdminSuccess.setValue(responseModel);

                        }
                    });

        } else {

        }
        return updateAdminSuccess;
    }

    public LiveData<ResponseModel> createProduct(JSONObject jsonObject, boolean b) {
        if (this.createProductSuccess == null) {
        }
        this.createProductSuccess = new MutableLiveData();

        if (b) {
            Request.Companion.getResponse(ApiConstants.Companion.getCreateProducts(), jsonObject, "", new ResponseCallback() {
                @Override
                public void response(ResponseModel responseModel) {
                    createProductSuccess.setValue(responseModel);
                }

                @Override
                public void response(ResponseObject responseModel) {
                    createProductSuccess.setValue(responseModel);

                }
            });

        } else {

        }
        return createProductSuccess;
    }

    public void refreshProducts(JSONObject jsonObject, boolean fetchFromOnline) {
        if (this.products == null) {
            this.products = new MutableLiveData();

        }
        if (fetchFromOnline) {
            Request.Companion.getResponse(ApiConstants.Companion.getProducts(), jsonObject, "", new ResponseCallback() {
                        @Override
                        public void response(ResponseModel responseModel) {
                            products.setValue(responseModel);
                        }

                        @Override
                        public void response(ResponseObject responseModel) {
                            products.setValue(responseModel);

                        }
                    }
            );

        } else {

        }
    }

}
