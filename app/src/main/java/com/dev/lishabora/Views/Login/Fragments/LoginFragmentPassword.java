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
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishabora.Application;
import com.dev.lishabora.COntrollers.LoginController;
import com.dev.lishabora.Models.Admin.AdminModel;
import com.dev.lishabora.Models.Login.AuthModel;
import com.dev.lishabora.Models.ResponseObject;
import com.dev.lishabora.Models.Trader.TraderModel;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.ViewModels.Login.LoginViewModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
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


public class LoginFragmentPassword extends Fragment implements View.OnClickListener {
    private static int spalsh_time_out = 1000;
    private String TAG = "lsbLoginTag";
    private LoginViewModel mViewModel;
    private TraderViewModel traderViewModel;
    private Gson gson = new Gson();

    //CARDS

    TextView txtForgotPassword;

    //WIDGETS
    ResponseObject responseModel;
    private MaterialCardView passwordCard;
    private AVLoadingIndicatorView aviPass;
    private TextInputEditText edtPassword;
    private TextView txtPasswordViewTitle;



    private Context context;
    private View view;
    private Button btnNextPasswordView;
    private String phoneNumber = "";

    private Fragment fragment;


    public static LoginFragmentPassword newInstance(ResponseObject responseObject) {
        LoginFragmentPassword loginFragmentPassword = new LoginFragmentPassword();
        Bundle bundle = new Bundle();
        bundle.putSerializable("response", responseObject);
        loginFragmentPassword.setArguments(bundle);
        return loginFragmentPassword;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment_password, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        traderViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
    }

    void initCards() {
        //Find views
        passwordCard = view.findViewById(R.id.card_password_view);


        initWidgets();


    }

    void initWidgets() {

        edtPassword = view.findViewById(R.id.edt_password);
        txtPasswordViewTitle = view.findViewById(R.id.txt_password_view_title);
        btnNextPasswordView = view.findViewById(R.id.btn_next_password_view);
        txtForgotPassword = view.findViewById(R.id.txt_forgot_password);
        aviPass = view.findViewById(R.id.avi_pass);

        edtPassword.requestFocus();
        edtPassword.setOnClickListener(this);
        btnNextPasswordView.setOnClickListener(this);
        txtPasswordViewTitle.setOnClickListener(this);
        txtForgotPassword.setOnClickListener(view -> forgotPassword());


    }


    @Override
    public void onStart() {
        super.onStart();
        context = getContext();
        initCards();

        if (getArguments() != null) {
            responseModel = (ResponseObject) getArguments().getSerializable("response");

            Gson gson = new Gson();
            TraderModel traderModel;
            AdminModel adminModel;

            switch (responseModel.getType()) {
                case LoginController.ADMIN:

                    adminModel = gson.fromJson(gson.toJson(responseModel.getData()), AdminModel.class);
                    txtPasswordViewTitle.setText(responseModel.getResultDescription());
                    this.phoneNumber = adminModel.getMobile();

                    break;
                case LoginController.TRADER:

                    traderModel = gson.fromJson(gson.toJson(responseModel.getData()), TraderModel.class);
                    txtPasswordViewTitle.setText(responseModel.getResultDescription());
                    this.phoneNumber = traderModel.getMobile();



                    break;
                default:
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

            case R.id.btn_next_password_view:

                nextOnPasswordInputClicked();

                break;

            default:
                Timber.d(" No action on click");
        }
    }



    private void nextOnPasswordInputClicked() {
        if (!TextUtils.isEmpty(edtPassword.getText().toString())) {

            aviPass.setVisibility(View.VISIBLE);
            aviPass.smoothToShow();
            AuthModel authModel = new AuthModel();
            authModel.setMobile(phoneNumber);
            authModel.setPassword(edtPassword.getText().toString());
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(gson.toJson(authModel));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            mViewModel.passwordAuth(jsonObject).observe(this, responseModel -> {
                snack(responseModel.getResultDescription());
                aviPass.smoothToHide();
                LoginConsts.setResponseObject(responseModel);

                if (responseModel.getResultCode() == 1) {
                    snack(responseModel.getResultDescription());

                    Gson gson = new Gson();
                    TraderModel traderModel;
                    AdminModel adminModel;

                    switch (responseModel.getType()) {
                        case LoginController.ADMIN:

                            adminModel = gson.fromJson(gson.toJson(responseModel.getData()), AdminModel.class);

                            loginAdmin(adminModel);

                            break;
                        case LoginController.TRADER:

                            traderModel = gson.fromJson(gson.toJson(responseModel.getData()), TraderModel.class);

                            loginTrader(traderModel);
                            break;
                        default:
                    }
                }
            });


        } else {

            aviPass.setVisibility(View.GONE);
            aviPass.smoothToHide();

            snack("Please enter  password");
            edtPassword.requestFocus();
            edtPassword.setError("Required");

        }

    }

    private void forgotPassword() {


        fragment = ForgotPassPhoneFragment.newInstance(responseModel);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()//.setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.container, fragment, "fragmentWelcome").addToBackStack(null).commit();

    }

    private void loginTrader(TraderModel traderModel) {
        PrefrenceManager prefrenceManager = new PrefrenceManager(context);

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
        if (context != null) {
            MyToast.toast(msg, context, Toast.LENGTH_SHORT);
        }
    }




}
