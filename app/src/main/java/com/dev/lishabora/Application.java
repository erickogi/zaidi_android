package com.dev.lishabora;

import android.support.multidex.MultiDexApplication;

import com.androidnetworking.AndroidNetworking;
import com.dev.lishaboramobile.BuildConfig;
import com.rohitss.uceh.UCEHandler;

import timber.log.Timber;

public class Application extends MultiDexApplication {

    public static final String TAG = Application.class
            .getSimpleName();
    private static Application mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());
        else
            Timber.plant(new NotLoggingTree());

        mInstance = this;
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.enableLogging();
        new UCEHandler.Builder(this).setTrackActivitiesEnabled(true).addCommaSeparatedEmailAddresses("eric@lishabora.com").build();

    }


}
