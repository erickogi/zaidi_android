package com.dev.lishaboramobile.Views.Farmer;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applandeo.materialcalendarview.EventDay;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Trader.Models.Cycles;
import com.dev.lishaboramobile.Views.Trader.TraderViewModel;
import com.dev.lishaboramobile.login.PrefrenceManager;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

public class FragmentCycleDetails extends Fragment implements BlockingStep {

    List<EventDay> events = new ArrayList<>();
    private View view;
    MaterialSpinner spinner;
    private TraderViewModel mViewModel;
    private List<Cycles> cycles;

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
        return inflater.inflate(R.layout.fragment_createfarmercycle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);



    }


    void setUp() {
        spinner = view.findViewById(R.id.spinnerCycle);
        setUpSpinners();

    }

    private void setUpSpinners() {
        mViewModel.getCycles(true).observe(this, new Observer<List<Cycles>>() {
            @Override
            public void onChanged(@Nullable List<Cycles> cycles) {

                if (cycles != null && cycles.size() > 0) {
                    new PrefrenceManager(getContext()).setIsCyclesListFirst(false);
                    FragmentCycleDetails.this.cycles = cycles;
                    String units[] = new String[cycles.size()];

                    // units[0]="Choose Unit ";
                    for (int a = 0; a < cycles.size(); a++) {
                        units[a] = cycles.get(a).getCycle();

                    }
                    spinner.setItems(units);

                }
            }
        });


        spinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> {
            if (position != 0) {
                setUpCalender(position);
            }
        });
    }

    private void setUpCalender(int position) {
//        CalendarView calendarView = view.findViewById(R.id.calendarView);
//        calendarView.setOnDayClickListener(eventDay -> {
//            events = new ArrayList<>();
//            Calendar calendar = eventDay.getCalendar();
//            if (position == 1) {
//                for (int a = 0; a < 7; a++) {
//                    calendar.add(Calendar.DAY_OF_MONTH, a);
//                    events.add(new EventDay(calendar, R.drawable.notification_background));
//
//                }
//                calendarView.setEvents(events);
//            } else if (position == 2) {
//                for (int a = 0; a < 7; a++) {
//                    calendar.add(Calendar.DAY_OF_MONTH, a);
//                    events.add(new EventDay(calendar, R.drawable.notification_background));
//
//                }
//                calendarView.setEvents(events);
//            } else if (position == 3) {
//                for (int a = 0; a < 7; a++) {
//                    calendar.add(Calendar.DAY_OF_MONTH, a);
//                    events.add(new EventDay(calendar, R.drawable.notification_background));
//
//                }
//                calendarView.setEvents(events);
//            }
//        });
//

    }


    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        FarmerConst.getFamerModel().setCyclecode("" + cycles.get(spinner.getSelectedIndex()).getCode());
        FarmerConst.getFamerModel().setCyclename(spinner.getItems().get(spinner.getSelectedIndex()).toString());

        FarmerConst.getFamerModel().setCycleStartDate("");
        FarmerConst.getFamerModel().setCycleStartDay("");
        FarmerConst.getFamerModel().setCycleStartEndDay("");
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
        return null;
    }

    @Override
    public void onSelected() {

        setUp();
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
