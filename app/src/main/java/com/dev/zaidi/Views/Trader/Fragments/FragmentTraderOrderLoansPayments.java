package com.dev.zaidi.Views.Trader.Fragments;

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

import com.dev.zaidi.Adapters.LoansOrdersPaymnetsAdapter;
import com.dev.zaidi.Models.Trader.FarmerOrdersTable;
import com.dev.zaidi.Models.Trader.LoanPayments;
import com.dev.zaidi.Models.Trader.OrderPayments;
import com.dev.zaidi.R;
import com.dev.zaidi.Utils.OnActivityTouchListener;
import com.dev.zaidi.Utils.OnclickRecyclerListener;
import com.dev.zaidi.Utils.RecyclerTouchListener;
import com.dev.zaidi.ViewModels.Trader.BalncesViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FragmentTraderOrderLoansPayments extends Fragment {
    LoansOrdersPaymnetsAdapter listAdapter;
    List<Integer> unclickableRows, unswipeableRows;
    private BalncesViewModel balncesViewModel;
    private RecyclerView recyclerView;
    private RecyclerTouchListener onTouchListener;
    private int openOptionsPosition;
    private OnActivityTouchListener touchListener;
    private View view;

    private List<LoanPayments> loanPayments;
    private List<OrderPayments> orderPayments;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;

    public void initList() {
        unclickableRows = new ArrayList<>();
        unswipeableRows = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerView);

        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        listAdapter = new LoansOrdersPaymnetsAdapter(getActivity(), loanPayments, orderPayments, new OnclickRecyclerListener() {
            @Override
            public void onMenuItem(int position, int menuItem) {

                // action();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_recycler_view, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        balncesViewModel = ViewModelProviders.of(this).get(BalncesViewModel.class);

        int type = getArguments().getInt("type");
        String code = getArguments().getString("code");

        if (type == 1) {
            balncesViewModel.getLoanPaymentByLoanCode(code).observe(this, loanPayments -> {
                if (loanPayments != null) {

                    this.loanPayments = loanPayments;
                    initList();

                } else {
                }
            });
        } else {
            balncesViewModel.getOrderPaymentByOrderCode(code).observe(this, orderPayments -> {
                if (orderPayments != null) {

                    this.orderPayments = orderPayments;
                    initList();

                } else {
                }
            });
        }

    }

    private void filter(List<FarmerOrdersTable> farmerOrdersTables) {


        setUplist(farmerOrdersTables);
    }

    private void setUplist(List<FarmerOrdersTable> farmerOrdersTables) {

    }
}
