package com.dev.lishabora.Views.Admin.Fragments.Tf;

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
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishabora.Adapters.FarmersAdapter;
import com.dev.lishabora.AppConstants;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.Cycles;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.FarmerBalance;
import com.dev.lishabora.Models.Notifications;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Models.ResponseModel;
import com.dev.lishabora.Models.RoutesModel;
import com.dev.lishabora.Models.UnitsModel;
import com.dev.lishabora.Utils.CollectListener;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.Draggable.helper.OnStartDragListener;
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
import com.dev.lishabora.Views.Trader.Fragments.CollectMilk;
import com.dev.lishabora.Views.Trader.Fragments.FragementFarmersDragList;
import com.dev.lishabora.Views.Trader.Fragments.FragmentGiveLoan;
import com.dev.lishabora.Views.Trader.OrderConstants;
import com.dev.lishabora.Views.Trader.PayoutConstants;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

import static com.dev.lishabora.Application.collectMilk;
import static com.dev.lishabora.Application.isTimeAutomatic;
import static com.dev.lishabora.Models.FamerModel.farmerDateComparator;
import static com.dev.lishabora.Models.FamerModel.farmerNameComparator;
import static com.dev.lishabora.Models.FamerModel.farmerPosComparator;

public class FragementFarmersList extends Fragment implements OnStartDragListener, CollectListener, RecyclerTouchListener.RecyclerTouchListenerHelper {
    public static Fragment fragment = null;
    private final int CHRONOLOGICAL = 1, ALPHABETICAL = 2, AUTOMATICALLY = 0, MANUALLY = 3;
    FarmersAdapter listAdapter;
    List<Integer> unclickableRows, unswipeableRows;
    Gson gson = new Gson();
    boolean isArchived = false;
    boolean isDummy = false;
    FloatingActionButton fab;
    MaterialSpinner spinner1, spinner2;
    LinearLayout lspinner1, lspinner2;
    List<UnitsModel> getUnits = new LinkedList<>();
    List<RoutesModel> getRoutess = new LinkedList<>();
    List<Cycles> getCycles = new LinkedList<>();
    int selectedInt = 0;
    private RecyclerTouchListener onTouchListener;
    private int openOptionsPosition;
    private OnActivityTouchListener touchListener;
    private View view;
    private Context context;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private TextView emptyTxt, txt_network_state;
    private AVLoadingIndicatorView avi;
    private String filterText = "";
    private SearchView searchView;
    private TraderViewModel mViewModel;
    private BalncesViewModel balncesViewModel;
    private PrefrenceManager prefrenceManager;
    private int SORTTYPE = 0;
    private List<RoutesModel> routesModels;
    private ItemTouchHelper mItemTouchHelper;
    private Button btnDrag;
    private boolean isDraggable = true;

    // private CollectMilk collectMilk = null;
    private FamerModel selectedFarmer;
    private PayoutsVewModel payoutsVewModel;

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void archiveFarmer(FamerModel famerModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(famerModel.getNames());
        builder.setMessage("Please confirm you want to archive this farmer ?");


        builder.setPositiveButton("Archive", (dialog, which) -> {
            famerModel.setStatus("Archived");
            famerModel.setArchived(1);
            avi.smoothToShow();
            mViewModel.updateFarmer(famerModel, false, true).observe(FragementFarmersList.this, responseModel -> avi.smoothToHide());
            FragementFarmersList.this.fetchFarmers(FragementFarmersList.this.getSelectedAccountStatus(), FragementFarmersList.this.getSelectedRoute());//update(famerModel);

        });
        builder.setNegativeButton("Back", (dialog, which) -> dialog.cancel());
        builder.show();


    }

    public void deleteFarmer(FamerModel famerModel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(famerModel.getNames());
        builder.setMessage("Please confirm you want to delete this farmer ?");


        builder.setPositiveButton("Delete", (dialog, which) -> {
            famerModel.setStatus("Deleted");
            famerModel.setDeleted(1);
            avi.smoothToShow();
            mViewModel.updateFarmer(famerModel, false, true).observe(FragementFarmersList.this, responseModel -> avi.smoothToHide());
            FragementFarmersList.this.fetchFarmers(FragementFarmersList.this.getSelectedAccountStatus(), FragementFarmersList.this.getSelectedRoute());//update(famerModel);

        });
        builder.setNegativeButton("Back", (dialog, which) -> dialog.cancel());
        builder.show();


    }

    private void timeIs() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        alertDialogBuilderUserInput.setTitle("Time Error");
        alertDialogBuilderUserInput.setMessage("Your time and Date settings is set to manual time settings.. For this app to run you need to enable automatic time settings");


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Okay", (dialogBox, id) -> {
                    // ToDo get user input here
                    // startActivity(new Intent(SplashActivity.this, SyncWorks.class));
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);

                });


        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        alertDialogAndroid.show();


    }


    public void initCollect(int position) {

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
            // new PrefrenceManager(Application.context).setTimeI
            timeIs();
        }


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
                // action();
            }

            @Override
            public void onSwipe(int adapterPosition, int direction) {

                FamerModel fm = FarmerConst.getSearchFamerModels().get(adapterPosition);
                fm.setLastCollectionTime(DateTimeUtils.Companion.getNow());
                mViewModel.updateFarmer(fm, false, false);


            }

            @Override
            public void onClickListener(int position) {

                //initCollect(position);


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
                try {
                    if (FarmerConst.getSearchFamerModels().get(adapterPosition).getDeleted() == 1) {

                    } else {
                        try {
                            popupMenu(adapterPosition, view, FarmerConst.getSearchFamerModels().get(adapterPosition));
                        } catch (Exception nm) {
                            nm.printStackTrace();
                        }
                    }
                } catch (Exception nm) {
                    nm.printStackTrace();
                }

            }
        }, this);
        onTouchListener = new RecyclerTouchListener(getActivity(), recyclerView);
        onTouchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        //  initCollect(position);
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {

                    }
                })
                .setLongClickable(true, position -> {

                });
//                .setSwipeOptionViews(R.id.txt_loan, R.id.txt_order, R.id.txt_profile, R.id.txt_delete)
//                .setSwipeable(R.id.rowFG, R.id.rowBG, (viewID, position) -> {
//                    switch (viewID) {
//
//                        case R.id.txt_loan:
//                            action(position, 1);
//
//                            break;
//                        case R.id.txt_order:
//                            action(position, 2);
//                            break;
//                        case R.id.txt_profile:
//                            action(position, 3);
//                            break;
//                        case R.id.txt_delete:
//                            action(position, 4);
//                            break;
//                        default:
//                    }
//
//
//                });


//        ItemTouchHelper.Callback callback = new RVHItemTouchHelperCallback(listAdapter, false, false,
//                true);
//        ItemTouchHelper helper = new ItemTouchHelper(callback);
//
//
//        helper.attachToRecyclerView(recyclerView);

//
//
//        recyclerView.addOnItemTouchListener(new RVHItemClickListener(context, (view, position) -> {
//           // if ( view.getId() == R.id.card || view.getId() == R.id.background) {
//           FamerModel fm= FarmerConst.getSearchFamerModels().get(position);
//            switch (view.getId()) {
//
//                case R.id.txt_loan:
//                    action(fm, 1);
//
//                    break;
//                case R.id.txt_order:
//                    action(fm, 2);
//                    break;
//                case R.id.txt_profile:
//                    action(fm, 3);
//                    break;
//                case R.id.txt_delete:
//                    action(fm, 4);
//                    break;
//                default:
//
//
//                    try {
//                        if (FarmerConst.getSearchFamerModels().get(position).getDeleted() == 0 && FarmerConst.getSearchFamerModels().get(position).getArchived() == 0) {
//                            //listenOnBalance(FarmerConst.getSearchFamerModels().get(position));
//
//                            List<Collection> collections = mViewModel.getCollectionsBetweenDatesOne(DateTimeUtils.Companion.getLongDate(DateTimeUtils.Companion.getDatePrevious(4)), DateTimeUtils.Companion.getLongDate(DateTimeUtils.Companion.getToday()), FarmerConst.getSearchFamerModels().get(position).getCode());
//
//                            // if (position == 1) {
//                            selectedFarmer = FarmerConst.getSearchFamerModels().get(position);
//
//                            collectMilk.collectMilk(FarmerConst.getSearchFamerModels().get(position), collections);
////                    } else {
////
////
////                        CollectMilkConstants.setFamerModel(FarmerConst.getSearchFamerModels().get(position));
////                        CollectMilkConstants.setCollectionss(collections);
////
////                        // CollectMilkConstants milkConstants=new CollectMilkConstants(FarmerConst.getSearchFamerModels().get(position),collections);
////                        startActivity(new Intent(getActivity(), ActivityCollect.class));
////
////                    }
//
//
//                        }
//                    } catch (Exception nm) {
//                        nm.printStackTrace();
//                    }
//            }
//          //  }
//
//        }));

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
//        mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);
//        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);
//        balncesViewModel = ViewModelProviders.of(this).get(BalncesViewModel.class);
//


        prefrenceManager = new PrefrenceManager(getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        emptyTxt = view.findViewById(R.id.empty_text);
        avi = view.findViewById(R.id.avi);
        txt_network_state = view.findViewById(R.id.txt_network_state);
        btnDrag = view.findViewById(R.id.draggaing);
        btnDrag.setVisibility(View.GONE);
        btnDrag.setOnClickListener(view13 -> {
            isDraggable = false;
            btnDrag.setVisibility(View.GONE);
            setDraggale(false);
            // updateItems();
        });
        setDraggale(false);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(viehw -> {
            if (isTimeAutomatic()) {

                if (mViewModel.getUnits1(false).size() < 1) {
                    getUnit();
                } else {
                    if (isSetUp()) {
                        FragementFarmersList.this.createFarmers();
                    } else {
                        initDataOffline(true, true, true);
                        MyToast.toast("Routes, Units and Cycles Not set Up", getContext(), R.drawable.ic_launcher, Toast.LENGTH_LONG);
                    }
                }
            } else {
                // new PrefrenceManager(Application.context).setTimeI
                timeIs();
            }
        });


        spinner1 = view.findViewById(R.id.spinner1);
        lspinner1 = view.findViewById(R.id.lspinner1);
        spinner2 = view.findViewById(R.id.spinner2);
        lspinner2 = view.findViewById(R.id.lspinner2);
        spinner2.setVisibility(View.VISIBLE);
        lspinner2.setVisibility(View.VISIBLE);


        // spinner1.setItems("Active", "Archived", "Deleted", "Dummy", "All(Status)");
        spinner1.setItems("Active", "Archived");
        getRoutes();

        spinner1.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view1, position, id, item) -> fetchFarmers(getSelectedAccountStatus(), getSelectedRoute()));
        spinner2.setOnItemSelectedListener((view12, position, id, item) -> fetchFarmers(getSelectedAccountStatus(), getSelectedRoute()));


        if (prefrenceManager.isRoutesListFirstTime() || prefrenceManager.isCycleListFirstTime() || prefrenceManager.isUnitListFirstTime()) {
            initDataOffline(false, false, false);
        }


        try {
            Objects.requireNonNull(getActivity()).setTitle("Farmers List");
        } catch (Exception nm) {
            nm.printStackTrace();
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

        Timber.d("update started");

        if (FarmerConst.getFamerModels() != null && listAdapter != null) {
            Timber.d("update started");

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
                setDraggale(false);
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
                setDraggale(false);
                // Do Fragment menu item stuff here
                return true;
            case R.id.action_alphabetically:
                SORTTYPE = ALPHABETICAL;
                prefrenceManager.setSortType(SORTTYPE);

                filterFarmers();
                setDraggale(false);
                // Do Fragment menu item stuff here
                return true;
            case R.id.action_smanually:
                SORTTYPE = MANUALLY;
                prefrenceManager.setSortType(SORTTYPE);

                filterFarmers();
                setDraggale(false);
                // Do Fragment menu item stuff here
                return true;


            default:
                break;
        }

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

                if (filterText.equals("MCU")) {
                    di();
                }

                return true;
            }
        });

    }


    void setDraggale(boolean draggale) {
        if (listAdapter != null) {
            listAdapter.setDraggale(draggale);
        }
        if (draggale) {
            isDraggable = true;
            btnDrag.setVisibility(View.VISIBLE);
        } else {
            isDraggable = false;
            btnDrag.setVisibility(View.GONE);
        }
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

    public void observeBalance() {
        balncesViewModel.fetchAll().observe(this, farmerBalances -> {

            fetchFarmers(FragementFarmersList.this.getSelectedAccountStatus(),
                    FragementFarmersList.this.getSelectedRoute());//update(famerModel);


        });
    }

    private void getUnit() {
        mViewModel.getUnits(true).observe(FragementFarmersList.this, new Observer<List<UnitsModel>>() {
            @Override
            public void onChanged(@Nullable List<UnitsModel> unitsModels) {
                if (unitsModels != null) {
                    //  FragementFarmersList.this.createFarmers();

                }
            }
        });
    }


    private void updateItems() {
        for (int a = 1; a < FarmerConst.getSearchFamerModels().size() + 1; a++) {
            FamerModel f = FarmerConst.getSearchFamerModels().get(a - 1);
            f.setPosition(a);

            mViewModel.updateFarmer(f, false, false);
        }
    }

    private int getSelectedAccountStatus() {
        Timber.d("Status selected" + spinner1.getSelectedIndex());
        return spinner1.getSelectedIndex();
    }

    private String getSelectedRoute() {

        if (spinner2.getSelectedIndex() == 0) {
            return "";
        } else return spinner2.getItems().get(spinner2.getSelectedIndex()).toString();
    }

    private void fetchFarmers(int staus, String route) {


        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);
        }

        mViewModel.getFarmerByStatusRoute(staus, route).observe(FragementFarmersList.this, famerModels -> {

            // avi.smoothToHide();
            prefrenceManager.setIsFarmerListFirst(false);
            if (famerModels != null) {


                for (int a = 0; a < famerModels.size(); a++) {

                    String bal = "0.0";
                    try {
                        FarmerBalance f = balncesViewModel.getByFarmerCodeByPayoutOne(famerModels.get(a).getCode(), famerModels.get(a).getCurrentPayoutCode());

                        if (f != null) {
                            bal = f.getBalanceToPay();
                            if (f.getPayoutStatus() == 1) {
                                bal = "0.0";
                            }
                        } else {

                        }

                    } catch (Exception NM) {
                        NM.printStackTrace();
                    }

                    famerModels.get(a).setTotalbalance(bal);
                    try {
                        famerModels.get(a).setPreviousBalance(CommonFuncs.getPreviousPayoutBalance(famerModels.get(a), balncesViewModel));

                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }
                }
            }

            update(famerModels);
        });


    }

    public void updateSelectedFarmer() {
        selectedFarmer.setLastCollectionTime(DateTimeUtils.Companion.getNow());
        mViewModel.updateFarmer(selectedFarmer, false, false);
    }

    @Override
    public void onStart() {
        super.onStart();
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
        observeBalance();

        observePayouts();

        if (collectMilk == null) {
            collectMilk = new CollectMilk(getContext(), true);

        }

    }

    private boolean isSetUp() {
        if (getCycles == null && getCycles.size() < 1) {
            return false;
        }
        if (getUnits == null && getUnits.size() < 1) {
            return false;
        }
        return getRoutess != null || getRoutess.size() >= 1;
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
                    //filterFarmers();
                } else {
                    // getRoutesOnline();
                }
                //spinner2.setItems(routesModels);
            });
        }
    }


    private void createFarmers() {
        startActivity(new Intent(getActivity(), CreateFarmerActivity.class));
    }


    private void populateTraders() {
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        listAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(listAdapter);

        emptyState(listAdapter.getItemCount() > 0);


        // emptyState(listAdapter.getItemCount() > 0, "We couldn't find any farmers records", empty_layout, null, emptyTxt);

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


    private void filterFarmers() {


        if (FarmerConst.getSearchFamerModels() == null) {

            FarmerConst.setSearchFamerModels(new LinkedList<>());
        }


        FarmerConst.getSearchFamerModels().clear();
        if (FarmerConst.getFamerModels() != null && FarmerConst.getFamerModels().size() > 0) {
            for (FamerModel famerModel : FarmerConst.getFamerModels()) {
                if (famerModel.getCode().toLowerCase().contains(filterText) ||
                        famerModel.getMobile().toLowerCase().contains(filterText) ||
                        famerModel.getNames().toLowerCase().contains(filterText)) {
                    FarmerConst.getSearchFamerModels().add(famerModel);


                }

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


    private void filterFarmersAlpahbetically() {


        Collections.sort(FarmerConst.getSearchFamerModels(), farmerNameComparator);
        listAdapter.notifyDataSetChanged();
        emptyState(listAdapter.getItemCount() > 0);


    }

    private void filterFarmersChronologically() {


        Collections.sort(FarmerConst.getSearchFamerModels(), farmerDateComparator);
        listAdapter.notifyDataSetChanged();
        emptyState(listAdapter.getItemCount() > 0);


    }

    private void filterFarmersManually() {


        Collections.sort(FarmerConst.getSearchFamerModels(), farmerPosComparator);
        listAdapter.notifyDataSetChanged();
        emptyState(listAdapter.getItemCount() > 0);


    }

    @Override
    public void onResume() {
        super.onResume();

        hideKeyboardFrom(getContext(), view);

        fetchFarmers(0, "");

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
                                        Payouts p = CommonFuncs.createPayoutsByCollection(c, payouts, payoutsVewModel, balncesViewModel, null, false, payoutsVewModel.getFarmersByCycleONe(payouts.getCycleCode()));


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

    private void initDataOffline(boolean isRoutesFirst, boolean isUnitFirst, boolean isCycles) {

        if (isRoutesFirst && isUnitFirst && isCycles) {
            avi.smoothToShow();
        }
        TraderViewModel mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);
        mViewModel.getRoutes(false).observe(this, routesModels -> {
            if (routesModels != null && routesModels.size() > 0) {
                prefrenceManager.setIsRoutesListFirst(false);
                this.getRoutess = routesModels;

                avi.smoothToHide();

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

    private void snack(String msg) {
        if (view != null) {
            Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null);//.show();

            AlertDialog.Builder d = new AlertDialog.Builder(getContext());
            d.setMessage(msg);
            d.setCancelable(true);
            d.setPositiveButton("Okay", (dialogInterface, i) -> dialogInterface.dismiss());
            d.show();

        }
    }


    void startAnim() {
        avi.show();

    }

    void stopAnim() {
        avi.hide();

    }


    private void popupMenu(int pos, View view, FamerModel famerModel) {
        PopupMenu popupMenu = new PopupMenu(Objects.requireNonNull(getContext()), view);
        popupMenu.inflate(R.menu.farmer_list_menu);

        isArchived = false;
        isDummy = false;
        if (famerModel.getDeleted() == 1) {

            popupMenu.getMenu().getItem(4).setTitle("Un-Delete");
        }

        if (famerModel.getArchived() == 1) {
            isArchived = true;
            popupMenu.getMenu().getItem(5).setTitle("Un-Archive");
        }


        if (famerModel.getDummy() == 1) {
            isDummy = true;
            popupMenu.getMenu().getItem(6).setTitle("Remove from dummy");


        }


        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.delete:


                    famerModel.setStatus("Deleted");
                    famerModel.setDeleted(1);
                    avi.smoothToShow();
                    mViewModel.updateFarmer(famerModel, false, true).observe(FragementFarmersList.this, responseModel -> avi.smoothToHide());
                    FragementFarmersList.this.fetchFarmers(FragementFarmersList.this.getSelectedAccountStatus(), FragementFarmersList.this.getSelectedRoute());//update(famerModel);


                    break;

                case R.id.archive:

                    if (isArchived) {
                        famerModel.setStatus("Active");
                        famerModel.setArchived(0);
                    } else {
                        famerModel.setStatus("Archived");
                        famerModel.setArchived(1);
                    }
                    avi.smoothToShow();
                    mViewModel.updateFarmer(famerModel, false, true).observe(FragementFarmersList.this, responseModel -> avi.smoothToHide());
                    FragementFarmersList.this.fetchFarmers(FragementFarmersList.this.getSelectedAccountStatus(), FragementFarmersList.this.getSelectedRoute());


                    break;

                case R.id.dummy:

                    if (isDummy) {
                        famerModel.setStatus("Active");
                        famerModel.setDummy(0);
                    } else {
                        famerModel.setStatus("Dummy");
                        famerModel.setDummy(1);
                    }
                    avi.smoothToShow();
                    mViewModel.updateFarmer(famerModel, false, true).observe(FragementFarmersList.this, new Observer<ResponseModel>() {
                        @Override
                        public void onChanged(@Nullable ResponseModel responseModel) {
                            avi.smoothToHide();
                            fetchFarmers(getSelectedAccountStatus(), getSelectedRoute());

                        }
                    });

                    break;
                case R.id.edit:
                    Intent intent = new Intent(getActivity(), FarmerProfile.class);
                    intent.putExtra("farmer", famerModel);
                    startActivity(intent);

                    break;

                case R.id.view:

                    Intent intent1 = new Intent(getActivity(), FarmerProfile.class);
                    intent1.putExtra("farmer", famerModel);
                    startActivity(intent1);
                    // FragementFarmersList.this.editTrader(famerModel, FragementFarmersList.this.getUnits(), FragementFarmersList.this.getCycles(), FragementFarmersList.this.getRoutess(), false);

                    break;

                case R.id.Loans:
                    fragment = new FragmentGiveLoan();
                    Bundle args = new Bundle();
                    args.putSerializable("farmer", famerModel);
                    fragment.setArguments(args);
                    popOutFragments();
                    setUpView();
                    break;

                case R.id.Orders:

                    OrderConstants.setFamerModel(famerModel);
                    Intent intent2 = new Intent(getActivity(), GiveOrder.class);
                    intent2.putExtra("farmer", famerModel);
                    startActivity(intent2);

                    break;

                case R.id.call:
                    intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "0" + famerModel.getMobile()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                case R.id.sms:
                    sendSMS("0" + famerModel.getMobile());
                default:
            }
            return false;
        });
        popupMenu.show();
    }

    private void sendSMS(String phone) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getContext()); // Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "text");

            if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
            // any app that support this intent.
            {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            startActivity(sendIntent);

        } else // For early versions, do what worked for you before.
        {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", phone);
            smsIntent.putExtra("sms_body", "message");
            startActivity(smsIntent);
        }
    }

    @Override
    public void createCollection(Collection c, FamerModel famerModel) {


        mViewModel.createCollections(c).observe(FragementFarmersList.this, responseModel -> {


            //  mViewModel.getCollectionByDateByFarmerByTime(f)

            if (Objects.requireNonNull(responseModel).getResultCode() == 1) {


                boolean hasToSyncFarmer = false;
                if (!famerModel.getCurrentPayoutCode().equals(responseModel.getPayoutCode())) {

                    hasToSyncFarmer = true;
                }

                famerModel.setCurrentPayoutCode(responseModel.getPayoutCode());


                CommonFuncs.updateBalance(famerModel, mViewModel, balncesViewModel, c, responseModel.getPayoutCode(), AppConstants.MILK, null, null);


                mViewModel.updateFarmer(famerModel, false, hasToSyncFarmer);


            } else {

                snack(responseModel.getResultDescription());

            }


        });
    }

    @Override
    public void updateCollection(Collection c, FamerModel famerModel) {

        mViewModel.updateCollection(c).observe(FragementFarmersList.this, responseModel -> {
            if (Objects.requireNonNull(responseModel).getResultCode() == 1) {


                boolean hasToSyncFarmer = false;
                if (!famerModel.getCurrentPayoutCode().equals(responseModel.getPayoutCode())) {

                    hasToSyncFarmer = true;
                }
                famerModel.setCurrentPayoutCode(responseModel.getPayoutCode());

                CommonFuncs.updateBalance(famerModel, mViewModel, balncesViewModel, c, responseModel.getPayoutCode(), AppConstants.MILK, null, null);
                mViewModel.updateFarmer(famerModel, false, hasToSyncFarmer);


            } else {
                snack(responseModel.getResultDescription());

            }
        });
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);


    }

    @Override
    public void error(String error) {
        snack(error);

    }

    @Override
    public void setOnActivityTouchListener(OnActivityTouchListener listener) {
        this.touchListener = listener;

    }

    private class DownloadFilesTask extends AsyncTask<String, Integer, List<Collection>> {
        protected List<Collection> doInBackground(String... data) {

            return mViewModel.getCollectionsBetweenDatesOne(DateTimeUtils.Companion.getLongDate(DateTimeUtils.Companion.getDatePrevious(4)), DateTimeUtils.Companion.getLongDate(DateTimeUtils.Companion.getToday()), data[0]);

        }


        protected void onPostExecute(List<Collection> collections) {
            // collectMilk.collectMilk(getActivity(), FarmerConst.getSearchFamerModels().get(selectedInt), collections, FragementFarmersList.this);

        }
    }
}
