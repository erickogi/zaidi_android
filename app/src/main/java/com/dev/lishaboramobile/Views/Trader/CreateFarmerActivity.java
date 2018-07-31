package com.dev.lishaboramobile.Views.Trader;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dev.lishaboramobile.Global.Utils.DateTimeUtils;
import com.dev.lishaboramobile.Global.Utils.GeneralUtills;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Views.Farmer.FarmerConst;
import com.dev.lishaboramobile.admin.adapters.FarmetRecruitAdapter;
import com.dev.lishaboramobile.login.PrefrenceManager;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;


/**
 * @author kogi
 */
public class CreateFarmerActivity extends AppCompatActivity implements StepperLayout.StepperListener {
    private StepperLayout mStepperLayout;
    private FarmetRecruitAdapter mStepperAdapter;
    private PrefrenceManager prefrenceManager;
    private TraderViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_farmer_activity);
        prefrenceManager = new PrefrenceManager(CreateFarmerActivity.this);
        mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);


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

        FarmerConst.getFamerModel().setCode("" + new GeneralUtills(this).getRandon(9999, 1000));
        FarmerConst.getFamerModel().setArchived(0);
        FarmerConst.getFamerModel().setDeleted(0);
        FarmerConst.getFamerModel().setDummy(0);
        FarmerConst.getFamerModel().setTransactiontime(DateTimeUtils.Companion.getNow());
        FarmerConst.getFamerModel().setSynched(false);
        FarmerConst.getFamerModel().setTransactedby(prefrenceManager.getTraderModel().getApikey());
        FarmerConst.getFamerModel().setTotalorders("0");
        FarmerConst.getFamerModel().setTotalbalance("0");
        FarmerConst.getFamerModel().setTotalloans("0");
        FarmerConst.getFamerModel().setTotalmilkcollection("0");
        FarmerConst.getFamerModel().setTransactioncode(prefrenceManager.getTraderModel().getCode() + "" + DateTimeUtils.Companion.getNow());
        FarmerConst.getFamerModel().setEntity("Trader");
        FarmerConst.getFamerModel().setEntitycode(prefrenceManager.getTraderModel().getCode());
        FarmerConst.getFamerModel().setCompositecode(prefrenceManager.getTraderModel().getCode() + "" + FarmerConst.getFamerModel().getCode());
        FarmerConst.getFamerModel().setFirebasetoken("");


        mViewModel.createFarmer(FarmerConst.getFamerModel(), false).observe(this, responseModel -> {
            if (responseModel != null && responseModel.getResultCode() == 1) {
                snack(responseModel.getResultDescription());
                finish();
            }
        });
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
