package com.dev.lishaboramobile.Views.Farmer;

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

import com.dev.lishaboramobile.Farmer.Models.FamerModel;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Views.Trader.TraderViewModel;
import com.dev.lishaboramobile.login.ui.login.LoginController;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class FragmentBasicDetails extends Fragment implements BlockingStep {

    private View view;
    private FamerModel famerModel;
    private TextInputEditText edtNames, edtMobile;
    private MaterialSpinner defaultPayment;
    private TraderViewModel mViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
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


    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        if (FarmerConst.getFamerModel() == null) {
            FarmerConst.setFamerModel(new FamerModel());
        }
        FarmerConst.getFamerModel().setNames(edtNames.getText().toString());
        FarmerConst.getFamerModel().setMobile(edtMobile.getText().toString());


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

        initData();

    }

    private void initData() {
        defaultPayment.setItems("Payment Options", "Mpesa", "Cash", "Bank");
        final char space = ' ';
        edtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // if(s.length()>0&&editTextCarrierNumber.getText().toString().charAt(0)!='0') {
                if (s.length() > 0 && (s.length() % 4) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (space == c) {
                        s.delete(s.length() - 1, s.length());
                    }

                }
                // Insert char where needed.
                if (s.length() > 0 && (s.length() % 4) == 0) {
                    char c = s.charAt(s.length() - 1);
                    // Only if its a digit where there should be a space we insert a space
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                        s.insert(s.length() - 1, String.valueOf(space));
                    }

                }
            }
        });


    }

    private boolean verifyMobile() {
        if (!TextUtils.isEmpty(edtMobile.getText().toString())) {
            String phoneNumber = edtMobile.getText().toString().replaceAll(" ", "").trim();
            if (LoginController.isValidPhoneNumber(phoneNumber)) {
                return true;

            }
            edtMobile.requestFocus();
            edtMobile.setError("Invalid phone Number");
            return false;

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
