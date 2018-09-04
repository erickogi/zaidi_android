package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dev.lishabora.Adapters.CollectionsAdapter;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.DayCollectionModel;
import com.dev.lishabora.Models.DaysDates;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.Views.Trader.PayoutConstants;
import com.dev.lishaboramobile.R;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class FragmentPayoutColloectionsList extends Fragment {
    public TextView status, startDate, cycleName, endDate, milkTotal, loanTotal, orderTotal, balance, approvedCount, unApprovedCount;
    public RelativeLayout background;
    public View statusview;
    private View view;

    private CollectionsAdapter listAdapter;
    private Context context;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout empty_layout;
    private Payouts payouts;
    private PayoutsVewModel payoutsVewModel;
    private List<DayCollectionModel> dayCollectionModels;
    private List<Collection> collections;
    private MaterialButton btnApprove, btnBack;

    public void initList() {
        recyclerView = view.findViewById(R.id.recyclerView);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        if (dayCollectionModels == null) {
            dayCollectionModels = new LinkedList<>();
        }
        listAdapter = new CollectionsAdapter(getActivity(), dayCollectionModels, new OnclickRecyclerListener() {
            @Override
            public void onSwipe(int adapterPosition, int direction) {


            }

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
        });
        recyclerView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payout_collections, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);
        btnApprove = view.findViewById(R.id.btn_approve);
        btnApprove.setVisibility(View.GONE);

        LinearLayout linearLayoutAmPm = view.findViewById(R.id.linear_am_pm);
        linearLayoutAmPm.setVisibility(View.VISIBLE);

        payouts = new Payouts();
        if (getArguments() != null) {
            payouts = (Payouts) getArguments().getSerializable("data");
        } else {
            payouts = PayoutConstants.getPayouts();
        }
        payoutsVewModel.getPayoutsByPayoutNumber("" + payouts.getPayoutnumber()).observe(this, new Observer<Payouts>() {
            @Override
            public void onChanged(@Nullable Payouts payouts) {
                // FragmentPayoutColloectionsList.this.payouts=payouts;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initCardHeader() {
        background = view.findViewById(R.id.background);
        startDate = view.findViewById(R.id.txt_date_start);
        endDate = view.findViewById(R.id.txt_date_end);


        cycleName = view.findViewById(R.id.txt_cycle);

        milkTotal = view.findViewById(R.id.txt_milk_totals);
        loanTotal = view.findViewById(R.id.txt_loans_total);
        orderTotal = view.findViewById(R.id.txt_orders_total);

        approvedCount = view.findViewById(R.id.txt_approved_farmers);
        unApprovedCount = view.findViewById(R.id.txt_pending_farmers);
        balance = view.findViewById(R.id.txt_Bal_out);

        if (payouts != null) {
            setCardHeaderData(payouts);
        }
        if (payouts.getStatus() == 1) {
            btnApprove.setVisibility(View.GONE);
        } else {
            if (payouts.getEndDate().equals(DateTimeUtils.Companion.getToday()) || DateTimeUtils.Companion.isPastLastDay(payouts.getEndDate())) {
                btnApprove.setVisibility(View.VISIBLE);
            } else {
                btnApprove.setVisibility(View.GONE);
            }
        }

        btnApprove.setOnClickListener(view -> {
            payouts.setStatus(1);
            payouts.setStatusName("Approved");
            payoutsVewModel.updatePayout(payouts);
            initCardHeader();

        });

    }

    public void setCardHeaderData(Payouts model) {
        startDate.setText(model.getStartDate());
        endDate.setText(model.getEndDate());
        cycleName.setText(model.getCyclename());
        milkTotal.setText(model.getMilkTotal());
        loanTotal.setText(model.getLoanTotal());
        orderTotal.setText(model.getOrderTotal());

        balance.setText(model.getBalance());
        approvedCount.setText(model.getApprovedCards());
        unApprovedCount.setText(model.getPendingCards());


        if (model.getStatus() == 1) {
            // status.setText("Active");
            background.setBackgroundColor(getContext().getResources().getColor(R.color.green_color_picker));


        } else if (model.getStatus() == 0) {
            background.setBackgroundColor(getContext().getResources().getColor(R.color.red));

        } else {
            background.setBackgroundColor(getContext().getResources().getColor(R.color.blue_color_picker));

        }

    }

    private void loadCollections() {
        payoutsVewModel.getCollectionByDateByPayout("" + payouts.getPayoutnumber()).observe(this, collections -> {
            if (collections != null) {
                FragmentPayoutColloectionsList.this.collections = collections;
                setUpDayCollectionsModel();
            }
        });
    }

    private void setUpDayCollectionsModel() {


        List<DaysDates> daysDates = DateTimeUtils.Companion.getDaysAndDatesBtnDates(payouts.getStartDate(), payouts.getEndDate());

        List<DayCollectionModel> dayCollectionModels = new LinkedList<>();
        for (DaysDates d : daysDates) {
            String milkAm = getMilk(d.getDate(), "AM");
            String milkPm = getMilk(d.getDate(), "PM");
            String loanAm = getLoan(d.getDate(), "AM");
            String laonPm = getLoan(d.getDate(), "PM");
            String orderAm = getOrder(d.getDate(), "AM");
            String orderPm = getOrder(d.getDate(), "PM");

            dayCollectionModels.add(new DayCollectionModel(payouts.getPayoutnumber(),
                    d.getDay(), d.getDate(), milkAm,
                    milkPm, loanAm, laonPm, orderAm,
                    orderPm, 0, 0

            ));
        }

        setUpList(dayCollectionModels);

    }

    private String getOrder(String date, String ampm) {
        double orderTotal = 0.0;
        if (collections != null) {
            for (Collection c : collections) {

                if (c.getDayDate().contains(date) && c.getTimeOfDay().equals(ampm)) {
                    try {
                        orderTotal += Double.valueOf(c.getOrderGivenOutPrice());
                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }
                }
            }

        }
        return String.valueOf(orderTotal);

    }

    private String getLoan(String date, String ampm) {
        double loanTotal = 0.0;
        if (collections != null) {
            for (Collection c : collections) {
                if (c.getDayDate().contains(date) && c.getTimeOfDay().equals(ampm)) {
                    try {
                        loanTotal += Double.valueOf(c.getLoanAmountGivenOutPrice());
                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }
                }
            }
        }
        return String.valueOf(loanTotal);


    }

    private String getMilk(String date, String ampm) {
        double milkTotal = 0.0;
        if (collections != null) {
            for (Collection c : collections) {
                if (c.getDayDate().equals(date) && c.getTimeOfDay().equals(ampm)) {
                    try {
                        milkTotal += Double.valueOf(c.getMilkCollected());
                        Log.d("CollectionsVsDate", " Date : " + date + "  Time " + ampm + "\nColDate : " + c.getDayDate() + "  ColTime " + c.getTimeOfDay() + "\n Milk " + c.getMilkCollected());

                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }
                }
            }
        }
        return String.valueOf(milkTotal);


    }

    private void setUpList(List<DayCollectionModel> dayCollectionModels) {
        this.dayCollectionModels = dayCollectionModels;
        initList();
    }

    @Override
    public void onStart() {
        super.onStart();
        initCardHeader();
        initList();
        loadCollections();
        setSpinner();
    }

    private void setSpinner() {
        try {
            RelativeLayout rspinner = Objects.requireNonNull(getActivity()).findViewById(R.id.linear_spinner);
            rspinner.setVisibility(View.VISIBLE);
            Spinner spinner = Objects.requireNonNull(getActivity()).findViewById(R.id.spinner);
            spinner.setVisibility(View.VISIBLE);
            spinner.setSelection(0);


            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if (i == 0) {


                    } else {

                        Fragment fragment = new FragmentPayoutFarmersList();
                        ((com.dev.lishabora.Views.Trader.Activities.Payouts) getActivity()).popOutFragments();
                        ((com.dev.lishabora.Views.Trader.Activities.Payouts) getActivity()).setUpView(fragment);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } catch (Exception nm) {
            nm.printStackTrace();
        }
    }
}
