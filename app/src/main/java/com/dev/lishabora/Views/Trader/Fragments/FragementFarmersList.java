package com.dev.lishabora.Views.Trader.Fragments;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.card.MaterialCardView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishabora.Adapters.FarmersAdapter;
import com.dev.lishabora.AppConstants;
import com.dev.lishabora.Application;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.Cycles;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.Notifications;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Models.ResponseModel;
import com.dev.lishabora.Models.RoutesModel;
import com.dev.lishabora.Models.UnitsModel;
import com.dev.lishabora.Models.collectMod;
import com.dev.lishabora.Utils.CollectListener;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.Utils.OnActivityTouchListener;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.Utils.RecyclerTouchListener;
import com.dev.lishabora.ViewModels.Trader.BalncesViewModel;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishabora.Views.Trader.Activities.CreateFarmerActivity;
import com.dev.lishabora.Views.Trader.Activities.FarmerProfile;
import com.dev.lishabora.Views.Trader.Activities.GiveOrder;
import com.dev.lishabora.Views.Trader.FarmerConst;
import com.dev.lishabora.Views.Trader.OrderConstants;
import com.dev.lishabora.Views.Trader.PayoutConstants;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.dev.lishabora.Application.collectMilk;
import static com.dev.lishabora.Application.isTimeAutomatic;
import static com.dev.lishabora.Models.FamerModel.farmerDateComparator;
import static com.dev.lishabora.Models.FamerModel.farmerNameComparator;
import static com.dev.lishabora.Models.FamerModel.farmerPosComparator;

//import android.support.v7.view.ActionMode;

public class FragementFarmersList extends Fragment implements CollectListener, RecyclerTouchListener.RecyclerTouchListenerHelper, RecyclerTouchListener.OnSwipeListener {
    FarmersAdapter listAdapter;
    List<Integer> unclickableRows, unswipeableRows;
    private RecyclerTouchListener onTouchListener;
    private int openOptionsPosition;
    private OnActivityTouchListener touchListener;


    private final int CHRONOLOGICAL = 1, ALPHABETICAL = 2, AUTOMATICALLY = 0, MANUALLY = 3;
    public static Fragment fragment = null;

    Gson gson = new Gson();
    boolean isArchived = false;
    boolean isDummy = false;
    private View view;
    private Context context;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private TextView emptyTxt, txt_network_state;
    private AVLoadingIndicatorView avi;
    private String filterText = "";
    FloatingActionButton fab;
    MaterialSpinner spinner1, spinner2;
    LinearLayout lspinner1, lspinner2;
    List<UnitsModel> getUnits = new LinkedList<>();
    private SearchView searchView;
    List<RoutesModel> getRoutess = new LinkedList<>();
    List<Cycles> getCycles = new LinkedList<>();
    private TraderViewModel mViewModel;
    int selectedInt = 0;
    private BalncesViewModel balncesViewModel;
    private PrefrenceManager prefrenceManager;
    private int SORTTYPE = 0;
    private List<RoutesModel> routesModels;
    private ItemTouchHelper mItemTouchHelper;
    private ActionMode mActionMode;
    private FamerModel selectedFarmer;
    private PayoutsVewModel payoutsVewModel;

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.farmer_list_menu, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                // Intent intent;
                case R.id.call:
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "0" + selectedFarmer.getMobile()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                    return true;

                case R.id.sms:
                    sendSMS("0" + selectedFarmer.getMobile());
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };
    private Date previousdate;

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void archiveFarmer(FamerModel famerModel) {
        String action = "Archive";
        if (famerModel.getArchived() == 1) {
            action = "Un-Archive";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(famerModel.getNames());
        builder.setMessage("Please confirm you want to " + action + " this farmer ?");


        builder.setPositiveButton(action, (dialog, which) -> {
            if (famerModel.getArchived() == 1) {
                famerModel.setStatus("Active");
                famerModel.setArchived(0);
            } else {
                famerModel.setStatus("Archived");
                famerModel.setArchived(1);
            }
            avi.smoothToShow();
            mViewModel.updateFarmer(famerModel, false, true).observe(FragementFarmersList.this, responseModel -> avi.smoothToHide());
            avi.smoothToHide();

            // FragementFarmersList.this.fetchFarmers(FragementFarmersList.this.getSelectedAccountStatus(), FragementFarmersList.this.getSelectedRoute());//update(famerModel);

        });
        builder.setNegativeButton("Back", (dialog, which) -> dialog.cancel());
        builder.show();


    }

    public void initCollect(int position) {

        Application.hasSynced a = Application.hasSyncInPast7Days();
        if (a.isHasSynced()) {
            if (isTimeAutomatic()) {
                try {
                    if (FarmerConst.getSearchFamerModels().get(position).getDeleted() == 0 && FarmerConst.getSearchFamerModels().get(position).getArchived() == 0) {
                        selectedInt = position;
                        selectedFarmer = FarmerConst.getSearchFamerModels().get(position);


                        new DownloadFilesTask().execute(selectedFarmer.getCode());


                    }
                } catch (Exception nm) {
                    nm.printStackTrace();
                }
            } else {
                CommonFuncs.timeIs(getActivity());
            }

        } else {
            CommonFuncs.syncDue(getActivity(), a.getDays());
        }

    }

    public void action(int position, int ca) {
        FamerModel famerModel = FarmerConst.getSearchFamerModels().get(position);
        switch (ca) {
            case 4:


                archiveFarmer(famerModel);
                break;


            case 3:
                Intent intent = new Intent(getActivity(), FarmerProfile.class);
                intent.putExtra("farmer", famerModel);
                startActivity(intent);

                break;


            case 1:

                fragment = new FragmentGiveLoan();
                Bundle args = new Bundle();
                args.putSerializable("farmer", famerModel);
                fragment.setArguments(args);
                popOutFragments();
                setUpView();
                break;

            case 2:

                OrderConstants.setFamerModel(famerModel);
                Intent intent2 = new Intent(getActivity(), GiveOrder.class);
                intent2.putExtra("farmer", famerModel);
                startActivity(intent2);

                break;
            default:
        }
    }


    public void di() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Dev");


        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) -> {
            if (!TextUtils.isEmpty(input.getText())) {
                new PrefrenceManager(getContext()).setDev_folder(input.getText().toString());
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();

    }

    public void update(List<FamerModel> famerModels) {


        if (FarmerConst.getFamerModels() != null && listAdapter != null) {

            FarmerConst.getFamerModels().clear();
            FarmerConst.getFamerModels().addAll(famerModels);
            filterFarmers();


        } else {

            FarmerConst.setFamerModels(new LinkedList<>());

            filterFarmers();
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        context = getContext();


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);


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

    }

    public void initList() {
        unclickableRows = new ArrayList<>();
        unswipeableRows = new ArrayList<>();


        recyclerView = view.findViewById(R.id.recyclerView);
        listAdapter = new FarmersAdapter(getActivity(), FarmerConst.getSearchFamerModels(), new OnclickRecyclerListener() {
            @Override
            public void onMenuItem(int position, int menuItem) {
                try {
                    if (FarmerConst.getSearchFamerModels().get(position).getDeleted() == 1) {

                    } else {
                        try {
                            action(position, menuItem);
                        } catch (Exception nm) {
                            nm.printStackTrace();
                        }
                    }
                } catch (Exception nm) {
                    nm.printStackTrace();
                }

            }

            @Override
            public void onSwipe(int adapterPosition, int direction) {

                FamerModel fm = FarmerConst.getSearchFamerModels().get(adapterPosition);
                fm.setLastCollectionTime(DateTimeUtils.Companion.getNow());
                mViewModel.updateFarmer(fm, false, false);


            }

            @Override
            public void onClickListener(int position) {

                initCollect(position);



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
//                try {
//                    if (FarmerConst.getSearchFamerModels().get(adapterPosition).getDeleted() == 1) {
//
//                    } else {
//                        try {
                //popupMenu(adapterPosition, view, FarmerConst.getSearchFamerModels().get(adapterPosition));
//                        } catch (Exception nm) {
//                            nm.printStackTrace();
//                        }
//                    }
//                } catch (Exception nm) {
//                    nm.printStackTrace();
//                }

                if (mActionMode != null) {
                    // return false;
                }
                selectedFarmer = FarmerConst.getSearchFamerModels().get(adapterPosition);

                // Start the CAB using the ActionMode.Callback defined above
                mActionMode = getActivity().startActionMode(mActionModeCallback);
                view.setSelected(true);
                // return true;

            }
        }, null);

        onTouchListener = new RecyclerTouchListener(getActivity(), recyclerView);

        onTouchListener

                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        initCollect(position);
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {

                    }
                })
                .setLongClickable(true, position -> {

                })
                .setLeftToRightSwipeable(R.id.rowFG, R.id.rowBG, (viewID, position) -> {
                    switch (viewID) {

                        case R.id.txt_loan:
                            action(position, 1);

                            break;
                        case R.id.txt_order:
                            action(position, 2);
                            break;
                        case R.id.txt_profile:
                            action(position, 3);
                            break;
                        case R.id.txt_delete:
                            action(position, 4);
                            break;
                        default:
                    }


                })
                .setSwipeOptionViews(R.id.txt_loan, R.id.txt_order, R.id.txt_profile, R.id.txt_delete)
                .setSwipeable(R.id.rowFG, R.id.rowBG, (viewID, position) -> {
                    switch (viewID) {

                        case R.id.txt_loan:
                            FragementFarmersList.this.action(position, 1);

                            break;
                        case R.id.txt_order:
                            FragementFarmersList.this.action(position, 2);
                            break;
                        case R.id.txt_profile:
                            FragementFarmersList.this.action(position, 3);
                            break;
                        case R.id.txt_delete:
                            FragementFarmersList.this.action(position, 4);
                            break;
                        default:
                    }


                        }


                );

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });
        emptyState(listAdapter.getItemCount() > 0);



    }

    private void listenOnSyncStatus() {
        mViewModel.getTrader(prefrenceManager.getCode()).observe(this, traderModel -> {

            if (traderModel != null) {
                if (traderModel.getSynchingStatus() == 1) {
                    txt_network_state.setVisibility(View.VISIBLE);
                    txt_network_state.setText("Syncing data ....");
                } else if (traderModel.getSynchingStatus() == 2) {
                    txt_network_state.setVisibility(View.VISIBLE);
                    if (Application.isConnected) {
                        txt_network_state.setText("Sync is experiencing issues");
                    } else {
                        txt_network_state.setText("No internet sync failed");

                    }


                } else {
                    txt_network_state.setVisibility(View.GONE);

                }
            }

        });
    }

    public void addRoutes(String mesg) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        alertDialogBuilderUserInput.setTitle("Routes");
        alertDialogBuilderUserInput.setMessage(mesg);


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Okay", (dialogBox, id) -> {
                    // ToDo get user input here
                    // startActivity(new Intent(SplashActivity.this, SyncWorks.class));

                    fragment = new FragmentRoutes();
                    popOutFragments();
                    setUpView();
                });


        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        alertDialogAndroid.show();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);
        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);
        balncesViewModel = ViewModelProviders.of(this).get(BalncesViewModel.class);
        observePayouts();


        FarmerConst.setFamerModels(new LinkedList<>());
        FarmerConst.setFilteredFamerModels(new LinkedList<>());
        FarmerConst.setSortedFamerModels(new LinkedList<>());
        FarmerConst.setSearchFamerModels(new LinkedList<>());

        try {
            Objects.requireNonNull(getActivity()).setTitle("Farmers");
        } catch (Exception nm) {
            nm.printStackTrace();
        }


        initList();
        populateTraders();
        fetchFarmers(0, "");


        prefrenceManager = new PrefrenceManager(getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        emptyTxt = view.findViewById(R.id.empty_text);
        avi = view.findViewById(R.id.avi);
        txt_network_state = view.findViewById(R.id.txt_network_state);

        spinner1 = view.findViewById(R.id.spinner1);
        lspinner1 = view.findViewById(R.id.lspinner1);
        spinner2 = view.findViewById(R.id.spinner2);
        lspinner2 = view.findViewById(R.id.lspinner2);
        spinner2.setVisibility(View.VISIBLE);
        lspinner2.setVisibility(View.VISIBLE);


        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(viehw -> {

            Application.hasSynced a = Application.hasSyncInPast7Days();
            if (a.isHasSynced()) {

                if (isTimeAutomatic()) {

                    try {
                        if (spinner2.getItems().size() > 0) {
                            FragementFarmersList.this.createFarmers();
                        } else {
                            routes("You have not added any routes");


                        }
                    } catch (Exception nm) {
                        nm.printStackTrace();
                        routes("You have not added any routes");

                    }

                } else {
                    CommonFuncs.timeIs(getActivity());
                }
            } else {
                CommonFuncs.syncDue(getActivity(), a.getDays());
            }
        });


        spinner1.setItems("Active", "Archived");
        getRoutes();

        spinner1.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view1, position, id, item) -> fetchFarmers(getSelectedAccountStatus(), getSelectedRoute()));
        spinner2.setOnItemSelectedListener((view12, position, id, item) -> fetchFarmers(getSelectedAccountStatus(), getSelectedRoute()));


        try {
            Objects.requireNonNull(getActivity()).setTitle("Farmers List");
        } catch (Exception nm) {
            nm.printStackTrace();
        }

        listenOnSyncStatus();


    }

    private void routes(String s) {
        addRoutes(s);
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

                if (filterText.equals("MCU")) {
                    di();
                }

                return true;
            }
        });

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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_farmers_list, container, false);

    }



    private int getSelectedAccountStatus() {
        return spinner1.getSelectedIndex();
    }

    private String getSelectedRoute() {

        if (spinner2.getSelectedIndex() == 0) {
            return "";
        } else return spinner2.getItems().get(spinner2.getSelectedIndex()).toString();
    }

    private void fetchFarmers(int staus, String route) {

        emptyState(true);

        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(FragementFarmersList.this).get(TraderViewModel.class);
        }
        mViewModel.getFarmerByStatusRoute(staus, route).observe(FragementFarmersList.this, famerModels -> update(famerModels));




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
                filterFarmers();
                //setDraggale(false);
                return true;
            case R.id.action_manually:
                SORTTYPE = MANUALLY;
                //startDrag();
                prefrenceManager.setSortType(SORTTYPE);

                fragment = new FragementFarmersDragList();
                setUpView();

                // Do Fragment menu item stuff here
                return true;
            case R.id.action_chronologically:
                SORTTYPE = CHRONOLOGICAL;
                prefrenceManager.setSortType(SORTTYPE);

                filterFarmers();
                // setDraggale(false);
                // Do Fragment menu item stuff here
                return true;
            case R.id.action_alphabetically:
                SORTTYPE = ALPHABETICAL;
                prefrenceManager.setSortType(SORTTYPE);

                filterFarmers();
                //setDraggale(false);
                // Do Fragment menu item stuff here
                return true;
            case R.id.action_smanually:
                SORTTYPE = MANUALLY;
                prefrenceManager.setSortType(SORTTYPE);

                filterFarmers();
                //setDraggale(false);
                // Do Fragment menu item stuff here
                return true;


            default:
                break;
        }

        return false;
    }


    @Override
    public void onStart() {
        super.onStart();


        if (collectMilk == null) {
            collectMilk = new CollectMilk(getContext(), true);

        }
        FarmerListBalanceFuncs.calCBalances(mViewModel, balncesViewModel, new LinkedList<>());


    }


    private void createFarmers() {
        startActivity(new Intent(getActivity(), CreateFarmerActivity.class));
    }

    private void getRoutes() {
        if (mViewModel != null) {
            mViewModel.getRoutes(false).observe(this, routesModels -> {
                prefrenceManager.setIsRoutesListFirst(false);
                if (routesModels != null && routesModels.size() > 0) {
                    FragementFarmersList.this.routesModels = routesModels;
                    String routes[] = new String[routesModels.size() + 1];
                    routes[0] = "All (Routes)";

                    for (int a = 1; a < routesModels.size() + 1; a++) {
                        routes[a] = routesModels.get(a - 1).getRoute();

                    }

                    spinner2.setItems(routes);

                } else {
                }
            });
        }
    }

    private void emptyState(boolean listHasData) {
        LinearLayout empty_layout;
        empty_layout = view.findViewById(R.id.empty_layout);

        if (listHasData) {
            empty_layout.setVisibility(View.GONE);
        } else {
            empty_layout.setVisibility(View.VISIBLE);

        }
    }

    private void populateTraders() {
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        listAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(listAdapter);

        emptyState(listAdapter.getItemCount() > 0);




    }


    private void filterFarmersAlpahbetically() {


        if (FarmerConst.getSearchFamerModels() != null) {
            try {
                Collections.sort(FarmerConst.getSearchFamerModels(), farmerNameComparator);
            } catch (Exception nm) {
                nm.printStackTrace();
            }
        }
        listAdapter.notifyDataSetChanged();
        emptyState(listAdapter.getItemCount() > 0);



    }

    private void filterFarmersChronologically() {

        if (FarmerConst.getSearchFamerModels() != null) {
            try {
                Collections.sort(FarmerConst.getSearchFamerModels(), farmerDateComparator);
            } catch (Exception nm) {
                nm.printStackTrace();
            }
        }
        listAdapter.notifyDataSetChanged();
        emptyState(listAdapter.getItemCount() > 0);



    }

    private void filterFarmersManually() {

        if (FarmerConst.getSearchFamerModels() != null) {
            try {
                Collections.sort(FarmerConst.getSearchFamerModels(), farmerPosComparator);
            } catch (Exception nm) {
                nm.printStackTrace();
            }
        }

        listAdapter.notifyDataSetChanged();
        emptyState(listAdapter.getItemCount() > 0);



    }

    @Override
    public void onResume() {
        super.onResume();

        hideKeyboardFrom(getContext(), view);


        recyclerView.addOnItemTouchListener(onTouchListener);

        if (collectMilk == null) {
            collectMilk = new CollectMilk(getContext(), true);

        }

    }

    private void observePayouts() {
        MaterialCardView cardView = view.findViewById(R.id.card_header);
        cardView.setVisibility(View.GONE);
        TextView txtTitle = view.findViewById(R.id.txt_title);
        TextView txtMsg = view.findViewById(R.id.txt_message);
        ImageView imgRemove = view.findViewById(R.id.cancel_icon);

        imgRemove.setOnClickListener(view -> cardView.setVisibility(View.GONE));
        mViewModel.getPayoutsByStatus("0").observe(this, payouts -> {
            if (payouts != null) {
                cardView.setVisibility(View.VISIBLE);

                List<Notifications> notifications = CommonFuncs.getPendingPayouts(payouts);
                if (notifications != null && notifications.size() > 0) {

                    if (notifications.size() > 1) {

                        cardView.setVisibility(View.VISIBLE);
                        txtTitle.setText("Hi . " + prefrenceManager.getTraderModel().getNames());
                        txtMsg.setText("You have " + notifications.size() + " Pending payouts that require approval");
                        cardView.setOnClickListener(view -> {
                            fragment = new FragmentPayouts();
                            popOutFragments();
                            setUpView();
                        });


                    } else {


                        cardView.setVisibility(View.VISIBLE);
                        txtTitle.setText("Hi . " + prefrenceManager.getTraderModel().getNames() + " You have 1 " + notifications.get(0).getTitle());
                        txtMsg.setText(notifications.get(0).getMessage());
                        cardView.setOnClickListener(view -> {
                            mViewModel.getPayoutByCode(notifications.get(0).getPayoutCode()).observe(FragementFarmersList.this, new Observer<Payouts>() {
                                @Override
                                public void onChanged(@Nullable Payouts payouts) {
                                    if (payouts != null) {
                                        List<Collection> c = payoutsVewModel.getCollectionByDateByPayoutListOne(payouts.getCode());
                                        Payouts p = CommonFuncs.createPayouts(c, payouts, payoutsVewModel, balncesViewModel);


                                        Intent intent = new Intent(getActivity(), com.dev.lishabora.Views.Trader.Activities.Payouts.class);
                                        intent.putExtra("data", p);
                                        PayoutConstants.setPayouts(p);
                                        startActivity(intent);

                                    }
                                }
                            });

                        });


                    }
                } else {
                    cardView.setVisibility(View.GONE);
                }

            } else {
                cardView.setVisibility(View.GONE);
            }


        });

    }


    @Override
    public void onPause() {
        super.onPause();
        filterText = "";
        recyclerView.removeOnItemTouchListener(onTouchListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        filterText = "";

    }

    private void filterFarmers() {



            if (FarmerConst.getSearchFamerModels() == null) {

                FarmerConst.setSearchFamerModels(new LinkedList<>());
            }


            FarmerConst.getSearchFamerModels().clear();
            if (FarmerConst.getFamerModels() != null && FarmerConst.getFamerModels().size() > 0) {

                try {
                    for (FamerModel famerModel : FarmerConst.getFamerModels()) {
                        if (famerModel.getCode().toLowerCase().contains(filterText) ||
                                famerModel.getMobile().toLowerCase().contains(filterText) ||
                                famerModel.getNames().toLowerCase().contains(filterText)) {
                            FarmerConst.getSearchFamerModels().add(famerModel);


                        }

                    }
                } catch (Exception nm) {
                    nm.printStackTrace();
                    FarmerConst.getSearchFamerModels().addAll(FarmerConst.getFamerModels());

                }
            }


                int sort = prefrenceManager.getSortType();


                switch (sort) {
                    case AUTOMATICALLY:

                        listAdapter.notifyDataSetChanged();
                        emptyState(listAdapter.getItemCount() > 0);


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
                        emptyState(listAdapter.getItemCount() > 0);

                }






    }




    private void snack(String msg) {
        if (view != null) {
            Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).setAction("Action", null);//.show();

            AlertDialog.Builder d = new AlertDialog.Builder(getContext());
            d.setMessage(msg);
            d.setCancelable(true);
            d.setPositiveButton("Okay", (dialogInterface, i) -> dialogInterface.dismiss());
            d.show();

        }
    }

    private void sendSMS(String phone) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getContext()); // Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "text");

            if (defaultSmsPackageName != null)
            {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            startActivity(sendIntent);

        } else
        {
            Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", phone);
            smsIntent.putExtra("sms_body", "message");
            startActivity(smsIntent);
        }
    }

    @Override
    public void createCollection(Collection c, FamerModel famerModel, Double aDouble, Double milk) {


        FamerModel m = famerModel;
        Double balance;
        Double milkBalance;
        try {
            balance = Double.valueOf(famerModel.getTotalbalance());
            milkBalance = Double.valueOf(famerModel.getMilkbalance());
            String bal = String.valueOf(balance + aDouble);
            String balMilk = String.valueOf(milkBalance + milk);
            m.setTotalbalance(bal);
            m.setMilkbalance(balMilk);


        } catch (Exception nm) {

            nm.printStackTrace();
        }
        mViewModel.updateFarmer(m, false, true);


        c.setGpsPoint(prefrenceManager.getLastCordiantes());
        new Collect().execute(new CommonFuncs.createCollection(c, famerModel, null));


    }

    @Override
    public void error(String error) {
        snack(error);

    }

    @Override
    public void setOnActivityTouchListener(OnActivityTouchListener listener) {
        this.touchListener = listener;

    }

    @Override
    public void updateCollection(Collection c, FamerModel famerModel, Double aDouble, Double aDoubleMilk) {

        FamerModel m = famerModel;
        Double balance;
        Double milkBalance;
        try {
            balance = Double.valueOf(famerModel.getTotalbalance());
            milkBalance = Double.valueOf(famerModel.getMilkbalance());


            String bal = String.valueOf(balance + aDouble);


            String balMilk = String.valueOf(milkBalance + aDoubleMilk);



            m.setTotalbalance(bal);
            m.setMilkbalance(balMilk);


        } catch (Exception nm) {

            nm.printStackTrace();
        }
        mViewModel.updateFarmer(m, false, true);



        c.setGpsPoint(prefrenceManager.getLastCordiantes());
        ResponseModel responseModel = mViewModel.updateCollection(c);//.observe(FragementFarmersList.this, responseModel -> {
        FarmerListBalanceFuncs.calCBalances(mViewModel, balncesViewModel, m);


    }

    private void log(String msg) {

        PeriodFormatter mPeriodFormat = new PeriodFormatterBuilder().appendYears()
                .appendMinutes().appendSuffix(" Mins")
                .appendSeconds().appendSuffix(" Secs")
                .appendMillis().appendSuffix("Mil")
                .toFormatter();
        if (previousdate == null) {
            previousdate = DateTimeUtils.Companion.getDateNow();
        }

        Period length = DateTimeUtils.Companion.calcDiff(previousdate, new Date());

        previousdate = new Date();

    }

    @Override
    public void onSwipeOptionsClosed() {

    }

    @Override
    public void onSwipeOptionsOpened() {

    }

    private class Balance extends AsyncTask<CommonFuncs.asyn, Integer, FamerModel> {
        protected FamerModel doInBackground(CommonFuncs.asyn... data) {


            FamerModel fm = CommonFuncs.updateBalance(data[0].getFamerModel(), mViewModel, balncesViewModel, data[0].getCollection(), data[0].getCollection().getPayoutCode(), AppConstants.MILK, null, null);

            FarmerListBalanceFuncs.calCBalances(mViewModel, balncesViewModel, fm);

            if (!data[0].getFamerModel().getTotalbalance().equals(fm.getTotalbalance())) {
                return fm;
            }

            return null;


        }


        protected void onPostExecute(FamerModel msg) {


            if (msg != null) {
                mViewModel.updateFarmer(msg, false, true);
            }

        }
    }

    public boolean canCollectBasedOnPayout(String cycleCode) {
        Payouts p = payoutsVewModel.getLastPayout(cycleCode);
        if (p != null) {
            String endDate = p.getEndDate();
            String startDate = p.getStartDate();


            Interval interval = new Interval(DateTimeUtils.Companion.conver2Date(startDate), DateTimeUtils.Companion.conver2Date(endDate));

            if (interval.containsNow() || DateTimeUtils.Companion.isTodayN(Objects.requireNonNull(DateTimeUtils.Companion.conver2Date(endDate)))) {
                return p.getStatus() != 1;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    private class Collect extends AsyncTask<CommonFuncs.createCollection, Integer, CommonFuncs.createCollection> {
        protected CommonFuncs.createCollection doInBackground(CommonFuncs.createCollection... data) {


            ResponseModel responseModel = mViewModel.createCollectionsU(data[0].getCollection());//.observe(FragementFarmersList.this, responseModel -> {

            // FamerModel f = data[0].getFamerModel();

            if (!responseModel.getPayoutCode().equals(data[0].getFamerModel().getCurrentPayoutCode())) {
                data[0].getFamerModel().setCurrentPayoutCode(responseModel.getPayoutCode());
            }



            return new CommonFuncs.createCollection(data[0].getCollection(), data[0].getFamerModel(), responseModel);


        }


        protected void onPostExecute(CommonFuncs.createCollection c) {


            if (Objects.requireNonNull(c.getResponseModel()).getResultCode() == 1) {
                c.getFamerModel().setCurrentPayoutCode(c.getResponseModel().getPayoutCode());
                new Balance().execute(new CommonFuncs.asyn(c.getFamerModel(), c.getCollection(), false));

            } else {

                snack(c.getResponseModel().getResultDescription());

            }
        }
    }

    private class DownloadFilesTask extends AsyncTask<String, Integer, collectMod> {
        protected collectMod doInBackground(String... data) {

            List<Collection> collections = mViewModel.getCollectionsBetweenDatesOne(DateTimeUtils.Companion.getLongDate(DateTimeUtils.Companion.getDatePrevious(4)), DateTimeUtils.Companion.getLongDate(DateTimeUtils.Companion.getToday()), data[0]);
            collectMod mod = new collectMod();
            if (collections != null) {
                for (Collection c : collections) {
                    if (c.getDayDate().contains(DateTimeUtils.Companion.getDatePrevious(3))) {
                        if (c.getMilkCollectedAm() != null && !c.getMilkCollectedAm().equals("0.0") && !c.getMilkCollectedAm().equals("0")) {
                            //day1am.setText(c.getMilkCollectedAm());


                            mod.setDay1Ams(c.getMilkCollectedAm());
                        }
                        if (c.getMilkCollectedPm() != null && !c.getMilkCollectedPm().equals("0.0") && !c.getMilkCollectedPm().equals("0")) {
                            mod.setDay1pms(c.getMilkCollectedPm());

                        }
                    } else if (c.getDayDate().contains(DateTimeUtils.Companion.getDatePrevious(2))) {
                        if (c.getMilkCollectedAm() != null && !c.getMilkCollectedAm().equals("0.0") && !c.getMilkCollectedAm().equals("0")) {
                            // day2am.setText(c.getMilkCollectedAm());
                            mod.setDay2Ams(c.getMilkCollectedAm());

                        }
                        if (c.getMilkCollectedPm() != null && !c.getMilkCollectedPm().equals("0.0") && !c.getMilkCollectedPm().equals("0")) {
                            // day2pm.setText(c.getMilkCollectedPm());
                            mod.setDay2pms(c.getMilkCollectedPm());

                        }
                    } else if (c.getDayDate().contains(DateTimeUtils.Companion.getDatePrevious(1))) {
                        if (c.getMilkCollectedAm() != null && !c.getMilkCollectedAm().equals("0.0") && !c.getMilkCollectedAm().equals("0")) {
                            // day3am.setText(c.getMilkCollectedAm());
                            mod.setDay3Ams(c.getMilkCollectedAm());


                        }
                        if (c.getMilkCollectedPm() != null && !c.getMilkCollectedPm().equals("0.0") && !c.getMilkCollectedPm().equals("0")) {
                            // day3pm.setText(c.getMilkCollectedPm());
                            mod.setDay3pms(c.getMilkCollectedPm());


                        }
                    } else if (c.getDayDate().contains(DateTimeUtils.Companion.getToday())) {
                        mod.setTodaysCollection(c);
                        if (c.getMilkCollectedAm() != null && !c.getMilkCollectedAm().equals("0.0") && !c.getMilkCollectedAm().equals("0")) {

                            mod.setDay4Ams(c.getMilkCollectedAm());

                        }
                        if (c.getMilkCollectedPm() != null && !c.getMilkCollectedPm().equals("0.0") && !c.getMilkCollectedPm().equals("0")) {
                            mod.setDay4pms(c.getMilkCollectedPm());


                        }
                    }

                }
            }


            return mod;










        }


        protected void onPostExecute(collectMod c) {

            if (canCollectBasedOnPayout(FarmerConst.getSearchFamerModels().get(selectedInt).getCyclecode())) {
                collectMilk.collectMilk(getActivity(), FarmerConst.getSearchFamerModels().get(selectedInt), c, FragementFarmersList.this);
            } else {
                MyToast.toast("Appropriate payout for this farmer has already being approved", getContext(), R.drawable.ic_error_outline_black_24dp, Toast.LENGTH_LONG);
            }


        }
    }


}
