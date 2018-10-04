package com.dev.lishabora;

import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.dev.lishabora.Database.LMDatabase;
import com.dev.lishabora.Models.SyncHolderModel;
import com.dev.lishabora.Models.SyncModel;
import com.dev.lishabora.Models.SyncResponseModel;
import com.dev.lishabora.Models.Trader.Data;
import com.dev.lishabora.Models.Trader.TraderModel;
import com.dev.lishabora.Network.ApiConstants;
import com.dev.lishabora.Network.Request;
import com.dev.lishabora.Repos.ProductsRepo;
import com.dev.lishabora.Repos.RoutesRepo;
import com.dev.lishabora.Repos.Trader.BalanceRepo;
import com.dev.lishabora.Repos.Trader.CollectionsRepo;
import com.dev.lishabora.Repos.Trader.FarmerRepo;
import com.dev.lishabora.Repos.Trader.LoanPaymentsRepo;
import com.dev.lishabora.Repos.Trader.LoansTableRepo;
import com.dev.lishabora.Repos.Trader.OrderPaymentsRepo;
import com.dev.lishabora.Repos.Trader.OrdersTableRepo;
import com.dev.lishabora.Repos.Trader.PayoutsRepo;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.Jobs.Evernote.SyncJobCreator;
import com.dev.lishabora.Utils.PrefrenceManager;
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
            Request.Companion.getResponseSyncDown(ApiConstants.Companion.getSyncDown(), jb, "",
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
                                    new BalanceRepo(mInstance).insert(responseModel.getBalanceModel());


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

    void initConnectivityListener() {
        ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnectedToInternet -> {
                    // do something with isConnectedToInternet value
                    isConnected = isConnectedToInternet;

                    if (isConnectedToInternet) {
                        sync();
                        syncDown();
                    } else {


                    }
                });
    }




}
