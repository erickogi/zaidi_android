package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.lishabora.Adapters.CollectionsAdapter;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.DayCollectionModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.ViewModels.Trader.BalncesViewModel;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishabora.Views.Trader.PayoutConstants;
import com.dev.lishaboramobile.R;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

import static com.dev.lishabora.Views.CommonFuncs.setPayoutActionStatus;

public class FragmentPayoutColloectionsList extends Fragment {
    public TextView status, startDate, cycleName, endDate, milkTotal, loanTotal, orderTotal, balance, approvedCount, unApprovedCount;
    public RelativeLayout background;
    public View statusview;
    private View view;
    private SearchView searchView;
    private String filterText = "";

    private CollectionsAdapter listAdapter;
    private Context context;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout empty_layout;
    private Payouts payouts;
    private PayoutsVewModel payoutsVewModel;
    private BalncesViewModel balncesViewModel;
    private List<DayCollectionModel> dayCollectionModels;
    private List<Collection> collections;
    TextView txtApprovalStatus;
    private MaterialButton btnApprove;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);

        //inflater.inflate(R.menu.menu_main, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);


        searchView = (SearchView) mSearch.getActionView();
        searchView.setVisibility(View.GONE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return false;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        searchView.setVisibility(View.GONE);
    }


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
        balncesViewModel = ViewModelProviders.of(this).get(BalncesViewModel.class);
        btnApprove = view.findViewById(R.id.btn_approve);
        txtApprovalStatus = view.findViewById(R.id.txt_approval_status);
        btnApprove.setVisibility(View.GONE);

        LinearLayout linearLayoutAmPm = view.findViewById(R.id.linear_farmers_titles);
        linearLayoutAmPm.setVisibility(View.GONE);

        payouts = new Payouts();
        if (getArguments() != null) {
            payouts = (Payouts) getArguments().getSerializable("data");
        } else {
            payouts = PayoutConstants.getPayouts();
        }
        if (payouts != null) {
            payoutsVewModel.getPayoutsByPayoutCode("" + payouts.getCode()).observe(this, payouts -> {
                this.payouts = CommonFuncs.createPayout(payouts, payoutsVewModel, balncesViewModel, null, false, payoutsVewModel.getFarmersByCycleONe(payouts.getCycleCode()));
                starterPack();

            });
        }
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

            //   CommonFuncs.doPayout(payouts,balncesViewModel,payoutsVewModel);
            starterPack();

        });
        setPayoutActionStatus(payouts, getContext(), btnApprove, txtApprovalStatus);



    }

    public void setCardHeaderData(Payouts model) {
        startDate.setText(model.getStartDate());
        endDate.setText(model.getEndDate());
        cycleName.setText(model.getCyclename());
        milkTotal.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getMilkTotalLtrs(), 1), getActivity().getString(R.string.ltrs)));
        loanTotal.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getLoanTotal(), 1), getActivity().getString(R.string.ksh)));
        orderTotal.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getOrderTotal(), 1), getActivity().getString(R.string.ksh)));
        balance.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getBalance(), 1), getActivity().getString(R.string.ksh)));

        approvedCount.setText(model.getApprovedCards());
        unApprovedCount.setText(model.getPendingCards());


        if (model.getStatus() == 1) {
            background.setBackgroundColor(getContext().getResources().getColor(R.color.green_color_picker));


        } else if (model.getStatus() == 0) {
            background.setBackgroundColor(getContext().getResources().getColor(R.color.red));

        } else {
            background.setBackgroundColor(getContext().getResources().getColor(R.color.blue_color_picker));

        }

    }

    private void loadCollections() {
        payoutsVewModel.getCollectionByDateByPayout(payouts.getCode()).observe(this, collections -> {
            if (collections != null) {
                FragmentPayoutColloectionsList.this.collections = collections;
                setUpDayCollectionsModel();
            }
        });
    }

    private void setUpDayCollectionsModel() {


        setUpList(CommonFuncs.setUpDayCollectionsModel(payouts, collections));

    }

    private void setUpList(List<DayCollectionModel> dayCollectionModels) {
        this.dayCollectionModels = dayCollectionModels;
        initList();
    }

    @Override
    public void onStart() {
        super.onStart();

        starterPack();
        //setSpinner();
    }

    private void starterPack() {
        initCardHeader();
        initList();
        loadCollections();
    }



}
