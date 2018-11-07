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
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.ViewModels.Trader.BalncesViewModel;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.Views.CommonFuncs;
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
            public void onMenuItem(int position, int menuItem) {

            }

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


        payouts = new Payouts();
        if (getArguments() != null) {
            payouts = (Payouts) getArguments().getSerializable("data");
        } else {
            payouts = PayoutConstants.getPayouts();
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initCardHeader() {
        setCardHeaderData(payouts);


    }

    public void setCardHeaderData(Payouts model) {


    }

    private void loadCollections() {
        try {
            payoutsVewModel.getCollectionByDateByPayout(payouts.getCode()).observe(this, collections -> {
                if (collections != null) {
                    FragmentPayoutColloectionsList.this.collections = collections;
                    setUpDayCollectionsModel();
                }
            });
        } catch (Exception nm) {
            nm.printStackTrace();
        }
    }

    private void setUpDayCollectionsModel() {


        setUpList(CommonFuncs.setUpDayCollectionsModel(payouts, collections));

    }

    private void setUpList(List<DayCollectionModel> dayCollectionModels) {
        this.dayCollectionModels = dayCollectionModels;
        Objects.requireNonNull(getActivity()).runOnUiThread(this::initList);


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
        new Thread(this::loadCollections).start();
    }



}
