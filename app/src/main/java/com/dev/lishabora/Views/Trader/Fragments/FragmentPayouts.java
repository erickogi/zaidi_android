package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.dev.lishabora.Adapters.PayoutesAdapter;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.Views.Trader.PayoutConstants;
import com.dev.lishaboramobile.R;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
    private Fragment fragment;

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


                fragment = new FragmentPayoutColloectionsList();
                Bundle args = new Bundle();
                args.putSerializable("data", payouts.get(position));
                PayoutConstants.setPayouts(payouts.get(position));
                fragment.setArguments(args);
                // popOutFragments();
                setUpView();





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
        });
        recyclerView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();


    }

    void setUpView() {
        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment)
                    .addToBackStack(null).commit();
        }

    }

    void popOutFragments() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
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

        try {
            Objects.requireNonNull(getActivity()).setTitle("Payouts List");
        } catch (Exception nm) {
            nm.printStackTrace();
        }

        try {
            Spinner spinner = getActivity().findViewById(R.id.spinner);
            spinner.setVisibility(View.GONE);
        } catch (Exception nm) {
            nm.printStackTrace();
        }

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
        payoutsVewModel.fetchAll(false).observe(this, payouts -> setData(payouts));
    }

    private void setData(List<com.dev.lishabora.Models.Payouts> payouts) {
        if (payouts != null) {


            for (com.dev.lishabora.Models.Payouts p : payouts) {
                List<Collection> c = payoutsVewModel.getCollectionByDateByPayoutListOne("" + p.getPayoutnumber());


                int status[] = getApprovedCards(c, "" + p.getPayoutnumber());

                int st = 0;
                String stText = "";
                for (Collection coll : c) {

                    milk = milk + Double.valueOf(coll.getMilkCollected());
                    loans = loans + Double.valueOf(coll.getLoanAmountGivenOutPrice());
                    orders = orders + Double.valueOf(coll.getOrderGivenOutPrice());


                }
                if (status[1] < status[0]) {
                    st = 0;
                    stText = "Pending";
                } else {
                    st = 1;
                    stText = "Approved";
                }



                p.setMilkTotal(String.valueOf(milk));
                p.setLoanTotal(String.valueOf(loans));
                p.setOrderTotal(String.valueOf(orders));
                p.setBalance(String.valueOf(milk - (orders + loans)));
                p.setFarmersCount("" + payoutsVewModel.getFarmersCountByCycle("" + p.getCycleCode()));


                p.setApprovedCards("" + status[1]);
                p.setPendingCards("" + status[2]);

                p.setStatus(p.getStatus());
                p.setStatusName(p.getStatusName());
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
