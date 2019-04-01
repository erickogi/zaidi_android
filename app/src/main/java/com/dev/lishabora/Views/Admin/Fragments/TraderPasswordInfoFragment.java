package com.dev.lishabora.Views.Admin.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.dev.lishabora.Models.Trader.TraderModel;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.Views.Admin.CreateTraderConstants;
import com.dev.lishaboramobile.R;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.Objects;

import static com.dev.lishabora.Views.Trader.TraderConst.INTENT_TYPE_EDIT;
import static com.dev.lishabora.Views.Trader.TraderConst.getTraderModel;
import static com.dev.lishabora.Views.Trader.TraderConst.getType_selected;

public class TraderPasswordInfoFragment extends Fragment implements BlockingStep {
    TextInputEditText password, confirmPassword;

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
        return inflater.inflate(R.layout.fragment_trader_passwordinfo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = getView();
        initViews();


    }

    private void initViews() {

        password = view.findViewById(R.id.edt_new_password);
        confirmPassword = view.findViewById(R.id.edt_new_confirm_password);


    }

    private void initData() {
        if (CreateTraderConstants.getTraderModel() != null) {
            traderModel = CreateTraderConstants.getTraderModel();

        }
        hideKeyboardFrom(Objects.requireNonNull(getContext()), getView());

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {


        traderModel.setPassword(password.getText().toString());
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
        if (!TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(confirmPassword.getText().toString())) {

            if (!password.getText().toString().equals(confirmPassword.getText().toString())) {

                snack("Please make sure the passwords are the same");


                confirmPassword.requestFocus();
                confirmPassword.setError("Required");


                return false;

            }
        } else {

            snack("Please enter a valid password");

            confirmPassword.requestFocus();
            confirmPassword.setError("Required");

            password.requestFocus();
            password.setError("Required");

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

    private void snack(String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
        if (getContext() != null) {
            MyToast.toast(msg, getContext(), R.drawable.ic_launcher, Toast.LENGTH_SHORT);
        }
    }
}
