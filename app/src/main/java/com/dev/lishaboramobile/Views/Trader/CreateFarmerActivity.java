package com.dev.lishaboramobile.Views.Trader;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.admin.adapters.FarmetRecruitAdapter;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;


/**
 * @author kogi
 */
public class CreateFarmerActivity extends AppCompatActivity implements StepperLayout.StepperListener {
    private StepperLayout mStepperLayout;
    private FarmetRecruitAdapter mStepperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_farmer_activity);


        mStepperLayout = findViewById(R.id.stepperLayout);
        mStepperAdapter = new FarmetRecruitAdapter(getSupportFragmentManager(), this);
        mStepperLayout.setAdapter(mStepperAdapter);
        mStepperLayout.setListener(this);
        mStepperLayout.setOffscreenPageLimit(4);

    }

    private void snack(String msg) {
        Snackbar.make(mStepperLayout, msg, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    @Override
    public void onCompleted(View completeButton) {

    }

    @Override
    public void onError(VerificationError verificationError) {
        snack(verificationError.getErrorMessage());
    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {

    }
}
