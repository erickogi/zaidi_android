package com.dev.lishabora;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dev.lishabora.Utils.SampleHelper;
import com.dev.lishaboramobile.R;


public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        SampleHelper.with(this).init().loadAbout();
    }
}
