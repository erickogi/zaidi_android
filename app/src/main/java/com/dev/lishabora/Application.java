package com.dev.lishabora;

import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.dev.lishabora.Database.LMDatabase;
import com.dev.lishabora.Models.ProductsModel;
import com.dev.lishabora.Models.SyncHolderModel;
import com.dev.lishabora.Models.SyncModel;
import com.dev.lishabora.Models.SyncResponseModel;
import com.dev.lishabora.Models.Trader.Data;
import com.dev.lishabora.Models.Trader.TraderModel;
import com.dev.lishabora.Network.ApiConstants;
import com.dev.lishabora.Network.Request;
import com.dev.lishabora.Repos.ProductsRepo;
import com.dev.lishabora.Repos.RoutesRepo;
import com.dev.lishabora.Repos.Trader.SyncRepo;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.Jobs.Evernote.SyncJobCreator;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.Utils.SyncDownResponseCallback;
import com.dev.lishabora.Utils.SyncResponseCallback;
import com.dev.lishaboramobile.BuildConfig;
import com.evernote.android.job.JobManager;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.rohitss.uceh.UCEHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
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

                sync(jsonObject, syncWorks1);
            }

        } catch (Exception nm) {
            nm.printStackTrace();
        }

    }

    public static void sync(JSONObject jsonObject, List<SyncModel> syncWorks) {
        Log.d("datasend", "Started in Aplication class");

        try {
            Request.Companion.getResponseSync(ApiConstants.Companion.getSync(), jsonObject, "", new SyncResponseCallback() {
                @Override
                public void response(SyncResponseModel responseModel) {
                    Log.d("datasend", responseModel.getResultDescription());


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

                }


            });
        } catch (Exception nm) {
            nm.printStackTrace();
        }

    }

    public static void deleteSync(SyncModel s) {
        LMDatabase lmDatabase = LMDatabase.getDatabase(context);
        lmDatabase.syncDao().deleteRecord(s);

    }

    public static void syncDown() {

        Gson gson = new Gson();



        try {
            TraderModel f = new PrefrenceManager(context).getTraderModel();

            JSONObject jb = new JSONObject(gson.toJson(f));
            Timber.tag("Syncdownpref").d(gson.toJson(jb.toString()));

            Request.Companion.getResponseSyncDown(ApiConstants.Companion.getSyncDown(), jb, "",
                    new SyncDownResponseCallback() {
                        @Override
                        public void response(Data responseModel) {

                            try {
                                // if (responseModel.getResultCode() != null && Integer.valueOf(responseModel.getResultCode()) == 1) {

                                Log.d("rerreew", "" + responseModel);
                                // new FarmerRepo(getActivity().getApplication()).insertMultiple(responseModel.getFarmerModels());
                                new RoutesRepo(mInstance).insertMultipleRoutes(responseModel.getRouteModels());
                                new ProductsRepo(mInstance).insert(responseModel.getProductModels());

                                List<ProductsModel> productsModels = new LinkedList<>();
                                for (ProductsModel p : responseModel.getProductModels()) {
                                    if (p.getSubscribed().equals("0")) {
                                        p.setSubscribed("1");
                                        productsModels.add(p);
                                    }

                                }

                                Gson gson = new Gson();
                                SyncModel syncModel = new SyncModel();
                                syncModel.setActionType(AppConstants.UPDATE);
                                JsonArray jsonArray = gson.toJsonTree(productsModels).getAsJsonArray();
                                Type listType = new TypeToken<LinkedList<ProductsModel>>() {
                                }.getType();


                                syncModel.setObjects(gson.toJson(jsonArray));


                                syncModel.setObjectData(null);
                                //syncModel.setObject(new Gson().toJson(o));
                                syncModel.setDataType(2);
                                syncModel.setEntityType(AppConstants.ENTITY_PRODUCTS);
                                syncModel.setSyncStatus(0);
                                syncModel.setTimeStamp(DateTimeUtils.Companion.getNow());
                                syncModel.setSyncTime("");
                                syncModel.setEntityTypeName("Products");
                                syncModel.setTraderCode(new PrefrenceManager(context).getTraderModel().getCode());
                                syncModel.setActionTypeName("Update");

                                syncModel.setEntityTypeName("Products");
                                new SyncRepo(mInstance).insert(syncModel);


                                // }
                            } catch (Exception nm) {
                                nm.printStackTrace();
                                Log.d("rerreew", "" + nm.toString());

                            }


                        }

                        @Override
                        public void response(String error) {

                            Log.d("rerreew", "" + error);

                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("rerreew", "" + e.toString());

        }

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
                    } else {


                    }
                });
    }




}
