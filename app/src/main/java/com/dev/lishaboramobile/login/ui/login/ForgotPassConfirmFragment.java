package com.dev.lishaboramobile.login.ui.login;


import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.card.MaterialCardView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dev.lishaboramobile.Admin.Models.AdminModel;
import com.dev.lishaboramobile.Global.Account.ResponseObject;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Trader.Models.TraderModel;
import com.dev.lishaboramobile.Views.Admin.AdminActivity;
import com.dev.lishaboramobile.Views.Trader.TraderActivity;
import com.dev.lishaboramobile.login.Models.AuthModel;
import com.dev.lishaboramobile.login.PrefrenceManager;
import com.google.gson.Gson;
import com.hololo.library.otpview.OTPView;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;


public class ForgotPassConfirmFragment extends Fragment implements View.OnClickListener {

    private static int spalsh_time_out = 1000;
    private LoginViewModel mViewModel;
    private String TAG = "lsbLoginTag";
    private Gson gson = new Gson();
    private String phoneNumber = "";
    private AdminModel adminModel = null;
    private TraderModel traderModel = null;


    //CARDS

    private MaterialCardView cardForgotPassPhoneView, card0tp, cardEnterPassword;

    //WIDGETS
    private OTPView otpView;

    private AVLoadingIndicatorView aviForgotPass, aviEnterOtp, aviEnterPassword;
    private TextInputEditText edtForgotPassPhone, edtNewPassword, edtConfirmPassword;

    private Button btnNextFogort, btnNextOtpVerify, btnNextOtpResend, btnNewPass;

    //CARD STATES
    private ResponseObject responseModel;

    private int forgotPassState, otpCardState, enterPassCardState;

    //STATES

    private int ISVISIBLE = 0;
    private int ISGONE = 1;
    private int INVISIBLE = 2;


    private Context context;
    private View view;
    private BroadcastReceiver codeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("closeoperation", "truecode received");
            if (intent.getAction().equals("com.lishabora.codereceived")) {
                try {


                } catch (NullPointerException nm) {
                    nm.printStackTrace();
                    Log.d("closeoperation", nm.toString());
                }


            }

        }
    };


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
        return inflater.inflate(R.layout.forgot_password_fragment, container, false);
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
        //Find views
        cardForgotPassPhoneView = view.findViewById(R.id.card_forgot_pass_phone_view);
        card0tp = view.findViewById(R.id.card_otp);
        cardEnterPassword = view.findViewById(R.id.card_new_password_view);

        //Set password view gone
        card0tp.setVisibility(View.GONE);
        cardEnterPassword.setVisibility(View.GONE);

        //initialize states
        forgotPassState = ISVISIBLE;
        otpCardState = ISGONE;
        enterPassCardState = ISGONE;

        initWidgets();


    }

    void initWidgets() {

        otpView = view.findViewById(R.id.otp_view);
        edtForgotPassPhone = view.findViewById(R.id.edt_forgot_pass_phone);
        edtNewPassword = view.findViewById(R.id.edt_new_password);
        edtConfirmPassword = view.findViewById(R.id.edt_new_confirm_password);
        btnNewPass = view.findViewById(R.id.btn_new_pass_next);


        btnNextFogort = view.findViewById(R.id.btn_next_forgot_pass);
        btnNextOtpVerify = view.findViewById(R.id.btn_next_verify);
        btnNextOtpResend = view.findViewById(R.id.btn_next_resend);

        aviForgotPass = view.findViewById(R.id.avi_forgot_pass);
        aviEnterOtp = view.findViewById(R.id.avi_otp);
        aviEnterPassword = view.findViewById(R.id.avi_new_pass);


        edtForgotPassPhone.setOnClickListener(this);
        edtNewPassword.setOnClickListener(this);
        edtConfirmPassword.setOnClickListener(this);
        btnNextFogort.setOnClickListener(this);
        btnNextOtpVerify.setOnClickListener(this);
        btnNextOtpResend.setOnClickListener(this);
        btnNewPass.setOnClickListener(this);


        otpView.setListener(s -> {

            btnNextOtpVerify.setEnabled(true);
            btnNextOtpResend.setEnabled(false);

            startVerify(s);
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        context = getContext();
        initCards();

        if (getArguments() != null) {
            responseModel = (ResponseObject) getArguments().getSerializable("response");
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
            }

            card0tp.setVisibility(View.GONE);
            cardForgotPassPhoneView.setVisibility(View.GONE);
            cardEnterPassword.setVisibility(View.VISIBLE);

            otpCardState = ISGONE;
            forgotPassState = ISGONE;
            enterPassCardState = ISVISIBLE;

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

    private void startVerify(String s) {
        handlerOnOtpVerify();
    }

    private void handlerOnOtpVerify() {
        new Handler().postDelayed(() -> {

                    aviEnterOtp.setVisibility(View.GONE);
                    aviEnterOtp.smoothToHide();

                    snack("Otp verify simulation success");

                    card0tp.setVisibility(View.GONE);
                    cardForgotPassPhoneView.setVisibility(View.GONE);
                    cardEnterPassword.setVisibility(View.VISIBLE);

                    otpCardState = ISGONE;
                    forgotPassState = ISGONE;
                    enterPassCardState = ISVISIBLE;


                }


                , spalsh_time_out);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next_forgot_pass:

                nextOnForgotPassPhoneClicked();

                break;

            case R.id.btn_next_verify:

                verifyOtplicked();

                break;

            case R.id.btn_next_resend:

                resendOtpClicked();


                break;

            case R.id.btn_new_pass_next:

                nextOnNewPassClicked();

                break;

            default:
                Log.d(TAG, " No action on click");
        }
    }

    private void nextOnForgotPassPhoneClicked() {
        if (!TextUtils.isEmpty(edtForgotPassPhone.getText().toString())) {

            if (LoginController.isValidPhoneNumber(edtForgotPassPhone.getText().toString())) {
                String phoneNumber = edtForgotPassPhone.getText().toString().replaceAll(" ", "").trim();

                this.phoneNumber = phoneNumber;
                aviForgotPass.setVisibility(View.VISIBLE);
                aviForgotPass.smoothToShow();
//TODO FINISH UPHERE
                AuthModel authModel = new AuthModel();
                authModel.setMobile(phoneNumber);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(gson.toJson(authModel));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                mViewModel.otpRequest(jsonObject).observe(this, responseModel -> {
                    snack(responseModel.getResultDescription());

                    if (responseModel != null && responseModel.getResultCode() == 1) {
                        snack(responseModel.getResultDescription());
                        aviForgotPass.setVisibility(View.GONE);
                        aviForgotPass.smoothToHide();
                    }
                });


                // handlerOnForgotPass();
            } else {
                snack("Invalid Phone");
                edtForgotPassPhone.requestFocus();
                edtForgotPassPhone.setError("Invalid");
            }

        } else {
            snack("Please Enter phone");
            edtForgotPassPhone.requestFocus();
            edtForgotPassPhone.setError("Required");
        }
        //TODO HANDLE FORGOT PASSWORD REQUEST
    }

    private void verifyOtplicked() {
        //TODO HANDLE VERIFYOTP REQUEST
    }

    private void resendOtpClicked() {
        //TODO HANDLE RESEND OTP REQUEST
    }

    private void nextOnNewPassClicked() {
        if (!TextUtils.isEmpty(edtNewPassword.getText().toString()) && !TextUtils.isEmpty(edtConfirmPassword.getText().toString())) {


            if (edtNewPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
                AuthModel authModel = new AuthModel();
                authModel.setMobile(phoneNumber);
                authModel.setPassword(edtConfirmPassword.getText().toString());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(gson.toJson(authModel));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                mViewModel.newPassConfirm(jsonObject).observe(this, responseModel -> {
                    snack(responseModel.getResultDescription());

                    if (responseModel != null && responseModel.getResultCode() == 1) {
                        snack(responseModel.getResultDescription());
                        aviEnterPassword.setVisibility(View.GONE);
                        aviEnterOtp.smoothToHide();

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
        getActivity().finish();
    }

    private void loginAdmin(AdminModel adminModel) {
        PrefrenceManager prefrenceManager = new PrefrenceManager(context);
        prefrenceManager.setIsLoggedIn(true, LoginController.ADMIN);
        prefrenceManager.setLoggedUser(adminModel);

        startActivity(new Intent(getActivity(), AdminActivity.class));
        getActivity().finish();
    }

    private void handlerOnForgotPass() {
        new Handler().postDelayed(() -> {

                    aviForgotPass.setVisibility(View.GONE);
                    aviForgotPass.smoothToHide();

                    snack("Phone Auth simulation success");

                    cardForgotPassPhoneView.setVisibility(View.GONE);
                    card0tp.setVisibility(View.VISIBLE);
                    handlerOnOtpWaitVerify();
                    aviEnterOtp.setVisibility(View.VISIBLE);
                    aviEnterOtp.smoothToShow();
                    otpCardState = ISVISIBLE;
                    forgotPassState = ISGONE;


                }


                , spalsh_time_out);

    }

    private void handlerOnNewPassword() {
        new Handler().postDelayed(() -> {

                    aviEnterPassword.setVisibility(View.GONE);
                    aviEnterPassword.smoothToHide();

                    snack("New password simulation success");

                    cardEnterPassword.setVisibility(View.GONE);

                    otpCardState = ISGONE;
                    forgotPassState = ISGONE;
                    enterPassCardState = ISGONE;

                    startActivity(new Intent(getActivity(), AdminActivity.class));
                    Objects.requireNonNull(getActivity()).finish();


                }


                , spalsh_time_out);

    }

    private void handlerOnOtpWaitVerify() {
        new Handler().postDelayed(() -> {

                    // aviEnterOtp.setVisibility(View.GONE);
                    // aviForgotPass.smoothToHide();

                    otpView.setOtp("32576");
                    snack("Otp Simulation was success");


                }


                , spalsh_time_out - 400);

    }

    private void snack(String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }
}
