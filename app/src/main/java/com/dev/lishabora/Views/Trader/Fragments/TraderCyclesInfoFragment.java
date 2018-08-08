package com.dev.lishabora.Views.Trader.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.dev.lishabora.Models.Trader.TraderModel;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishaboramobile.R;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.Objects;

public class TraderCyclesInfoFragment extends Fragment implements BlockingStep, View.OnClickListener {
    TraderModel traderModel;
    TextView sun, mon, tue, wed, thur, fri, sat;
    TextView txtStartDay, txtEndDay;
    private View view;
    private PrefrenceManager prefrenceManager;

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
        prefrenceManager = new PrefrenceManager(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trader_weeklycycle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = getView();
        initViews();


    }

    private void initViews() {
        sun = view.findViewById(R.id.sun);
        mon = view.findViewById(R.id.mon);
        tue = view.findViewById(R.id.tue);
        wed = view.findViewById(R.id.wed);
        thur = view.findViewById(R.id.thur);
        fri = view.findViewById(R.id.fri);
        sat = view.findViewById(R.id.sat);


        txtStartDay = view.findViewById(R.id.starts);
        txtEndDay = view.findViewById(R.id.ends);


        sun.setOnClickListener(this);
        sat.setOnClickListener(this);
        fri.setOnClickListener(this);
        mon.setOnClickListener(this);
        tue.setOnClickListener(this);
        wed.setOnClickListener(this);
        thur.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.sun:

                txtStartDay.setText(R.string.sunday);
                txtEndDay.setText("Saturday");

                break;
            case R.id.mon:

                txtStartDay.setText("Monday");
                txtEndDay.setText("Sunday");

                break;
            case R.id.tue:

                txtStartDay.setText("Tuesday");
                txtEndDay.setText("Monday");

                break;
            case R.id.wed:

                txtStartDay.setText("Wednesday");
                txtEndDay.setText("Tuesday");

                break;
            case R.id.thur:

                txtStartDay.setText("Thursday");
                txtEndDay.setText("Wednesday");

                break;
            case R.id.fri:

                txtStartDay.setText("Friday");
                txtEndDay.setText("Thursday");

                break;
            case R.id.sat:

                txtStartDay.setText("Saturday");
                txtEndDay.setText("Friday");

                break;
            default:


        }
    }

    private void initData() {
        traderModel = prefrenceManager.getTraderModel();
        txtStartDay.setText(traderModel.getCycleStartDay());
        txtEndDay.setText(traderModel.getCycleEndDay());


        hideKeyboardFrom(Objects.requireNonNull(getContext()), getView());


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

        traderModel.setCycleStartDay(txtStartDay.getText().toString());
        traderModel.setCycleEndDay(txtEndDay.getText().toString());
        prefrenceManager.setLoggedUser(traderModel);
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
        if (!TextUtils.isEmpty(txtStartDay.getText().toString()) && !TextUtils.isEmpty(txtEndDay.getText().toString())) {
            return null;
        } else {
            return new VerificationError("Make sure to select a starting day");
        }
    }

    @Override
    public void onSelected() {
        initData();

    }


    @Override
    public void onError(@NonNull VerificationError error) {


    }
}
