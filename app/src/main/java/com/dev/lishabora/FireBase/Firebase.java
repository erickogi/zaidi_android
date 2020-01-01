package com.dev.lishabora.FireBase;

import com.dev.lishabora.Utils.Logs;
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

        Logs.Companion.d(TAG, refreshedToken);
        new PrefrenceManager(getApplicationContext()).setFirebase(refreshedToken);


    }
}
