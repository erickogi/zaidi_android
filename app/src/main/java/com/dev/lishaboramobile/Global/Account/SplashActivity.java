package com.dev.lishaboramobile.Global.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dev.lishaboramobile.Views.Trader.TraderActivity;
import com.dev.lishaboramobile.admin.AdminsActivity;
import com.dev.lishaboramobile.login.LoginActivity;
import com.dev.lishaboramobile.login.PrefrenceManager;
import com.dev.lishaboramobile.login.ui.login.LoginController;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);
    }


    @Override
    protected void onStart() {
        super.onStart();
        PrefrenceManager globalPrefs = new PrefrenceManager(this);
        Intent intent;
        if (globalPrefs.isLoggedIn()) {
            switch (globalPrefs.getTypeLoggedIn()) {
                case LoginController.ADMIN:
                    intent = new Intent(this, AdminsActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case LoginController.TRADER:
                    intent = new Intent(this, TraderActivity.class);
                    startActivity(intent);
                    finish();
                    break;


                default:
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();

            }
        } else {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();

        }


    }
}
