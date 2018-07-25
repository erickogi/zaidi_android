package com.dev.lishaboramobile.login;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.login.ui.login.ForgotPassOtpFragment;
import com.dev.lishaboramobile.login.ui.login.ForgotPassPhoneFragment;
import com.dev.lishaboramobile.login.ui.login.LoginFragmentPhone;

import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;

public class LoginActivity extends AppCompatActivity {

    private ForgotPassOtpFragment forgotPassOtpFragment;
    private BroadcastReceiver codeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("closeoperation", "truecode received");
            if (intent.getAction().equals("com.lisha.codereceived")) {
                try {
                    // Intent hhtpIntent = new Intent(LoginActivity.this, HttpService.class);
                    // hhtpIntent.putExtra("otp", intent.getExtras().getString("code"));

                    //startService(hhtpIntent);

                    forgotPassOtpFragment = (ForgotPassOtpFragment) getSupportFragmentManager().findFragmentById(R.id.container);
                    forgotPassOtpFragment.setCode(intent.getExtras().getString("code"));


                    //signUpFragment.setText("23432");
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
