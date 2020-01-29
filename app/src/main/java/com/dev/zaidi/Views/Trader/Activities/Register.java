package com.dev.zaidi.Views.Trader.Activities;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dev.zaidi.Adapters.RegisterTraderAdapter;
import com.dev.zaidi.Application;
import com.dev.zaidi.COntrollers.LoginController;
import com.dev.zaidi.Models.Admin.AdminModel;
import com.dev.zaidi.Models.Trader.TraderModel;
import com.dev.zaidi.R;
import com.dev.zaidi.Utils.DateTimeUtils;
import com.dev.zaidi.Utils.GeneralUtills;
import com.dev.zaidi.Utils.PrefrenceManager;
import com.dev.zaidi.ViewModels.Admin.AdminsViewModel;
import com.dev.zaidi.ViewModels.Trader.TraderViewModel;
import com.dev.zaidi.Views.Admin.Activities.AdminsActivity;
import com.dev.zaidi.Views.Admin.CreateTraderConstants;
import com.google.gson.Gson;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Register extends AppCompatActivity implements StepperLayout.StepperListener {
    private StepperLayout mStepperLayout;
    private RegisterTraderAdapter mStepperAdapter;
    private PrefrenceManager prefrenceManager;
    private AdminsViewModel mViewModel;
    private TraderViewModel traderViewModel;

    private int pos;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_launch);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        prefrenceManager = new PrefrenceManager(Register.this);
        mViewModel = ViewModelProviders.of(this).get(AdminsViewModel.class);
        traderViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);


        mStepperLayout = findViewById(R.id.stepperLayout);
        mStepperAdapter = new RegisterTraderAdapter(getSupportFragmentManager(), this);
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


        traderModel.setEntitycode("Self Registration");

        traderModel.setTransactioncode(DateTimeUtils.Companion.getNow());

        traderModel.setPasswordstatus(1);
        traderModel.setBalance("0");
        traderModel.setApikey("");
        traderModel.setFirebasetoken("");
        traderModel.setStatus("Active");
        traderModel.setTransactiontime(DateTimeUtils.Companion.getNow());
        traderModel.setSynctime(DateTimeUtils.Companion.getNow());
        traderModel.setTransactedby("Self Registration");


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

        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Registering");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.show();

        mViewModel.registerTrader(jsonObject, true).observe(this, responseModel -> {
            // stopAnim();


            if (responseModel.getResultCode() == 1) {
                // snack(responseModel.getResultDescription());

                Gson gson = new Gson();
                TraderModel traderModel1;
                AdminModel adminModel;

                switch (responseModel.getType()) {
                    case LoginController.ADMIN:

                        adminModel = gson.fromJson(gson.toJson(responseModel.getData()), AdminModel.class);

                        loginAdmin(adminModel);

                        break;
                    case LoginController.TRADER:

                        traderModel1 = gson.fromJson(gson.toJson(responseModel.getData()), TraderModel.class);

                        loginTrader(traderModel1);
                        break;
                    default:
                }
            } else {
                progressDialog.dismiss();

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

    private void loginTrader(TraderModel traderModel) {
        PrefrenceManager prefrenceManager = new PrefrenceManager(this);

        prefrenceManager.setIsLoggedIn(true, LoginController.TRADER);
        prefrenceManager.setLoggedUser(traderModel);

        Application.syncDown();

        traderViewModel.createTrader(traderModel);


        if (!prefrenceManager.isFirebaseUpdated()) {
            traderModel.setFirebasetoken(prefrenceManager.getFirebase());
            JSONObject jsonObject = new JSONObject();


            try {
                jsonObject = new JSONObject(new Gson().toJson(traderModel));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Application.updateTrader(jsonObject);
        }

        progressDialog.dismiss();


        startActivity(new Intent(this, TraderActivity.class));
        Objects.requireNonNull(this).finish();
    }

    private void loginAdmin(AdminModel adminModel) {
        PrefrenceManager prefrenceManager = new PrefrenceManager(this);
        prefrenceManager.setIsLoggedIn(true, LoginController.ADMIN);
        prefrenceManager.setLoggedUser(adminModel);
        progressDialog.dismiss();

        startActivity(new Intent(this, AdminsActivity.class));
        Objects.requireNonNull(this).finish();
    }

//    private void snack(String msg) {
//        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
//        if ( != null) {
//            MyToast.toast(msg, context, R.drawable.ic_launcher, Toast.LENGTH_SHORT);
//        }
//    }


}
