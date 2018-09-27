package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishabora.Adapters.FarmersAdapter;
import com.dev.lishabora.AppConstants;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.Cycles;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.ResponseModel;
import com.dev.lishabora.Models.RoutesModel;
import com.dev.lishabora.Models.Trader.FarmerBalance;
import com.dev.lishabora.Models.UnitsModel;
import com.dev.lishabora.Utils.CollectListener;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.Draggable.helper.OnStartDragListener;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.ViewModels.Trader.BalncesViewModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishabora.Views.Trader.Activities.CreateFarmerActivity;
import com.dev.lishabora.Views.Trader.Activities.FarmerProfile;
import com.dev.lishabora.Views.Trader.Activities.FirstTimeLaunch;
import com.dev.lishabora.Views.Trader.Activities.GiveOrder;
import com.dev.lishabora.Views.Trader.FarmerConst;
import com.dev.lishabora.Views.Trader.OrderConstants;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import github.nisrulz.recyclerviewhelper.RVHItemClickListener;
import github.nisrulz.recyclerviewhelper.RVHItemDividerDecoration;
import github.nisrulz.recyclerviewhelper.RVHItemTouchHelperCallback;
import timber.log.Timber;

import static com.dev.lishabora.Models.FamerModel.farmerDateComparator;
import static com.dev.lishabora.Models.FamerModel.farmerNameComparator;
import static com.dev.lishabora.Models.FamerModel.farmerPosComparator;

public class FragementFarmersList extends Fragment implements OnStartDragListener, CollectListener {
    FarmersAdapter listAdapter;


    private final int CHRONOLOGICAL = 1, ALPHABETICAL = 2, AUTOMATICALLY = 0, MANUALLY = 3;
    public static Fragment fragment = null;

    Gson gson = new Gson();
    boolean isArchived = false;
    boolean isDummy = false;
    private View view;
    private Context context;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout empty_layout;
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
    private BalncesViewModel balncesViewModel;
    private PrefrenceManager prefrenceManager;
    private List<RoutesModel> routesModels;
    private ItemTouchHelper mItemTouchHelper;
    private Button btnDrag;
    private boolean isDraggable = true;
    private int SORTTYPE = 0;
    private CollectMilk collectMilk;








    public void initList() {
        recyclerView = view.findViewById(R.id.recyclerView);
        //famerModelsLst=FarmerConst.getFamerModels();
        listAdapter = new FarmersAdapter(getActivity(), FarmerConst.getSearchFamerModels(), new OnclickRecyclerListener() {
            @Override
            public void onSwipe(int adapterPosition, int direction) {

                FamerModel fm = FarmerConst.getSearchFamerModels().get(adapterPosition);
                fm.setLastCollectionTime(DateTimeUtils.Companion.getNow());
                mViewModel.updateFarmer(fm, false, false);


            }

            @Override
            public void onClickListener(int position) {

                try {
                    if (FarmerConst.getSearchFamerModels().get(position).getDeleted() == 0 && FarmerConst.getSearchFamerModels().get(position).getArchived() == 0) {
                        collectMilk.collectMilk(FarmerConst.getSearchFamerModels().get(position));
                    }
                } catch (Exception nm) {
                    nm.printStackTrace();
                }


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


        ItemTouchHelper.Callback callback = new RVHItemTouchHelperCallback(listAdapter, false, true,
                true);
        ItemTouchHelper helper = new ItemTouchHelper(callback);


        helper.attachToRecyclerView(recyclerView);

        // Set the divider in the recyclerview
        recyclerView.addItemDecoration(new RVHItemDividerDecoration(Objects.requireNonNull(getContext()), LinearLayoutManager.VERTICAL));


        recyclerView.addOnItemTouchListener(new RVHItemClickListener(context, (view, position) -> {

            try {
                if (FarmerConst.getSearchFamerModels().get(position).getDeleted() == 0 && FarmerConst.getSearchFamerModels().get(position).getArchived() == 0) {

                    collectMilk.collectMilk(FarmerConst.getSearchFamerModels().get(position));
                }
            } catch (Exception nm) {
                nm.printStackTrace();
            }

        }));

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
                filterFarmers();
                setDraggale(false);
                return true;
            case R.id.action_manually:
                //SORTTYPE = MANUALLY;
                //startDrag();
                fragment = new FragementFarmersDragList();
                setUpView();

                // Do Fragment menu item stuff here
                return true;
            case R.id.action_chronologically:
                SORTTYPE = CHRONOLOGICAL;
                filterFarmers();
                setDraggale(false);
                // Do Fragment menu item stuff here
                return true;
            case R.id.action_alphabetically:
                SORTTYPE = ALPHABETICAL;
                filterFarmers();
                setDraggale(false);
                // Do Fragment menu item stuff here
                return true;
            case R.id.action_smanually:
                SORTTYPE = MANUALLY;
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
            //listAdapter.setDraggale(false);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);
        balncesViewModel = ViewModelProviders.of(this).get(BalncesViewModel.class);

        prefrenceManager = new PrefrenceManager(getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        empty_layout = view.findViewById(R.id.empty_layout);
        emptyTxt = view.findViewById(R.id.empty_text);
        avi = view.findViewById(R.id.avi);
        txt_network_state = view.findViewById(R.id.txt_network_state);
        btnDrag = view.findViewById(R.id.draggaing);
        btnDrag.setVisibility(View.GONE);
        btnDrag.setOnClickListener(view13 -> {
            isDraggable = false;
            btnDrag.setVisibility(View.GONE);
            setDraggale(false);
            updateItems();
        });
        setDraggale(false);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(viehw -> {

            if (prefrenceManager.isTraderFirstTime()) {
                alertDialogFirstTime();
            } else {
                if (isSetUp()) {
                    FragementFarmersList.this.createFarmers();
                } else {
                    initDataOffline(true, true, true);
                    MyToast.toast("Routes, Units and Cycles Not set Up", getContext(), R.drawable.ic_launcher, Toast.LENGTH_LONG);
                }
            }
        });


        spinner1 = view.findViewById(R.id.spinner1);
        lspinner1 = view.findViewById(R.id.lspinner1);
        spinner2 = view.findViewById(R.id.spinner2);
        lspinner2 = view.findViewById(R.id.lspinner2);
        spinner2.setVisibility(View.VISIBLE);
        lspinner2.setVisibility(View.VISIBLE);


        spinner1.setItems("Active", "Archived", "Deleted", "Dummy", "All(Status)");
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


        collectMilk = new CollectMilk(getContext(), mViewModel, balncesViewModel, this, true);

    }


    private void alertDialogFirstTime() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setTitle("Initial Account Setup ");
        alertDialogBuilderUserInput.setCancelable(true);
        alertDialogBuilderUserInput.setMessage("It seems to be the first time you are logging into the app.. \nTo continue using the app fully, you will need to setup you basic details ,routes , cycles and products information .\n Press SET UP to do so  thank you.");


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Set Up", (dialogBox, id) -> {
                    // ToDo get user input here
                    startActivity(new Intent(getActivity(), FirstTimeLaunch.class));


                })

        ;

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        alertDialogAndroid.show();


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

        avi.smoothToShow();
        avi.setVisibility(View.VISIBLE);

        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);


        }

        mViewModel.getFarmerByStatusRoute(staus, route).observe(FragementFarmersList.this, famerModels -> {
            avi.smoothToHide();
            prefrenceManager.setIsFarmerListFirst(false);

            if (famerModels != null) {
                for (int a = 0; a < famerModels.size(); a++) {
                    FarmerBalance farmerBalance = balncesViewModel.getByFarmerCodeOne(famerModels.get(a).getCode());
                    if (farmerBalance == null) {
                        farmerBalance = new FarmerBalance();
                    }
                    famerModels.get(a).setTotalbalance(farmerBalance.getBalanceToPay());

                }
            }
            update(famerModels);
        });
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


        emptyState(listAdapter.getItemCount() > 0, "We couldn't find any farmers records", empty_layout, null, emptyTxt);

    }

    private void emptyState(boolean listHasData, String text, LinearLayout empty_layout, ImageView empty_image, TextView emptyTxt) {
        if (listHasData) {
            //  empty_layout.setVisibility(View.GONE);
        } else {
            //  empty_layout.setVisibility(View.VISIBLE);
            if (text != null) {
                //     emptyTxt.setText(text);
            }
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


        switch (SORTTYPE) {
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









        }



    private void filterFarmersAlpahbetically() {


        Collections.sort(FarmerConst.getSearchFamerModels(), farmerNameComparator);
        listAdapter.notifyDataSetChanged();


    }

    private void filterFarmersChronologically() {


        Collections.sort(FarmerConst.getSearchFamerModels(), farmerDateComparator);
        listAdapter.notifyDataSetChanged();


    }

    private void filterFarmersManually() {


        Collections.sort(FarmerConst.getSearchFamerModels(), farmerPosComparator);
        listAdapter.notifyDataSetChanged();


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



    }

    @Override
    public void onResume() {
        super.onResume();


        fetchFarmers(0, "");
        filterFarmers();

    }

    @Override
    public void onPause() {
        super.onPause();
        filterText = "";

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
                default:
            }
            return false;
        });
        popupMenu.show();
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

        mViewModel.getUnits(isUnitFirst).observe(this, unitsModels -> {
            if (unitsModels != null && unitsModels.size() > 0) {
                prefrenceManager.setIsRoutesListFirst(false);
                FragementFarmersList.this.getUnits = unitsModels;


            }
        });
        mViewModel.getCycles(true).observe(this, cycles -> {
            if (cycles != null && cycles.size() > 0) {
                prefrenceManager.setIsCyclesListFirst(false);
                FragementFarmersList.this.getCycles = cycles;


            }
        });
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);


    }


    @Override
    public void createCollection(Collection c) {
        mViewModel.createCollections(c, false).observe(FragementFarmersList.this, responseModel -> {
            if (Objects.requireNonNull(responseModel).getResultCode() == 1) {

                CommonFuncs.addBalance(mViewModel, balncesViewModel, c, responseModel.getPayoutkey(), AppConstants.MILK, null, null);

            } else {
                snack(responseModel.getResultDescription());

            }


        });
    }

    @Override
    public void createCollection(Collection cAm, Collection cPm) {
        mViewModel.createCollections(cAm, false).observe(FragementFarmersList.this, responseModel -> {
            if (Objects.requireNonNull(responseModel).getResultCode() == 1) {

            } else {
                snack(responseModel.getResultDescription());

            }


        });
        mViewModel.createCollections(cPm, false).observe(FragementFarmersList.this, responseModel -> {
            if (Objects.requireNonNull(responseModel).getResultCode() == 1) {

            } else {
                snack(responseModel.getResultDescription());

            }


        });
    }

    @Override
    public void updateCollection(Collection c) {

        mViewModel.updateCollection(c).observe(FragementFarmersList.this, responseModel -> {
            if (Objects.requireNonNull(responseModel).getResultCode() == 1) {
                CommonFuncs.updateBalance(mViewModel, balncesViewModel, c, responseModel.getPayoutkey(), AppConstants.MILK, null, null);

            } else {
                snack(responseModel.getResultDescription());

            }
        });
    }

    @Override
    public void updateCollection(Collection cAm, Collection cPm) {


    }

    @Override
    public void error(String error) {
        snack(error);

    }
}
