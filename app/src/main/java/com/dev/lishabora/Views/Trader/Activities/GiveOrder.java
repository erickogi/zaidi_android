package com.dev.lishabora.Views.Trader.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dev.lishabora.Adapters.GiveOrderAdapter;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.Trader.OrderConstants;
import com.dev.lishaboramobile.R;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.LinkedList;

public class GiveOrder extends AppCompatActivity implements StepperLayout.StepperListener {
    private StepperLayout mStepperLayout;
    private GiveOrderAdapter mStepperAdapter;
    private PrefrenceManager prefrenceManager;
    private TraderViewModel mViewModel;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_launch);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        OrderConstants.setProductOrderModels(new LinkedList<>());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        prefrenceManager = new PrefrenceManager(GiveOrder.this);
        mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);


        mStepperLayout = findViewById(R.id.stepperLayout);
        mStepperAdapter = new GiveOrderAdapter(getSupportFragmentManager(), this);
        mStepperLayout.setAdapter(mStepperAdapter);
        mStepperLayout.setListener(this);
        mStepperLayout.setOffscreenPageLimit(4);

    }

    @Override
    public void onCompleted(View completeButton) {
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
}
