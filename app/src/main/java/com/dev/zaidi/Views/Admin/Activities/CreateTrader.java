package com.dev.zaidi.Views.Admin.Activities;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.dev.zaidi.Adapters.CreateTraderAdapter;
import com.dev.zaidi.Models.Trader.TraderModel;
import com.dev.zaidi.R;
import com.dev.zaidi.Utils.DateTimeUtils;
import com.dev.zaidi.Utils.GeneralUtills;
import com.dev.zaidi.Utils.MyToast;
import com.dev.zaidi.Utils.PrefrenceManager;
import com.dev.zaidi.ViewModels.Admin.AdminsViewModel;
import com.dev.zaidi.Views.Admin.CreateTraderConstants;
import com.dev.zaidi.Views.Trader.TraderConst;
import com.google.gson.Gson;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class CreateTrader extends AppCompatActivity implements StepperLayout.StepperListener {
    private StepperLayout mStepperLayout;
    private CreateTraderAdapter mStepperAdapter;
    private PrefrenceManager prefrenceManager;
    private AdminsViewModel mViewModel;
    private int pos;
    private ProgressDialog progressDialog;

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
        prefrenceManager = new PrefrenceManager(CreateTrader.this);
        mViewModel = ViewModelProviders.of(this).get(AdminsViewModel.class);


        mStepperLayout = findViewById(R.id.stepperLayout);
        mStepperAdapter = new CreateTraderAdapter(getSupportFragmentManager(), this);
        mStepperLayout.setAdapter(mStepperAdapter);
        mStepperLayout.setListener(this);
        mStepperLayout.setOffscreenPageLimit(4);
    }

    @Override
    public void onCompleted(View completeButton) {
        TraderModel traderModel = CreateTraderConstants.getTraderModel();
        //if(type==ISCREATE) {
        //traderModel.setId(0);
        traderModel.setCode("" + new GeneralUtills(this).getRandon(9000, 1000));
        traderModel.setEntity("Admin");
        traderModel.setEntityname("LishaBora");
        //traderModel.setBusinessname(bussines);
        traderModel.setDefaultpaymenttype("Mpesa");


        traderModel.setEntitycode(new PrefrenceManager(this).getAdmin().getCode());

        traderModel.setTransactioncode(DateTimeUtils.Companion.getNow());

        traderModel.setPassword("password");
        traderModel.setBalance("0");
        traderModel.setApikey("");
        traderModel.setFirebasetoken("");
        traderModel.setStatus("Active");
        traderModel.setTransactiontime(DateTimeUtils.Companion.getNow());
        traderModel.setSynctime(DateTimeUtils.Companion.getNow());
        traderModel.setTransactedby(" "+new PrefrenceManager(this).getAdmin().getApikey());


        traderModel.setArchived(0);
        traderModel.setDeleted(0);
        traderModel.setSynced(0);
        traderModel.setDummy(0);


        traderModel.setIsdeleted(false);
        traderModel.setIsarchived(false);
        traderModel.setIsdummy(false);

        // CreateTraderConstants.setTraderModel(traderModel);

//        TraderPayload traderPayload=new TraderPayload();
//        traderPayload.setTraderModel(traderModel);
//        traderPayload.setProductModels(CreateTraderConstants.getProductModels());
//

        //startAnim();


        traderModel.setProductModels(CreateTraderConstants.getProductModels());
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(new Gson().toJson(traderModel));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialog = new ProgressDialog(CreateTrader.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Creating Trader");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.show();

        mViewModel.registerTrader(jsonObject, true).observe(this, responseModel -> {
            // stopAnim();


            progressDialog.dismiss();

            if (responseModel != null) {
                if (responseModel.getResultCode() == 1) {
                    MyToast.toast(responseModel.getResultDescription(), CreateTrader.this, R.drawable.ic_account_circle_black_24dp, Toast.LENGTH_LONG);

                    Intent returnIntent = new Intent();


                    setResult(RESULT_OK, returnIntent);
                    finish();


                } else {
                    MyToast.errorToast(responseModel.getResultDescription(), CreateTrader.this);

                }
            }

        });


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
