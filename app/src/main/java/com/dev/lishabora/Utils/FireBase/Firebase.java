package com.dev.lishabora.Utils.FireBase;

import android.util.Log;

import com.dev.lishabora.Utils.PrefrenceManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

//import com.erickogi14gmail.photozuri.Data.PrefManager;

/**
 * Created by Eric on 3/27/2018.
 */


public class Firebase extends FirebaseInstanceIdService {

    private String TAG = "taggs";

    public Firebase() {
        super();
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, refreshedToken);
        new PrefrenceManager(getApplicationContext()).setFirebase(refreshedToken);


    }
}
