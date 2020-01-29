package com.dev.zaidi.Views.Admin.Fragments.Tf;

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

import com.dev.zaidi.Adapters.PayoutFarmersAdapter;
import com.dev.zaidi.Models.Collection;
import com.dev.zaidi.Models.FamerModel;
import com.dev.zaidi.Models.PayoutFarmersCollectionModel;
import com.dev.zaidi.Models.Payouts;
import com.dev.zaidi.R;
import com.dev.zaidi.Utils.DateTimeUtils;
import com.dev.zaidi.Utils.MyToast;
import com.dev.zaidi.Utils.OnclickRecyclerListener;
import com.dev.zaidi.Utils.PrefrenceManager;
import com.dev.zaidi.ViewModels.Trader.BalncesViewModel;
import com.dev.zaidi.ViewModels.Trader.PayoutsVewModel;
import com.dev.zaidi.Views.CommonFuncs;
import com.dev.zaidi.Views.Trader.Activities.PayCard;
import com.dev.zaidi.Views.Trader.PayoutConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;

import static com.dev.zaidi.Models.FamerModel.farmerDateComparator;
import static com.dev.zaidi.Models.FamerModel.farmerNameComparator;
import static com.dev.zaidi.Models.FamerModel.farmerPosComparator;
import static com.dev.zaidi.Views.CommonFuncs.setPayoutActionStatus;

public class FragmentPayoutFarmersList extends Fragment {
    private final int CHRONOLOGICAL = 1, ALPHABETICAL = 2, AUTOMATICALLY = 0, MANUALLY = 3;
    TextView txtApprovalStatus;
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
    private MaterialButton btnApprove;
    private PrefrenceManager prefrenceManager;
    private SearchView searchView;
    private String filterText = "";
    private int SORTTYPE = 0;
    private PeriodFormatter mPeriodFormat;
    private Date previousdate;

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
                MyToast.errorToast("Some farmer cards in this payout are not approved yet", getContext());
            }

        });
        setPayoutActionStatus(payouts, getContext(), btnApprove, txtApprovalStatus);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Not implemented here
                return false;
            case R.id.action_search:
                // Do Fragment menu item stuff here

                return true;
            case R.id.action_automatically:
                // Do Fragment menu item stuff here
                SORTTYPE = AUTOMATICALLY;
                prefrenceManager.setSortType(SORTTYPE);
                setUpFarmerCollectionList();

                return true;

            case R.id.action_chronologically:
                SORTTYPE = CHRONOLOGICAL;
                prefrenceManager.setSortType(SORTTYPE);

                setUpFarmerCollectionList();

                return true;
            case R.id.action_alphabetically:
                SORTTYPE = ALPHABETICAL;
                prefrenceManager.setSortType(SORTTYPE);

                setUpFarmerCollectionList();

                // Do Fragment menu item stuff here
                return true;
            case R.id.action_smanually:
                SORTTYPE = MANUALLY;
                prefrenceManager.setSortType(SORTTYPE);

                setUpFarmerCollectionList();

                // Do Fragment menu item stuff here
                return true;


            default:
                break;
        }

        return false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payout_farmers, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        prefrenceManager = new PrefrenceManager(getContext());

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

        int sort = new PrefrenceManager(getContext()).getSortType();
        switch (sort) {
            case AUTOMATICALLY:
                listAdapter.notifyDataSetChanged();
                break;
            case ALPHABETICAL:
                filterFarmersAlpahbetically();
                break;
            case CHRONOLOGICAL:
                filterFarmersChronologically();
                break;
            case MANUALLY:
                filterFarmersManually();
                break;
            default:
                listAdapter.notifyDataSetChanged();
        }


        for (FamerModel famerModel : famerModels) {


            collectionModels.add(CommonFuncs.getFarmersCollectionModel(famerModel, collections, payouts, balncesViewModel));


        }

        setUpList(collectionModels);


    }

    private void filterFarmersAlpahbetically() {


        Collections.sort(famerModels, farmerNameComparator);
        listAdapter.notifyDataSetChanged();


    }

    private void filterFarmersChronologically() {


        Collections.sort(famerModels, farmerDateComparator);
        listAdapter.notifyDataSetChanged();


    }

    private void filterFarmersManually() {


        Collections.sort(famerModels, farmerPosComparator);
        listAdapter.notifyDataSetChanged();


    }

    private void setUpList(List<PayoutFarmersCollectionModel> dayCollectionModels) {
        this.dayCollectionModels = dayCollectionModels;
        this.dayCollectionModels1 = dayCollectionModels;

        listAdapter.refresh(dayCollectionModels);
        //initList();
    }

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


        MenuItem mAutomatically = menu.findItem(R.id.action_automatically);
        MenuItem mManually = menu.findItem(R.id.action_manually);
        MenuItem mChronologically = menu.findItem(R.id.action_chronologically);
        MenuItem mAlphabetically = menu.findItem(R.id.action_alphabetically);
        MenuItem mRearrangeManually = menu.findItem(R.id.action_smanually);

        mAutomatically.setVisible(false);
        mChronologically.setVisible(true);
        mManually.setVisible(true);
        mAlphabetically.setVisible(true);
        mRearrangeManually.setVisible(true);


        searchView = (SearchView) mSearch.getActionView();
        searchView.setVisibility(View.GONE);

        searchView = (SearchView) mSearch.getActionView();
        searchView.setVisibility(View.GONE);

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
