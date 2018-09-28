package com.dev.lishabora;

import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.dev.lishabora.Database.LMDatabase;
import com.dev.lishabora.Models.SyncHolderModel;
import com.dev.lishabora.Models.SyncModel;
import com.dev.lishabora.Models.SyncResponseModel;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.Jobs.Evernote.SyncJobCreator;
import com.dev.lishabora.Utils.Network.ApiConstants;
import com.dev.lishabora.Utils.Network.Request;
import com.dev.lishabora.Utils.PrefrenceManager;
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

    public static void sync() {
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


    }

    public static void sync(JSONObject jsonObject, List<SyncModel> syncWorks) {
        Log.d("datasend", "Started in Aplication class");

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

                Toast.makeText(context, responseModel.getResultDescription(), Toast.LENGTH_LONG);

                // Snackbar.make(view, responseModel.getResultDescription(), Snackbar.LENGTH_LONG).show();

            }

            @Override
            public void response(String error) {
                Log.d("datasend", error);
                Toast.makeText(context, error, Toast.LENGTH_LONG);

                //Snackbar.make(view, error, Snackbar.LENGTH_LONG).show();


            }


        });

    }

    public static void deleteSync(SyncModel s) {
        LMDatabase lmDatabase = LMDatabase.getDatabase(context);
        lmDatabase.syncDao().deleteRecord(s);

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
