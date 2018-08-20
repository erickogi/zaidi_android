package com.dev.lishabora.ViewModels.Trader;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.Cycles;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Models.ProductsModel;
import com.dev.lishabora.Models.RPFSearchModel;
import com.dev.lishabora.Models.ResponseModel;
import com.dev.lishabora.Models.ResponseObject;
import com.dev.lishabora.Models.RoutesModel;
import com.dev.lishabora.Models.UnitsModel;
import com.dev.lishabora.Repos.ProductsRepo;
import com.dev.lishabora.Repos.RoutesRepo;
import com.dev.lishabora.Repos.Trader.CollectionsRepo;
import com.dev.lishabora.Repos.Trader.CyclesRepo;
import com.dev.lishabora.Repos.Trader.FarmerRepo;
import com.dev.lishabora.Repos.Trader.PayoutsRepo;
import com.dev.lishabora.Repos.Trader.UnitsRepo;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.Network.ApiConstants;
import com.dev.lishabora.Utils.Network.Request;
import com.dev.lishabora.Utils.PayoutsCyclesDatesUtills;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.Utils.ResponseCallback;
import com.dev.lishabora.Views.Trader.FarmerConst;
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
    ProductsRepo productsRepo;
    CollectionsRepo collectionsRepo;
    PayoutsRepo payoutsRepo;

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
    private LiveData<List<FamerModel>> farmers;
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


//
        payouts = new MutableLiveData<>();
        payout = new MutableLiveData<>();
        payoutOne = new Payouts();

//
//        farmers.setValue(farmerRepo.fetchAllData(false));
//        routes.setValue(routesRepo.fetchAllData(false));
//        units.setValue(unitsRepo.fetchAllData(false));
//        cycles.setValue(cyclesRepo.fetchAllData(false));
    }

    public Collection getLastCollection(String cyclecode) {


        return collectionsRepo.getLast(cyclecode);
    }
    public LiveData<ResponseModel> getProductsModels(JSONObject jsonObject, boolean fetchFromOnline) {

        if (this.productss == null) {
            this.productss = new MutableLiveData();
            if (fetchFromOnline) {
                Request.Companion.getResponse(ApiConstants.Companion.getProducts(), jsonObject, "", new ResponseCallback() {
                            @Override
                            public void response(ResponseModel responseModel) {
                                productss.setValue(responseModel);
                            }

                            @Override
                            public void response(ResponseObject responseModel) {

                                productss.setValue(responseModel);
                            }
                        }
                );

            } else {

            }


        }


        return productss;
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

    public LiveData<List<FamerModel>> getFarmerByStatusRoute(int status, String route) {

        Log.d("byRouteByStatus", "Status " + status + "\n Route " + route);

        if (farmers == null) {
            farmers = new MutableLiveData();
        }

        switch (status) {
            case FarmerConst.ACTIVE:

                farmers = (farmerRepo.getFarmersByStatusRoute(0, 0, 0, route));
                break;
            case FarmerConst.DELETED:
                farmers = (farmerRepo.getFarmersByStatusRouteDeleted(1, route));
                break;
            case FarmerConst.DUMMY:
                farmers = (farmerRepo.getFarmersByStatusRouteDummy(1, route));
                break;
            case FarmerConst.ARCHIVED:
                farmers = (farmerRepo.getFarmersByStatusRouteArchived(1, route));
                break;
            case FarmerConst.ALL:
                farmers = (farmerRepo.fetchAllData(false, route));
                break;
            default:
                farmers = (farmerRepo.fetchAllData(false));


        }



        return farmers;
    }

    public LiveData<List<FamerModel>> getFarmersByName(String names) {
        if (farmers == null) {
            farmers = new MutableLiveData();
            farmers = (farmerRepo.searchByNames(names));
        }

        return farmers;
    }

    public LiveData<List<FamerModel>> getFarmersByCycle(String code) {
        if (farmers == null) {
            farmers = new MutableLiveData();
            farmers = (farmerRepo.getFarmersByCycle(code));
        }

        return farmers;
    }

    public int getFarmersCountByCycle(String code) {
        return (farmerRepo.getFarmersCountByCycle(code));


    }

    public LiveData<FamerModel> getLastFarmer() {
        if (farmer == null) {
            farmer = new MutableLiveData();
            farmer = (farmerRepo.getLastFarmer());
        }

        return farmer;
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

    public LiveData<List<ProductsModel>> getProducts(boolean isOnline) {
        if (products == null) {
            products = new MutableLiveData();
        }

        if (isOnline) {
            Request.Companion.getResponse(ApiConstants.Companion.getProducts(), getTraderRoutesObject(), "", new ResponseCallback() {
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
        products = (productsRepo.fetchAllData(false));
        Log.d("fetchall", "inserting repo" + products);


        return products;
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

    public LiveData<Cycles> getCycles(String code, boolean isOnline) {
        if (cycle == null) {
            cycle = new MutableLiveData()
            ;
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
        cycle = (cyclesRepo.getCycleByKeyCode(code, false));


        return cycle;
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

    public int noOfFarmersPerRoute(String routecode) {
        return farmerRepo.getNoOfRows(routecode);
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

    public LiveData<ResponseModel> updateRoute(RoutesModel routesModel, JSONObject jsonObject, boolean b) {
        if (this.updateRouteSuccess == null) {
        }
        this.updateRouteSuccess = new MutableLiveData();

        if (b) {
            Request.Companion.getResponse(ApiConstants.Companion.getCreateRoutes(), jsonObject, "", new ResponseCallback() {
                @Override
                public void response(ResponseModel responseModel) {
                    updateRouteSuccess.setValue(responseModel);
                    routesRepo.insert(routesModel);

                }

                @Override
                public void response(ResponseObject responseModel) {
                    updateRouteSuccess.setValue(responseModel);

                    routesRepo.insert(routesModel);

                }
            });

        } else {
            routesRepo.upDateRecord(routesModel);
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
            Request.Companion.getResponse(ApiConstants.Companion.getCreateRoutes(), jsonObject, "", new ResponseCallback() {
                @Override
                public void response(ResponseModel responseModel) {
                    deleteRouteSuccess.setValue(responseModel);
                    routesRepo.insert(routesModel);

                }

                @Override
                public void response(ResponseObject responseModel) {
                    deleteRouteSuccess.setValue(responseModel);

                    routesRepo.insert(routesModel);

                }
            });

        } else {
            routesRepo.deleteRecord(routesModel);
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResultCode(1);
            responseModel.setResultDescription("Deleted");
            responseModel.setData(null);
            deleteRouteSuccess.setValue(responseModel);

        }
        return deleteRouteSuccess;
    }


    public LiveData<ResponseModel> createProduct(ProductsModel productsModel, boolean b) {
        if (this.createProductSuccess == null) {
        }
        this.createProductSuccess = new MutableLiveData();

        if (b) {

        } else {
            productsRepo.insert(productsModel);
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResultCode(1);
            responseModel.setResultDescription("Added");
            responseModel.setData(null);
            createProductSuccess.setValue(responseModel);

        }
        return createProductSuccess;
    }

    public LiveData<ResponseModel> createProducts(List<ProductsModel> productsModels, boolean b) {
        if (this.createProductSuccess == null) {
        }
        this.createProductSuccess = new MutableLiveData();

        if (b) {

        } else {

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

    public LiveData<ResponseModel> createCollections(Collection collection, boolean b) {
        if (this.createCollectionSuccess == null) {
        }
        this.createCollectionSuccess = new MutableLiveData();


        Payouts p = getLastPayout(collection.getCycleCode());
        Payouts plastIfOne = getLastPayout();

        Cycles c = getCycleO(collection.getCycleCode());
        if (c == null) {
            insertCycles();
            c = getCycleO(collection.getCycleCode());

        }
        int farmerCountPerCycle = getFarmersCountByCycle(collection.getCycleCode());

        if (p == null) {

            int tradersStartDay = prefrenceManager.getTraderModel().getCycleStartDayNumber();
            int tradersEndDay = prefrenceManager.getTraderModel().getCycleEndDayNumber();



            Payouts payouts = new Payouts();
            payouts.setCycleCode(collection.getCycleCode());
            payouts.setCyclename(c.getCycle());
            payouts.setFarmersCount("" + farmerCountPerCycle);
            payouts.setStatus(0);

            PayoutsCyclesDatesUtills.EndAndStart endAndStart = new PayoutsCyclesDatesUtills.EndAndStart();
            endAndStart = PayoutsCyclesDatesUtills.getPayoutStartEndDate(c.getCode(), new PayoutsCyclesDatesUtills.EndAndStart(tradersStartDay, tradersEndDay), null);
            payouts.setStartDate(endAndStart.getStartDate());
            payouts.setEndDate(endAndStart.getEndDate());

            payouts.setApprovedCards("");
            payouts.setMilkTotal("");
            payouts.setBalance("");
            payouts.setLoanTotal("");

            if (plastIfOne != null) {
                payouts.setPayoutnumber(plastIfOne.getPayoutnumber() + 1);

            } else {
                payouts.setPayoutnumber(1);

            }


            insertPayout(payouts);
            collection.setCycleStartedOn(payouts.getStartDate());
            collection.setPayoutnumber(payouts.getPayoutnumber());


            collectionsRepo.insert(collection);
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResultCode(1);
            responseModel.setResultDescription("Collection Inserted \nNew  payout \n(No other  payouts available)");
            responseModel.setData(null);
            createCollectionSuccess.setValue(responseModel);



        } else {


            if (DateTimeUtils.Companion.isPastLastDay(p.getEndDate())) {


                int tradersStartDay = prefrenceManager.getTraderModel().getCycleStartDayNumber();
                int tradersEndDay = prefrenceManager.getTraderModel().getCycleEndDayNumber();


                Payouts plast = getLastPayout();

                Payouts payouts = new Payouts();
                payouts.setCycleCode(collection.getCycleCode());
                payouts.setCyclename(c.getCycle());
                payouts.setFarmersCount("" + farmerCountPerCycle);
                payouts.setStatus(0);

                PayoutsCyclesDatesUtills.EndAndStart endAndStart;
                endAndStart = PayoutsCyclesDatesUtills.getPayoutStartEndDate(c.getCode(), new PayoutsCyclesDatesUtills.EndAndStart(tradersStartDay, tradersEndDay), new PayoutsCyclesDatesUtills.EndAndStart(p.getStartDate(), p.getEndDate()));
                payouts.setStartDate(endAndStart.getStartDate());
                payouts.setEndDate(endAndStart.getEndDate());

                payouts.setApprovedCards("");
                payouts.setMilkTotal("");
                payouts.setBalance("");
                payouts.setLoanTotal("");
                payouts.setPayoutnumber(plast.getPayoutnumber() + 1);


                insertPayout(payouts);
                collection.setCycleStartedOn(payouts.getStartDate());
                collection.setPayoutnumber(payouts.getPayoutnumber());


                collectionsRepo.insert(collection);
                ResponseModel responseModel = new ResponseModel();
                responseModel.setResultCode(1);
                responseModel.setResultDescription("Collection Inserted \nNew  payout \nOther cycles payouts available");
                responseModel.setData(null);
                createCollectionSuccess.setValue(responseModel);


            } else {
                collection.setCycleStartedOn(p.getStartDate());
                collection.setPayoutnumber(p.getPayoutnumber());

                collectionsRepo.insert(collection);
                ResponseModel responseModel = new ResponseModel();
                responseModel.setResultCode(1);
                responseModel.setResultDescription("Collection Inserted \nExisting payout");
                responseModel.setData(null);
                createCollectionSuccess.setValue(responseModel);

            }


        }




 

        return createCollectionSuccess;
    }

    private void insertCycles() {
        Cycles cycles = new Cycles();
        cycles.setCode(1);
        cycles.setCycle("Weekly");
        cycles.setPeriod("7");
        cycles.setStatus("1");

        Cycles cycles1 = new Cycles();
        cycles1.setCode(1);
        cycles1.setCycle("Bi Weekly");
        cycles1.setPeriod("14");
        cycles1.setStatus("1");


        Cycles cycles2 = new Cycles();
        cycles2.setCode(1);
        cycles2.setCycle("Semi Monthly");
        cycles2.setPeriod("14");
        cycles2.setStatus("1");


        Cycles cycles3 = new Cycles();
        cycles3.setCode(4);
        cycles3.setCycle("Monthly");
        cycles3.setPeriod("30");
        cycles3.setStatus("1");

        List<Cycles> cycles4 = new LinkedList<>();
        cycles4.add(cycles);
        cycles4.add(cycles1);
        cycles4.add(cycles2);
        cycles4.add(cycles3);

        cyclesRepo.insert(cycles4);


    }

    public LiveData<ResponseModel> updateProduct(ProductsModel productsModel, boolean b) {
        if (this.updateProductSuccess == null) {
        }
        this.updateProductSuccess = new MutableLiveData();

        if (b) {

        } else {
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

    public LiveData<ResponseModel> deleteFarmer(FamerModel famerModel, boolean b) {
        if (this.deleteFarmerSuccess == null) {
        }
        this.deleteFarmerSuccess = new MutableLiveData();

        if (b) {
            Request.Companion.getResponse(ApiConstants.Companion.getCreateFarmer(), getFarmerJson(), "", new ResponseCallback() {
                @Override
                public void response(ResponseModel responseModel) {
                    deleteFarmerSuccess.setValue(responseModel);
                    farmerRepo.insert(famerModel);

                }

                @Override
                public void response(ResponseObject responseModel) {
                    deleteFarmerSuccess.setValue(responseModel);

                    farmerRepo.insert(famerModel);

                }
            });

        } else {
            farmerRepo.deleteRecord(famerModel);
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResultCode(1);
            responseModel.setResultDescription("Farmer deleted successfully");
            responseModel.setData(null);
            deleteFarmerSuccess.setValue(responseModel);

        }
        return deleteFarmerSuccess;
    }

    public LiveData<ResponseModel> updateFarmer(FamerModel famerModel, boolean b) {
        if (this.updateFarmerSuccess == null) {
        }
        this.updateFarmerSuccess = new MutableLiveData();

        if (b) {
            Request.Companion.getResponse(ApiConstants.Companion.getCreateFarmer(), getFarmerJson(), "", new ResponseCallback() {
                @Override
                public void response(ResponseModel responseModel) {
                    updateFarmerSuccess.setValue(responseModel);
                    farmerRepo.upDateRecord(famerModel);

                }

                @Override
                public void response(ResponseObject responseModel) {
                    updateFarmerSuccess.setValue(responseModel);

                    farmerRepo.upDateRecord(famerModel);

                }
            });

        } else {
            farmerRepo.upDateRecord(famerModel);
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResultCode(1);
            responseModel.setResultDescription("Farmer updated successfully");
            responseModel.setData(null);
            updateFarmerSuccess.setValue(responseModel);

        }
        return updateFarmerSuccess;
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

    public LiveData<List<Collection>> getCollectionByDateByPayout(String payoutnumber) {
        if (collections == null) {
            collections = new MutableLiveData<>();
        }
        return collectionsRepo.getCollectionByPayout(payoutnumber);
    }


    public LiveData<List<Payouts>> fetchAll(boolean isOnline) {
        return payoutsRepo.fetchAllData(false);
    }

    public LiveData<Payouts> getPayoutById(int id) {
        return payoutsRepo.getPayoutById(id);
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
        payoutsRepo.insert(payouts);
    }

    public void insertPayout(List<Payouts> payouts) {
        payoutsRepo.insert(payouts);
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

}
