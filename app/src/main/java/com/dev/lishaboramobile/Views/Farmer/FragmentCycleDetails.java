package com.dev.lishaboramobile.Views.Farmer;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applandeo.materialcalendarview.EventDay;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Trader.Models.Cycles;
import com.dev.lishaboramobile.Views.Trader.TraderViewModel;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

public class FragmentCycleDetails extends Fragment implements BlockingStep, View.OnClickListener {

    List<EventDay> events = new ArrayList<>();
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
        spinner = view.findViewById(R.id.spinnerCycle);
        setUpSpinners();

    }

    private void setUpSpinners() {
//        mViewModel.getCycles(true).observe(this, new Observer<List<Cycles>>() {
//            @Override
//            public void onChanged(@Nullable List<Cycles> cycles) {
//
//                if (cycles != null && cycles.size() > 0) {
//                    new PrefrenceManager(getContext()).setIsCyclesListFirst(false);
//                    FragmentCycleDetails.this.cycles = cycles;
//                    String units[] = new String[cycles.size()];
//
//                    // units[0]="Choose Unit ";
//                    for (int a = 0; a < cycles.size(); a++) {
//                        units[a] = cycles.get(a).getCycle();
//
//                    }
//                    spinner.setItems(units);
//
//                }
//            }
//        });
        spinner.setItems("Weekly", "Monthly", "Bi-Monthly", "");


        spinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> {
            if (position == 0) {
                isWeekly = true;
                SELECTED = WEEKLY;
                setUpCalender(position);
            } else {
                if (position == 1) {
                    SELECTED = MONTHLY;
                } else if (position == 2) {
                    SELECTED = BIMONTHLY;
                }
                isWeekly = false;
                lcyclestart.setVisibility(View.GONE);
            }
        });
    }

    private void setUpCalender(int position) {
        lcyclestart.setVisibility(View.VISIBLE);





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
//        FarmerConst.getFamerModel().setCyclecode("" + SELECTED);
//        FarmerConst.getFamerModel().setCyclename(spinner.getItems().get(spinner.getSelectedIndex()).toString());
//
//        FarmerConst.getFamerModel().setCycleStartDate("");
//        FarmerConst.getFamerModel().setCycleStartDay("");
//        FarmerConst.getFamerModel().setCycleStartEndDay("");
//        callback.goToNextStep();

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        FarmerConst.getFamerModel().setCyclecode("" + SELECTED);
        FarmerConst.getFamerModel().setCyclename(spinner.getItems().get(spinner.getSelectedIndex()).toString());

        String startDay = "";
        String endDay = "";
        String price = "";
        if (isWeekly) {
            startDay = txtStartDay.getText().toString();
            endDay = txtEndDay.getText().toString();
        }
        FarmerConst.getFamerModel().setCycleStartDate(startDay);
        FarmerConst.getFamerModel().setCycleStartDay(startDay);
        FarmerConst.getFamerModel().setCycleStartEndDay(endDay);

        callback.complete();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();

    }

    @Nullable
    @Override
    public VerificationError verifyStep() {

        if (SELECTED == WEEKLY) {
            if (isWeekly && isStartSelected) {
                return null;
            } else if (isWeekly && !isStartSelected) {
                return new VerificationError("Select start day");
            } else {
                return null;
            }
        } else if (SELECTED > 0) {
            return null;

        } else {
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
