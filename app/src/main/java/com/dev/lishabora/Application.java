package com.dev.lishabora;

import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;

import com.androidnetworking.AndroidNetworking;
import com.dev.lishabora.Utils.Jobs.Evernote.SyncJobCreator;
import com.dev.lishaboramobile.BuildConfig;
import com.evernote.android.job.JobManager;
import com.rohitss.uceh.UCEHandler;

import timber.log.Timber;

public class Application extends MultiDexApplication {

    public static final String TAG = Application.class
            .getSimpleName();
    private static Application mInstance;
    public static volatile Context context;
    public static volatile Handler applicationHandler;
    private static volatile boolean applicationInited = false;


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

    }


}
