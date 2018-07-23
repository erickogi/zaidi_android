package com.dev.lishaboramobile.login;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.login.ui.login.ForgotPassPhoneFragment;
import com.dev.lishaboramobile.login.ui.login.LoginFragmentPhone;

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


    public void forgotPasswordClicked(View view) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ForgotPassPhoneFragment.newInstance(null))
                // .addToBackStack("nulo")
                .commitNow();
    }
}
