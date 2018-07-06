package com.dev.lishaboramobile.Global.Account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dev.lishaboramobile.Admin.Views.AdminActivity;
import com.dev.lishaboramobile.Global.Data.GlobalPrefs;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Trader.Views.TraderActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GlobalPrefs globalPrefs=new GlobalPrefs(this);
        Intent intent;
        switch (globalPrefs.userLoggedIn()){
            case 0:
                intent=new Intent(this,EntryActivity.class);
                startActivity(intent);
                finish();
                break;
            case 1:
                intent=new Intent(this,TraderActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent=new Intent(this,EntryActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent=new Intent(this,EntryActivity.class);
                startActivity(intent);
                break;
            case 4:
                intent=new Intent(this,EntryActivity.class);
                startActivity(intent);
                break;
            case 5:
                intent=new Intent(this,EntryActivity.class);
                startActivity(intent);
                break;
            case 6:
                intent=new Intent(this,AdminActivity.class);
                startActivity(intent);
                break;

                default:
        }


    }
}
