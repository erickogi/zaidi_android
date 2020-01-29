package com.dev.zaidi.Views.Trader.Fragments;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.zaidi.AppConstants;
import com.dev.zaidi.Models.Cycles;
import com.dev.zaidi.Models.FamerModel;
import com.dev.zaidi.R;
import com.dev.zaidi.Utils.PrefrenceManager;
import com.dev.zaidi.ViewModels.Trader.TraderViewModel;
import com.dev.zaidi.Views.Trader.FarmerConst;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.List;

public class FragmentCycleDetails extends Fragment implements BlockingStep, View.OnClickListener {

    private View view;
    MaterialSpinner spinner;
    private TraderViewModel mViewModel;
    private List<Cycles> cycles;
    TextView sun, mon, tue, wed, thur, fri, sat;
    TextView txtStartDay, txtEndDay;
    int WEEKLY = 1, BIMONTHLY = 2, MONTHLY = 3, SELECTED = 0;
    private LinearLayout lcyclestart;
    private boolean isWeekly = false;
    private boolean isStartSelected = false;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_createfarmercycle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);
        lcyclestart = view.findViewById(R.id.linear_start_day);
        lcyclestart.setVisibility(View.GONE);
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


    void setUp() {
        hideKeyboardFrom(getContext(), view);

        spinner = view.findViewById(R.id.spinnerCycle);
        setUpSpinners();

        if (FarmerConst.getCreateFarmerIntentType() == 1) {
            FamerModel fm = FarmerConst.getFamerModel();
            if (fm != null) {
                setEditData(fm);
            }
        }

    }

    private void setEditData(FamerModel fm) {

        spinner.setSelectedIndex(Integer.valueOf(fm.getCyclecode()) - 1);
        SELECTED = spinner.getSelectedIndex() + 1;
        spinner.setEnabled(false);

    }

    private void setUpSpinners() {

        spinner.setItems(AppConstants.SEVENDAYS, AppConstants.FIFTEENDAYS, AppConstants.THIRTYDAYS);
        spinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> {
            SELECTED = position + 1;
        });
    }



    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        PrefrenceManager prefrenceManager = new PrefrenceManager(getContext());
        FarmerConst.getFamerModel().setCyclecode("" + SELECTED);
        FarmerConst.getFamerModel().setCyclename(spinner.getItems().get(spinner.getSelectedIndex()).toString());


        FarmerConst.getFamerModel().setCycleStartDate(prefrenceManager.getTraderModel().getCycleStartDay());
        FarmerConst.getFamerModel().setCycleStartDay(prefrenceManager.getTraderModel().getCycleStartDay());
        FarmerConst.getFamerModel().setCycleStartEndDay(prefrenceManager.getTraderModel().getCycleEndDay());


        callback.complete();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();

    }

    @Nullable
    @Override
    public VerificationError verifyStep() {

        if (SELECTED > 0) {

            return null;

        } else {
            Toast.makeText(getContext(), "kjkj" + SELECTED, Toast.LENGTH_LONG).show();
            return new VerificationError("Select Cycle");
        }
    }

    @Override
    public void onSelected() {

        setUp();
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onClick(View view) {
        isStartSelected = true;
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
}
