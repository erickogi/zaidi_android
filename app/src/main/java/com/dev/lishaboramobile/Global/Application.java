package com.dev.lishaboramobile.Global;

import android.support.multidex.MultiDexApplication;

import com.androidnetworking.AndroidNetworking;
import com.rohitss.uceh.UCEHandler;

public class Application extends MultiDexApplication {

    public static final String TAG = Application.class
            .getSimpleName();
    private static Application mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.enableLogging();
      //  new UCEHandler.Builder(this).setTrackActivitiesEnabled(true).addCommaSeparatedEmailAddresses("erickogi14@gmail.com").build();

    }


}
