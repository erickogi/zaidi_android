package com.dev.lishabora;

import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.dev.lishabora.COntrollers.LoginController;
import com.dev.lishabora.Database.LMDatabase;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.ProductsModel;
import com.dev.lishabora.Models.ResponseModel;
import com.dev.lishabora.Models.ResponseObject;
import com.dev.lishabora.Models.RoutesModel;
import com.dev.lishabora.Models.SyncHolderModel;
import com.dev.lishabora.Models.SyncModel;
import com.dev.lishabora.Models.SyncResponseModel;
import com.dev.lishabora.Models.Trader.Data;
import com.dev.lishabora.Models.Trader.FarmerBalance;
import com.dev.lishabora.Models.Trader.FarmerLoansTable;
import com.dev.lishabora.Models.Trader.FarmerOrdersTable;
import com.dev.lishabora.Models.Trader.LoanPayments;
import com.dev.lishabora.Models.Trader.OrderPayments;
import com.dev.lishabora.Models.Trader.TraderModel;
import com.dev.lishabora.Network.ApiConstants;
import com.dev.lishabora.Network.Request;
import com.dev.lishabora.Repos.ProductsRepo;
import com.dev.lishabora.Repos.RoutesRepo;
import com.dev.lishabora.Repos.Trader.BalanceRepo;
import com.dev.lishabora.Repos.Trader.CollectionsRepo;
import com.dev.lishabora.Repos.Trader.CyclesRepo;
import com.dev.lishabora.Repos.Trader.FarmerRepo;
import com.dev.lishabora.Repos.Trader.LoanPaymentsRepo;
import com.dev.lishabora.Repos.Trader.LoansTableRepo;
import com.dev.lishabora.Repos.Trader.OrderPaymentsRepo;
import com.dev.lishabora.Repos.Trader.OrdersTableRepo;
import com.dev.lishabora.Repos.Trader.PayoutsRepo;
import com.dev.lishabora.Repos.Trader.TraderRepo;
import com.dev.lishabora.Repos.Trader.UnitsRepo;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.Jobs.Evernote.SyncJobCreator;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.Utils.ResponseCallback;
import com.dev.lishabora.Utils.SyncDownResponseCallback;
import com.dev.lishabora.Utils.SyncResponseCallback;
import com.dev.lishaboramobile.BuildConfig;
import com.evernote.android.job.JobManager;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.gson.Gson;
import com.rohitss.uceh.UCEHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class Application extends MultiDexApplication {

    public static final String TAG = Application.class
            .getSimpleName();
    private static Application mInstance;
    public static volatile Context context;
    public static volatile Handler applicationHandler;
    private static volatile boolean applicationInited = false;

    public static volatile boolean isConnected;
    public static volatile Application application;

    public static volatile int UpsyncTag = 0;
    public static volatile int DownsyncTag = 0;


    public static void sync() {
        try {
            LMDatabase lmDatabase = LMDatabase.getDatabase(context);
            List<SyncModel> list = lmDatabase.syncDao().getAllByStatusRaw(0);


            if (list != null && list.size() > 0) {

                List<SyncModel> syncWorks1 = new LinkedList<>();
                syncWorks1.addAll(list);


                PrefrenceManager p = new PrefrenceManager(context);
                SyncHolderModel s = new SyncHolderModel();


                s.setEntityCode(p.getTraderModel().getCode());
                s.setEntityType(AppConstants.ENTITY_TRADER);
                s.setSyncModels(syncWorks1);
                s.setSyncType(1);
                s.setTime(DateTimeUtils.Companion.getNow());


                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(new Gson().toJson(s));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (UpsyncTag == 0) {
                    sync(jsonObject, syncWorks1);
                }
            }

        } catch (Exception nm) {
            nm.printStackTrace();
        }

    }

    public static void sync(JSONObject jsonObject, List<SyncModel> syncWorks) {

        try {
            UpsyncTag = 1;
            Request.Companion.getResponseSync(ApiConstants.Companion.getSync(), jsonObject, "", new SyncResponseCallback() {
                @Override
                public void response(SyncResponseModel responseModel) {
                    UpsyncTag = 0;


                    if (responseModel.getResultCode() == 2) {
                        int failureId = Integer.valueOf(responseModel.getFailureId());
                        for (int a = failureId; a > 0; a--) {

                            for (SyncModel d : syncWorks) {
                                if (d.getId() == failureId) {
                                    deleteSync(d);
                                }
                            }
                        }
                    } else if (responseModel.getResultCode() == 1) {
                        for (SyncModel s : syncWorks) {
                            deleteSync(s);
                        }

                    }


                }

                @Override
                public void response(String error) {
                    Log.d("datasend", error);
                    UpsyncTag = 0;

                }


            });
        } catch (Exception nm) {
            nm.printStackTrace();
            UpsyncTag = 0;
        }

    }

    public static void deleteSync(SyncModel s) {
        LMDatabase lmDatabase = LMDatabase.getDatabase(context);
        lmDatabase.syncDao().deleteRecord(s);

    }

    public static void syncDown() {
        //if(DownsyncTag==0) {
        syncDown(true);
        // }
    }

    public static void syncDown(boolean s) {

        Gson gson = new Gson();



        try {
            TraderModel f = new PrefrenceManager(context).getTraderModel();

            JSONObject jb = new JSONObject(gson.toJson(f));
            Timber.tag("Syncdownpref").d(gson.toJson(jb.toString()));

            DownsyncTag = 1;
            Request.Companion.getResponseSyncDown(ApiConstants.Companion.getViewInfo(), jb, "",
                    new SyncDownResponseCallback() {
                        @Override
                        public void response(Data responseModel) {

                            DownsyncTag = 0;
                            try {


                                try {

                                    new FarmerRepo(mInstance).insertMultiple(responseModel.getFarmerModels());
                                    new RoutesRepo(mInstance).insertMultipleRoutes(responseModel.getRouteModels());
                                    new ProductsRepo(mInstance).insert(responseModel.getProductModels());
                                    new CollectionsRepo(mInstance).insert(responseModel.getCollectionModels());
                                    new PayoutsRepo(mInstance).insert(responseModel.getPayoutModels());

                                } catch (Exception nm) {
                                    nm.printStackTrace();
                                }

                                try {


                                    new LoansTableRepo(mInstance).insertMultiple(responseModel.getLoanModels());
                                    new OrdersTableRepo(mInstance).insertMultiple(responseModel.getOrderModels());
                                    new LoanPaymentsRepo(mInstance).insertMultiple(responseModel.getLoanPaymentModels());
                                    new OrderPaymentsRepo(mInstance).insertMultiple(responseModel.getOrderPaymentModels());
                                    new BalanceRepo(mInstance).insertMultiple(responseModel.getBalanceModel());
                                    new CyclesRepo(mInstance).insert(responseModel.getCycleModels());
                                    new UnitsRepo(mInstance).insert(responseModel.getUnitsModels());


                                    try {
                                        if (responseModel.getTraderModel() != null) {

                                            new PrefrenceManager(context).setLoggedUser(responseModel.getTraderModel());
                                        }
                                    } catch (Exception nm) {
                                        nm.printStackTrace();
                                    }


                                } catch (Exception nm) {
                                    nm.printStackTrace();
                                }

                            } catch (Exception nm) {
                                nm.printStackTrace();

                            }


                        }

                        @Override
                        public void response(String error) {
                            DownsyncTag = 0;


                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
            DownsyncTag = 0;


        }

    }

    public static boolean canLogOut() {
        LMDatabase lmDatabase = LMDatabase.getDatabase(context);
        int count = lmDatabase.syncDao().getCount();
        return count <= 0;

    }



    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        applicationHandler = new Handler(context.getMainLooper());


        if (BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());
        else
            Timber.plant(new NotLoggingTree());

        mInstance = this;
        JobManager.create(this).addJobCreator(new SyncJobCreator());

        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.enableLogging();
        new UCEHandler.Builder(this).setTrackActivitiesEnabled(true).addCommaSeparatedEmailAddresses("eric@lishabora.com").build();

        initConnectivityListener();

    }

    public static void updateTrader(JSONObject jsonObject) {
        Request.Companion.getResponse(ApiConstants.Companion.getUpdateTrader(), jsonObject, "",
                new ResponseCallback() {
                    @Override
                    public void response(ResponseModel responseModel) {
                        if (responseModel.getResultCode() == 1) {
                            new PrefrenceManager(context).setIsFirebaseUdated(true);
                        }
                        //  updateSuccess.setValue(responseModel);
                    }

                    @Override
                    public void response(ResponseObject responseModel) {
                        // traders.setValue(responseModel);
                        if (responseModel.getResultCode() == 1) {
                            new PrefrenceManager(context).setIsFirebaseUdated(true);
                        }
                    }
                });

    }

    public static void farmer(SyncModel s) {
        if (s.getActionType() == AppConstants.INSERT) {
            if (s.getDataType() == 1) {
                FamerModel famerModel = new Gson().fromJson(s.getObject(), FamerModel.class);
                new FarmerRepo(mInstance).insert(famerModel);

            } else {

            }
        } else if (s.getActionType() == AppConstants.UPDATE) {
            if (s.getDataType() == 1) {
                FamerModel famerModel = new Gson().fromJson(s.getObject(), FamerModel.class);
                new FarmerRepo(mInstance).upDateRecord(famerModel);

            } else {

            }
        }
    }

    public static void trader(SyncModel s) {
        if (s.getActionType() == AppConstants.INSERT) {
            if (s.getDataType() == 1) {
                TraderModel model = new Gson().fromJson(s.getObject(), TraderModel.class);
                new TraderRepo(mInstance).insert(model);
                new PrefrenceManager(context).setLoggedUser(model);

            } else {

            }
        } else if (s.getActionType() == AppConstants.UPDATE) {
            if (s.getDataType() == 1) {
                TraderModel model = new Gson().fromJson(s.getObject(), TraderModel.class);
                new TraderRepo(mInstance).upDateRecord(model);
                new PrefrenceManager(context).setLoggedUser(model);

            } else {

            }
        }
    }

    public static void product(SyncModel s) {
        if (s.getActionType() == AppConstants.INSERT) {
            if (s.getDataType() == 1) {
                ProductsModel productsModel = new Gson().fromJson(s.getObject(), ProductsModel.class);
                new ProductsRepo(mInstance).insert(productsModel);

            } else {

            }
        } else if (s.getActionType() == AppConstants.UPDATE) {
            if (s.getDataType() == 1) {
                ProductsModel productsModel = new Gson().fromJson(s.getObject(), ProductsModel.class);
                new ProductsRepo(mInstance).upDateRecord(productsModel);

            } else {

            }
        }
    }

    public static void route(SyncModel s) {
        if (s.getActionType() == AppConstants.INSERT) {
            if (s.getDataType() == 1) {
                RoutesModel routesModel = new Gson().fromJson(s.getObject(), RoutesModel.class);
                new RoutesRepo(mInstance).insert(routesModel);

            } else {

            }
        } else if (s.getActionType() == AppConstants.UPDATE) {
            if (s.getDataType() == 1) {
                RoutesModel routesModel = new Gson().fromJson(s.getObject(), RoutesModel.class);
                new RoutesRepo(mInstance).upDateRecord(routesModel);

            } else {

            }
        }
    }

    public static void collection(SyncModel s) {
        if (s.getActionType() == AppConstants.INSERT) {
            if (s.getDataType() == 1) {
                Collection collection = new Gson().fromJson(s.getObject(), Collection.class);
                new CollectionsRepo(mInstance).insert(collection);

            } else {

            }
        } else if (s.getActionType() == AppConstants.UPDATE) {
            if (s.getDataType() == 1) {
                Collection collection = new Gson().fromJson(s.getObject(), Collection.class);
                new CollectionsRepo(mInstance).upDateRecord(collection);

            } else {

            }
        }
    }

    public static void balance(SyncModel s) {
        if (s.getActionType() == AppConstants.INSERT) {
            if (s.getDataType() == 1) {
                FarmerBalance balance = new Gson().fromJson(s.getObject(), FarmerBalance.class);
                new BalanceRepo(mInstance).insert(balance);

            } else {

            }
        } else if (s.getActionType() == AppConstants.UPDATE) {
            if (s.getDataType() == 1) {
                FarmerBalance balance = new Gson().fromJson(s.getObject(), FarmerBalance.class);
                new BalanceRepo(mInstance).updateRecord(balance);

            } else {

            }
        }
    }

    public static void loan(SyncModel s) {
        if (s.getActionType() == AppConstants.INSERT) {
            if (s.getDataType() == 1) {
                FarmerLoansTable loansTable = new Gson().fromJson(s.getObject(), FarmerLoansTable.class);
                new LoansTableRepo(mInstance).insert(loansTable);

            } else {

            }
        } else if (s.getActionType() == AppConstants.UPDATE) {
            if (s.getDataType() == 1) {
                FarmerLoansTable loansTable = new Gson().fromJson(s.getObject(), FarmerLoansTable.class);
                new LoansTableRepo(mInstance).updateRecord(loansTable);

            } else {

            }
        }
    }

    public static void order(SyncModel s) {
        if (s.getActionType() == AppConstants.INSERT) {
            if (s.getDataType() == 1) {
                FarmerOrdersTable farmerOrdersTable = new Gson().fromJson(s.getObject(), FarmerOrdersTable.class);
                new OrdersTableRepo(mInstance).insert(farmerOrdersTable);

            } else {

            }
        } else if (s.getActionType() == AppConstants.UPDATE) {
            if (s.getDataType() == 1) {
                FarmerOrdersTable farmerOrdersTable = new Gson().fromJson(s.getObject(), FarmerOrdersTable.class);
                new OrdersTableRepo(mInstance).updateRecord(farmerOrdersTable);

            } else {

            }
        }
    }

    public static void loanPayment(SyncModel s) {
        if (s.getActionType() == AppConstants.INSERT) {
            if (s.getDataType() == 1) {
                LoanPayments loanPayments = new Gson().fromJson(s.getObject(), LoanPayments.class);
                new LoanPaymentsRepo(mInstance).insertSingle(loanPayments);

            } else {

            }
        } else if (s.getActionType() == AppConstants.UPDATE) {
            if (s.getDataType() == 1) {
                LoanPayments loanPayments = new Gson().fromJson(s.getObject(), LoanPayments.class);
                new LoanPaymentsRepo(mInstance).updateRecord(loanPayments);

            } else {

            }
        }
    }

    public static void orderPayment(SyncModel s) {
        if (s.getActionType() == AppConstants.INSERT) {
            if (s.getDataType() == 1) {
                OrderPayments orderPayments = new Gson().fromJson(s.getObject(), OrderPayments.class);
                new OrderPaymentsRepo(mInstance).insertSingle(orderPayments);

            } else {

            }
        } else if (s.getActionType() == AppConstants.UPDATE) {
            if (s.getDataType() == 1) {
                OrderPayments orderPayments = new Gson().fromJson(s.getObject(), OrderPayments.class);
                new OrderPaymentsRepo(mInstance).updateRecord(orderPayments);

            } else {

            }
        }
    }

    static void syncChanges(SyncHolderModel syncHolderModel) {

        List<SyncModel> syncModels = syncHolderModel.getSyncModels();
        for (SyncModel s : syncModels) {

            if (s.getEntityType() == AppConstants.ENTITY_FARMER) {


                farmer(s);
                Log.d("syncddoa", new Gson().toJson(s));

            }
            if (s.getEntityType() == AppConstants.ENTITY_TRADER) {
                trader(s);
            }
            if (s.getEntityType() == AppConstants.ENTITY_PRODUCTS) {
                product(s);
            }
            if (s.getEntityType() == AppConstants.ENTITY_ROUTES) {
                route(s);
            }
            if (s.getEntityType() == AppConstants.ENTITY_COLLECTION) {
                collection(s);
            }
            if (s.getEntityType() == AppConstants.ENTITY_BALANCES) {
                balance(s);
            }
            if (s.getEntityType() == AppConstants.ENTITY_LOANS) {
                loan(s);
            }
            if (s.getEntityType() == AppConstants.ENTITY_ORDERS) {
                order(s);
            }
            if (s.getEntityType() == AppConstants.ENTITY_LOAN_PAYMNENTS) {
                loanPayment(s);
            }
            if (s.getEntityType() == AppConstants.ENTITY_ORDER_PAYMENTS) {
                orderPayment(s);
            }

        }

    }

    public static void syncChanges() {
        PrefrenceManager prefrenceManager = new PrefrenceManager(context);
        if (prefrenceManager.isLoggedIn() && prefrenceManager.getTraderModel() != null && prefrenceManager.getTypeLoggedIn() == LoginController.TRADER) {
            TraderModel traderModel = prefrenceManager.getTraderModel();
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject = new JSONObject(new Gson().toJson(traderModel));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Request.Companion.getResponseSingle(ApiConstants.Companion.getSyncDown(), jsonObject, "", new ResponseCallback() {
                @Override
                public void response(ResponseModel responseModel) {

                }

                @Override
                public void response(ResponseObject responseModel) {
                    //
                    Log.d("syncddoa", "" + responseModel.getResultCode() + " " + responseModel.getResultDescription());


                    if (responseModel.getResultCode() == 1) {

                        String data = new Gson().toJson(responseModel.getData());
                        SyncHolderModel syncHolderModel = new Gson().fromJson(data, SyncHolderModel.class);
//
                        Log.d("syncddoa", data);

                        syncChanges(syncHolderModel);

                        // Log.d("syncddoa", new Gson().toJson(syncHolderModel));


                    } else {
                        Log.d("syncddoa", "sdf");

                    }
                }
            });

        }
    }

    void initConnectivityListener() {
        ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnectedToInternet -> {
                    // do something with isConnectedToInternet value
                    isConnected = isConnectedToInternet;

                    if (isConnectedToInternet) {
                        sync();
                        //syncDown();
                    } else {


                    }
                });
    }

}
