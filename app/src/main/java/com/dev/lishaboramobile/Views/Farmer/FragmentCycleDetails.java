package com.dev.lishaboramobile.Views.Farmer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.dev.lishaboramobile.R;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentCycleDetails extends Fragment implements BlockingStep {

    List<EventDay> events = new ArrayList<>();
    private View view;

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


    }

    void setUp() {
        MaterialSpinner spinner = view.findViewById(R.id.spinnerCycle);
        spinner.setItems("Choose Payout Cycle", "Weekly (7)", "Bi-Weekly", "Monthly");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (position != 0) {
                    setUpCalender(position);
                }
            }
        });
    }

    private void setUpCalender(int position) {
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDayClickListener(eventDay -> {
            events = new ArrayList<>();
            Calendar calendar = eventDay.getCalendar();
            if (position == 1) {
                for (int a = 0; a < 7; a++) {
                    calendar.add(Calendar.DAY_OF_MONTH, a);
                    events.add(new EventDay(calendar, R.drawable.notification_background));

                }
                calendarView.setEvents(events);
            } else if (position == 2) {
                for (int a = 0; a < 7; a++) {
                    calendar.add(Calendar.DAY_OF_MONTH, a);
                    events.add(new EventDay(calendar, R.drawable.notification_background));

                }
                calendarView.setEvents(events);
            } else if (position == 3) {
                for (int a = 0; a < 7; a++) {
                    calendar.add(Calendar.DAY_OF_MONTH, a);
                    events.add(new EventDay(calendar, R.drawable.notification_background));

                }
                calendarView.setEvents(events);
            }
        });


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        callback.goToNextStep();

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

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
