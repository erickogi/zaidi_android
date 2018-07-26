package com.dev.lishaboramobile.login;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.dev.lishaboramobile.BasePermissionAppCompatActivity;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.login.ui.login.ForgotPassOtpFragment;
import com.dev.lishaboramobile.login.ui.login.ForgotPassPhoneFragment;
import com.dev.lishaboramobile.login.ui.login.LoginFragmentPhone;

import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;

public class LoginActivity extends BasePermissionAppCompatActivity {

    private BroadcastReceiver codeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("closeoperation", "truecode received");
            if (intent.getAction().equals("com.lisha.codereceived")) {
                try {

                    ForgotPassOtpFragment forgotPassOtpFragment = (ForgotPassOtpFragment) getSupportFragmentManager().findFragmentById(R.id.container);
                    forgotPassOtpFragment.setCode(intent.getExtras().getString("code"));

                } catch (NullPointerException nm) {
                    nm.printStackTrace();
                }

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, LoginFragmentPhone.newInstance())
                    .commitNow();
        }
        registerReceiver(codeReceiver, new IntentFilter("com.lisha.codereceived"));
        getReadSMSPermission(new RequestPermissionAction() {
            @Override
            public void permissionDenied() {
                // Call Back, when permission is Denied
                // TODO, task after permission is not greante
                Log.d("myPermissions", "not granted");

            }

            @Override
            public void permissionGranted() {
                Log.d("myPermissions", "granted");

                // Call Back, when permission is Granted
                // TODO, task after permission is greante
            }
        });

    }

    public void forgotPasswordClicked(View view) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ForgotPassPhoneFragment.newInstance(null))
                // .addToBackStack("nulo")
                .commitNow();
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), RECEIVE_SMS);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_SMS);

        return result == PackageManager.PERMISSION_GRANTED;// && result1 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(codeReceiver);

    }


}
