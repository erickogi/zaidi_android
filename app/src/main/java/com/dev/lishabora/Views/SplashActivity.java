package com.dev.lishabora.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dev.lishabora.COntrollers.LoginController;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.Views.Admin.Activities.AdminsActivity;
import com.dev.lishabora.Views.Login.Activities.LoginActivity;
import com.dev.lishabora.Views.Trader.Activities.TraderActivity;

import timber.log.Timber;

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
            Timber.d("I have tried Log");

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
