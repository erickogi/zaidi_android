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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishabora.Adapters.PayoutFarmersAdapter;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.PayoutFarmersCollectionModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.ViewModels.Trader.BalncesViewModel;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishabora.Views.Trader.Activities.PayCard;
import com.dev.lishabora.Views.Trader.PayoutConstants;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;

import static com.dev.lishabora.Views.CommonFuncs.setPayoutActionStatus;

public class FragmentPayoutFarmersList extends Fragment {
    //    public TextView status, startDate, cycleName, endDate, milkTotal, loanTotal, orderTotal, balance, approvedCount, unApprovedCount;
//    public RelativeLayout background;
//    public View statusview;
    private View view;

    private PayoutFarmersAdapter listAdapter;
    private Context context;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout empty_layout;
    private Payouts payouts;
    private PayoutsVewModel payoutsVewModel;
    private BalncesViewModel balncesViewModel;
    private List<PayoutFarmersCollectionModel> dayCollectionModels;
    private List<PayoutFarmersCollectionModel> dayCollectionModels1;
    private List<FamerModel> famerModels;
    private List<Collection> collections;
    TextView txtApprovalStatus;
    private MaterialButton btnApprove;

    private SearchView searchView;
    private String filterText = "";



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
            public void onMenuItem(int position, int menuItem) {

            }

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


        if (CommonFuncs.canApprovePayout(payouts)) {
            btnApprove.setVisibility(View.VISIBLE);
        } else {
            btnApprove.setVisibility(View.GONE);
        }



        btnApprove.setOnClickListener(view -> {

            if (CommonFuncs.allCollectionsAreApproved(payoutsVewModel, payouts)) {
                payouts.setStatus(1);
                payouts.setStatusName("Approved");
                payoutsVewModel.updatePayout(payouts);
                starterPack();
            } else {
                MyToast.toast("Some farmer cards in this payout are not approved yet", getContext(), R.drawable.ic_error_outline_black_24dp, Toast.LENGTH_LONG);
            }

        });
        setPayoutActionStatus(payouts, getContext(), btnApprove, txtApprovalStatus);





    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payout_collections, container, false);
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);
        balncesViewModel = ViewModelProviders.of(this).get(BalncesViewModel.class);


        if (getArguments() != null) {
            payouts = (Payouts) getArguments().getSerializable("data");
        } else {
            payouts = PayoutConstants.getPayouts();
        }


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        btnApprove = view.findViewById(R.id.btn_approve);
        txtApprovalStatus = view.findViewById(R.id.txt_approval_status);
        btnApprove.setVisibility(View.GONE);
        initList();




    }


    private void loadFarmers() {



        payoutsVewModel.getFarmersByCycle("" + payouts.getCycleCode()).observe(this, famerModels -> {
            if (famerModels != null) {
                log("LOAD FARMERS RESULT  " + famerModels.size());


                FragmentPayoutFarmersList.this.famerModels = famerModels;

                loadCollectionPayouts();

            } else {

            }
        });
    }

    private void loadCollectionPayouts() {

        payoutsVewModel.getCollectionByDateByPayout("" + payouts.getCode()).observe(this, collections -> {
            if (collections != null) {


                FragmentPayoutFarmersList.this.collections = collections;
                setUpFarmerCollectionList();
            }
        });


    }

    private void setUpFarmerCollectionList() {
        log("SETUP FARMER  COLLECTIONS STARTED  ");

        List<PayoutFarmersCollectionModel> collectionModels = new LinkedList<>();

        for (FamerModel famerModel : famerModels) {



            collectionModels.add(CommonFuncs.getFarmersCollectionModel(famerModel, collections, payouts));


        }

        setUpList(collectionModels);


    }

    private void setUpList(List<PayoutFarmersCollectionModel> dayCollectionModels) {
        this.dayCollectionModels = dayCollectionModels;
        this.dayCollectionModels1 = dayCollectionModels;

        listAdapter.refresh(dayCollectionModels);
        //initList();
    }

    private PeriodFormatter mPeriodFormat;
    private Date previousdate;

    private void log(String msg) {

        mPeriodFormat = new PeriodFormatterBuilder().appendYears()
                .appendMinutes().appendSuffix(" Mins")
                .appendSeconds().appendSuffix(" Secs")
                .appendMillis().appendSuffix("Mil")
                .toFormatter();
        if (previousdate == null) {
            previousdate = DateTimeUtils.Companion.getDateNow();
        }

        Period length = DateTimeUtils.Companion.calcDiff(previousdate, new Date());

        previousdate = new Date();
        Timber.tag("debugfarmersclist").d("  Length " + mPeriodFormat.print(length) + "" + msg);

    }

    @Override
    public void onStart() {
        super.onStart();
        starterPack();
    }

    private void starterPack() {
        // initCardHeader();
        initList();
        loadFarmers();
    }

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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filterText = s;
                filterFarmers();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterText = s;
                filterFarmers();

                return true;
            }
        });

    }

    private void filterFarmers() {
        if (dayCollectionModels1 == null) {

            dayCollectionModels1 = new LinkedList<>();
            //  FarmerConst.setSearchFamerModels(new LinkedList<>());
        }


        dayCollectionModels.clear();
        if (dayCollectionModels1 != null && dayCollectionModels1.size() > 0) {
            for (PayoutFarmersCollectionModel famerModel : dayCollectionModels1) {


                if (famerModel.getFarmername().toLowerCase().contains(filterText.toLowerCase()) || famerModel.getFarmercode().toLowerCase().contains(filterText.toLowerCase())) {
                    dayCollectionModels.add(famerModel);
                }

            }
        }
        listAdapter.refresh(dayCollectionModels);
    }

}
