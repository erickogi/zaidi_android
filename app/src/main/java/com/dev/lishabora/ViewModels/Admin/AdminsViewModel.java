package com.dev.lishabora.ViewModels.Admin;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.dev.lishabora.Models.NetworkAnalytics;
import com.dev.lishabora.Models.ProductsModel;
import com.dev.lishabora.Models.ResponseModel;
import com.dev.lishabora.Models.ResponseObject;
import com.dev.lishabora.Network.ApiConstants;
import com.dev.lishabora.Network.Request;
import com.dev.lishabora.Repos.ProductsRepo;
import com.dev.lishabora.Repos.RoutesRepo;
import com.dev.lishabora.Repos.Trader.FarmerRepo;
import com.dev.lishabora.Repos.Trader.TraderRepo;
import com.dev.lishabora.Utils.ResponseCallback;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.LinkedList;

import timber.log.Timber;

public class AdminsViewModel extends AndroidViewModel {
    private TraderRepo traderRepo;
    private RoutesRepo routesRepo;
    private FarmerRepo farmerRepo;
    private ProductsRepo productsRepo;


    private MutableLiveData traders;
    private MutableLiveData products;
    private MutableLiveData traderRoutes;
    private MutableLiveData traderProducts;
    private MutableLiveData traderFarmers;
    private MutableLiveData updateSuccess;
    private MutableLiveData updateProductSuccess;
    private MutableLiveData updateAdminSuccess;
    private MutableLiveData createSuccess;
    private MutableLiveData createProductSuccess;
    private MutableLiveData subscribeProductSuccess;
    private MutableLiveData createRouteSuccess;
    private MutableLiveData updateRouteSuccess;
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
                            public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                                traders.setValue(responseModel);
                            }

                            @Override
                            public void response(ResponseObject responseModel, NetworkAnalytics analytics) {

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
                            public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                                products.setValue(responseModel);
                            }

                            @Override
                            public void response(ResponseObject responseModel, NetworkAnalytics analytics) {

                                products.setValue(responseModel);
                            }
                        }
                );

            } else {

            }


        }


        return products;
    }

    public LiveData<ResponseModel> getTraderRoutesModels(JSONObject jsonObject, boolean fetchFromOnline) {

        if (this.traderRoutes == null) {
            this.traderRoutes = new MutableLiveData();
            if (fetchFromOnline) {
                Request.Companion.getResponse(ApiConstants.Companion.getTraderRoutes(), jsonObject, "", new ResponseCallback() {
                            @Override
                            public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                                traderRoutes.setValue(responseModel);
                            }

                            @Override
                            public void response(ResponseObject responseModel, NetworkAnalytics analytics) {

                                traderRoutes.setValue(responseModel);
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
                            public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                                traderProducts.setValue(responseModel);

                                Gson gson = new Gson();
                                JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                                Type listType = new TypeToken<LinkedList<ProductsModel>>() {
                                }.getType();
                                // productsModel = ;
                                Timber.d("routes update called");
                                //update(gson.fromJson(jsonArray, listType));



                            }

                            @Override
                            public void response(ResponseObject responseModel, NetworkAnalytics analytics) {

                                traderProducts.setValue(responseModel);
                            }
                        }
                );

            } else {

            }


        }


        return traderProducts;
    }

    public LiveData<ResponseModel> getTraderFarmersModels(JSONObject jsonObject, boolean fetchFromOnline) {

        if (this.traderFarmers == null) {
            this.traderFarmers = new MutableLiveData();
            if (fetchFromOnline) {
                Request.Companion.getResponse(ApiConstants.Companion.getTraderFarmers(), jsonObject, "", new ResponseCallback() {
                            @Override
                            public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                                traderFarmers.setValue(responseModel);
                            }

                            @Override
                            public void response(ResponseObject responseModel, NetworkAnalytics analytics) {

                                traderFarmers.setValue(responseModel);
                            }
                        }
                );

            } else {

            }


        }


        return traderFarmers;
    }



    public void refresh(JSONObject jsonObject, boolean fetchFromOnline) {
        if (this.traders == null) {
            this.traders = new MutableLiveData();

        }
        this.traders = new MutableLiveData();

        if (fetchFromOnline) {
            Request.Companion.getResponse(ApiConstants.Companion.getTraders(), jsonObject, "", new ResponseCallback() {
                        @Override
                        public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                            traders.setValue(responseModel);
                            //traderRepo.insertMultipleTraders();
                        }

                        @Override
                        public void response(ResponseObject responseModel, NetworkAnalytics analytics) {
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
                        public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                            updateSuccess.setValue(responseModel);
                        }

                        @Override
                        public void response(ResponseObject responseModel, NetworkAnalytics analytics) {
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
                public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                    createSuccess.setValue(responseModel);
                }

                @Override
                public void response(ResponseObject responseModel, NetworkAnalytics analytics) {
                    traders.setValue(responseModel);

                }
            });

        } else {

        }
        return createSuccess;
    }

    public LiveData<ResponseObject> registerTrader(JSONObject requestData, boolean createOnline) {

        if (this.createSuccess == null) {
        }
        this.createSuccess = new MutableLiveData();

        if (createOnline) {
            Request.Companion.getResponseSingle(ApiConstants.Companion.getCreateTrader(), requestData, "", new ResponseCallback() {
                @Override
                public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                    createSuccess.setValue(responseModel);
                }

                @Override
                public void response(ResponseObject responseModel, NetworkAnalytics analytics) {
                    createSuccess.setValue(responseModel);

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
                        public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                            updateAdminSuccess.setValue(responseModel);
                        }

                        @Override
                        public void response(ResponseObject responseModel, NetworkAnalytics analytics) {
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
                public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                    createProductSuccess.setValue(responseModel);
                }

                @Override
                public void response(ResponseObject responseModel, NetworkAnalytics analytics) {
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
                        public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                            products.setValue(responseModel);
                        }

                        @Override
                        public void response(ResponseObject responseModel, NetworkAnalytics analytics) {
                            products.setValue(responseModel);

                        }
                    }
            );

        } else {

        }
    }


    public LiveData<ResponseModel> updateProduct(JSONObject jsonObject, boolean b) {

        this.updateProductSuccess = new MutableLiveData();

        if (b) {
            Request.Companion.getResponse(ApiConstants.Companion.getUpdateProducts(), jsonObject, "", new ResponseCallback() {
                @Override
                public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                    updateProductSuccess.setValue(responseModel);
                }

                @Override
                public void response(ResponseObject responseModel, NetworkAnalytics analytics) {
                    updateProductSuccess.setValue(responseModel);

                }
            });

        } else {

        }
        return updateProductSuccess;
    }


}
