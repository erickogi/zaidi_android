package com.dev.lishabora.Views.Admin.Fragments;

import android.app.Activity;
import android.content.Context;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.dev.lishabora.COntrollers.LoginController;
import com.dev.lishabora.Models.Trader.TraderModel;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.Views.Admin.CreateTraderConstants;
import com.dev.lishaboramobile.R;
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
        phone = view.findViewById(R.id.edt_traders_phone);
        txtKe = view.findViewById(R.id.txt_ke);
        final char space = ' ';

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // if(s.length()>0&&editTextCarrierNumber.getText().toString().charAt(0)!='0') {
                try {
                    if (s.charAt(0) == '0') {
                        s.delete(s.length() - 1, s.length());
                        txtKe.setText("+254-0");
                    } else if (s != null && s.length() < 1) {
                        txtKe.setText("+254");

                    }


                } catch (Exception nm) {
                    nm.printStackTrace();
                }
                //if (s.length() < 9) {
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
                // }else {
                // s.delete(9,9);
                // edtMobile.setError("Tulia");
                // }
            }
        });


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

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        String bs = "";
        if (!bussinessname.getText().toString().isEmpty()) {

            bs = bussinessname.getText().toString();
        }
        traderModel.setBusinessname(bs);
        traderModel.setNames(name.getText().toString());
        String phoneNumber = phone.getText().toString().replaceAll(" ", "").trim();

        traderModel.setMobile(phoneNumber);
        CreateTraderConstants.setTraderModel(traderModel);
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
        if (verify()) {
            return null;
        } else {
            return new VerificationError("Form has errors");
        }
    }

    boolean verify() {
        if (name.getText().toString().isEmpty()) {
            name.setError("Required");
            name.requestFocus();
            return false;
        }


        if (phone.getText().toString().isEmpty()) {
            phone.setError("Required");
            phone.requestFocus();
            return false;
        }
        String phoneNumber = phone.getText().toString().replaceAll(" ", "").trim();

        if (!LoginController.isValidPhoneNumber(phoneNumber) && GeneralUtills.Companion.isValidPhoneNumber(phoneNumber)) {
            phone.requestFocus();
            phone.setError("Invalid Phone number");
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
