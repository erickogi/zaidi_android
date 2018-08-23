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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.lishabora.Adapters.MonthlyFarmerCollAdapter;
import com.dev.lishabora.Adapters.PayoutesAdapter;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.FarmerHistoryByDateModel;
import com.dev.lishabora.Models.MonthsDates;
import com.dev.lishabora.Models.PayoutFarmersCollectionModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.Trader.Activities.PayCard;
import com.dev.lishaboramobile.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class FragmentFarmerHistory extends Fragment {
    MaterialSpinner spinner;
    private View view;
    private FamerModel famerModel;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private PayoutsVewModel payoutsVewModel;
    private TraderViewModel traderViewModel;
    double total, milk, loans, orders;

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
            } else if (position == 1) {
                initByPayouts();
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
                        payoutsList.add(createPayoutsByCollection(collections, p));
                        listpayouts = payoutsList;
                        payoutesAdapter.refresh(payoutsList);

                    }

                }
            });

        }


    }

    private Payouts createPayoutsByCollection(List<Collection> collections, Payouts p) {


        for (Collection coll : collections) {

            milk = milk + Double.valueOf(coll.getMilkCollected());
            loans = loans + Double.valueOf(coll.getLoanAmountGivenOutPrice());
            orders = orders + Double.valueOf(coll.getOrderGivenOutPrice());


        }
        int status[] = getApprovedCards(collections, "" + p.getPayoutnumber());
        p.setMilkTotal(String.valueOf(milk));
        p.setLoanTotal(String.valueOf(loans));
        p.setOrderTotal(String.valueOf(orders));
        p.setBalance(String.valueOf(milk - (orders + loans)));
        p.setFarmersCount("" + payoutsVewModel.getFarmersCountByCycle("" + p.getCycleCode()));
        p.setApprovedCards("" + status[1]);
        p.setPendingCards("" + status[2]);

        milk = 0.0;
        total = 0.0;
        loans = 0.0;
        orders = 0.0;


        return p;

    }

    private String getBalance(String milkTotal, String loanTotal, String orderTotal) {
        double balance = 0.0;
        try {
            balance = Double.valueOf(milkTotal);
        } catch (Exception nm) {
            nm.printStackTrace();
        }
        try {
            return String.valueOf(Double.valueOf(milkTotal) - (Double.valueOf(loanTotal) + Double.valueOf(orderTotal)));

        } catch (Exception nm) {
            nm.printStackTrace();
        }
        return String.valueOf(balance);
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
                String bal = getBalance(p.getMilkTotal(), p.getLoanTotal(), p.getOrderTotal());
                PayoutFarmersCollectionModel payoutFarmersCollectionModel = new PayoutFarmersCollectionModel(
                        famerModel.getCode(),
                        famerModel.getNames(),
                        p.getMilkTotal(),
                        p.getLoanTotal(),
                        p.getOrderTotal(),
                        p.getStatus(),
                        p.getStatusName(),
                        bal, p.getPayoutnumber(), famerModel.getCyclecode()
                );


                Log.d("farmerCilcked", "clicked " + position);
                Intent intent = new Intent(getActivity(), PayCard.class);
                intent.putExtra("data", payoutFarmersCollectionModel);
                intent.putExtra("payout", p);
                intent.putExtra("farmers", "null");
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

//                fragment = new FragmentPayoutFarmersList();
//                Bundle args = new Bundle();
//                args.putSerializable("data", payouts.get(adapterPosition));
//                PayoutConstants.setPayouts(payouts.get(adapterPosition));
//                fragment.setArguments(args);
//                // popOutFragments();
//                setUpView();

            }
        }, true);
        recyclerView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();


        return listAdapter;
    }


    private void initData() {

        payoutsVewModel.getCollectionByFarmer(famerModel.getCode()).observe(this, collections -> {
            if (collections != null && collections.size() > 0) {
                createMonthlyList(collections);
            } else {
                initMonthlyList(null);
            }
        });
    }

    private void createMonthlyList(List<Collection> collections) {

        List<MonthsDates> monthsDates = DateTimeUtils.Companion.getMonths(12);
        if (monthsDates != null && monthsDates.size() > 0) {

            LinkedList<FarmerHistoryByDateModel> fmh = new LinkedList<>();

            for (MonthsDates mds : monthsDates) {

                String[] totals = getCollectionsTotals(mds, collections);
                fmh.add(new FarmerHistoryByDateModel(mds, famerModel, totals[0], totals[1], totals[2], totals[3]));

            }
            initMonthlyList(fmh);

        }


    }

    private String[] getCollectionsTotals(MonthsDates mds, List<Collection> collections) {
        String cycleCode = "";
        double milk = 0.0;
        double loan = 0.0;
        double order = 0.0;

        for (Collection collection : collections) {
            if (DateTimeUtils.Companion.isInMonth(collection.getDayDate(), mds.getMonthName())) {
                milk = milk + Double.valueOf(collection.getMilkCollected());
                loan = loan + Double.valueOf(collection.getLoanAmountGivenOutPrice());
                order = order + Double.valueOf(collection.getOrderGivenOutPrice());
            }

        }
        double[] totals = {milk, loan, order};


        return new String[]{String.valueOf(totals[0]), String.valueOf(totals[1]), String.valueOf(totals[2]), cycleCode};
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

    public int[] getApprovedCards(List<Collection> collections, String pcode) {

        int[] statusR = new int[3];
        int farmerStatus = 0;


        List<FamerModel> f = payoutsVewModel.getFarmersByCycleONe(pcode);


        statusR[0] = f.size();


        int approved = 0;

        for (FamerModel famerModel : f) {
            int status = 0;
            int collectionNo = 0;
            for (Collection c : collections) {


                if (c.getFarmerCode().equals(famerModel.getCode())) {


                    collectionNo = collectionNo + 1;

                    try {
                        status += c.getApproved();

                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }
                }


            }

            if (status == collectionNo) {
                approved += 1;
            }


        }
        statusR[1] = approved;
        statusR[2] = statusR[0] - approved;


        return statusR;


    }



}
