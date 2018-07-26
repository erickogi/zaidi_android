package com.dev.lishaboramobile.admin.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Trader.Models.TraderModel;
import com.dev.lishaboramobile.admin.models.AdminModel;
import com.dev.lishaboramobile.login.Models.AuthModel;
import com.dev.lishaboramobile.login.PrefrenceManager;
import com.dev.lishaboramobile.login.ui.login.LoginController;
import com.dev.lishaboramobile.login.ui.login.LoginViewModel;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

public class ResetPassword extends AppCompatActivity {
    private TextInputEditText edtForgotPassPhone, edtNewPassword, edtConfirmPassword;
    private LoginViewModel mViewModel;
    private Button btnNewPass;
    private PrefrenceManager prefrenceManager;
    private AVLoadingIndicatorView avLoadingIndicatorView;
    private RelativeLayout parentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        prefrenceManager = new PrefrenceManager(this);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        avLoadingIndicatorView = findViewById(R.id.avi_new_pass);
        parentView = findViewById(R.id.parent_view);

        edtNewPassword = findViewById(R.id.edt_new_password);
        edtConfirmPassword = findViewById(R.id.edt_new_confirm_password);
        btnNewPass = findViewById(R.id.btn_new_pass_next);
        btnNewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextOnNewPassClicked();
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    private void nextOnNewPassClicked() {
        if (!TextUtils.isEmpty(edtNewPassword.getText().toString()) && !TextUtils.isEmpty(edtConfirmPassword.getText().toString())) {

            String phoneNumber = "";

            Gson gson = new Gson();
            if (edtNewPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
                switch (prefrenceManager.getTypeLoggedIn()) {
                    case LoginController.ADMIN:

                        AdminModel adminModel = prefrenceManager.getAdmin();

                        phoneNumber = adminModel.getMobile();


                        break;
                    case LoginController.TRADER:
                        TraderModel traderModel = prefrenceManager.getTraderModel();

                        phoneNumber = traderModel.getMobile();

                        break;

                    default:
                }
                AuthModel authModel = new AuthModel();
                authModel.setMobile(phoneNumber);
                authModel.setPassword(edtConfirmPassword.getText().toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(gson.toJson(authModel));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                avLoadingIndicatorView.setVisibility(View.VISIBLE);
                avLoadingIndicatorView.smoothToShow();
                mViewModel.newPassConfirm(jsonObject).observe(this, responseModel -> {
                    snack(responseModel.getResultDescription());

                    if (responseModel != null && responseModel.getResultCode() == 1) {
                        snack(responseModel.getResultDescription());
                        avLoadingIndicatorView.smoothToHide();
                        switch (responseModel.getType()) {
                            case LoginController.ADMIN:

                                AdminModel adminModel = gson.fromJson(gson.toJson(responseModel.getData()), AdminModel.class);

                                loginAdmin(adminModel);

                                break;
                            case LoginController.TRADER:

                                TraderModel traderModel = gson.fromJson(gson.toJson(responseModel.getData()), TraderModel.class);

                                loginTrader(traderModel);
                                break;
                            default:
                        }


                    }
                });

                //   handlerOnNewPassword();
            } else {
                snack("Passwords do not match");
                edtConfirmPassword.requestFocus();
                edtConfirmPassword.setError("Password don't match");
            }
            //TODO HANDLE NEW PASSWORD REQUEST

        } else {
            snack("Fill all fields");
            edtConfirmPassword.setError("Required");
            edtNewPassword.setError("Required");
        }
    }

    private void snack(String msg) {
        Snackbar.make(parentView, msg, Snackbar.LENGTH_LONG).show();
    }


    private void loginTrader(TraderModel traderModel) {
        PrefrenceManager prefrenceManager = new PrefrenceManager(this);

        prefrenceManager.setIsLoggedIn(true, LoginController.TRADER);
        prefrenceManager.setLoggedUser(traderModel);

        finish();
    }

    private void loginAdmin(AdminModel adminModel) {
        PrefrenceManager prefrenceManager = new PrefrenceManager(this);
        prefrenceManager.setIsLoggedIn(true, LoginController.ADMIN);
        prefrenceManager.setLoggedUser(adminModel);
        finish();
    }
}
