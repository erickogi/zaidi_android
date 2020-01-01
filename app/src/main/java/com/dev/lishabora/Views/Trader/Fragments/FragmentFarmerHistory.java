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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.dev.lishabora.Adapters.FarmerHistoryCollAdapter;
import com.dev.lishabora.Adapters.PayoutesAdapter;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.FarmerHistoryByDateModel;
import com.dev.lishabora.Models.MonthsDates;
import com.dev.lishabora.Models.PayoutFarmersCollectionModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.MaterialIntro;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.ViewModels.Trader.BalncesViewModel;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishabora.Views.Trader.Activities.PayCard;
import com.dev.lishabora.Views.Trader.FarmerToolBarUI;
import com.dev.lishaboramobile.R;
import com.getkeepsafe.taptargetview.TapTarget;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

import static com.dev.lishabora.Views.CommonFuncs.getFarmersCollectionModel;

public class FragmentFarmerHistory extends Fragment implements DatePickerDialog.OnDateSetListener {
    private FarmerToolBarUI toolbar;

    private View view;
    private FamerModel famerModel;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private PayoutsVewModel payoutsVewModel;
    private BalncesViewModel balncesViewModel;

    private boolean isTO;
    private View.OnClickListener fromClicked = view -> {
        isTO = false;
        selectDate();
    };

    private View.OnClickListener toClicked = view -> {
        isTO = true;
        selectDate();

    };
    private MaterialSpinner.OnItemSelectedListener spinnerCatListener = (view, position, id, item) -> {
        toolbar.show();
        reload();

    };
    private MaterialSpinner.OnItemSelectedListener spinnerMonthListener = (view, position, id, item) -> {
        toolbar.show();
        reload();

    };
    private MaterialSpinner.OnItemSelectedListener spinnerTypeListener = (view, position, id, item) -> {
        toolbar.show();
        reload();
    };



    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);


    }
    private ImageButton helpView;
    private ImageButton editView;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem mHelp = menu.findItem(R.id.action_help);
        MenuItem mEdit = menu.findItem(R.id.action_edit);
        mEdit.setVisible(false);

        helpView = (ImageButton) mHelp.getActionView();
        helpView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_help));
        helpView.setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
        if (!new PrefrenceManager(getContext()).isFarmersHistoryFragmentIntroShown()) {
            showIntro();
        }
        helpView.setOnClickListener(v -> showIntro());

    }

    void showIntro() {
        List<TapTarget> targets = new ArrayList<>();
        if(toolbar.findViewById(R.id.lspinner_type).getVisibility()==View.VISIBLE) {
            targets.add(TapTarget.forView(toolbar.findViewById(R.id.lspinner_type), "Click here to filter by type (Milk,Loans,AAll)", getContext().getResources().getString(R.string.dismiss_intro)).cancelable(false).id(17).transparentTarget(true));
        }
        if(toolbar.findViewById(R.id.lspinner_cat).getVisibility()==View.VISIBLE) {
            targets.add(TapTarget.forView(toolbar.findViewById(R.id.lspinner_cat), "Click here to filter Month, Year or Payout", getContext().getResources().getString(R.string.dismiss_intro)).cancelable(false).id(18).transparentTarget(true));
        }
        if(toolbar.findViewById(R.id.date_range).getVisibility()==View.VISIBLE) {
            targets.add(TapTarget.forView(toolbar.findViewById(R.id.date_range), "Click here to select a date range", getContext().getResources().getString(R.string.dismiss_intro)).cancelable(false).id(18).transparentTarget(true));
        }

         targets.add(TapTarget.forView(helpView, "Click here to see this introduction again", getContext().getResources().getString(R.string.dismiss_intro)).cancelable(false).id(19).transparentTarget(true));

        MaterialIntro.Companion.showIntroSequence(getActivity(), targets);
        new PrefrenceManager(getContext()).setFarmersHistoryFragmentIntroShown(true);

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);
        balncesViewModel = ViewModelProviders.of(this).get(BalncesViewModel.class);



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
        if (getArguments() != null) {
            famerModel = (FamerModel) getArguments().getSerializable("farmer");
        }
        toolbar = view.findViewById(R.id.toolbar);

        toolbar.setOnFromClickListener(fromClicked);
        toolbar.setOnToClickListener(toClicked);
        toolbar.setOnCatSelectListener(spinnerCatListener);
        toolbar.setOnTypeSelectListener(spinnerTypeListener);
        toolbar.setOnMonthSelectListener(spinnerMonthListener);


        toolbar.show();
        reload();










    }

    @Override
    public void onStart() {
        super.onStart();

    }




    private List<Payouts> listpayouts;


    private void fetch() {
        payoutsVewModel.getPayoutsByCycleCode(famerModel.getCyclecode()).observe(this, this::setData);
    }


    private void setData(List<com.dev.lishabora.Models.Payouts> payouts) {
        if (payouts != null) {

            PayoutesAdapter payoutesAdapter = initPayoutList();

            LinkedList<Payouts> payouts1 = new LinkedList<>();
            for (int a = 0; a < payouts.size(); a++) {
                List<Collection> c = payoutsVewModel.getCollectionByDateByPayoutByFarmerListOne(payouts.get(a).getCode(), famerModel.getCode());
                payouts1.add(CommonFuncs.createPayoutsByCollection(c, payouts.get(a), payoutsVewModel, balncesViewModel, famerModel.getCode()));


            }
            listpayouts = payouts1;
            payoutesAdapter.refresh(payouts1);


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
            public void onMenuItem(int position, int menuItem) {

            }

            @Override
            public void onSwipe(int adapterPosition, int direction) {


            }

            @Override
            public void onClickListener(int position) {


                Payouts p = listpayouts.get(position);
                PayoutFarmersCollectionModel payoutFarmersCollectionModel = getFarmersCollectionModel(famerModel, p);


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


    private void initYearly() {

        payoutsVewModel.getCollectionByFarmer(famerModel.getCode()).observe(this, collections -> {
            if (collections != null && collections.size() > 0) {
                initList(CommonFuncs.createHistoryList(collections, null, true));

            } else {
                initList(null);

            }
        });


    }

    private void initMonth(MonthsDates monthsDates) {

        payoutsVewModel.getCollectionByFarmer(famerModel.getCode()).observe(this, collections -> {
            if (collections != null && collections.size() > 0) {
                initList(CommonFuncs.createHistoryList(collections, monthsDates, false));

            } else {
                initList(null);

            }
        });


    }


    public void initList(List<FarmerHistoryByDateModel> models) {

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        if (models == null) {
            models = new LinkedList<>();
        }
        FarmerHistoryCollAdapter listAdapter = new FarmerHistoryCollAdapter(getContext(), models, new OnclickRecyclerListener() {
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
        }, toolbar.getWhichType());
        recyclerView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();


    }

    private void selectDate() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(FragmentFarmerHistory.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));
        dpd.setThemeDark(false);
        dpd.vibrate(true);
        dpd.dismissOnPause(true);
        dpd.showYearPickerFirst(true);
        dpd.setMaxDate(now);

        if (isTO) {
            dpd.setTitle("To ? ");
        } else {
            dpd.setTitle("From ? ");

        }
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);

        dpd.show(Objects.requireNonNull(getActivity()).getFragmentManager(), "DatePicker");
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

        if (isTO) {
            toolbar.setTo(date);
        } else {
            toolbar.setFrom(date);
        }

        reload();
    }

    private void reload() {
        int which = toolbar.getWhichCat();
        if (which == FarmerToolBarUI.CAT_PAYOUTS) {
            //initByPayouts();
            fetch();
        } else if (which == FarmerToolBarUI.CAT_YEAR) {
            initYearly();
        } else if (which == FarmerToolBarUI.CAT_MONTHS) {

            initMonth(toolbar.getWhichMonth());

        } else if (which == FarmerToolBarUI.CAT_DAYS) {
            initDays();
        }


    }

    private void initDays() {
        payoutsVewModel.getCollectionsBetweenDates(DateTimeUtils.Companion.getLongDate(toolbar.getDateFrom()), DateTimeUtils.Companion.getLongDate(toolbar.getDateTo()), famerModel.getCode()).observe(FragmentFarmerHistory.this, new Observer<List<Collection>>() {
            @Override
            public void onChanged(@Nullable List<Collection> collections) {

                if (collections != null && collections.size() > 0) {
                    initList(CommonFuncs.createHistoryList(collections, null, false));

                } else {
                    initList(null);

                }
            }
        });
    }
}
