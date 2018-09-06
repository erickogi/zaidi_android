package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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

import com.dev.lishabora.Adapters.MonthlyFarmerCollAdapter;
import com.dev.lishabora.Adapters.PayoutesAdapter;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.FarmerHistoryByDateModel;
import com.dev.lishabora.Models.PayoutFarmersCollectionModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishabora.Views.Trader.Activities.PayCard;
import com.dev.lishaboramobile.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;

import static com.dev.lishabora.Views.CommonFuncs.createPayoutsByCollection;

public class FragmentFarmerHistory extends Fragment {
    MaterialSpinner spinner;
    private View view;
    private FamerModel famerModel;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private PayoutsVewModel payoutsVewModel;
    private TraderViewModel traderViewModel;
    private LinearLayout linearLayoutTitles;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);
        traderViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_farmer_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        famerModel = (FamerModel) getArguments().getSerializable("farmer");
        spinner = view.findViewById(R.id.spinner);
        linearLayoutTitles = view.findViewById(R.id.linear_titles);


    }

    @Override
    public void onStart() {
        super.onStart();
        setUpSpinners();
    }

    private void setUpSpinners() {

        spinner.setItems("By Month", "By Payout Cycle");


        spinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item) -> {

            if (position == 0) {
                initData();
                linearLayoutTitles.setVisibility(View.VISIBLE);
            } else if (position == 1) {
                initByPayouts();
                linearLayoutTitles.setVisibility(View.GONE);
            }
        });
        spinner.setSelectedIndex(0);
        initData();
    }

    private List<Payouts> listpayouts;

    private void initByPayouts() {

        payoutsVewModel.getPayoutsByCycleCode(famerModel.getCyclecode()).observe(this, payouts -> {
            if (payouts != null && payouts.size() > 0) {

                getCollectionsPerPayout(payouts);

            } else {
                initPayoutList();

            }
        });

    }

    private void getCollectionsPerPayout(List<Payouts> payouts) {

        List<Payouts> payoutsList = new LinkedList<>();
        PayoutesAdapter payoutesAdapter = initPayoutList();

        for (Payouts p : payouts) {
            payoutsVewModel.getCollectionByDateByPayoutByFarmer("" + p.getPayoutnumber(), famerModel.getCode()).observe(this, new Observer<List<Collection>>() {
                @Override
                public void onChanged(@Nullable List<Collection> collections) {

                    if (collections != null) {
                        payoutsList.add(createPayoutsByCollection(collections, p, payoutsVewModel));
                        listpayouts = payoutsList;
                        payoutesAdapter.refresh(payoutsList);

                    }

                }
            });

        }


    }




    private PayoutesAdapter initPayoutList() {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        if (listpayouts == null) {
            listpayouts = new LinkedList<>();
        }
        PayoutesAdapter listAdapter = new PayoutesAdapter(getActivity(), listpayouts, new OnclickRecyclerListener() {
            @Override
            public void onSwipe(int adapterPosition, int direction) {


            }

            @Override
            public void onClickListener(int position) {


                Payouts p = listpayouts.get(position);
                PayoutFarmersCollectionModel payoutFarmersCollectionModel = new PayoutFarmersCollectionModel(
                        famerModel.getCode(),
                        famerModel.getNames(),
                        p.getMilkTotal(),
                        p.getLoanTotal(),
                        p.getOrderTotal(),
                        p.getStatus(),
                        p.getStatusName(),
                        p.getBalance(), p.getPayoutnumber(), famerModel.getCyclecode(),
                        p.getMilkTotalKsh(), p.getMilkTotalLtrs()
                );


                Timber.tag("farmerCilcked").d("clicked " + position);
                Intent intent = new Intent(getActivity(), PayCard.class);
                intent.putExtra("data", payoutFarmersCollectionModel);
                intent.putExtra("payout", p);
                intent.putExtra("farmers", "null");
                intent.putExtra("farmer", famerModel);
                startActivity(intent);


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
        }, true);
        recyclerView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();


        return listAdapter;
    }


    private void initData() {

        payoutsVewModel.getCollectionByFarmer(famerModel.getCode()).observe(this, collections -> {
            if (collections != null && collections.size() > 0) {


                initMonthlyList(CommonFuncs.createMonthlyList(collections, famerModel));

            } else {

                initMonthlyList(null);

            }
        });
    }




    public void initMonthlyList(List<FarmerHistoryByDateModel> models) {

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        if (models == null) {
            models = new LinkedList<>();
        }
        MonthlyFarmerCollAdapter listAdapter = new MonthlyFarmerCollAdapter(getContext(), models, new OnclickRecyclerListener() {
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




}
