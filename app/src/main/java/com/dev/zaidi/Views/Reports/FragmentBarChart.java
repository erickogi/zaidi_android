package com.dev.zaidi.Views.Reports;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.zaidi.Adapters.ReportListAdapter;
import com.dev.zaidi.Models.Collection;
import com.dev.zaidi.Models.DaysDates;
import com.dev.zaidi.Models.MonthsDates;
import com.dev.zaidi.Models.Reports.ReportLineChartModel;
import com.dev.zaidi.R;
import com.dev.zaidi.Utils.DateTimeUtils;
import com.dev.zaidi.ViewModels.Trader.PayoutsVewModel;
import com.dev.zaidi.ViewModels.Trader.TraderViewModel;
import com.dev.zaidi.Views.CommonFuncs;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class FragmentBarChart extends Fragment implements DatePickerDialog.OnDateSetListener {
    List<ReportLineChartModel> models;
    List<MonthsDates> monthsDates;
    List<DaysDates> daysDates;
    BarChart chart;
    LineChart lchart;
    private MaterialSpinner spinnerType, spinnerCat;
    private ImageView imageFrom, imgTo;
    private LinearLayout lspinnerType, lspinnerCat, lfrom, lto;
    private RelativeLayout rdateRange;
    private View view;
    private RecyclerView recyclerView;
    private ReportListAdapter listAdapter;
    private TextView txtFrom, txtTo, txtTotal, txtValueLabel1, txtValueLabel2;
    private int dateImage = 0;
    private String date1, date2;
    private int dataType = 0;
    private PayoutsVewModel payoutsVewModel;
    private TraderViewModel traderViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payoutsVewModel = ViewModelProviders.of(FragmentBarChart.this).get(PayoutsVewModel.class);
        traderViewModel = ViewModelProviders.of(FragmentBarChart.this).get(TraderViewModel.class);

        if (getArguments() != null) {
            dataType = getArguments().getInt("type");
        } else {
            dataType = 0;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_chart, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinnerType = view.findViewById(R.id.spinner_type);
        spinnerCat = view.findViewById(R.id.spinner_cat);

        lspinnerType = view.findViewById(R.id.lspinner_type);
        lspinnerCat = view.findViewById(R.id.lspinner_cat);

        txtValueLabel1 = view.findViewById(R.id.txt_value1_label);
        txtValueLabel2 = view.findViewById(R.id.txt_value2_label);

        lfrom = view.findViewById(R.id.lfrom);
        lto = view.findViewById(R.id.lto);

        chart = view.findViewById(R.id.chartbar);
        lchart = view.findViewById(R.id.chartline);
        lchart.setVisibility(View.GONE);

        rdateRange = view.findViewById(R.id.date_range);

        txtFrom = view.findViewById(R.id.txt_from);
        txtTo = view.findViewById(R.id.txt_to);

        imageFrom = view.findViewById(R.id.img_from);
        imgTo = view.findViewById(R.id.img_to);

        //txtTotal = view.findViewById(R.id.txt_total);


        // initList();
        initViewData();


        imageFrom.setOnClickListener(view1 -> {
            dateImage = 1;
            selectDate();
        });
        imgTo.setOnClickListener(view12 -> {
            dateImage = 2;
            selectDate();
        });

        lfrom.setOnClickListener(view1 -> {
            dateImage = 1;
            selectDate();
        });
        lto.setOnClickListener(view12 -> {
            dateImage = 2;
            selectDate();
        });


    }

    private void createMonthlyList() {


        monthsDates = DateTimeUtils.Companion.getMonths(12);
        String[] months = new String[monthsDates.size()];
        for (int a = 0; a < monthsDates.size(); a++) {
            months[a] = monthsDates.get(a).getMonthName() + " ";

        }
        spinnerCat.setItems(months);


    }


    private void initViewData() {


//        if (dataType != 1) {
//            txtValueLabel1.setVisibility(View.GONE);
//            txtValueLabel2.setText("value");
//        }
        spinnerType.setItems("Custom", "Months", "Yearly");
        createMonthlyList();


        spinnerType.setOnItemSelectedListener((view, position, id, item) -> {
            if (position == 0) {
                lspinnerCat.setVisibility(View.GONE);
                rdateRange.setVisibility(View.VISIBLE);
                reloadData(position, spinnerCat.getSelectedIndex());


            } else if (position == 1) {


                rdateRange.setVisibility(View.GONE);
                lspinnerCat.setVisibility(View.VISIBLE);
                reloadData(position, spinnerCat.getSelectedIndex());

            } else if (position == 2) {


                rdateRange.setVisibility(View.GONE);
                lspinnerCat.setVisibility(View.GONE);
                reloadData(position, spinnerCat.getSelectedIndex());

            }


        });
        spinnerCat.setOnItemSelectedListener((view, position, id, item) -> {
            if (position == 0) {


                reloadData(spinnerType.getSelectedIndex(), position);


            } else if (position == 1) {

                reloadData(spinnerType.getSelectedIndex(), position);

            }


        });

        spinnerType.setSelectedIndex(0);
        lspinnerCat.setVisibility(View.GONE);
        rdateRange.setVisibility(View.VISIBLE);


        reloadData(spinnerType.getSelectedIndex(), spinnerCat.getSelectedIndex());


    }

    private void reloadData(int type, int category) {

        if (type == 0) {

            if (date1 != null && date2 != null) {
                payoutsVewModel.getCollectionsBetweenDates(DateTimeUtils.Companion.getLongDate(date1), DateTimeUtils.Companion.getLongDate(date2)).observe(FragmentBarChart.this, new Observer<List<Collection>>() {
                    @Override
                    public void onChanged(@Nullable List<Collection> collections) {
                        models = CommonFuncs.getCollectionsCustomReport(collections, DateTimeUtils.Companion.getDaysAndDatesBtnDates(date1, date2), dataType);
                        calcTotal(models);
                        refreshChart(models);
                    }
                });

            }
        } else if (type == 1) {


            String from = (monthsDates.get(category).getMonthStartDate());
            String to = (monthsDates.get(category).getMonthEndDate());


            payoutsVewModel.getCollectionsBetweenDates(DateTimeUtils.Companion.getLongDate(from), DateTimeUtils.Companion.getLongDate(to)).observe(FragmentBarChart.this, collections -> {
                models = CommonFuncs.getCollectionsCustomReport(collections, DateTimeUtils.Companion.getDaysAndDatesBtnDates(from, to), dataType);
                calcTotal(models);
                refreshChart(models);

            });
        } else if (type == 2) {

            DateTime date = new DateTime().dayOfYear().withMinimumValue().withTimeAtStartOfDay();
            Long from = DateTimeUtils.Companion.getLongDate(DateTimeUtils.Companion.convert2String(date));
            Long to = DateTimeUtils.Companion.getLongDate(DateTimeUtils.Companion.getToday());


            payoutsVewModel.getCollectionsBetweenDates(from, to).observe(FragmentBarChart.this, collections -> {
                models = CommonFuncs.getCollectionsMonthlyReport(collections, monthsDates, dataType);
                calcTotal(models);
                refreshChart(models);

            });
        }


    }


    private void refreshChart(List<ReportLineChartModel> models) {
        List<BarEntry> entries = new ArrayList<>();

        float xvalue = 0;
        for (ReportLineChartModel data : models) {

            // turn your data into Entry objects
            entries.add(new BarEntry(xvalue, Float.valueOf(data.getYvalue())));
            xvalue++;

        }

        BarDataSet dataSet = new BarDataSet(entries, "Collection Report"); // add entries to dataset
        //dataSet.setColor();
        // dataSet.setValueTextColor(...); // styling, ...
        BarData lineData = new BarData(dataSet);
        chart.setData(lineData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                try {
                    return models.get((int) value).getXvalue();
                } catch (Exception nm) {
                    nm.printStackTrace();
                    return "";
                }
            }
        });


        chart.invalidate(); // refresh


    }

    private void calcTotal(List<ReportLineChartModel> models) {

        double total1 = 0.0;
        double total2 = 0.0;

        for (ReportLineChartModel r : models) {
            if (r.getYvalue() != null) {
                total1 = total1 + Double.valueOf(r.getYvalue());
            }
//            if (r.getValue2() != null) {
//                total2 = total2 + Double.valueOf(r.getValue2());
//            }
        }
        //txtTotal.setText(String.valueOf(total1 + total2));
    }


    private void selectDate() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(FragmentBarChart.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        dpd.setThemeDark(false);
        dpd.vibrate(true);
        dpd.dismissOnPause(true);
        dpd.showYearPickerFirst(true);
        dpd.setMaxDate(now);

        if (dateImage == 1) {
            dpd.setTitle("From ? ");
        } else {
            dpd.setTitle("To ? ");

        }
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);

        dpd.show(Objects.requireNonNull(getActivity()).getFragmentManager(), "DatePicker");
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if (dateImage == 1) {
            date1 = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            txtFrom.setText("..From .." + date1);
            reloadData(spinnerType.getSelectedIndex(), spinnerCat.getSelectedIndex());
        } else {
            date2 = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            txtTo.setText("..To .." + date2);
            reloadData(spinnerType.getSelectedIndex(), spinnerCat.getSelectedIndex());
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
