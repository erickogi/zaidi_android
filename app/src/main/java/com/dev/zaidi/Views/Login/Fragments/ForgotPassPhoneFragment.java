package com.dev.zaidi.Views.Login.Fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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

import com.dev.zaidi.COntrollers.LoginController;
import com.dev.zaidi.Models.Admin.AdminModel;
import com.dev.zaidi.Models.Login.AuthModel;
import com.dev.zaidi.Models.ResponseObject;
import com.dev.zaidi.Models.Trader.TraderModel;
import com.dev.zaidi.R;
import com.dev.zaidi.Utils.GeneralUtills;
import com.dev.zaidi.ViewModels.Login.LoginViewModel;
import com.dev.zaidi.Views.Login.LoginConsts;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;


public class ForgotPassPhoneFragment extends Fragment implements View.OnClickListener {

    private static int spalsh_time_out = 1000;
    private LoginViewModel mViewModel;
    private String TAG = "lsbLoginTag";
    private Gson gson = new Gson();
    private String phoneNumber = "";
    private AdminModel adminModel = null;
    private TraderModel traderModel = null;


    //CARDS

    private MaterialCardView cardForgotPassPhoneView;
    private Fragment fragment;


    private AVLoadingIndicatorView aviForgotPass;
    private TextInputEditText edtForgotPassPhone;

    private Button btnNextFogort;


    private ResponseObject responseModel;



    private Context context;
    private View view;



    public static ForgotPassPhoneFragment newInstance(ResponseObject responseModel) {
        ForgotPassPhoneFragment forgotPassPhoneFragment = new ForgotPassPhoneFragment();
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
        return inflater.inflate(R.layout.forgot_password_fragment_phone, container, false);
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


        initWidgets();


    }

    void initWidgets() {

        edtForgotPassPhone = view.findViewById(R.id.edt_forgot_pass_phone);

        btnNextFogort = view.findViewById(R.id.btn_next_forgot_pass);

        aviForgotPass = view.findViewById(R.id.avi_forgot_pass);

        edtForgotPassPhone.setOnClickListener(this);
        btnNextFogort.setOnClickListener(this);

        edtForgotPassPhone.requestFocus();
        if (phoneNumber != null) {
            edtForgotPassPhone.setText(phoneNumber);

            edtForgotPassPhone.setSelection(edtForgotPassPhone.getText().length());
        }



    }

    @Override
    public void onStart() {
        super.onStart();
        context = getContext();

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
        initCards();


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
            case R.id.btn_next_forgot_pass:

                nextOnForgotPassPhoneClicked();

                break;


            default:
                Timber.d(" No action on click");
        }
    }

    private void nextOnForgotPassPhoneClicked() {
        if (!TextUtils.isEmpty(edtForgotPassPhone.getText().toString())) {

            if (LoginController.isValidPhoneNumber(edtForgotPassPhone.getText().toString()) && GeneralUtills.Companion.isValidPhoneNumber(edtForgotPassPhone.getText().toString())) {
                String phoneNumber = edtForgotPassPhone.getText().toString().replaceAll(" ", "").trim();

                this.phoneNumber = phoneNumber;
                aviForgotPass.setVisibility(View.VISIBLE);
                aviForgotPass.smoothToShow();

                AuthModel authModel = new AuthModel();
                authModel.setMobile(phoneNumber);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(gson.toJson(authModel));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                mViewModel.otpRequest(jsonObject).observe(this, responseModel -> {
                    aviForgotPass.setVisibility(View.GONE);
                    aviForgotPass.smoothToHide();

                    if (responseModel != null && responseModel.getResultCode() == 1) {
                        LoginConsts.setPhone(phoneNumber);
                        otpFragment(responseModel);
                    } else {
                        if (responseModel != null) {
                            snack(responseModel.getResultDescription());
                        }

                    }
                });



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

    }





    public void otpFragment(ResponseObject responseModel) {
        // getChildFragmentManager()
//        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.right_enter, R.anim.left_out)
//
//                .replace(R.id.container, ForgotPassOtpFragment.newInstance(responseModel))
//                // .addToBackStack("null")
//                .commitNow();
//

        fragment = ForgotPassOtpFragment.newInstance(responseModel);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.container, fragment, "fragmentWelcome").addToBackStack(null).commit();


    }

    private void snack(String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }
}
