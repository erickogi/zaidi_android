package com.dev.lishabora.Views.Login.Fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.card.MaterialCardView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dev.lishabora.COntrollers.LoginController;
import com.dev.lishabora.Models.Admin.AdminModel;
import com.dev.lishabora.Models.Login.AuthModel;
import com.dev.lishabora.Models.ResponseObject;
import com.dev.lishabora.Models.Trader.TraderModel;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.ViewModels.Login.LoginViewModel;
import com.dev.lishabora.Views.Admin.Activities.AdminsActivity;
import com.dev.lishabora.Views.Login.LoginConsts;
import com.dev.lishabora.Views.Trader.Activities.TraderActivity;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import timber.log.Timber;


public class ForgotPassConfirmFragment extends Fragment implements View.OnClickListener {

    private static int spalsh_time_out = 1000;
    private LoginViewModel mViewModel;
    private String TAG = "lsbLoginTag";
    private Gson gson = new Gson();
    private String phoneNumber = "";
    private AdminModel adminModel = null;
    private TraderModel traderModel = null;


    //CARDS

    private MaterialCardView cardEnterPassword;

    private AVLoadingIndicatorView aviEnterPassword;
    private TextInputEditText edtNewPassword, edtConfirmPassword;

    private Button btnNewPass;

    //CARD STATES
    private ResponseObject responseModel;



    private Context context;
    private View view;



    public static ForgotPassConfirmFragment newInstance(ResponseObject responseModel) {
        ForgotPassConfirmFragment forgotPassPhoneFragment = new ForgotPassConfirmFragment();
        if (responseModel != null) {
            Bundle args = new Bundle();
            args.putSerializable("response", responseModel);
            forgotPassPhoneFragment.setArguments(args);
        }

        return forgotPassPhoneFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.forgot_password_fragment_newpassword, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
    }

    void initCards() {
        cardEnterPassword = view.findViewById(R.id.card_new_password_view);


        initWidgets();
    }

    void initWidgets() {

        edtNewPassword = view.findViewById(R.id.edt_new_password);
        edtConfirmPassword = view.findViewById(R.id.edt_new_confirm_password);
        btnNewPass = view.findViewById(R.id.btn_new_pass_next);

        aviEnterPassword = view.findViewById(R.id.avi_new_pass);


        edtNewPassword.setOnClickListener(this);
        edtConfirmPassword.setOnClickListener(this);
        btnNewPass.setOnClickListener(this);






    }

    @Override
    public void onStart() {
        super.onStart();
        context = getContext();
        initCards();

        if (getArguments() != null) {
            responseModel = (ResponseObject) getArguments().getSerializable("response");
        } else {
            responseModel = LoginConsts.getResponseObject();
        }

        if (responseModel != null) {
            switch (responseModel.getType()) {
                case LoginController.ADMIN:

                    adminModel = gson.fromJson(gson.toJson(responseModel.getData()), AdminModel.class);

                    phoneNumber = adminModel.getMobile();

                    break;
                case LoginController.TRADER:

                    traderModel = gson.fromJson(gson.toJson(responseModel.getData()), TraderModel.class);

                    phoneNumber = traderModel.getMobile();

                    break;
                default:
                    phoneNumber = LoginConsts.getPhone();
            }


        }

    }

    @Override
    public void onResume() {
        super.onResume();
        context = getContext();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_new_pass_next:

                nextOnNewPassClicked();

                break;

            default:
                Timber.d(" No action on click");
        }
    }


    private void nextOnNewPassClicked() {
        if (!TextUtils.isEmpty(edtNewPassword.getText().toString()) && !TextUtils.isEmpty(edtConfirmPassword.getText().toString())) {


            if (edtNewPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
                AuthModel authModel = new AuthModel();
                authModel.setMobile(LoginConsts.getPhone());
                authModel.setPassword(edtConfirmPassword.getText().toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(gson.toJson(authModel));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                aviEnterPassword.smoothToShow();
                aviEnterPassword.setVisibility(View.VISIBLE);

                mViewModel.newPassConfirm(jsonObject).observe(this, responseModel -> {
                    aviEnterPassword.smoothToHide();
                    aviEnterPassword.setVisibility(View.GONE);

                    if (responseModel != null && responseModel.getResultCode() == 1) {

                        switch (responseModel.getType()) {
                            case LoginController.ADMIN:

                                //adminModel = gson.fromJson(gson.toJson(responseModel.getData()), AdminModel.class);
                                adminModel = gson.fromJson(gson.toJson(this.responseModel.getData()), AdminModel.class);

                                loginAdmin(adminModel);

                                break;
                            case LoginController.TRADER:

                                traderModel = gson.fromJson(gson.toJson(this.responseModel.getData()), TraderModel.class);

                                loginTrader(traderModel);
                                break;
                            default:
                        }


                    } else {
                        snack(responseModel.getResultDescription());

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


    private void loginTrader(TraderModel traderModel) {
        PrefrenceManager prefrenceManager = new PrefrenceManager(context);

        prefrenceManager.setIsLoggedIn(true, LoginController.TRADER);
        prefrenceManager.setLoggedUser(traderModel);

        startActivity(new Intent(getActivity(), TraderActivity.class));
        Objects.requireNonNull(getActivity()).finish();
    }

    private void loginAdmin(AdminModel adminModel) {
        PrefrenceManager prefrenceManager = new PrefrenceManager(context);
        prefrenceManager.setIsLoggedIn(true, LoginController.ADMIN);
        prefrenceManager.setLoggedUser(adminModel);

        startActivity(new Intent(getActivity(), AdminsActivity.class));
        Objects.requireNonNull(getActivity()).finish();
    }



    private void snack(String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }
}
