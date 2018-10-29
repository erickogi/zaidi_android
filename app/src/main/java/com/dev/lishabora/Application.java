package com.dev.lishabora;

import android.app.Notification;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.dev.lishabora.COntrollers.LoginController;
import com.dev.lishabora.Database.LMDatabase;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.FarmerBalance;
import com.dev.lishabora.Models.ProductsModel;
import com.dev.lishabora.Models.ResponseModel;
import com.dev.lishabora.Models.ResponseObject;
import com.dev.lishabora.Models.RoutesModel;
import com.dev.lishabora.Models.SyncDownResponse;
import com.dev.lishabora.Models.SyncHolderModel;
import com.dev.lishabora.Models.SyncModel;
import com.dev.lishabora.Models.SyncResponseModel;
import com.dev.lishabora.Models.Trader.Data;
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
import com.dev.lishabora.Utils.Jobs.Evernote.UpSyncJob;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.Utils.ResponseCallback;
import com.dev.lishabora.Utils.SyncChangesCallback;
import com.dev.lishabora.Utils.SyncDownResponseCallback;
import com.dev.lishabora.Utils.SyncResponseCallback;
import com.dev.lishabora.Views.Trader.Fragments.CollectMilk;
import com.dev.lishaboramobile.BuildConfig;
import com.dev.lishaboramobile.R;
import com.evernote.android.job.JobManager;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.gson.Gson;
import com.rohitss.uceh.UCEHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class Application extends MultiDexApplication {

    private Location currentBestLocation = null;
    public static final String TAG = Application.class
            .getSimpleName();
    private static Application mInstance;
    public static volatile Context context;
    public static volatile Handler applicationHandler;
    private static volatile boolean applicationInited = false;

    public static volatile boolean isConnected;
    public static volatile Application application;


    public static CollectMilk collectMilk = null;


    public static boolean getIfHasSyncData() {
        LMDatabase lmDatabase = LMDatabase.getDatabase(context);
        List<SyncModel> list = lmDatabase.syncDao().getAllByStatusRaw();
        if (list != null) {
            Log.d("testSyncUp", "SYNC ITEMS ARE +" + list.size());
        }


        return list != null && list.size() > 0;

    }
    public static void sync() {

        try {
            LMDatabase lmDatabase = LMDatabase.getDatabase(context);
            List<SyncModel> list = lmDatabase.syncDao().getAllByStatusRaw();
            if (list != null) {
            }


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
                sync(jsonObject, syncWorks1);

            }

        } catch (Exception nm) {
            nm.printStackTrace();

        }

    }

    public static void sync(JSONObject jsonObject, List<SyncModel> syncWorks) {

        LMDatabase lmDatabase = LMDatabase.getDatabase(context);

        TraderModel traderModel = lmDatabase.tradersDao().getTraderByCodeOne(new PrefrenceManager(context).getCode());
        if (traderModel != null) {
            traderModel.setSynchingStatus(1);

            lmDatabase.tradersDao().updateRecord(traderModel);

        }
        try {
            Request.Companion.getResponseSync(ApiConstants.Companion.getSync(), jsonObject, "", new SyncResponseCallback() {
                @Override
                public void response(SyncResponseModel responseModel) {


                    if (traderModel != null) {
                        traderModel.setSynchingStatus(0);
                        lmDatabase.tradersDao().updateRecord(traderModel);

                    }

                    if (responseModel.getResultCode() == 2) {
                        int failureId = Integer.valueOf(responseModel.getFailureId());
                        for (int a = failureId; a > 0; a--) {

                            for (SyncModel d : syncWorks) {
                                if (d.getId() == a) {
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
                    if (traderModel != null) {
                        traderModel.setSynchingStatus(2);
                        traderModel.setLastsynchingMessage(error);

                        lmDatabase.tradersDao().updateRecord(traderModel);

                    }


                }


            });
        } catch (Exception nm) {
            nm.printStackTrace();
            if (traderModel != null) {
                traderModel.setSynchingStatus(0);

                lmDatabase.tradersDao().updateRecord(traderModel);

            }

        }

    }

    public static void deleteSync(SyncModel s) {
        LMDatabase lmDatabase = LMDatabase.getDatabase(context);
        lmDatabase.syncDao().deleteRecord(s);

        new PrefrenceManager(Application.context).setLastTransaction(DateTimeUtils.Companion.getNow());

    }

    public static void syncDown() {
        new Thread(() -> syncDown(true)).start();
    }

    public static void syncDown(boolean s) {

        Gson gson = new Gson();



        try {
            TraderModel f = new PrefrenceManager(context).getTraderModel();

            JSONObject jb = new JSONObject(gson.toJson(f));
            LMDatabase lmDatabase = LMDatabase.getDatabase(context);

            TraderModel traderModel = lmDatabase.tradersDao().getTraderByCodeOne(new PrefrenceManager(context).getCode());
            if (traderModel != null) {
                traderModel.setSynchingStatus(1);

                lmDatabase.tradersDao().updateRecord(traderModel);

            }
            Request.Companion.getResponseSyncDown(ApiConstants.Companion.getViewInfo(), jb, "",
                    new SyncDownResponseCallback() {
                        @Override
                        public void response(Data responseModel) {

                            if (traderModel != null) {
                                traderModel.setSynchingStatus(0);
                                lmDatabase.tradersDao().updateRecord(traderModel);

                            }
                            try {


                                try {

                                    new FarmerRepo(mInstance).insertMultiple(responseModel.getFarmerModels());
                                    new RoutesRepo(mInstance).insertMultipleRoutes(responseModel.getRouteModels());
                                    new ProductsRepo(mInstance).insert(responseModel.getProductModels());
                                    new CollectionsRepo(mInstance).insert(responseModel.getCollectionModels());
                                    new PayoutsRepo(mInstance).insert(responseModel.getPayoutModels());
                                    new BalanceRepo(mInstance).insertMultiple(responseModel.getBalanceModels());
                                    new CyclesRepo(mInstance).insert(responseModel.getCycleModels());
                                    new UnitsRepo(mInstance).insert(responseModel.getUnitsModels());

                                } catch (Exception nm) {

                                    nm.printStackTrace();
                                }

                                try {


                                    new LoansTableRepo(mInstance).insertMultiple(responseModel.getLoanModels());
                                    new OrdersTableRepo(mInstance).insertMultiple(responseModel.getOrderModels());
                                    new LoanPaymentsRepo(mInstance).insertMultiple(responseModel.getLoanPaymentModels());
                                    new OrderPaymentsRepo(mInstance).insertMultiple(responseModel.getOrderPaymentModels());


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

                            if (traderModel != null) {
                                traderModel.setSynchingStatus(2);
                                traderModel.setLastsynchingMessage(error);


                                lmDatabase.tradersDao().updateRecord(traderModel);

                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();


        }

    }

    public static boolean canLogOut() {
        LMDatabase lmDatabase = LMDatabase.getDatabase(context);
        int count = lmDatabase.syncDao().getCount();

        return count < 1;

    }

    public static int periodAfterSync() {

        LMDatabase lmDatabase = LMDatabase.getDatabase(context);

        SyncModel syncModel = lmDatabase.syncDao().getEarliest();

        if (syncModel != null) {
            String dta = syncModel.getTimeStamp();
            org.joda.time.Period p = DateTimeUtils.Companion.calcDiff(DateTimeUtils.Companion.conver2Date(dta), DateTimeUtils.Companion.getTodayDate());

            Log.d("predios", "" + p.getDays());
            return p.getDays();

        }
        return 0;
    }

    public static hasSynced hasSyncInPast7Days() {

        int p = periodAfterSync();
        if (p >= 6) {
            return new hasSynced(false, p);
        } else {
            return new hasSynced(true, p);
        }


    }

    public static void syncChanges() {
        new Thread(() -> syncChanges(true)).start();
    }

    public static void syncChanges(boolean mw) {
        PrefrenceManager prefrenceManager = new PrefrenceManager(context);
        if (prefrenceManager.isLoggedIn() && prefrenceManager.getTraderModel() != null && prefrenceManager.getTypeLoggedIn() == LoginController.TRADER) {
            TraderModel traderModel = prefrenceManager.getTraderModel();
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject = new JSONObject(new Gson().toJson(traderModel));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Request.Companion.getResponseSyncChanges(ApiConstants.Companion.getSyncDown(), jsonObject, "", new SyncChangesCallback() {
                @Override
                public void onSucces(SyncDownResponse response) {
                    if (!getIfHasSyncData()) {
                        syncChanges(response.getData());
                    } else {

                        Notification notification = new NotificationCompat.Builder(Application.context)
                                .setContentTitle("Sync Down issue")
                                .setContentText("You've recieved new updates but we cant process them as you have un-synced data")
                                .setAutoCancel(true)
                                // .setContentIntent(pi)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setShowWhen(true)
                                .setColor(Color.RED)
                                .setLocalOnly(true)
                                .build();

                        NotificationManagerCompat.from(Application.context)
                                .notify(new Random().nextInt(), notification);
                    }

                }

                @Override
                public void onError(String error) {

                }


            });

        }
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

    /**
     * @param s
     */
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

    public static void initConnectivityListener() {
        ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnectedToInternet -> {
                    // do something with isConnectedToInternet value
                    isConnected = isConnectedToInternet;

                    if (isConnectedToInternet) {
                        UpSyncJob.scheduleExact();

                    } else {


                    }
                });
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

        UpSyncJob.schedulePeriodic();

        new UCEHandler.Builder(this).setTrackActivitiesEnabled(true).addCommaSeparatedEmailAddresses("eric@lishabora.com").build();

        initConnectivityListener();


    }

    public static class hasSynced {
        private boolean hasSynced = false;
        private int days;

        public hasSynced(boolean hasSynced, int days) {
            this.hasSynced = hasSynced;
            this.days = days;
        }

        public boolean isHasSynced() {
            return hasSynced;
        }

        public void setHasSynced(boolean hasSynced) {
            this.hasSynced = hasSynced;
        }

        public int getDays() {
            return days;
        }

        public void setDays(int days) {
            this.days = days;
        }
    }

    public static boolean isTimeAutomatic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(context.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }


}
