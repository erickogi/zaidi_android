package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import android.widget.LinearLayout;

import com.dev.lishabora.Adapters.PayoutesAdapter;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishaboramobile.R;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class FragmentPayouts extends Fragment {
    double total, milk, loans, orders;
    private PayoutesAdapter listAdapter;
    private View view;
    private Context context;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout empty_layout;
    private List<com.dev.lishabora.Models.Payouts> payouts;
    private PayoutsVewModel payoutsVewModel;

    public void initList() {
        recyclerView = view.findViewById(R.id.recyclerView);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        if (payouts == null) {
            payouts = new LinkedList<>();
        }
        listAdapter = new PayoutesAdapter(getActivity(), payouts, new OnclickRecyclerListener() {
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
        return inflater.inflate(R.layout.fragment_trader_routes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;
        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);


    }

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        initList();
        fetch();
    }

    private void fetch() {
        payoutsVewModel.fetchAll(false).observe(this, new Observer<List<com.dev.lishabora.Models.Payouts>>() {
            @Override
            public void onChanged(@Nullable List<com.dev.lishabora.Models.Payouts> payouts) {
                setData(payouts);
            }
        });
    }

    private void setData(List<com.dev.lishabora.Models.Payouts> payouts) {
        if (payouts != null) {


            for (com.dev.lishabora.Models.Payouts p : payouts) {
                List<Collection> c = payoutsVewModel.getCollectionByDateByPayoutListOne("" + p.getPayoutnumber());
                for (Collection coll : c) {

                    milk = milk + Double.valueOf(coll.getMilkCollected());
                    loans = loans + Double.valueOf(coll.getLoanAmountGivenOutPrice());
                    orders = orders + Double.valueOf(coll.getOrderGivenOutPrice());


                }
                p.setMilkTotal(String.valueOf(milk));
                p.setLoanTotal(String.valueOf(loans));
                p.setOrderTotal(String.valueOf(orders));
                p.setBalance(String.valueOf(milk - (orders + loans)));
                p.setFarmersCount("" + payoutsVewModel.getFarmersCountByCycle("" + p.getCycleCode()));
                p.setApprovedCards("0");
                p.setPendingCards(p.getFarmersCount());
                p.setStatus(0);
                p.setStatusName("Pending");
                milk = 0.0;
                total = 0.0;
                loans = 0.0;
                orders = 0.0;


            }

            if (this.payouts == null) {
                this.payouts = new LinkedList<>();
                this.payouts.addAll(payouts);
                listAdapter.notifyDataSetChanged();

            } else {
                this.payouts.clear();
                this.payouts.addAll(payouts);
                listAdapter.notifyDataSetChanged();
            }
            initList();
        }
    }
}
