package com.dev.lishabora.Views.Login.Fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.card.MaterialCardView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dev.lishabora.COntrollers.LoginController;
import com.dev.lishabora.Models.Admin.AdminModel;
import com.dev.lishabora.Models.ResponseObject;
import com.dev.lishabora.Models.Trader.TraderModel;
import com.dev.lishabora.ViewModels.Login.LoginViewModel;
import com.dev.lishabora.Views.Login.LoginConsts;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.hololo.library.otpview.OTPView;
import com.wang.avi.AVLoadingIndicatorView;


public class ForgotPassOtpFragment extends Fragment implements View.OnClickListener {

    private static int spalsh_time_out = 1000;
    private LoginViewModel mViewModel;
    private String TAG = "lsbLoginTag";
    private Gson gson = new Gson();
    private String phoneNumber = "";
    private AdminModel adminModel = null;
    private TraderModel traderModel = null;


    //CARDS

    private MaterialCardView card0tp;

    //WIDGETS
    private OTPView otpView;

    private AVLoadingIndicatorView aviEnterOtp;
    private Button btnNextOtpVerify, btnNextOtpResend;

    //CARD STATES
    private ResponseObject responseModel;



    private Context context;
    private View view;



    public static ForgotPassOtpFragment newInstance(ResponseObject responseModel) {
        ForgotPassOtpFragment forgotPassPhoneFragment = new ForgotPassOtpFragment();
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
        return inflater.inflate(R.layout.forgot_password_fragment_otp, container, false);
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
        card0tp = view.findViewById(R.id.card_otp);
        card0tp.setVisibility(View.VISIBLE);

        initWidgets();


    }

    void initWidgets() {

        otpView = view.findViewById(R.id.otp_view);
        btnNextOtpVerify = view.findViewById(R.id.btn_next_verify);
        aviEnterOtp = view.findViewById(R.id.avi_otp);
        btnNextOtpVerify.setOnClickListener(this);
        //btnNextOtpResend.setOnClickListener(this);

        otpView.setListener(s -> {

            Log.d("ReTrReq", "Sucees " + s);
            btnNextOtpVerify.setEnabled(true);
//            btnNextOtpResend.setEnabled(false);
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
            Log.d("responsemodel", responseModel.getResultDescription());
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

    private void startVerify(String s) {
        if (s.toLowerCase().equals(responseModel.getCode())) {
            handlerOnOtpVerify();
        } else {
            snack("Code mismatch");
        }
    }


    private void handlerOnOtpVerify() {
        aviEnterOtp.setVisibility(View.GONE);
                    aviEnterOtp.smoothToHide();
        //TODO VERIFY THIS CODE

        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.right_enter, R.anim.left_out)

                .replace(R.id.container, ForgotPassConfirmFragment.newInstance(responseModel))
                .commitNow();




    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_next_verify:

                verifyOtplicked();

                break;

            case R.id.btn_next_resend:

                resendOtpClicked();


                break;



            default:
                Log.d(TAG, " No action on click");
        }
    }


    private void verifyOtplicked() {

        if (otpView.getOtp() != null && otpView.getOtp().length() == 4) {
            startVerify(otpView.getOtp());
        } else {
            snack("Wrong code");
        }
    }

    private void resendOtpClicked() {
        //TODO HANDLE RESEND OTP REQUEST
    }


    private void snack(String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }

    public void setCode(String code) {
        otpView.setOtp(code);
        startVerify(code);
    }
}
