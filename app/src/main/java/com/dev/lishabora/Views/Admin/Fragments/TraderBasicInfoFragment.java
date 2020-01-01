package com.dev.lishabora.Views.Admin.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.dev.lishabora.COntrollers.LoginController;
import com.dev.lishabora.Models.Trader.TraderModel;
import com.dev.lishabora.Views.Admin.CreateTraderConstants;
import com.dev.lishaboramobile.R;
import com.hbb20.CountryCodePicker;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.Objects;

import static com.dev.lishabora.Views.Trader.TraderConst.INTENT_TYPE_EDIT;
import static com.dev.lishabora.Views.Trader.TraderConst.getTraderModel;
import static com.dev.lishabora.Views.Trader.TraderConst.getType_selected;

public class TraderBasicInfoFragment extends Fragment implements BlockingStep {
    TextInputEditText name, phone, bussinessname;
    TextView txtKe;

    CheckBox chkDummy;
    Spinner spinnerPayment;
    TraderModel traderModel;
    private View view;
    private CountryCodePicker ccp;

    //private PrefrenceManager prefrenceManager;

    public void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {

            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // prefrenceManager = new PrefrenceManager(getContext());
        if (getType_selected() == INTENT_TYPE_EDIT) {
            traderModel = getTraderModel();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trader_basicinfo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = getView();
        initViews();


    }

    private void initViews() {

        bussinessname = view.findViewById(R.id.edt_traders_business_name);
        name = view.findViewById(R.id.edt_traders_names);
        phone = view.findViewById(R.id.edt_trader_phone);

        ccp = view.findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phone);


    }

    private void initData() {
        if (CreateTraderConstants.getTraderModel() != null) {
            traderModel = CreateTraderConstants.getTraderModel();
            bussinessname.setText(traderModel.getBusinessname());
            name.setText(traderModel.getNames());
            phone.setText(traderModel.getMobile());
        }
        hideKeyboardFrom(Objects.requireNonNull(getContext()), getView());

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    private String phoneNumber = "";

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

        if (!TextUtils.isEmpty(phone.getText().toString())) {
            String phoneNumber1 = ccp.getFullNumber();
            if (phoneNumber1.startsWith("25407")) {
                phoneNumber = phoneNumber1.replace("2540", "");
            }
            if (phoneNumber1.startsWith("2547")) {
                phoneNumber = phoneNumber1.replace("254", "");
            }
            if (LoginController.isValidPhoneNumber(phoneNumber)

            ) {
                String bs = "";
                if (!bussinessname.getText().toString().isEmpty()) {

                    bs = bussinessname.getText().toString();
                }
                traderModel.setBusinessname(bs);
                traderModel.setNames(name.getText().toString());
                traderModel.setMobile(phoneNumber);
                CreateTraderConstants.setTraderModel(traderModel);
                callback.goToNextStep();

            }
        }




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
    private boolean verifyMobile() {
        if (!TextUtils.isEmpty(phone.getText().toString())) {
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
                phone.requestFocus();
                phone.setError("Invalid phone Number");
                return false;
            }
        }
        phone.requestFocus();
        phone.setError("Valid phone required");
        return false;
    }

    boolean verifyNames() {
        if (name.getText().toString().isEmpty()) {
            name.setError("Required");
            name.requestFocus();
            return false;
        }




        return true;


    }


    @Override
    public void onSelected() {
        view = getView();
        Objects.requireNonNull(getActivity()).setTitle("Basic Details");

        initData();


    }


    @Override
    public void onError(@NonNull VerificationError error) {


    }
}
