package com.dev.zaidi.Views.Trader.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.zaidi.COntrollers.LoginController;
import com.dev.zaidi.Models.FamerModel;
import com.dev.zaidi.R;
import com.dev.zaidi.Utils.GeneralUtills;
import com.dev.zaidi.ViewModels.Trader.TraderViewModel;
import com.dev.zaidi.Views.Trader.FarmerConst;
import com.hbb20.CountryCodePicker;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class FragmentBasicDetails extends Fragment implements BlockingStep {

    private View view;
    private FamerModel famerModel;
    TextView txtKe;
    private TextInputEditText edtNames, edtMobile;
    private MaterialSpinner defaultPayment;
    private TraderViewModel mViewModel;
    private CountryCodePicker ccp;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }










    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_createfarmerbasic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);

    }

    private String phoneNumber = "";

    public void next() {
        if (FarmerConst.getCreateFarmerIntentType() == 1 && FarmerConst.getFamerModel() != null) {

        } else {

            FarmerConst.setFamerModel(new FamerModel());
        }
        if (!TextUtils.isEmpty(edtMobile.getText().toString())) {
            String phoneNumber1 = ccp.getFullNumber();
            if (phoneNumber1.startsWith("25407")) {
                phoneNumber = phoneNumber1.replace("2540", "");
            }
            if (phoneNumber1.startsWith("2547")) {
                phoneNumber = phoneNumber1.replace("254", "");
            }
            if (LoginController.isValidPhoneNumber(phoneNumber)
                // && GeneralUtills.Companion.isValidPhoneNumber(edtPhone.getText().toString())

            ) {
                String phoneNumber = edtMobile.getText().toString().replaceAll(" ", "").trim();
                FarmerConst.getFamerModel().setNames(GeneralUtills.Companion.capitalize(edtNames.getText().toString()));
                FarmerConst.getFamerModel().setMobile(phoneNumber);


            }
        }




    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

        next();
        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

        callback.complete();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();

    }

    @Nullable
    @Override
    public VerificationError verifyStep() {


        return verify();

    }

    public VerificationError verify() {
        if (verifyNames() && verifyMobile()) {


            return null;
        }
        return new VerificationError("Form has errors");
    }

    @Override
    public void onSelected() {

        initViews();
    }

    @Override
    public void onError(@NonNull VerificationError error) {


    }

    private void initViews() {
        edtNames = view.findViewById(R.id.edt_farmer_names);
        edtMobile = view.findViewById(R.id.edt_farmer_phone);
        defaultPayment = view.findViewById(R.id.spinnerPayments);
        txtKe = view.findViewById(R.id.txt_ke);

        ccp = view.findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(edtMobile);


        edtNames.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !TextUtils.isEmpty(edtNames.getText())) {

                }
            }
        });


        if (FarmerConst.getCreateFarmerIntentType() == 1) {
            FamerModel fm = FarmerConst.getFamerModel();
            if (fm != null) {
                setEditData(fm);
            }
        }
        initData();


    }

    private void setEditData(FamerModel fm) {
        edtNames.setText(fm.getNames());
        edtMobile.setText(fm.getMobile());
    }

    private void initData() {
        defaultPayment.setItems("Payment Options", "Mpesa", "Cash", "Bank");
    }

    private boolean verifyMobile() {
        if (!TextUtils.isEmpty(edtMobile.getText().toString())) {
            String phoneNumber1 = ccp.getFullNumber();
            if (phoneNumber1.startsWith("25407")) {
                phoneNumber = phoneNumber1.replace("2540", "");
            }
            if (phoneNumber1.startsWith("2547")) {
                phoneNumber = phoneNumber1.replace("254", "");
            }
            if (LoginController.isValidPhoneNumber(phoneNumber)) {
                return true;

            } else {
                edtMobile.requestFocus();
                edtMobile.setError("Invalid phone Number");
                return false;
            }
        }
        edtMobile.requestFocus();
        edtMobile.setError("Valid phone required");
        return false;
    }

    private boolean verifyNames() {
        if (!TextUtils.isEmpty(edtNames.getText().toString()) && edtNames.getText().toString().length() > 3) {
            return true;
        }
        edtNames.requestFocus();
        edtNames.setError("Valid Name Required");
        return false;
    }

    private boolean verifySpinner() {
        return defaultPayment.getSelectedIndex() != 0;
    }
}
