package com.dev.lishabora.Views.Trader.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dev.lishabora.Adapters.TraderFirstTimeAdapter;
import com.dev.lishabora.Models.Trader.TraderModel;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.Trader.TraderConst;
import com.dev.lishaboramobile.R;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.Objects;

public class FirstTimeLaunch extends AppCompatActivity implements StepperLayout.StepperListener {
    private StepperLayout mStepperLayout;
    private TraderFirstTimeAdapter mStepperAdapter;
    private PrefrenceManager prefrenceManager;
    private TraderViewModel mViewModel;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_launch);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (TraderConst.getType_selected() == TraderConst.INTENT_TYPE_EDIT) {
            TraderModel t = TraderConst.getTraderModel();

        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        prefrenceManager = new PrefrenceManager(FirstTimeLaunch.this);
        mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);


        mStepperLayout = findViewById(R.id.stepperLayout);
        mStepperAdapter = new TraderFirstTimeAdapter(getSupportFragmentManager(), this);
        mStepperLayout.setAdapter(mStepperAdapter);
        mStepperLayout.setListener(this);
        mStepperLayout.setOffscreenPageLimit(4);
    }

    @Override
    public void onCompleted(View completeButton) {
        prefrenceManager.setIsTraderFirstTime(false);
        finish();

    }

    @Override
    public void onError(VerificationError verificationError) {

    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {

    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(this.getSupportActionBar()).hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(this.getSupportActionBar()).show();
    }
}
