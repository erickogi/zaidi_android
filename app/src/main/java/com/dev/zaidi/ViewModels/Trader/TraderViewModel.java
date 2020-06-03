package com.dev.zaidi.ViewModels.Trader;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.dev.zaidi.AppConstants;
import com.dev.zaidi.Models.Collection;
import com.dev.zaidi.Models.Cycles;
import com.dev.zaidi.Models.FamerModel;
import com.dev.zaidi.Models.FarmerRouteBalance;
import com.dev.zaidi.Models.NetworkAnalytics;
import com.dev.zaidi.Models.Notifications;
import com.dev.zaidi.Models.Payouts;
import com.dev.zaidi.Models.ProductsModel;
import com.dev.zaidi.Models.RPFSearchModel;
import com.dev.zaidi.Models.ResponseModel;
import com.dev.zaidi.Models.ResponseObject;
import com.dev.zaidi.Models.RoutesModel;
import com.dev.zaidi.Models.SyncDownObserver;
import com.dev.zaidi.Models.SyncModel;
import com.dev.zaidi.Models.Trader.TraderModel;
import com.dev.zaidi.Models.UnitsModel;
import com.dev.zaidi.Network.ApiConstants;
import com.dev.zaidi.Network.Request;
import com.dev.zaidi.Repos.NotificationRepo;
import com.dev.zaidi.Repos.ProductsRepo;
import com.dev.zaidi.Repos.RoutesRepo;
import com.dev.zaidi.Repos.SyncDownObserverRepo;
import com.dev.zaidi.Repos.Trader.CollectionsRepo;
import com.dev.zaidi.Repos.Trader.CyclesRepo;
import com.dev.zaidi.Repos.Trader.FarmerRepo;
import com.dev.zaidi.Repos.Trader.PayoutsRepo;
import com.dev.zaidi.Repos.Trader.SyncRepo;
import com.dev.zaidi.Repos.Trader.TraderRepo;
import com.dev.zaidi.Repos.Trader.UnitsRepo;
import com.dev.zaidi.Utils.DateTimeUtils;
import com.dev.zaidi.Utils.GeneralUtills;
import com.dev.zaidi.Utils.Logs;
import com.dev.zaidi.Utils.PayoutsCyclesDatesUtills;
import com.dev.zaidi.Utils.PrefrenceManager;
import com.dev.zaidi.Utils.ResponseCallback;
import com.dev.zaidi.Views.Trader.FarmerConst;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;

public class TraderViewModel extends AndroidViewModel

{
    FarmerRepo farmerRepo;
    RoutesRepo routesRepo;
    UnitsRepo unitsRepo;
    CyclesRepo cyclesRepo;
    ProductsRepo productsRepo;
    CollectionsRepo collectionsRepo;
    PayoutsRepo payoutsRepo;
    SyncRepo syncRepo;
    TraderRepo traderRepo;
    NotificationRepo notificationRepo;
    SyncDownObserverRepo syncDownObserverRepo;


    Gson gson = new Gson();
    private MutableLiveData createRouteSuccess;
    private MutableLiveData updateRouteSuccess;
    private MutableLiveData deleteRouteSuccess;

    private MutableLiveData createProductSuccess;
    private MutableLiveData updateProductSuccess;
    private MutableLiveData deleteProductSuccess;

    private MutableLiveData createCollectionSuccess;
    private MutableLiveData updateCollectionSuccess;
    private MutableLiveData deleteCollectionSuccess;



    private LiveData farmer;

    private MutableLiveData createFarmerSuccess;
    private MutableLiveData updateFarmerSuccess;
    private MutableLiveData deleteFarmerSuccess;

    private MutableLiveData unit;
    private LiveData<List<FarmerRouteBalance>> farmers;
    private MutableLiveData route;
    private LiveData<List<UnitsModel>> units;
    private LiveData cycle;
    private Cycles cycle0ne;
    private LiveData<List<RoutesModel>> routes;
    private LiveData<List<Cycles>> cycles;

    private LiveData<List<ProductsModel>> products;
    private MutableLiveData productss;

    private LiveData<List<Collection>> collections;
    private List<Collection> collectionslist;
    private Collection collection;


    private LiveData<List<Payouts>> payouts;
    private LiveData<Payouts> payout;
    private Payouts payoutOne;

    LiveData<Double> milkTotal;
    LiveData<Double> milkTotalLtrs;
    LiveData<Double> milkTotalKsh;



    private Application application;
    private PrefrenceManager prefrenceManager;


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
        productsRepo = new ProductsRepo(application);
        collectionsRepo = new CollectionsRepo(application);
        payoutsRepo = new PayoutsRepo(application);
        prefrenceManager = new PrefrenceManager(application);
        syncRepo = new SyncRepo(application);
        traderRepo = new TraderRepo(application);
        notificationRepo = new NotificationRepo(application);

        syncDownObserverRepo = new SyncDownObserverRepo(application);


//
        payouts = new MutableLiveData<>();
        payout = new MutableLiveData<>();
        payoutOne = new Payouts();

//
//
    }

    public LiveData<SyncDownObserver> observeSyncDown() {
        return syncDownObserverRepo.getObserver();
    }

    public LiveData<List<Notifications>> getNotifications(int viewd) {
        return notificationRepo.fetchAllData(viewd);
    }

    public LiveData<List<Notifications>> getNotifications() {
        return notificationRepo.fetchAllData();
    }

    public void insertNotification(Notifications notifications) {
        notificationRepo.insert(notifications);
    }

    public void updateNotification(Notifications notifications) {
        notificationRepo.upDateRecord(notifications);
    }
    public LiveData<TraderModel> getTrader(String traderCode) {
        return traderRepo.getTraderByCode(traderCode);
    }

    public void updateTrader(TraderModel traderModel) {
        traderRepo.upDateRecord(traderModel);
    }

    public void updateTraderDirect(TraderModel traderModel) {
        traderRepo.upDateRecordDirect(traderModel);
    }

    public void createTrader(TraderModel traderModel) {
        traderRepo.insert(traderModel);
    }

    public LiveData<List<SyncModel>> fetchAll() {
        return syncRepo.fetchAllData(false);
    }

    public LiveData<List<SyncModel>> fetchByStatus(int status) {
        return syncRepo.getAllByStatus(status);
    }

    public LiveData<SyncModel> fetchById(int id) {
        return syncRepo.getSynce(id);
    }

    public void createSync(SyncModel model) {
        syncRepo.insert(model);

    }

    public void updateSync(SyncModel syncModel) {
        syncRepo.upDateRecord(syncModel);

    }

    public void deleteSync(SyncModel syncModel) {
        syncRepo.deleteRecord(syncModel);

    }



    public void synch(int action, int entity, Object o, List<ProductsModel> objects, int type) {
        Gson gson = new Gson();
        SyncModel syncModel = new SyncModel();
        syncModel.setActionType(action);
        syncModel.setObjectData(o);
        //syncModel.setObject(new Gson().toJson(o));
        syncModel.setDataType(type);
        syncModel.setEntityType(entity);
        syncModel.setSyncStatus("");
        syncModel.setTimeStamp(DateTimeUtils.Companion.getNow());
        syncModel.setSyncTime("");
        syncModel.setTraderCode(prefrenceManager.getTraderModel().getCode());
        switch (action) {
            case AppConstants.INSERT:
                syncModel.setActionTypeName("Insert");

                break;
            case AppConstants.UPDATE:
                syncModel.setActionTypeName("Update");
                break;
            case AppConstants.DELETE:
                syncModel.setActionTypeName("Delete");
                break;

        }
        switch (entity) {
            case AppConstants.ENTITY_FARMER:
                syncModel.setEntityTypeName("Farmer");

                syncModel.setObject(new Gson().toJson(o, FamerModel.class));
                //syncModel.setObjectData(o);

                break;
            case AppConstants.ENTITY_PRODUCTS:
                syncModel.setEntityTypeName("Products");

                if (type == 1) {
                    syncModel.setObject(new Gson().toJson(o, ProductsModel.class));
                    // syncModel.setObjectData(o);

                } else {
                    JsonArray jsonArray = gson.toJsonTree(objects).getAsJsonArray();
                    Type listType = new TypeToken<LinkedList<ProductsModel>>() {
                    }.getType();


                    syncModel.setObjects(gson.toJson(jsonArray));


                }

                break;
            case AppConstants.ENTITY_PAYOUTS:
                syncModel.setEntityTypeName("Payout");
                syncModel.setObject(new Gson().toJson(o, Payouts.class));

                break;
            case AppConstants.ENTITY_COLLECTION:
                syncModel.setEntityTypeName("Collection");
                syncModel.setObject(new Gson().toJson(o, Collection.class));

                break;
            case AppConstants.ENTITY_ROUTES:
                syncModel.setEntityTypeName("Route");
                syncModel.setObject(new Gson().toJson(o, RoutesModel.class));

                break;
            case AppConstants.ENTITY_TRADER:

                syncModel.setEntityTypeName("Trader");
                syncModel.setObject(new Gson().toJson(o, TraderModel.class));

                break;
        }

        createSync(syncModel);
    }

    public LiveData<Double> getSumOfMilkForPayoutKsh(String farmercode, String payoutCode) {
        if (milkTotalKsh == null) {
            milkTotalKsh = new MutableLiveData<>();
        }
        milkTotalKsh = collectionsRepo.getSumOfMilkFarmerPayoutKsh(farmercode, payoutCode);

        return milkTotalKsh;
    }

    public Double getSumOfMilkForPayoutKshD(String farmercode, String payoutCode) {

        return collectionsRepo.getSumOfMilkFarmerPayoutKshD(farmercode, payoutCode);

        //return milkTotalKsh;
    }


    public Double getSumOfMilkFarmerByApproveStatusKsh(String farmercode, int status) {

        return collectionsRepo.getSumOfMilkFarmerByApproveStatusKsh(farmercode, status);

        //return milkTotalKsh;
    }

    public Double getSumOfMilkFarmerByApproveStatusLtrs(String farmercode, int status) {

        return collectionsRepo.getSumOfMilkFarmerByApproveStatusLtrs(farmercode, status);

        //return milkTotalKsh;
    }

    public Collection getLastCollection(String cyclecode) {


        return collectionsRepo.getLast(cyclecode);
    }

    public LiveData<ResponseModel> getProductsModels(JSONObject jsonObject, boolean fetchFromOnline) {

        if (this.productss == null) {
            this.productss = new MutableLiveData();
            if (fetchFromOnline) {
                Request.Companion.getResponse(getApplication().getBaseContext(),ApiConstants.Companion.getProducts(), jsonObject, "", new ResponseCallback() {
                            @Override
                            public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                                productss.setValue(responseModel);
                            }

                            @Override
                            public void response(ResponseObject responseModel, NetworkAnalytics analytics) {

                                productss.setValue(responseModel);
                            }
                        }
                );

            } else {


                // farmers = (productsRepo.fetchAllData(false));

            }


        }


        return productss;
    }

    public LiveData<List<ProductsModel>> getProductsModels(int status) {


        return productsRepo.getAllByStatus(status);


    }

    public List<ProductsModel> getProductsModelsOne(int status) {


        return productsRepo.getAllByStatusOne(status);


    }


    public List<FamerModel> fetchAllFarmers() {
        return farmerRepo.fetchAll();
    }

    public LiveData<List<FarmerRouteBalance>> getFarmerByStatusRouteJoined(int status, String route) {

        Timber.d("Status " + status + "\n Route " + route);

        if (farmers == null) {
            farmers = new MutableLiveData();
        }

        switch (status) {
            case FarmerConst.ACTIVE:

                return (farmerRepo.getFarmersByStatusRouteJoined(0, 0, 0, route));
            case FarmerConst.DELETED:
                return (farmerRepo.getFarmersByStatusRouteDeletedJoined(1, route));
            case FarmerConst.DUMMY:
                return (farmerRepo.getFarmersByStatusRouteDummyJoined(1, route));
            case FarmerConst.ARCHIVED:
                return (farmerRepo.getFarmersByStatusRouteArchivedJoined(1, route));
            case FarmerConst.ALL:
                return (farmerRepo.fetchAllDataJoined(false, route));
            default:
                return (farmerRepo.fetchAllDataJoined(false));


        }



    }

    public LiveData<List<FamerModel>> getFarmerByStatusRoute(int status, String route) {

        Timber.d("Status " + status + "\n Route " + route);

        if (farmers == null) {
            farmers = new MutableLiveData();
        }

        switch (status) {
            case FarmerConst.ACTIVE:

                return (farmerRepo.getFarmersByStatusRoute(0, 0, 0, route));
            case FarmerConst.DELETED:
                return (farmerRepo.getFarmersByStatusRouteDeleted(1, route));
            case FarmerConst.DUMMY:
                return (farmerRepo.getFarmersByStatusRouteDummy(1, route));
            case FarmerConst.ARCHIVED:
                return (farmerRepo.getFarmersByStatusRouteArchived(1, route));
            case FarmerConst.ALL:
                return (farmerRepo.fetchAllData(false, route));
            default:
                return (farmerRepo.fetchAllData(false));


        }




    }


    public LiveData<FamerModel> getFarmersByCode(String code) {

        return farmerRepo.getFramerByCode(code);
    }

    public FamerModel getFarmersByCodeOne(String code) {

        return farmerRepo.getFramerByCodeOne(code);
    }

    public int getFarmersCountByCycle(String code) {
        return (farmerRepo.getFarmersCountByCycle(code));


    }

    public int getProductsCount() {
        return (productsRepo.getCount());


    }

    public LiveData<Integer> getProductsCountLive() {
        return (productsRepo.getCountLive());


    }

    public LiveData<Integer> getRoutesCountLive() {
        return (routesRepo.getCount());


    }






    public LiveData<FamerModel> getLastFarmer() {
        if (farmer == null) {
            farmer = new MutableLiveData();
            farmer = (farmerRepo.getLastFarmer());
        }

        return farmer;
    }


    public LiveData<List<RoutesModel>> getRoutes(boolean isOnline) {
        if (routes == null) {
            routes = new MutableLiveData();
        }

        if (isOnline) {
            Request.Companion.getResponse(getApplication().getBaseContext(),ApiConstants.Companion.getTraderRoutes(), getTraderRoutesObject(), "", new ResponseCallback() {
                        @Override
                        public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                            if (responseModel.getResultCode() == 1) {
                                JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                                Type listType = new TypeToken<LinkedList<RoutesModel>>() {
                                }.getType();
                                routesRepo.insertMultipleRoutes(gson.fromJson(jsonArray, listType));

                            }

                        }

                        @Override
                        public void response(ResponseObject responseModel, NetworkAnalytics analytics) {
                            JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                            Type listType = new TypeToken<LinkedList<RoutesModel>>() {
                            }.getType();
                            // routesModel = ;
                            Timber.d("routes update called");
                            routesRepo.insertMultipleRoutes(gson.fromJson(jsonArray, listType));

                        }
                    }
            );
        }
        routes = (routesRepo.fetchAllData(false));


        return routes;
    }

    public LiveData<List<ProductsModel>> getProducts(boolean isOnline) {
        if (products == null) {
            products = new MutableLiveData();
        }


        products = productsRepo.fetchAllData(false);


        return products;
    }


    public LiveData<List<UnitsModel>> getUnits(boolean isOnline) {
        if (units == null) {
            units = new MutableLiveData();
        }
        if (isOnline) {
            if (isOnline) {
                Request.Companion.getResponse(getApplication().getBaseContext(),ApiConstants.Companion.getUnits(), getTraderUnitObject(), "", new ResponseCallback() {
                            @Override
                            public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                                JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                                Type listType = new TypeToken<LinkedList<UnitsModel>>() {
                                }.getType();
                                // routesModel = ;
                                Timber.d("routes update called");
                                unitsRepo.insertMultipleUnits(gson.fromJson(jsonArray, listType));


                            }

                            @Override
                            public void response(ResponseObject responseModel, NetworkAnalytics analytics) {
                                JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                                Type listType = new TypeToken<LinkedList<UnitsModel>>() {
                                }.getType();
                                // routesModel = ;
                                Timber.d("routes update called");
                                unitsRepo.insertMultipleUnits(gson.fromJson(jsonArray, listType));

                            }
                        }
                );
            }
        }
        units = (unitsRepo.fetchAllData(false));


        return units;
    }

    public List<UnitsModel> getUnits1(boolean isOnline) {


        return unitsRepo.getUnits();


        //return units;
    }

    public LiveData<List<Cycles>> getCycles(boolean isOnline) {
        if (cycles == null) {
            cycles = new MutableLiveData();
        }
        if (isOnline) {
            if (isOnline) {
                Request.Companion.getResponse(getApplication().getBaseContext(),ApiConstants.Companion.getCycles(), getTraderCycleObject(), "", new ResponseCallback() {
                            @Override
                            public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                                try {
                                    JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                                    Type listType = new TypeToken<LinkedList<Cycles>>() {
                                    }.getType();
                                    // routesModel = ;
                                    Timber.d("routes update called");
                                    cyclesRepo.insert(gson.fromJson(jsonArray, listType));

                                } catch (Exception mn) {

                                }


                            }

                            @Override
                            public void response(ResponseObject responseModel, NetworkAnalytics analytics) {
                                JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                                Type listType = new TypeToken<LinkedList<Cycles>>() {
                                }.getType();
                                // routesModel = ;
                                Timber.d("routes update called");
                                cyclesRepo.insert(gson.fromJson(jsonArray, listType));

                            }
                        }
                );
            }
        }
        cycles = (cyclesRepo.fetchAllData(false));


        return cycles;
    }

    public LiveData<Cycles> getCycles(String code, boolean isOnline) {
        if (cycle == null) {
            cycle = new MutableLiveData()
            ;
        }
        if (isOnline) {
            if (isOnline) {
                Request.Companion.getResponse(getApplication().getBaseContext(),ApiConstants.Companion.getCycles(), getTraderCycleObject(), "", new ResponseCallback() {
                            @Override
                            public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                                JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                                Type listType = new TypeToken<LinkedList<Cycles>>() {
                                }.getType();
                                // routesModel = ;
                                Timber.d("routes update called");
                                cyclesRepo.insert(gson.fromJson(jsonArray, listType));


                            }

                            @Override
                            public void response(ResponseObject responseModel, NetworkAnalytics analytics) {
                                JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                                Type listType = new TypeToken<LinkedList<Cycles>>() {
                                }.getType();
                                // routesModel = ;
                                Timber.d("routes update called");
                                cyclesRepo.insert(gson.fromJson(jsonArray, listType));

                            }
                        }
                );
            }
        }
        cycle = (cyclesRepo.getCycleByKeyCode(code, false));


        return cycle;
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

    public int noOfFarmersPerRoute(String routecode) {
        return farmerRepo.getNoOfRows(routecode);
    }


    public LiveData<ResponseModel> createRoute(RoutesModel routesModel, JSONObject jsonObject, boolean b) {
        if (this.createRouteSuccess == null) {
        }
        this.createRouteSuccess = new MutableLiveData();

        if (b) {
            Request.Companion.getResponse(getApplication().getBaseContext(),ApiConstants.Companion.getCreateRoutes(), jsonObject, "", new ResponseCallback() {
                @Override
                public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                    createRouteSuccess.setValue(responseModel);
                    routesRepo.insert(routesModel);

                }

                @Override
                public void response(ResponseObject responseModel, NetworkAnalytics analytics) {
                    createRouteSuccess.setValue(responseModel);

                    routesRepo.insert(routesModel);

                }
            });

        } else {
            routesModel.setTraderCode(prefrenceManager.getTraderModel().getCode());

            synch(AppConstants.INSERT, AppConstants.ENTITY_ROUTES, routesModel, null, 1);

            routesRepo.insert(routesModel);
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResultCode(1);
            responseModel.setResultDescription("Added");
            responseModel.setData(null);
            createRouteSuccess.setValue(responseModel);

        }
        return createRouteSuccess;
    }

    public LiveData<List<FamerModel>> getAllByRouteCode(String routeCode) {
        return farmerRepo.getFramersByRoute(routeCode);
    }
    public LiveData<ResponseModel> updateRoute(RoutesModel routesModel, JSONObject jsonObject, boolean b) {
        if (this.updateRouteSuccess == null) {
        }
        this.updateRouteSuccess = new MutableLiveData();

        if (b) {
            Request.Companion.getResponse(getApplication().getBaseContext(),ApiConstants.Companion.getCreateRoutes(), jsonObject, "", new ResponseCallback() {
                @Override
                public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                    updateRouteSuccess.setValue(responseModel);
                    routesRepo.insert(routesModel);

                }

                @Override
                public void response(ResponseObject responseModel, NetworkAnalytics analytics) {
                    updateRouteSuccess.setValue(responseModel);

                    routesRepo.insert(routesModel);

                }
            });

        } else {
            routesModel.setTraderCode(prefrenceManager.getTraderModel().getCode());

            routesRepo.upDateRecord(routesModel);


            synch(AppConstants.UPDATE, AppConstants.ENTITY_ROUTES, routesModel, null, 1);

            ResponseModel responseModel = new ResponseModel();
            responseModel.setResultCode(1);
            responseModel.setResultDescription("Updated");
            responseModel.setData(null);
            updateRouteSuccess.setValue(responseModel);

        }
        return updateRouteSuccess;
    }

    public LiveData<ResponseModel> deleteRoute(RoutesModel routesModel, JSONObject jsonObject, boolean b) {
        if (this.deleteRouteSuccess == null) {
        }

        this.deleteRouteSuccess = new MutableLiveData();

        if (b) {
            Request.Companion.getResponse(getApplication().getBaseContext(),ApiConstants.Companion.getCreateRoutes(), jsonObject, "", new ResponseCallback() {
                @Override
                public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                    deleteRouteSuccess.setValue(responseModel);
                    routesRepo.insert(routesModel);

                }

                @Override
                public void response(ResponseObject responseModel, NetworkAnalytics analytics) {
                    deleteRouteSuccess.setValue(responseModel);

                    routesRepo.insert(routesModel);

                }
            });

        } else {
            routesModel.setTraderCode(prefrenceManager.getTraderModel().getCode());

            routesRepo.deleteRecord(routesModel);

            synch(AppConstants.DELETE, AppConstants.ENTITY_ROUTES, routesModel, null, 1);
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResultCode(1);
            responseModel.setResultDescription("Deleted");
            responseModel.setData(null);
            deleteRouteSuccess.setValue(responseModel);

        }
        return deleteRouteSuccess;
    }



    public LiveData<ResponseModel> createProducts(List<ProductsModel> productsModels, boolean b) {
        if (this.createProductSuccess == null) {
        }

        this.createProductSuccess = new MutableLiveData();

        if (b) {

        } else {
            for (int i = 0; i < productsModels.size(); i++) {
                productsModels.get(i).setTraderCode(prefrenceManager.getTraderModel().getCode());
            }

            synch(AppConstants.UPDATE, AppConstants.ENTITY_PRODUCTS, null, productsModels, 2);

            if (productsRepo.insert(productsModels)) {
                ResponseModel responseModel = new ResponseModel();
                responseModel.setResultCode(1);
                responseModel.setResultDescription("Added");
                responseModel.setData(null);
                createProductSuccess.setValue(responseModel);
            } else {

            }

        }
        return createProductSuccess;
    }

    public LiveData<ResponseModel> createCollections(Collection collection) {

        ResponseModel responseModel = new ResponseModel();

        collection.setTraderCode(prefrenceManager.getTraderModel().getCode());


        if (this.createCollectionSuccess == null) {
            this.createCollectionSuccess = new MutableLiveData();

        }


        Payouts p = getLastPayout(collection.getCycleCode());
        Payouts plastIfOne = getLastPayout();


        Cycles c = getCycleO(collection.getCycleCode());
        if (c == null) {
            c = insertCycles(collection.getCycleCode());

        }
        int farmerCountPerCycle = getFarmersCountByCycle(collection.getCycleCode());
        int tradersStartDay = prefrenceManager.getTraderModel().getCycleStartDayNumber();
        int tradersEndDay = prefrenceManager.getTraderModel().getCycleEndDayNumber();


        Payouts payouts = new Payouts();
        payouts.setCycleCode(collection.getCycleCode());
        payouts.setCyclename(getCycleName(collection.getCycleCode()));
        payouts.setFarmersCount("" + farmerCountPerCycle);
        payouts.setStatus(0);
        payouts.setCode(GeneralUtills.Companion.createCode());
        PayoutsCyclesDatesUtills.EndAndStart endAndStart = new PayoutsCyclesDatesUtills.EndAndStart();


        payouts.setApprovedCards("");
        payouts.setMilkTotal("");
        payouts.setBalance("");
        payouts.setLoanTotal("");


        if (p == null) {
            endAndStart = PayoutsCyclesDatesUtills.getPayoutStartEndDate(c.getCode(), new PayoutsCyclesDatesUtills.EndAndStart(tradersStartDay, tradersEndDay), null);
            payouts.setStartDate(endAndStart.getStartDate());
            payouts.setEndDate(endAndStart.getEndDate());


            if (plastIfOne != null) {
                payouts.setPayoutnumber(plastIfOne.getPayoutnumber() + 1);

            } else {
                payouts.setPayoutnumber(1);

            }


            insertPayout(payouts);


            collection.setCycleStartedOn(payouts.getStartDate());
            collection.setPayoutnumber(payouts.getPayoutnumber());
            collection.setPayoutCode(payouts.getCode());



            responseModel.setResultCode(1);
            responseModel.setResultDescription("Collection Inserted \nNew  payout \n(No other  payouts available)");
            responseModel.setData(null);
            responseModel.setPayoutCode(payouts.getCode());




        } else {


            if (DateTimeUtils.Companion.isPastLastDay(p.getEndDate())) {


                endAndStart = PayoutsCyclesDatesUtills.getPayoutStartEndDate(c.getCode(), new PayoutsCyclesDatesUtills.EndAndStart(tradersStartDay, tradersEndDay), new PayoutsCyclesDatesUtills.EndAndStart(p.getStartDate(), p.getEndDate()));
                payouts.setStartDate(endAndStart.getStartDate());
                payouts.setEndDate(endAndStart.getEndDate());
                payouts.setPayoutnumber(plastIfOne.getPayoutnumber() + 1);

                insertPayout(payouts);



                collection.setCycleStartedOn(payouts.getStartDate());
                collection.setPayoutnumber(payouts.getPayoutnumber());
                collection.setPayoutCode(payouts.getCode());

                responseModel.setResultCode(1);
                responseModel.setResultDescription("Collection Inserted \nNew  payout \nOther cycles payouts available");
                responseModel.setData(null);
                responseModel.setPayoutCode(payouts.getCode());



            } else {
                collection.setCycleStartedOn(p.getStartDate());
                collection.setPayoutnumber(p.getPayoutnumber());
                collection.setPayoutCode(p.getCode());

                responseModel.setResultCode(1);
                responseModel.setResultDescription("Collection Inserted \nExisting payout");
                responseModel.setData(null);
                responseModel.setPayoutCode(p.getCode());


            }


        }

        if (collection.getPayoutCode() != null) {
            responseModel.setPayoutCode(collection.getPayoutCode());
            collectionsRepo.insert(collection);

            synch(AppConstants.INSERT, AppConstants.ENTITY_COLLECTION, collection, null, 1);


        } else {
            responseModel.setResultCode(0);
            responseModel.setResultDescription("Collection not updated");

        }

        createCollectionSuccess.setValue(responseModel);
        return createCollectionSuccess;
    }

    public ResponseModel createCollectionsU(Collection collection) {

        ResponseModel responseModel = new ResponseModel();
        collection.setTraderCode(prefrenceManager.getTraderModel().getCode());
        Payouts p = getLastPayout(collection.getCycleCode());
        Payouts plastIfOne = getLastPayout();
        Cycles c = getCycleO(collection.getCycleCode());
        if (c == null) {
            c = insertCycles(collection.getCycleCode());

        }
        int farmerCountPerCycle = getFarmersCountByCycle(collection.getCycleCode());
        int tradersStartDay = prefrenceManager.getTraderModel().getCycleStartDayNumber();
        int tradersEndDay = prefrenceManager.getTraderModel().getCycleEndDayNumber();


        Payouts payouts = new Payouts();
        payouts.setCycleCode(collection.getCycleCode());
        payouts.setCyclename(getCycleName(collection.getCycleCode()));
        payouts.setFarmersCount("" + farmerCountPerCycle);
        payouts.setStatus(0);
        payouts.setCode(GeneralUtills.Companion.createCode());
        PayoutsCyclesDatesUtills.EndAndStart endAndStart = new PayoutsCyclesDatesUtills.EndAndStart();


        payouts.setApprovedCards("");
        payouts.setMilkTotal("");
        payouts.setBalance("");
        payouts.setLoanTotal("");


        if (p == null) {
            endAndStart = PayoutsCyclesDatesUtills.getPayoutStartEndDate(c.getCode(), new PayoutsCyclesDatesUtills.EndAndStart(tradersStartDay, tradersEndDay), null);
            payouts.setStartDate(endAndStart.getStartDate());
            payouts.setEndDate(endAndStart.getEndDate());


            if (plastIfOne != null) {
                payouts.setPayoutnumber(plastIfOne.getPayoutnumber() + 1);

            } else {
                payouts.setPayoutnumber(1);

            }

            final Runnable r = () -> insertPayout(payouts);
            r.run();


            collection.setCycleStartedOn(payouts.getStartDate());
            collection.setPayoutnumber(payouts.getPayoutnumber());
            collection.setPayoutCode(payouts.getCode());

            responseModel.setResultCode(1);
            responseModel.setResultDescription("Collection Inserted \nNew  payout \n(No other  payouts available)");
            responseModel.setData(null);
            responseModel.setPayoutCode(payouts.getCode());




        } else {


            if (DateTimeUtils.Companion.isPastLastDay(p.getEndDate())) {


                endAndStart = PayoutsCyclesDatesUtills.getPayoutStartEndDate(c.getCode(), new PayoutsCyclesDatesUtills.EndAndStart(tradersStartDay, tradersEndDay), new PayoutsCyclesDatesUtills.EndAndStart(p.getStartDate(), p.getEndDate()));
                payouts.setStartDate(endAndStart.getStartDate());
                payouts.setEndDate(endAndStart.getEndDate());
                payouts.setPayoutnumber(plastIfOne.getPayoutnumber() + 1);

                final Runnable r = () -> insertPayout(payouts);
                r.run();


                collection.setCycleStartedOn(payouts.getStartDate());
                collection.setPayoutnumber(payouts.getPayoutnumber());
                collection.setPayoutCode(payouts.getCode());

                responseModel.setResultCode(1);
                responseModel.setResultDescription("Collection Inserted \nNew  payout \nOther cycles payouts available");
                responseModel.setData(null);
                responseModel.setPayoutCode(payouts.getCode());


            } else {
                collection.setCycleStartedOn(p.getStartDate());
                collection.setPayoutnumber(p.getPayoutnumber());
                collection.setPayoutCode(p.getCode());

                responseModel.setResultCode(1);
                responseModel.setResultDescription("Collection Inserted \nExisting payout");
                responseModel.setData(null);
                responseModel.setPayoutCode(p.getCode());


            }


        }

        if (collection.getPayoutCode() != null) {
            responseModel.setPayoutCode(collection.getPayoutCode());
            collectionsRepo.insert(collection);
            final Runnable r = () -> synch(AppConstants.INSERT, AppConstants.ENTITY_COLLECTION, collection, null, 1);
            r.run();


        } else {
            responseModel.setResultCode(0);
            responseModel.setResultDescription("Collection not updated");

        }


        return responseModel;
    }


    public ResponseModel updateCollection(Collection c) {
        ResponseModel responseModel = new ResponseModel();

        if (c != null && c.getPayoutCode() != null) {

            c.setTraderCode(prefrenceManager.getTraderModel().getCode());

            collectionsRepo.upDateRecord(c);
            synch(AppConstants.UPDATE, AppConstants.ENTITY_COLLECTION, c, null, 1);


            this.updateCollectionSuccess = new MutableLiveData();
            responseModel.setResultCode(1);
            responseModel.setPayoutCode(c.getPayoutCode());
            responseModel.setResultDescription("Farmer updated successfully");

        } else {
            responseModel.setResultCode(0);
            responseModel.setResultDescription("Collection not updated");

        }


        responseModel.setData(null);


        // updateCollectionSuccess.setValue(responseModel);

        return responseModel;

    }

    public void updateCollectionLocalOnly(Collection c) {
        ResponseModel responseModel = new ResponseModel();

        if (c != null && c.getPayoutCode() != null) {

            c.setTraderCode(prefrenceManager.getTraderModel().getCode());

            collectionsRepo.upDateRecord(c);
            // synch(AppConstants.UPDATE, AppConstants.ENTITY_COLLECTION, c, null, 1);


            this.updateCollectionSuccess = new MutableLiveData();
            responseModel.setResultCode(1);
            responseModel.setPayoutCode(c.getPayoutCode());
            responseModel.setResultDescription("Farmer updated successfully");

        } else {
            responseModel.setResultCode(0);
            responseModel.setResultDescription("Collection not updated");

        }


        responseModel.setData(null);


        // updateCollectionSuccess.setValue(responseModel);

        // return responseModel;

    }



    private String getCycleName(String cycleCode) {
        if (cycleCode.equals("1")) {
            return AppConstants.SEVENDAYS;
        } else if (cycleCode.equals("2")) {
            return AppConstants.FIFTEENDAYS;
        } else if (cycleCode.equals("3")) {
            return AppConstants.THIRTYDAYS;
        } else {
            return "";
        }
    }

    private Cycles insertCycles(String cycleCode) {
        Cycles cycles = new Cycles();
        cycles.setCode(1);
        cycles.setCycle(AppConstants.SEVENDAYS);
        cycles.setPeriod("7");
        cycles.setStatus("1");

        Cycles cycles1 = new Cycles();
        cycles1.setCode(2);
        cycles1.setCycle(AppConstants.FIFTEENDAYS);
        cycles1.setPeriod("14");
        cycles1.setStatus("1");


        Cycles cycles2 = new Cycles();
        cycles2.setCode(3);
        cycles2.setCycle(AppConstants.THIRTYDAYS);
        cycles2.setPeriod("30");
        cycles2.setStatus("1");



        List<Cycles> cycles4 = new LinkedList<>();
        cycles4.add(cycles);
        cycles4.add(cycles1);
        cycles4.add(cycles2);

        cyclesRepo.insert(cycles4);

        if (cycleCode.equals("1")) {
            return cycles;
        } else if (cycleCode.equals("2")) {
            return cycles1;
        } else if (cycleCode.equals("3")) {
            return cycles2;
        } else {
            return null;
        }
    }

    public LiveData<ResponseModel> updateProduct(ProductsModel productsModel, boolean b) {
        if (this.updateProductSuccess == null) {
        }

        this.updateProductSuccess = new MutableLiveData();

        if (b) {

        } else {
            productsModel.setTraderCode(prefrenceManager.getTraderModel().getCode());
            synch(AppConstants.UPDATE, AppConstants.ENTITY_PRODUCTS, productsModel, null, 1);

            productsRepo.upDateRecord(productsModel);
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResultCode(1);
            responseModel.setResultDescription("Updated");
            responseModel.setData(null);
            updateProductSuccess.setValue(responseModel);

        }
        return updateProductSuccess;
    }

    public LiveData<ResponseModel> deleteProduct(ProductsModel productsModel, boolean b) {
        if (this.deleteProductSuccess == null) {
        }
        this.deleteProductSuccess = new MutableLiveData();

        if (b) {

        } else {
            productsModel.setTraderCode(prefrenceManager.getTraderModel().getCode());

            synch(AppConstants.DELETE, AppConstants.ENTITY_PRODUCTS, productsModel, null, 1);

            productsRepo.deleteRecord(productsModel);
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResultCode(1);
            responseModel.setResultDescription("Deleted");
            responseModel.setData(null);
            deleteProductSuccess.setValue(responseModel);

        }
        return deleteProductSuccess;
    }



    public LiveData<ResponseModel> createFarmer(FamerModel famerModel, boolean b) {

        if (this.createFarmerSuccess == null) {
        }
        this.createFarmerSuccess = new MutableLiveData();

        if (b) {
            Request.Companion.getResponse(getApplication().getBaseContext(),ApiConstants.Companion.getCreateFarmer(), getFarmerJson(), "", new ResponseCallback() {
                @Override
                public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                    createFarmerSuccess.setValue(responseModel);
                    farmerRepo.insert(famerModel);

                }

                @Override
                public void response(ResponseObject responseModel, NetworkAnalytics analytics) {
                    createFarmerSuccess.setValue(responseModel);

                    farmerRepo.insert(famerModel);

                }
            });

        } else {
            farmerRepo.insert(famerModel);
            famerModel.setTraderCode(prefrenceManager.getTraderModel().getCode());

            synch(AppConstants.INSERT, AppConstants.ENTITY_FARMER, famerModel, null, 1);
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResultCode(1);
            responseModel.setResultDescription("Farmer added successfully");
            responseModel.setData(null);
            createFarmerSuccess.setValue(responseModel);

        }
        return createFarmerSuccess;
    }


    public LiveData<ResponseModel> deleteFarmer(FamerModel famerModel) {
        if (this.deleteFarmerSuccess == null) {
        }
        this.deleteFarmerSuccess = new MutableLiveData();


            farmerRepo.deleteRecord(famerModel);
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResultCode(1);
            responseModel.setResultDescription("Farmer deleted successfully");
            responseModel.setData(null);
            deleteFarmerSuccess.setValue(responseModel);


        return deleteFarmerSuccess;
    }

    public void updateFarmer(String from,FamerModel famerModel) {
        Logs.Companion.d("updatefarmer","D -> "+from,famerModel);

        farmerRepo.upDateRecordDirect(famerModel);
    }

    public ResponseModel updateFarmer(String from,FamerModel famerModel, boolean isOnline, boolean isFarmerProfileUpdate) {
        Logs.Companion.d("updatefarmer","N -> "+from,famerModel);

        if (this.updateFarmerSuccess == null) {
        }
        ResponseModel responseModel = new ResponseModel();

        this.updateFarmerSuccess = new MutableLiveData();

        if (isOnline) {
            Request.Companion.getResponse(getApplication().getBaseContext(),ApiConstants.Companion.getCreateFarmer(), getFarmerJson(), "", new ResponseCallback() {
                @Override
                public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                    updateFarmerSuccess.setValue(responseModel);
                    farmerRepo.upDateRecord(famerModel);

                }

                @Override
                public void response(ResponseObject responseModel, NetworkAnalytics analytics) {
                    updateFarmerSuccess.setValue(responseModel);

                    farmerRepo.upDateRecord(famerModel);

                }
            });

        } else {
//            famerModel.setTraderCode(prefrenceManager.getTraderModel().getCode());


            farmerRepo.upDateRecord(famerModel);

            if (isFarmerProfileUpdate) {
                synch(AppConstants.UPDATE, AppConstants.ENTITY_FARMER, famerModel, null, 1);
            }
            responseModel.setResultCode(1);
            responseModel.setResultDescription("Farmer updated successfully");
            responseModel.setData(null);
            // updateFarmerSuccess.setValue(responseModel);

        }
        return responseModel;
    }

    public ResponseModel updateFarmer(FamerModel famerModel, boolean isOnline, boolean isFarmerProfileUpdate) {

        return updateFarmer("UnDocumentedSource",famerModel,isOnline,isFarmerProfileUpdate);
    }

    private JSONObject getFarmerJson() {

        return null;
    }


    public List<Collection> getCollectionByDateByFarmer(String code, String date) {
        if (collectionslist == null) {
            collectionslist = new LinkedList<>();
        }
        return collectionsRepo.getCollectionByFarmerByDa(code, date);
    }

    public List<Collection> getCollectionByDateByFarmerByTime(String code, String today, String ampm) {
        if (collectionslist == null) {
            collectionslist = new LinkedList<>();
        }
        return collectionsRepo.getCollectionByDateByFarmerByTime(code, today, ampm);
    }

    public Collection getCollectionByDateByFarmerByTimeSngle(String code, String today, String ampm) {

        return collectionsRepo.getCollectionByDateByFarmerByTimeSingle(code, today, ampm);
    }

    public LiveData<Collection> getCollectionByCode(String collectionCode) {

        return collectionsRepo.getCollectionByCode(collectionCode);
    }

    public Collection getCollectionByDateByFarmerByTimeSngle(String code, String today) {

        return collectionsRepo.getCollectionByDateByFarmerByTimeSingle(code, today);
    }

    public LiveData<Collection> getCollectionByDateByFarmerByTime(String code, String today) {

        return collectionsRepo.getCollectionByDateByFarmerByTime(code, today);
    }


    public LiveData<List<Collection>> getCollectionsBetweenDates(Long date1, Long date2, String code) {
        return collectionsRepo.getCollectionsBetweenDates(date1, date2, code);
    }

    public List<Collection> getCollectionsBetweenDatesOne(Long date1, Long date2, String code) {
        return collectionsRepo.getCollectionsBetweenDatesOne(date1, date2, code);
    }
    public LiveData<List<Collection>> getCollectionByDateByPayout(String payoutnumber) {
        if (collections == null) {
            collections = new MutableLiveData<>();
        }
        return collectionsRepo.getCollectionByPayout(payoutnumber);
    }


    public LiveData<List<Payouts>> fetchAll(boolean isOnline) {
        return payoutsRepo.fetchAllData(false);
    }

    public LiveData<Payouts> getPayoutByCode(String code) {
        return payoutsRepo.getPayoutByCode(code);
    }

    public Payouts getPayoutByCodeOne(String code) {
        return payoutsRepo.getPayoutByCodeOne(code);
    }

    public LiveData<List<Payouts>> getPayoutsByCycleCode(String code) {
        return payoutsRepo.getPayoutsByCycleCode(code);
    }

    public LiveData<List<Payouts>> getPayoutsByStatus(String status) {
        return payoutsRepo.getPayoutsByStatus(status);
    }

    public LiveData<List<Payouts>> getPayoutsByPayoutsByDates(String startDate, String endDate) {
        return payoutsRepo.getPayoutsByPayoutsByDate(startDate, endDate);
    }

    public LiveData<Payouts> getPayoutsByPayoutNumber(String number) {
        return payoutsRepo.getPayoutsByPayout(number);
    }

    public Payouts getLastPayout(String cycleCode) {
        return payoutsRepo.getLast(cycleCode);
    }

    public Payouts getLastPayout() {
        return payoutsRepo.getLast();
    }

    public void updatePayout(Payouts payouts) {
        payoutsRepo.upDateRecord(payouts);
    }

    public void deletePayout(Payouts payouts) {
        payoutsRepo.deleteRecord(payouts);
    }

    public void insertPayout(Payouts payouts) {
        payouts.setTraderCode(prefrenceManager.getTraderModel().getCode());
        payoutsRepo.insert(payouts);

        synch(AppConstants.INSERT, AppConstants.ENTITY_PAYOUTS, payouts, null, 1);
    }


    public LiveData<Cycles> getCycle(String cycleCode) {
        if (cycle == null) {
            cycle = new MutableLiveData();
        }

        cycle = (cyclesRepo.getCycleByKeyCode(cycleCode, false));


        return cycle;
    }

    public Cycles getCycleO(String cycleCode) {

        cycle0ne = (cyclesRepo.getCycleByKeyCodeOne(cycleCode, false));


        return cycle0ne;
    }



    public Payouts createPayout(Cycles cycles, FamerModel famerModel) {


        Payouts p = getLastPayout("" + cycles.getCode());


        Payouts plastIfOne = getLastPayout();

        Cycles c = getCycleO("" + cycles.getCode());
        if (c == null) {
            c = insertCycles("" + cycles.getCode());
            // c = getCycleO("" + cycles.getCode());

        }

        int tradersStartDay = prefrenceManager.getTraderModel().getCycleStartDayNumber();
        int tradersEndDay = prefrenceManager.getTraderModel().getCycleEndDayNumber();

        int farmerCountPerCycle = getFarmersCountByCycle("" + cycles.getCode());

        Payouts payouts = new Payouts();
        payouts.setCycleCode("" + cycles.getCode());
        payouts.setCyclename(c.getCycle());
        payouts.setFarmersCount("" + farmerCountPerCycle);
        payouts.setStatus(0);
        payouts.setCode(GeneralUtills.Companion.createCode());


        PayoutsCyclesDatesUtills.EndAndStart endAndStart = new PayoutsCyclesDatesUtills.EndAndStart();

        payouts.setApprovedCards("");
        payouts.setMilkTotal("");
        payouts.setBalance("");
        payouts.setLoanTotal("");


        if (p == null) {


            endAndStart = PayoutsCyclesDatesUtills.getPayoutStartEndDate(c.getCode(), new PayoutsCyclesDatesUtills.EndAndStart(tradersStartDay, tradersEndDay), null);
            payouts.setStartDate(endAndStart.getStartDate());
            payouts.setEndDate(endAndStart.getEndDate());


            if (plastIfOne != null) {
                payouts.setPayoutnumber(plastIfOne.getPayoutnumber() + 1);

            } else {
                payouts.setPayoutnumber(1);

            }


            insertPayout(payouts);

            return getLastPayout("" + cycles.getCode());

        } else {


            if (DateTimeUtils.Companion.isPastLastDay(p.getEndDate())) {
                Payouts plast = getLastPayout();


                endAndStart = PayoutsCyclesDatesUtills.getPayoutStartEndDate(c.getCode(), new PayoutsCyclesDatesUtills.EndAndStart(tradersStartDay, tradersEndDay), new PayoutsCyclesDatesUtills.EndAndStart(p.getStartDate(), p.getEndDate()));
                payouts.setStartDate(endAndStart.getStartDate());
                payouts.setEndDate(endAndStart.getEndDate());

                payouts.setPayoutnumber(plast.getPayoutnumber() + 1);
                insertPayout(payouts);
                return getLastPayout("" + cycles.getCode());


            } else {

                return p;

            }


        }


    }

    public double getBalance(String code) {
        return collectionsRepo.getFarmerBalance(code);
    }

    public boolean getCollectionByFarmerPreviousPayoutUnApproved(String code, String today, String status) {
        List<Collection> collections = collectionsRepo.getCollectionByFarmerPreviousPayoutUnApproved(code, today, status);
        return collections != null && collections.size() > 0;

    }


    public void setOpened() {

    }
}
