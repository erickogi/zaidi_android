package com.dev.lishabora.Views.Login.Activities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.dev.lishabora.Utils.BasePermissionAppCompatActivity;
import com.dev.lishabora.Views.Login.Fragments.ForgotPassOtpFragment;
import com.dev.lishabora.Views.Login.Fragments.ForgotPassPhoneFragment;
import com.dev.lishabora.Views.Login.Fragments.LoginFragmentPhone;
import com.dev.lishaboramobile.R;

import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;

public class LoginActivity extends BasePermissionAppCompatActivity {

    private Fragment fragment;
    private BroadcastReceiver codeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);



        fragment = new LoginFragmentPhone();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()//.setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.container, fragment, "fragmentWelcome").commit();


        registerReceiver(codeReceiver, new IntentFilter("com.lisha.codereceived"));


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
