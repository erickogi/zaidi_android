package com.dev.lishaboramobile.Login;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dev.lishaboramobile.Login.ui.login.LoginFragmentPhone;
import com.dev.lishaboramobile.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, LoginFragmentPhone.newInstance())
                    .commitNow();
        }
    }
}
