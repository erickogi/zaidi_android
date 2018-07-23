package com.dev.lishaboramobile.admin;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.admin.ui.admins.AdminsFragment;

public class AdminsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admins_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, AdminsFragment.newInstance())
                    .commitNow();
        }
    }
}
