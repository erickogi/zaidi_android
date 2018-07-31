package com.dev.lishaboramobile.Views.Trader;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dev.lishaboramobile.Farmer.Models.FamerModel;
import com.dev.lishaboramobile.Global.Account.ResponseObject;
import com.dev.lishaboramobile.Global.Data.Operations.Repo.CyclesRepo;
import com.dev.lishaboramobile.Global.Data.Operations.Repo.FarmerRepo;
import com.dev.lishaboramobile.Global.Data.Operations.Repo.RoutesRepo;
import com.dev.lishaboramobile.Global.Data.Operations.Repo.UnitsRepo;
import com.dev.lishaboramobile.Global.Models.ResponseModel;
import com.dev.lishaboramobile.Global.Network.ApiConstants;
import com.dev.lishaboramobile.Global.Network.Request;
import com.dev.lishaboramobile.Global.Utils.ResponseCallback;
import com.dev.lishaboramobile.Trader.Models.Cycles;
import com.dev.lishaboramobile.Trader.Models.RPFSearchModel;
import com.dev.lishaboramobile.Trader.Models.RoutesModel;
import com.dev.lishaboramobile.Trader.Models.UnitsModel;
import com.dev.lishaboramobile.login.PrefrenceManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class TraderViewModel extends AndroidViewModel

{
    FarmerRepo farmerRepo;
    RoutesRepo routesRepo;
    UnitsRepo unitsRepo;
    CyclesRepo cyclesRepo;
    Gson gson = new Gson();
    private MutableLiveData createRouteSuccess;



    private MutableLiveData farmer;
    private MutableLiveData createFarmerSuccess;
    private MutableLiveData unit;
    private LiveData<List<FamerModel>> farmers;
    private MutableLiveData route;
    private LiveData<List<UnitsModel>> units;
    private MutableLiveData cycle;
    private LiveData<List<RoutesModel>> routes;
    private LiveData<List<Cycles>> cycles;
    private Application application;


    public TraderViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        farmers = new MutableLiveData();
        routes = new MutableLiveData();
        units = new MutableLiveData();
        cycles = new MutableLiveData();
        farmerRepo = new FarmerRepo(application);
        unitsRepo = new UnitsRepo(application);
        routesRepo = new RoutesRepo(application);
        cyclesRepo = new CyclesRepo(application);
//
//        farmers.setValue(farmerRepo.fetchAllData(false));
//        routes.setValue(routesRepo.fetchAllData(false));
//        units.setValue(unitsRepo.fetchAllData(false));
//        cycles.setValue(cyclesRepo.fetchAllData(false));
    }

    public LiveData<List<FamerModel>> getFarmers(JSONObject jsonObject, boolean isOnline) {
        if (farmers == null) {
            farmers = new MutableLiveData();


        }

        if (isOnline) {
            Request.Companion.getResponse(ApiConstants.Companion.getTraderFarmers(), jsonObject, "", new ResponseCallback() {
                        @Override
                        public void response(ResponseModel responseModel) {
                            JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                            Type listType = new TypeToken<LinkedList<FamerModel>>() {
                            }.getType();
                            // famerModels = gson.fromJson(jsonArray, listType);


                            Log.d("ReTrUp", "farmers update called");
                            farmerRepo.insertMultipleTraders(gson.fromJson(jsonArray, listType));
                        }

                        @Override
                        public void response(ResponseObject responseModel) {

                            JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                            Type listType = new TypeToken<LinkedList<FamerModel>>() {
                            }.getType();
                            // famerModels = gson.fromJson(jsonArray, listType);


                            Log.d("ReTrUp", "farmers update called");
                            farmerRepo.insertMultipleTraders(gson.fromJson(jsonArray, listType));
                        }
                    }
            );


        }
        farmers = (farmerRepo.fetchAllData(false));
        return farmers;
    }

    public LiveData<List<FamerModel>> getFarmersByName(String names) {
        if (farmers == null) {
            farmers = new MutableLiveData();
            farmers = (farmerRepo.searchByNames(names));
        }

        return farmers;
    }

    public LiveData<List<FamerModel>> getFarmersByMobile(String mobile) {
        if (farmers == null) {
            farmers = new MutableLiveData();
        }

        farmers = (farmerRepo.searchByMobile(mobile));

        return farmers;
    }

    public LiveData<List<FamerModel>> getFarmersByRoute(String route) {
        if (farmers == null) {
            farmers = new MutableLiveData();
        }
        farmers = (farmerRepo.getFramersByRoute(route));


        return farmers;
    }

    public LiveData<List<RoutesModel>> getRoutes(boolean isOnline) {
        if (routes == null) {
            routes = new MutableLiveData();
        }

        if (isOnline) {
            Request.Companion.getResponse(ApiConstants.Companion.getTraderRoutes(), getTraderRoutesObject(), "", new ResponseCallback() {
                        @Override
                        public void response(ResponseModel responseModel) {
                            if (responseModel.getResultCode() == 1) {
                                JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                                Type listType = new TypeToken<LinkedList<RoutesModel>>() {
                                }.getType();
                                // routesModel = ;
                                Log.d("ReTrUp", "routes update called");
                                routesRepo.insertMultipleRoutes(gson.fromJson(jsonArray, listType));

                            }

                        }

                        @Override
                        public void response(ResponseObject responseModel) {
                            JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                            Type listType = new TypeToken<LinkedList<RoutesModel>>() {
                            }.getType();
                            // routesModel = ;
                            Log.d("ReTrUp", "routes update called");
                            routesRepo.insertMultipleRoutes(gson.fromJson(jsonArray, listType));

                        }
                    }
            );
        }
        routes = (routesRepo.fetchAllData(false));


        return routes;
    }

    public LiveData<List<UnitsModel>> getUnits(boolean isOnline) {
        if (units == null) {
            units = new MutableLiveData();
        }
        if (isOnline) {
            if (isOnline) {
                Request.Companion.getResponse(ApiConstants.Companion.getUnits(), getTraderUnitObject(), "", new ResponseCallback() {
                            @Override
                            public void response(ResponseModel responseModel) {
                                JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                                Type listType = new TypeToken<LinkedList<UnitsModel>>() {
                                }.getType();
                                // routesModel = ;
                                Log.d("ReTrUp", "routes update called");
                                unitsRepo.insertMultipleUnits(gson.fromJson(jsonArray, listType));


                            }

                            @Override
                            public void response(ResponseObject responseModel) {
                                JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                                Type listType = new TypeToken<LinkedList<UnitsModel>>() {
                                }.getType();
                                // routesModel = ;
                                Log.d("ReTrUp", "routes update called");
                                unitsRepo.insertMultipleUnits(gson.fromJson(jsonArray, listType));

                            }
                        }
                );
            }
        }
        units = (unitsRepo.fetchAllData(false));


        return units;
    }

    public LiveData<List<Cycles>> getCycles(boolean isOnline) {
        if (cycles == null) {
            cycles = new MutableLiveData();
        }
        if (isOnline) {
            if (isOnline) {
                Request.Companion.getResponse(ApiConstants.Companion.getCycles(), getTraderCycleObject(), "", new ResponseCallback() {
                            @Override
                            public void response(ResponseModel responseModel) {
                                JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                                Type listType = new TypeToken<LinkedList<Cycles>>() {
                                }.getType();
                                // routesModel = ;
                                Log.d("ReTrUp", "routes update called");
                                cyclesRepo.insert(gson.fromJson(jsonArray, listType));


                            }

                            @Override
                            public void response(ResponseObject responseModel) {
                                JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                                Type listType = new TypeToken<LinkedList<Cycles>>() {
                                }.getType();
                                // routesModel = ;
                                Log.d("ReTrUp", "routes update called");
                                cyclesRepo.insert(gson.fromJson(jsonArray, listType));

                            }
                        }
                );
            }
        }
        cycles = (cyclesRepo.fetchAllData(false));


        return cycles;
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
                    farmers = (farmerRepo.fetchAllData(fetchFromOnline));
                }

                @Override
                public void response(ResponseObject responseModel) {

                }
            });

        } else {
            farmers = (farmerRepo.fetchAllData(fetchFromOnline));


        }


    }

    private JSONObject getTraderRoutesObject() {


        Gson gson = new Gson();
        RPFSearchModel rpfSearchModel = new RPFSearchModel();
        rpfSearchModel.setEntitycode(new PrefrenceManager(application).getTraderModel().getCode());
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(rpfSearchModel));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject getTraderUnitObject() {


        Gson gson = new Gson();
        RPFSearchModel rpfSearchModel = new RPFSearchModel();
        rpfSearchModel.setAll("1");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(rpfSearchModel));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject getTraderCycleObject() {


        Gson gson = new Gson();
        RPFSearchModel rpfSearchModel = new RPFSearchModel();
        rpfSearchModel.setAll("1");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(rpfSearchModel));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public LiveData<ResponseModel> createRoute(RoutesModel routesModel, JSONObject jsonObject, boolean b) {
        if (this.createRouteSuccess == null) {
        }
        this.createRouteSuccess = new MutableLiveData();

        if (b) {
            Request.Companion.getResponse(ApiConstants.Companion.getCreateRoutes(), jsonObject, "", new ResponseCallback() {
                @Override
                public void response(ResponseModel responseModel) {
                    createRouteSuccess.setValue(responseModel);
                    routesRepo.insert(routesModel);

                }

                @Override
                public void response(ResponseObject responseModel) {
                    createRouteSuccess.setValue(responseModel);

                    routesRepo.insert(routesModel);

                }
            });

        } else {
            routesRepo.insert(routesModel);
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResultCode(1);
            responseModel.setResultDescription("Added");
            responseModel.setData(null);
            createRouteSuccess.setValue(responseModel);

        }
        return createRouteSuccess;
    }


    public LiveData<ResponseModel> createFarmer(FamerModel famerModel, boolean b) {
        if (this.createFarmerSuccess == null) {
        }
        this.createFarmerSuccess = new MutableLiveData();

        if (b) {
            Request.Companion.getResponse(ApiConstants.Companion.getCreateFarmer(), getFarmerJson(), "", new ResponseCallback() {
                @Override
                public void response(ResponseModel responseModel) {
                    createFarmerSuccess.setValue(responseModel);
                    farmerRepo.insert(famerModel);

                }

                @Override
                public void response(ResponseObject responseModel) {
                    createFarmerSuccess.setValue(responseModel);

                    farmerRepo.insert(famerModel);

                }
            });

        } else {
            farmerRepo.insert(famerModel);
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResultCode(1);
            responseModel.setResultDescription("Farmer added successfully");
            responseModel.setData(null);
            createFarmerSuccess.setValue(responseModel);

        }
        return createFarmerSuccess;
    }

    private JSONObject getFarmerJson() {

        return null;
    }
}
