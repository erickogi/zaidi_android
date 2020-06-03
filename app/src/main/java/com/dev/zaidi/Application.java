package com.dev.zaidi;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.dev.zaidi.COntrollers.LoginController;
import com.dev.zaidi.Database.LMDatabase;
import com.dev.zaidi.Jobs.Evernote.SyncJobCreator;
import com.dev.zaidi.Jobs.Evernote.UpSyncJob;
import com.dev.zaidi.Jobs.WorkManager.UpWorkCreator;
import com.dev.zaidi.Models.Collection;
import com.dev.zaidi.Models.FamerModel;
import com.dev.zaidi.Models.FarmerBalance;
import com.dev.zaidi.Models.NetworkAnalytics;
import com.dev.zaidi.Models.ProductsModel;
import com.dev.zaidi.Models.ResponseModel;
import com.dev.zaidi.Models.ResponseObject;
import com.dev.zaidi.Models.RoutesModel;
import com.dev.zaidi.Models.SyncDownObserver;
import com.dev.zaidi.Models.SyncDownResponse;
import com.dev.zaidi.Models.SyncHolderModel;
import com.dev.zaidi.Models.SyncModel;
import com.dev.zaidi.Models.Trader.Data;
import com.dev.zaidi.Models.Trader.FarmerLoansTable;
import com.dev.zaidi.Models.Trader.FarmerOrdersTable;
import com.dev.zaidi.Models.Trader.LoanPayments;
import com.dev.zaidi.Models.Trader.OrderPayments;
import com.dev.zaidi.Models.Trader.TraderModel;
import com.dev.zaidi.Network.ApiConstants;
import com.dev.zaidi.Network.Request;
import com.dev.zaidi.Network.RequestListener;
import com.dev.zaidi.Repos.ProductsRepo;
import com.dev.zaidi.Repos.RoutesRepo;
import com.dev.zaidi.Repos.Trader.BalanceRepo;
import com.dev.zaidi.Repos.Trader.CollectionsRepo;
import com.dev.zaidi.Repos.Trader.CyclesRepo;
import com.dev.zaidi.Repos.Trader.FarmerRepo;
import com.dev.zaidi.Repos.Trader.LoanPaymentsRepo;
import com.dev.zaidi.Repos.Trader.LoansTableRepo;
import com.dev.zaidi.Repos.Trader.OrderPaymentsRepo;
import com.dev.zaidi.Repos.Trader.OrdersTableRepo;
import com.dev.zaidi.Repos.Trader.PayoutsRepo;
import com.dev.zaidi.Repos.Trader.TraderRepo;
import com.dev.zaidi.Repos.Trader.UnitsRepo;
import com.dev.zaidi.Utils.DateTimeUtils;
import com.dev.zaidi.Utils.PrefrenceManager;
import com.dev.zaidi.Utils.ResponseCallback;
import com.dev.zaidi.Utils.SyncChangesCallback;
import com.dev.zaidi.Utils.SyncDownResponseCallback;
import com.dev.zaidi.Views.Trader.Fragments.CollectMilk;
import com.evernote.android.job.JobManager;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.dev.zaidi.AppConstants.getAppSignature;
import static com.dev.zaidi.AppConstants.getGoogleLogAuthKey;

//import com.amitshekhar.DebugDB;

//import com.rohitss.uceh.UCEHandler;

//import com.rohitss.uceh.UCEHandler;

//import com.rohitss.uceh.UCEHandler;

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
    public static NotificationManager _completeNotificationManager = null;

    public static CollectMilk collectMilk = null;


    public static boolean getIfHasSyncData() {
        LMDatabase lmDatabase = LMDatabase.getDatabase(context);
        List<SyncModel> list = lmDatabase.syncDao().getAllByStatusRaw(new PrefrenceManager(context).syncNumber());
        if (list != null) {
        }


        return list != null && list.size() > 0;

    }

    static SyncDownObserver sm = new SyncDownObserver();

    public static void syncDown() {
        new Thread(() -> syncDown(true)).start();
    }

    static SyncDownObserver smo = new SyncDownObserver();

    private static void buildNotification() {

        try {

            NotificationCompat.Builder mBuilder;
            String NOTIFICATION_CHANNEL_ID = "10001";

            mBuilder = new NotificationCompat.Builder(com.dev.zaidi.Application.context);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setContentTitle(context.getString(R.string.app_name))
                    .setContentText("Downloading server data")
                    .setAutoCancel(false)

                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            //.setContentIntent(resultPendingIntent);

            _completeNotificationManager = (NotificationManager) com.dev.zaidi.Application.context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                assert _completeNotificationManager != null;
                mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                _completeNotificationManager.createNotificationChannel(notificationChannel);
            }
            assert _completeNotificationManager != null;
            _completeNotificationManager.notify(6 /* Request Code */, mBuilder.build());

        } catch (Exception nm) {
            nm.printStackTrace();
        }
    }

    public static void syncDown(boolean s) {


        Gson gson = new Gson();
        LMDatabase lmDatabase = LMDatabase.getDatabase(context);

        buildNotification();

        if (lmDatabase.syncObserverDao().getCount() < 1) {
            sm.setStatus(0);
            sm.setResponse("Syncing");
            sm.setTransactiontime(DateTimeUtils.Companion.getNow());
            lmDatabase.syncObserverDao().insertSingleSync(sm);
        }

        smo = lmDatabase.syncObserverDao().getRecord();


        try {
            TraderModel f = new PrefrenceManager(context).getTraderModel();
            JSONObject jb = new JSONObject(gson.toJson(f));


            Request.Companion.getResponseSyncDown(context,ApiConstants.Companion.getViewInfo(), jb, "",
                    new SyncDownResponseCallback() {
                        @Override
                        public void response(Data responseModel, NetworkAnalytics analytics) {
                            if (_completeNotificationManager != null) {
                                _completeNotificationManager.cancel(6);
                            }

                            if (smo != null) {
                                smo.setStatus(1);
                                smo.setResponse(responseModel.getResultDescription());
                                lmDatabase.syncObserverDao().updateRecord(smo);

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
                        public void response(String error, NetworkAnalytics analytics) {
                            if (_completeNotificationManager != null) {
                                _completeNotificationManager.cancel(6);
                            }
                            syncDown(true);
                            if (smo != null) {
                                smo.setStatus(2);
                                smo.setResponse(error);
                                lmDatabase.syncObserverDao().updateRecord(smo);

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

    public static void startSync(){
        context.startService(new Intent(context, SyncService.class));
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
            Request.Companion.getResponseSyncChanges(context, ApiConstants.Companion.getSyncDown(), jsonObject, "", new SyncChangesCallback() {
                @Override
                public void onSucces(SyncDownResponse response, NetworkAnalytics analytics) {
                    if (!getIfHasSyncData()) {
                        syncChanges(response.getData());
                    } else {

                        Notification notification = new NotificationCompat.Builder(Application.context)
                                .setContentTitle("Sync Down issue")
                                .setContentText("You've received new updates but we cant process them as you have un-synced data")
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
                public void onError(String error, NetworkAnalytics analytics) {

                }


            });

        }
    }

    public static void updateTrader(JSONObject jsonObject) {
        new Thread(() -> updateTrader(jsonObject, true)).start();
    }

    public static void updateTrader(JSONObject jsonObject, boolean a) {
        Request.Companion.getResponse(context,ApiConstants.Companion.getUpdateTrader(), jsonObject, "",
                new ResponseCallback() {
                    @Override
                    public void response(ResponseModel responseModel, NetworkAnalytics analytics) {
                        if (responseModel.getResultCode() == 1) {
                            new PrefrenceManager(context).setIsFirebaseUdated(true);
                        }
                    }

                    @Override
                    public void response(ResponseObject responseModel, NetworkAnalytics analytics) {
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
        } else if (s.getActionType() == AppConstants.DELETE) {
            if (s.getDataType() == 1) {
                FamerModel famerModel = new Gson().fromJson(s.getObject(), FamerModel.class);
                new FarmerRepo(mInstance).deleteRecord(famerModel);

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
        } else if (s.getActionType() == AppConstants.DELETE) {
            if (s.getDataType() == 1) {
                TraderModel model = new Gson().fromJson(s.getObject(), TraderModel.class);
                new TraderRepo(mInstance).deleteRecord(model);
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
        } else if (s.getActionType() == AppConstants.DELETE) {
            if (s.getDataType() == 1) {
                ProductsModel productsModel = new Gson().fromJson(s.getObject(), ProductsModel.class);
                new ProductsRepo(mInstance).deleteRecord(productsModel);

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
        } else if (s.getActionType() == AppConstants.DELETE) {
            if (s.getDataType() == 1) {
                RoutesModel routesModel = new Gson().fromJson(s.getObject(), RoutesModel.class);
                new RoutesRepo(mInstance).deleteRecord(routesModel);

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
        } else if (s.getActionType() == AppConstants.DELETE) {
            if (s.getDataType() == 1) {
                Collection collection = new Gson().fromJson(s.getObject(), Collection.class);
                new CollectionsRepo(mInstance).deleteRecord(collection);

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
        } else if (s.getActionType() == AppConstants.DELETE) {
            if (s.getDataType() == 1) {
                FarmerBalance balance = new Gson().fromJson(s.getObject(), FarmerBalance.class);
                new BalanceRepo(mInstance).deleteRecord(balance);

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
        } else if (s.getActionType() == AppConstants.DELETE) {
            if (s.getDataType() == 1) {
                FarmerLoansTable loansTable = new Gson().fromJson(s.getObject(), FarmerLoansTable.class);
                new LoansTableRepo(mInstance).deleteRecord(loansTable);

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
        } else if (s.getActionType() == AppConstants.DELETE) {
            if (s.getDataType() == 1) {
                FarmerOrdersTable farmerOrdersTable = new Gson().fromJson(s.getObject(), FarmerOrdersTable.class);
                new OrdersTableRepo(mInstance).deleteRecord(farmerOrdersTable);

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
        } else if (s.getActionType() == AppConstants.DELETE) {
            if (s.getDataType() == 1) {
                LoanPayments loanPayments = new Gson().fromJson(s.getObject(), LoanPayments.class);
                new LoanPaymentsRepo(mInstance).deleteRecord(loanPayments);

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
        } else if (s.getActionType() == AppConstants.DELETE) {
            if (s.getDataType() == 1) {
                OrderPayments orderPayments = new Gson().fromJson(s.getObject(), OrderPayments.class);
                new OrderPaymentsRepo(mInstance).deleteRecord(orderPayments);

            } else {

            }
        }
    }

    static void syncChanges(SyncHolderModel syncHolderModel) {

        List<SyncModel> syncModels = syncHolderModel.getSyncModels();
        for (SyncModel s : syncModels) {

            if (s.getEntityType() == AppConstants.ENTITY_FARMER) {
                farmer(s);

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
                    }
                }).dispose();
    }

    public static boolean isTimeAutomatic() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return Settings.Global.getInt(context.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
            } else {
                return android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
            }
        } catch (Exception nm) {
            nm.printStackTrace();
            return true;
        }
        //  return true;
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

    public static void init() {
        PrefrenceManager prefManager = new PrefrenceManager(context);
        prefManager.setGoogleAuthConnected(true);


        Request.Companion.getRequest(getGoogleLogAuthKey(), "", new RequestListener() {
            @Override
            public void onError(@NotNull ANError error, @NotNull NetworkAnalytics analytics) {
                prefManager.setGoogleAuth(getAppSignature(context));

            }

            @Override
            public void onError(@NotNull String error, @NotNull NetworkAnalytics analytics) {
                prefManager.setGoogleAuth(getAppSignature(context));

            }

            @Override
            public void onSuccess(@NotNull String response, @NotNull NetworkAnalytics analytics) {


                try {
                    JSONObject jsonObject = new
                            JSONObject(response);
                    if (jsonObject.optBoolean("error")) {

                        prefManager.setGoogleAuth(jsonObject.getString("message"));
                        prefManager.setGoogleAuthConnected(false);
                    } else {
                        // prefManager.setGoogleAuth(jsonObject.getString("signature"));
                        prefManager.setGoogleAuthConnected(true);
                    }
                } catch (Exception nm) {
                    prefManager.setGoogleAuthConnected(true);
                    prefManager.setGoogleAuth(getAppSignature(context));

                    nm.printStackTrace();
                }
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

        new UpWorkCreator().createUpSyncWork();

      //  new UCEHandler.Builder(this).setTrackActivitiesEnabled(true).addCommaSeparatedEmailAddresses("erickogi14@gmail.com.com").build();
        initConnectivityListener();

    }


}
