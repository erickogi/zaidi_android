package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dev.lishabora.Adapters.PayoutFarmersAdapter;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.PayoutFarmersCollectionModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishabora.Views.Trader.Activities.PayCard;
import com.dev.lishabora.Views.Trader.PayoutConstants;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

import static com.dev.lishabora.Views.CommonFuncs.setPayoutActionStatus;

public class FragmentPayoutFarmersList extends Fragment {
    public TextView status, startDate, cycleName, endDate, milkTotal, loanTotal, orderTotal, balance, approvedCount, unApprovedCount;
    public RelativeLayout background;
    public View statusview;
    private View view;

    private PayoutFarmersAdapter listAdapter;
    private Context context;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout empty_layout;
    private Payouts payouts;
    private PayoutsVewModel payoutsVewModel;
    private List<PayoutFarmersCollectionModel> dayCollectionModels;
    private List<FamerModel> famerModels;
    private List<Collection> collections;
    TextView txtApprovalStatus;
    private MaterialButton btnApprove;

    public void initList() {
        recyclerView = view.findViewById(R.id.recyclerView);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        if (dayCollectionModels == null) {
            dayCollectionModels = new LinkedList<>();
        }
        listAdapter = new PayoutFarmersAdapter(getActivity(), dayCollectionModels, new OnclickRecyclerListener() {
            @Override
            public void onSwipe(int adapterPosition, int direction) {


            }

            @Override
            public void onClickListener(int position) {

                Gson gson = new Gson();
                String element = gson.toJson(dayCollectionModels, new TypeToken<ArrayList<PayoutFarmersCollectionModel>>() {
                }.getType());
                payoutsVewModel.getFarmerByCode(dayCollectionModels.get(position).getFarmercode()).observe(FragmentPayoutFarmersList.this, new Observer<FamerModel>() {
                    @Override
                    public void onChanged(@Nullable FamerModel famerModel) {
                        if (famerModel != null) {
                            Gson g = new Gson();
                            Timber.tag("farmerCilcked").d("clicked " + position);
                            Intent intent = new Intent(getActivity(), PayCard.class);
                            intent.putExtra("data", dayCollectionModels.get(position));
                            intent.putExtra("payout", payouts);
                            intent.putExtra("farmers", element);
                            intent.putExtra("farmer", famerModel);

                            startActivity(intent);
                        } else {
                            Timber.tag("farmerCilcked").d("clicked " + position + "  eRROR Farmern Not found");

                        }

                    }
                });


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
            starterPack();

        });
        setPayoutActionStatus(payouts, getContext(), btnApprove, txtApprovalStatus);





    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);

        if (getArguments() != null) {
            payouts = (Payouts) getArguments().getSerializable("data");
        } else {
            payouts = PayoutConstants.getPayouts();
        }
        if (payouts != null) {
            payoutsVewModel.getPayoutsByPayoutNumber("" + payouts.getPayoutnumber()).observe(this, payouts -> {
                this.payouts = CommonFuncs.createPayout(payouts, payoutsVewModel);

                starterPack();

            });
        }

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
        btnApprove = view.findViewById(R.id.btn_approve);
        txtApprovalStatus = view.findViewById(R.id.txt_approval_status);
        btnApprove.setVisibility(View.GONE);

        LinearLayout linearLayoutAmPm = view.findViewById(R.id.linear_collection_titles);
        linearLayoutAmPm.setVisibility(View.GONE);


    }

    private void setSpinner() {
        try {
            RelativeLayout rspinner = Objects.requireNonNull(getActivity()).findViewById(R.id.linear_spinner);
            rspinner.setVisibility(View.VISIBLE);

            Spinner spinner = Objects.requireNonNull(getActivity()).findViewById(R.id.spinner);
            spinner.setVisibility(View.VISIBLE);
            spinner.setSelection(1);


            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if (i == 0) {
                        Fragment fragment = new FragmentPayoutColloectionsList();
                        ((com.dev.lishabora.Views.Trader.Activities.Payouts) getActivity()).popOutFragments();
                        ((com.dev.lishabora.Views.Trader.Activities.Payouts) getActivity()).setUpView(fragment);


                    } else {


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
        setSpinner();

    }

    public void setCardHeaderData(Payouts model) {
        startDate.setText(model.getStartDate());
        endDate.setText(model.getEndDate());
        cycleName.setText(model.getCyclename());

        milkTotal.setText(String.format("%s %s", model.getMilkTotalLtrs(), getActivity().getString(R.string.ltrs)));
        loanTotal.setText(String.format("%s %s", model.getLoanTotal(), getActivity().getString(R.string.ksh)));
        orderTotal.setText(String.format("%s %s", model.getOrderTotal(), getActivity().getString(R.string.ksh)));


        balance.setText(String.format("%s %s", model.getBalance(), getActivity().getString(R.string.ksh)));
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

    private void loadFarmers() {
        payoutsVewModel.getFarmersByCycle("" + payouts.getCycleCode()).observe(this, famerModels -> {
            if (famerModels != null) {
                FragmentPayoutFarmersList.this.famerModels = famerModels;
                Timber.tag("farmersPayouts").d("Farmers found " + famerModels.size());

                loadCollectionPayouts();

            } else {
                Timber.tag("farmersPayouts").d("Farmers found null ");

            }
        });
    }

    private void loadCollectionPayouts() {

        payoutsVewModel.getCollectionByDateByPayout("" + payouts.getPayoutnumber()).observe(this, collections -> {
            if (collections != null) {
                Timber.tag("farmersPayouts").d("Collections found " + collections.size());

                FragmentPayoutFarmersList.this.collections = collections;
                setUpFarmerCollectionList();
            }
        });


    }

    private void setUpFarmerCollectionList() {
        List<PayoutFarmersCollectionModel> collectionModels = new LinkedList<>();

        for (FamerModel famerModel : famerModels) {


            collectionModels.add(CommonFuncs.getFarmersCollectionModel(famerModel, collections, payouts));


        }

        setUpList(collectionModels);


    }


    private void setUpList(List<PayoutFarmersCollectionModel> dayCollectionModels) {
        this.dayCollectionModels = dayCollectionModels;
        initList();
    }

    @Override
    public void onStart() {
        super.onStart();
        starterPack();
    }

    private void starterPack() {
        initCardHeader();
        initList();
        loadFarmers();
    }
}
