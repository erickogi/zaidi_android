package com.dev.lishaboramobile.Views.Trader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Views.Trader.ui.createfarmer.CreateFarmerFragment;


/**
 * @author kogi
 */
public class CreateFarmerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_farmer_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CreateFarmerFragment.newInstance())
                    .commitNow();
        }
    }
}
