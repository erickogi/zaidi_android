package com.dev.lishabora.Views.Reports;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.lishabora.Adapters.ReportListAdapter;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.MonthsDates;
import com.dev.lishabora.Models.Reports.ReportListModel;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishaboramobile.R;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class FragmentList extends Fragment implements DatePickerDialog.OnDateSetListener {
    List<ReportListModel> models;
    List<MonthsDates> monthsDates;
    private MaterialSpinner spinnerType, spinnerCat;
    private ImageView imageFrom, imgTo;
    private TextView txtFrom, txtTo, txtTotal, txtValueLabel1, txtValueLabel2;

    private LinearLayout lspinnerType, lspinnerCat, lfrom, lto;
    private RelativeLayout rdateRange;
    private View view;
    private RecyclerView recyclerView;
    private ReportListAdapter listAdapter;
    private int dateImage = 0;
    private String date1, date2;
    private int dataType = 0;
    private PayoutsVewModel payoutsVewModel;
    private TraderViewModel traderViewModel;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payoutsVewModel = ViewModelProviders.of(FragmentList.this).get(PayoutsVewModel.class);
        traderViewModel = ViewModelProviders.of(FragmentList.this).get(TraderViewModel.class);

        if (getArguments() != null) {
            dataType = getArguments().getInt("type");
        } else {
            dataType = 0;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_list, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        recyclerView = view.findViewById(R.id.recyclerView);

        spinnerType = view.findViewById(R.id.spinner_type);
        spinnerCat = view.findViewById(R.id.spinner_cat);

        lspinnerType = view.findViewById(R.id.lspinner_type);
        lspinnerCat = view.findViewById(R.id.lspinner_cat);

        txtValueLabel1 = view.findViewById(R.id.txt_value1_label);
        txtValueLabel2 = view.findViewById(R.id.txt_value2_label);

        lfrom = view.findViewById(R.id.lfrom);
        lto = view.findViewById(R.id.lto);

        rdateRange = view.findViewById(R.id.date_range);

        txtFrom = view.findViewById(R.id.txt_from);
        txtTo = view.findViewById(R.id.txt_to);

        imageFrom = view.findViewById(R.id.img_from);
        imgTo = view.findViewById(R.id.img_to);

        //txtTotal = view.findViewById(R.id.txt_total);


        initList();
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

    public void initList() {
        recyclerView = view.findViewById(R.id.recyclerView);
        listAdapter = new ReportListAdapter(getActivity(), models, new OnclickRecyclerListener() {
            @Override
            public void onClickListener(int position) {

            }

            @Override
            public void onLongClickListener(int position) {

            }

            @Override
            public void onCheckedClickListener(int position) {

            }

            @Override
            public void onMoreClickListener(int position) {

            }

            @Override
            public void onClickListener(int adapterPosition, @NotNull View view) {

            }

            @Override
            public void onSwipe(int adapterPosition, int direction) {

            }
        });


        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        listAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(listAdapter);


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


        if (dataType != 1) {
            txtValueLabel1.setVisibility(View.GONE);
            txtValueLabel2.setText("value");
        }
        spinnerType.setItems("Custom", "Months");
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

            }


        });
        spinnerCat.setOnItemSelectedListener((view, position, id, item) -> {
            if (position == 0) {


                //lspinnerCat.setVisibility(View.GONE);
                reloadData(spinnerType.getSelectedIndex(), position);


            } else if (position == 1) {

                //lspinnerCat.setVisibility(View.VISIBLE);
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
                payoutsVewModel.getCollectionsBetweenDates(DateTimeUtils.Companion.getLongDate(date1), DateTimeUtils.Companion.getLongDate(date2)).observe(FragmentList.this, new Observer<List<Collection>>() {
                    @Override
                    public void onChanged(@Nullable List<Collection> collections) {
                        models = CommonFuncs.generateReportListModel(collections, dataType);
                        listAdapter.refresh(models);
                        calcTotal(models);
                    }
                });

            }
        } else {

            DateTime date = new DateTime().dayOfYear().withMinimumValue().withTimeAtStartOfDay();
            Long from = DateTimeUtils.Companion.getLongDate(DateTimeUtils.Companion.convert2String(date));
            Long to = DateTimeUtils.Companion.getLongDate(DateTimeUtils.Companion.getToday());


            payoutsVewModel.getCollectionsBetweenDates(from, to).observe(FragmentList.this, collections -> {
                models = CommonFuncs.generateReportListModel(monthsDates.get(category), collections, dataType);
                listAdapter.refresh(models);
                calcTotal(models);

            });
        }


    }

    private void calcTotal(List<ReportListModel> models) {

        double total1 = 0.0;
        double total2 = 0.0;

        for (ReportListModel r : models) {
            if (r.getValue1() != null) {
                total1 = total1 + Double.valueOf(r.getValue1());
            }
            if (r.getValue2() != null) {
                total2 = total2 + Double.valueOf(r.getValue2());
            }
        }
        txtTotal.setText(String.valueOf(total1 + total2));
    }


    private void selectDate() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(FragmentList.this,
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
