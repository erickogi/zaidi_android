package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
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
import android.util.Log;
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

import com.dev.lishabora.Adapters.FarmersDragAdapter;
import com.dev.lishabora.Models.Cycles;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.RPFSearchModel;
import com.dev.lishabora.Models.ResponseModel;
import com.dev.lishabora.Models.RoutesModel;
import com.dev.lishabora.Models.UnitsModel;
import com.dev.lishabora.Utils.Draggable.helper.OnStartDragListener;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.Utils.RequestDataCallback;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.Trader.Activities.CreateFarmerActivity;
import com.dev.lishabora.Views.Trader.FarmerConst;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import github.nisrulz.recyclerviewhelper.RVHItemClickListener;
import github.nisrulz.recyclerviewhelper.RVHItemDividerDecoration;
import github.nisrulz.recyclerviewhelper.RVHItemTouchHelperCallback;

import static com.dev.lishabora.Models.FamerModel.farmerDateComparator;
import static com.dev.lishabora.Models.FamerModel.farmerNameComparator;
import static com.dev.lishabora.Models.FamerModel.farmerPosComparator;

public class FragementFarmersDragList extends Fragment implements OnStartDragListener {
    private final int CHRONOLOGICAL = 1, ALPHABETICAL = 2, AUTOMATICALLY = 0, MANUALLY = 3;
    FarmersDragAdapter listAdapter;
    Gson gson = new Gson();
    boolean isArchived = false;
    boolean isDummy = false;
    FloatingActionButton fab;
    MaterialSpinner spinner1, spinner2;
    LinearLayout lspinner1, lspinner2;
    List<UnitsModel> getUnits = new LinkedList<>();
    List<RoutesModel> getRoutess = new LinkedList<>();
    List<Cycles> getCycles = new LinkedList<>();
    private View view;
    private Context context;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout empty_layout;
    private TextView emptyTxt, txt_network_state;
    private AVLoadingIndicatorView avi;
    private String filterText = "";
    private SearchView searchView;
    private TraderViewModel mViewModel;
    private PrefrenceManager prefrenceManager;
    private List<RoutesModel> routesModels;
    private ItemTouchHelper mItemTouchHelper;
    private Button btnDrag;
    private boolean isDraggable = true;
    private int SORTTYPE = 0;


    public void collectMilk(FamerModel famerModel) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_collect_milk, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        alertDialogBuilderUserInput.setView(mView);


        alertDialogBuilderUserInput
                .setCancelable(false);
        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialogAndroid.show();

        MaterialButton btnPositive, btnNegative, btnNeutral;
        TextView txtTitle;
        LinearLayout lTitle;
        ImageView imgIcon;


        TextView names, balance, day1, day2, day3, day1am, day1pm, day2am, day2pm, day3am, day3pm, today;
        TextInputEditText edtTodayAm, edtTodayPm;


        names = mView.findViewById(R.id.txt_name);
        balance = mView.findViewById(R.id.txt_balance);

        day1 = mView.findViewById(R.id.txt_day_1);
        day1am = mView.findViewById(R.id.txt_day_1_am);
        day1pm = mView.findViewById(R.id.txt_day_1_pm);

        day2 = mView.findViewById(R.id.txt_day_2);
        day2am = mView.findViewById(R.id.txt_day_2_am);
        day2pm = mView.findViewById(R.id.txt_day_2_pm);

        day3 = mView.findViewById(R.id.txt_day_3);
        day3am = mView.findViewById(R.id.txt_day_3_am);
        day3pm = mView.findViewById(R.id.txt_day_3_pm);

        today = mView.findViewById(R.id.txt_today);

        edtTodayAm = mView.findViewById(R.id.edt_am);
        edtTodayPm = mView.findViewById(R.id.edt_pm);


        names.setText(famerModel.getNames());
        balance.setText(famerModel.getTotalbalance());

//        today.setText(DateTimeUtils.Companion.getDayOfWeek(DateTimeUtils.Companion.getTodayDate(),"E"));
//        day3.setText(DateTimeUtils.Companion.getDayPrevious(1));
//        day2.setText(DateTimeUtils.Companion.getDayPrevious(2));
//        day1.setText(DateTimeUtils.Companion.getDayPrevious(3));
//

        btnPositive = mView.findViewById(R.id.btn_positive);
        btnNegative = mView.findViewById(R.id.btn_negative);
        btnNeutral = mView.findViewById(R.id.btn_neutral);
        txtTitle = mView.findViewById(R.id.txt_title);
        lTitle = mView.findViewById(R.id.linear_title);
        imgIcon = mView.findViewById(R.id.img_icon);


        btnNeutral.setVisibility(View.GONE);
        lTitle.setVisibility(View.GONE);
        txtTitle.setVisibility(View.VISIBLE);
        imgIcon.setVisibility(View.VISIBLE);
        imgIcon.setImageResource(R.drawable.ic_add_black_24dp);
        txtTitle.setText("Route");

        btnPositive.setOnClickListener(view -> alertDialogAndroid.dismiss());
        btnNeutral.setOnClickListener(view -> {

        });
        btnNegative.setOnClickListener(view -> alertDialogAndroid.dismiss());

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

    public void initList() {
        recyclerView = view.findViewById(R.id.recyclerView);
        listAdapter = new FarmersDragAdapter(getActivity(), FarmerConst.getSearchFamerModels(), new OnclickRecyclerListener() {
            @Override
            public void onSwipe(int adapterPosition, int direction) {

            }

            @Override
            public void onClickListener(int position) {

                collectMilk(FarmerConst.getSearchFamerModels().get(position));


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
                // view.setBackgroundColor(context.getResources().getColor(R.drawable.ms__shadow_background));

            }
        }, this);

//        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(listAdapter);
//        mItemTouchHelper = new ItemTouchHelper(callback);
//
//        mItemTouchHelper.attachToRecyclerView(recyclerView);


        // Setup onItemTouchHandler to enable drag and drop , swipe left or right
        ItemTouchHelper.Callback callback = new RVHItemTouchHelperCallback(listAdapter, true, false,
                false);
        ItemTouchHelper helper = new ItemTouchHelper(callback);


        helper.attachToRecyclerView(recyclerView);

        // Set the divider in the recyclerview
        recyclerView.addItemDecoration(new RVHItemDividerDecoration(getContext(), LinearLayoutManager.VERTICAL));


        recyclerView.addOnItemTouchListener(new RVHItemClickListener(context, (view, position) -> {

            //view.setBackgroundColor(context.getResources().getColor(R.color.red));


            //Toast.makeText(MainActivity.this, value, Toast.LENGTH_SHORT).show();
        }));


    }


    public void update(List<FamerModel> famerModels) {

        Log.d("ReTr", "update started");

        if (FarmerConst.getFamerModels() != null && listAdapter != null) {
            Log.d("ReTr", "update started");

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

        try {
            Objects.requireNonNull(getActivity()).setTitle("Farmers List");
        } catch (Exception nm) {
            nm.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);

        //inflater.inflate(R.menu.menu_main, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        MenuItem sett = menu.findItem(R.id.action_settings);
        mSearch.setVisible(false);
        sett.setVisible(false);


        MenuItem mAutomatically = menu.findItem(R.id.action_automatically);
        MenuItem mManually = menu.findItem(R.id.action_manually);
        MenuItem mChronologically = menu.findItem(R.id.action_chronologically);
        MenuItem mAlphabetically = menu.findItem(R.id.action_alphabetically);
        MenuItem mRearrangeManually = menu.findItem(R.id.action_smanually);

        mAutomatically.setVisible(false);
        mChronologically.setVisible(false);
        mManually.setVisible(false);
        mAlphabetically.setVisible(false);
        mRearrangeManually.setVisible(false);


        searchView = (SearchView) mSearch.getActionView();
        searchView.setVisibility(View.GONE);


    }

    void setDraggale(boolean draggale) {
//        if (listAdapter != null) {
//            listAdapter.setDraggale(draggale);
//        }
//        if (draggale) {
//            isDraggable = true;
//            btnDrag.setVisibility(View.VISIBLE);
//        } else {
//            //listAdapter.setDraggale(false);
//            isDraggable = false;
//            btnDrag.setVisibility(View.GONE);
//        }
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


            default:
                break;
        }

        return false;
    }

    private void startDrag() {

        setDraggale(true);
        filterFarmers();
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

        prefrenceManager = new PrefrenceManager(getContext());

        recyclerView = view.findViewById(R.id.recyclerView);
        empty_layout = view.findViewById(R.id.empty_layout);
        emptyTxt = view.findViewById(R.id.empty_text);
        avi = view.findViewById(R.id.avi);
        txt_network_state = view.findViewById(R.id.txt_network_state);
        btnDrag = view.findViewById(R.id.draggaing);
        btnDrag.setVisibility(View.VISIBLE);
        btnDrag.setOnClickListener(view13 -> {

            updateItems();
        });
        setDraggale(false);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(viehw -> {
            if (isSetUp()) {
                FragementFarmersDragList.this.createFarmers();
            } else {
                initDataOffline(true, true, true);
                MyToast.toast("Routes, Units and Cycles Not set Up", getContext(), R.drawable.ic_launcher, Toast.LENGTH_LONG);
            }
        });
        fab.hide();


        spinner1 = view.findViewById(R.id.spinner1);
        lspinner1 = view.findViewById(R.id.lspinner1);
        spinner2 = view.findViewById(R.id.spinner2);
        lspinner2 = view.findViewById(R.id.lspinner2);
        spinner2.setVisibility(View.VISIBLE);
        lspinner2.setVisibility(View.VISIBLE);
        spinner1.setVisibility(View.GONE);
        lspinner1.setVisibility(View.GONE);
        //spinner1.setItems("Active","Archived","Deleted","Dummy","All");

//
//        spinner1.setItems("Active", "Archived", "Deleted", "Dummy", "All");
        getRoutes();
//
//        spinner1.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view1, position, id, item) -> {
//
//            fetchFarmers(getSelectedAccountStatus(), getSelectedRoute());
//
//        });
//
        spinner2.setOnItemSelectedListener((view12, position, id, item) -> {

            fetchFarmers(getSelectedAccountStatus(), getSelectedRoute());


        });
//        if (prefrenceManager.isRoutesListFirstTime() || prefrenceManager.isCycleListFirstTime() || prefrenceManager.isUnitListFirstTime()) {
//            initDataOffline(false, false, false);
//        }


    }

    void popOutFragments() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }

    private void updateItems() {
        for (int a = 1; a < FarmerConst.getSearchFamerModels().size() + 1; a++) {
            FamerModel f = FarmerConst.getSearchFamerModels().get(a - 1);
            f.setPosition(a);

            mViewModel.updateFarmer(f, false);
        }
        popOutFragments();

    }

    private int getSelectedAccountStatus() {
        Log.d("Acsel", "" + spinner1.getSelectedIndex());
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

        mViewModel.getFarmerByStatusRoute(staus, route).observe(FragementFarmersDragList.this, famerModels -> {
            avi.smoothToHide();
            prefrenceManager.setIsFarmerListFirst(false);
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

    private void setUpSpinner2Listner() {
        spinner2.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view1, position, id, item) -> {

            filterFarmers();
        });
    }

    private void getRoutes() {
        if (mViewModel != null) {
            mViewModel.getRoutes(false).observe(this, routesModels -> {
                prefrenceManager.setIsRoutesListFirst(false);
                if (routesModels != null && routesModels.size() > 0) {
                    FragementFarmersDragList.this.routesModels = routesModels;
                    String routes[] = new String[routesModels.size() + 1];
                    routes[0] = "All";

                    for (int a = 1; a < routesModels.size() + 1; a++) {
                        routes[a] = routesModels.get(a - 1).getRoute();

                    }

                    spinner2.setItems(routes);
                    //filterFarmers();
                } else {
                    getRoutesOnline();
                }
                //spinner2.setItems(routesModels);
            });
        }
    }

    private void getRoutesOnline() {
        if (mViewModel != null) {
            mViewModel.getRoutes(true).observe(this, routesModels -> {
                prefrenceManager.setIsRoutesListFirst(false);
                if (routesModels != null && routesModels.size() > 0) {
                    FragementFarmersDragList.this.routesModels = routesModels;
                    String routes[] = new String[routesModels.size() + 1];
                    routes[0] = "All";

                    for (int a = 1; a < routesModels.size() + 1; a++) {
                        routes[a] = routesModels.get(a - 1).getRoute();

                    }

                    spinner2.setItems(routes);
                    //filterFarmers();
                }
                //spinner2.setItems(routesModels);
            });
        }
    }

    private void createFarmers() {
        startActivity(new Intent(getActivity(), CreateFarmerActivity.class));
    }


    private JSONObject getTraderFarmeroductsObject() {
        RPFSearchModel rpfSearchModel = new RPFSearchModel();
        rpfSearchModel.setEntitycode(new PrefrenceManager(getActivity()).getTraderModel().getCode());
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(rpfSearchModel));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void getFarmers() {
        avi.smoothToShow();
        avi.setVisibility(View.VISIBLE);

        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);

        }

        mViewModel.getFarmers(getTraderFarmeroductsObject(), prefrenceManager.isFarmerListFirstTime()).observe(FragementFarmersDragList.this, new Observer<List<FamerModel>>() {
            @Override
            public void onChanged(@Nullable List<FamerModel> famerModels) {
                avi.smoothToHide();
                prefrenceManager.setIsFarmerListFirst(false);
                update(famerModels);
            }
        });
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
        filterFarmersManually();


//        switch (SORTTYPE) {
//            case AUTOMATICALLY:
//                listAdapter.notifyDataSetChanged();
//                break;
//            case ALPHABETICAL:
//                filterFarmersAlpahbetically();
//                break;
//            case CHRONOLOGICAL:
//                filterFarmersChronologically();
//                break;
//            case MANUALLY:
//
//                filterFarmersManually();
//                break;
//            default:
//                listAdapter.notifyDataSetChanged();
//        }


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
        fetchFarmers(getSelectedAccountStatus(), getSelectedRoute());

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private void snack(String msg) {
        if (view != null) {
            Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();

        }
        Log.d("SnackMessage", msg);
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
                    mViewModel.updateFarmer(famerModel, false).observe(FragementFarmersDragList.this, responseModel -> avi.smoothToHide());
                    FragementFarmersDragList.this.fetchFarmers(FragementFarmersDragList.this.getSelectedAccountStatus(), FragementFarmersDragList.this.getSelectedRoute());//update(famerModel);


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
                    mViewModel.updateFarmer(famerModel, false).observe(FragementFarmersDragList.this, responseModel -> avi.smoothToHide());
                    FragementFarmersDragList.this.fetchFarmers(FragementFarmersDragList.this.getSelectedAccountStatus(), FragementFarmersDragList.this.getSelectedRoute());


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
                    mViewModel.updateFarmer(famerModel, false).observe(FragementFarmersDragList.this, new Observer<ResponseModel>() {
                        @Override
                        public void onChanged(@Nullable ResponseModel responseModel) {
                            avi.smoothToHide();
                            fetchFarmers(getSelectedAccountStatus(), getSelectedRoute());

                        }
                    });

                    break;
                case R.id.edit:


                    Log.d("farmerdialog", "edit clicked");
                    FragementFarmersDragList.this.editTrader(famerModel, FragementFarmersDragList.this.getUnits(), FragementFarmersDragList.this.getCycles(), FragementFarmersDragList.this.getRoutess(), true);
                    break;
                case R.id.view:

                    FragementFarmersDragList.this.editTrader(famerModel, FragementFarmersDragList.this.getUnits(), FragementFarmersDragList.this.getCycles(), FragementFarmersDragList.this.getRoutess(), false);

                    break;
                default:
            }
            return false;
        });
        popupMenu.show();
    }

    private List<RoutesModel> getRoutess() {
        return getRoutess;
    }

    private List<Cycles> getCycles() {
        return getCycles;
    }

    private List<UnitsModel> getUnits() {
        return getUnits;
    }

    public void editTrader(FamerModel famerModel, List<UnitsModel> unitsModels,
                           List<Cycles> cycles, List<RoutesModel> routesModels, boolean isEditale) {
        Log.d("farmerdialog", "in edit");

        if (context != null) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
            View mView = layoutInflaterAndroid.inflate(R.layout.dialog_edit_farmer, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(context));
            alertDialogBuilderUserInput.setView(mView);
//            alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
//            alertDialogBuilderUserInput.setTitle(famerModel.getNames() + " " + famerModel.getCode());


            UnitsModel unitsModel = new UnitsModel();
            RoutesModel routesModel = new RoutesModel();
            Cycles cyclesM = new Cycles();
            avi = mView.findViewById(R.id.avi);

            MaterialSpinner spinnerRoute, spinnerUnit;

            TextInputEditText edtNames, edtMobile;
            MaterialSpinner defaultPayment;
            TraderViewModel mViewModel;
            MaterialSpinner spinner;
            TextInputEditText edtRouteName, edtRouteCode, edtUnitName, edtUnitPrice, edtUnitMeasurement;
            LinearLayout lcycle;
            TextView txtStartDay, txtEndDay, txtCycle;

            txtStartDay = mView.findViewById(R.id.starts);
            txtEndDay = mView.findViewById(R.id.ends);
            txtCycle = mView.findViewById(R.id.txt_cycle);


            try {
                txtStartDay.setText(famerModel.getCycleStartDay());
                txtEndDay.setText(famerModel.getCycleStartEndDay());
                txtCycle.setText(famerModel.getCyclename());
            } catch (Exception nm) {
                nm.printStackTrace();
            }


            edtNames = mView.findViewById(R.id.edt_farmer_names);
            edtMobile = mView.findViewById(R.id.edt_farmer_phone);
            defaultPayment = mView.findViewById(R.id.spinnerPayments);

            spinnerUnit = mView.findViewById(R.id.spinnerUnit);
            spinnerRoute = mView.findViewById(R.id.spinnerRoute);
            edtUnitName = mView.findViewById(R.id.edt_unit_names);
            edtUnitPrice = mView.findViewById(R.id.edt_unit_price);
            edtUnitMeasurement = mView.findViewById(R.id.edt_unit_size);
            edtRouteName = mView.findViewById(R.id.edt_route_names);
            edtRouteCode = mView.findViewById(R.id.edt_route_code);
            spinner = mView.findViewById(R.id.spinnerCycle);

            if (!isEditale) {
                edtNames.setEnabled(false);
                edtMobile.setEnabled(false);
                //defaultPayment.setEnabled(false);

                spinnerUnit.setEnabled(false);
                spinnerUnit.setVisibility(View.GONE);
                spinnerRoute.setEnabled(false);
                spinnerRoute.setVisibility(View.GONE);
                edtUnitName.setEnabled(false);
                edtUnitPrice.setEnabled(false);
                edtUnitMeasurement.setEnabled(false);
                edtRouteName.setEnabled(false);
                edtRouteCode.setEnabled(false);
                spinner.setSelected(false);
                spinner.setVisibility(View.GONE);
                spinner.setEnabled(false);

            }


            edtNames.setText(famerModel.getNames());
            edtMobile.setText(famerModel.getMobile());
            edtUnitPrice.setText("" + famerModel.getUnitprice());
            edtUnitMeasurement.setText("" + famerModel.getUnitcapacity());
            edtRouteName.setText("" + famerModel.getRoutename());
            edtUnitName.setText("" + famerModel.getUnitname());
            edtRouteCode.setText("" + famerModel.getRoutecode());

//Units
            if (unitsModels != null && unitsModels.size() > 0) {
                prefrenceManager.setIsRoutesListFirst(false);
                String units[] = new String[unitsModels.size() + 1];

                units[0] = "Choose Unit ";
                for (int a = 0; a < unitsModels.size(); a++) {
                    units[a + 1] = unitsModels.get(a).getUnit();

                }
                spinnerUnit.setItems(units);

            }

//Routes
            if (routesModels != null && routesModels.size() > 0) {
                prefrenceManager.setIsRoutesListFirst(false);

                String routes[] = new String[routesModels.size() + 1];

                routes[0] = "Choose Route";
                for (int a = 0; a < routesModels.size(); a++) {
                    routes[a + 1] = routesModels.get(a).getRoute();

                }
                spinnerRoute.setItems(routes);
            }

//Cycles
            if (cycles != null && cycles.size() > 0) {
                new PrefrenceManager(getContext()).setIsCyclesListFirst(false);
                String units[] = new String[cycles.size()];

                // units[0]="Choose Unit ";
                for (int a = 0; a < cycles.size(); a++) {
                    units[a] = cycles.get(a).getCycle();

                }
                spinner.setItems(units);

            }


            alertDialogBuilderUserInput
                    .setCancelable(false);
//                    .setPositiveButton("Update", (dialogBox, id) -> {
//                        // ToDo get user input here
//
//
//                    })
//
//                    .setNegativeButton("Dismiss",
//                            (dialogBox, id) -> dialogBox.cancel());

            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.setCancelable(false);
            alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            alertDialogAndroid.show();
//
//            Button theButton = alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE);
//            theButton.setOnClickListener(new EditCustomListener(alertDialogAndroid, famerModel, unitsModel, routesModel, cyclesM));

            MaterialButton btnPositive, btnNegative, btnNeutral;
            TextView txtTitle;
            LinearLayout lTitle;
            ImageView imgIcon;
            btnPositive = mView.findViewById(R.id.btn_positive);
            btnNegative = mView.findViewById(R.id.btn_negative);
            btnNeutral = mView.findViewById(R.id.btn_neutral);
            txtTitle = mView.findViewById(R.id.txt_title);
            lTitle = mView.findViewById(R.id.linear_title);
            imgIcon = mView.findViewById(R.id.img_icon);


            btnNeutral.setVisibility(View.GONE);
            btnPositive.setVisibility(View.GONE);
            lTitle.setVisibility(View.GONE);
            txtTitle.setVisibility(View.VISIBLE);
            imgIcon.setVisibility(View.VISIBLE);
            imgIcon.setImageResource(R.drawable.ic_add_black_24dp);
            txtTitle.setText(famerModel.getNames() + " " + famerModel.getCode());

            btnPositive.setOnClickListener(new EditCustomListener(alertDialogAndroid, famerModel, unitsModel, routesModel, cyclesM));
            btnNeutral.setOnClickListener(view -> {

            });
            btnNegative.setOnClickListener(view -> alertDialogAndroid.dismiss());

        } else {
            Log.d("farmerdialog", "context nulll edit clicked");

        }

    }

    private void initDataOffline(boolean isRoutesFirst, boolean isUnitFirst, boolean isCycles) {

        if (isRoutesFirst && isUnitFirst && isCycles) {
            avi.smoothToShow();
        }
        TraderViewModel mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);
        mViewModel.getRoutes(isRoutesFirst).observe(this, routesModels -> {
            if (routesModels != null && routesModels.size() > 0) {
                prefrenceManager.setIsRoutesListFirst(false);
                this.getRoutess = routesModels;

                avi.smoothToHide();

            }

        });

        mViewModel.getUnits(isUnitFirst).observe(this, unitsModels -> {
            if (unitsModels != null && unitsModels.size() > 0) {
                prefrenceManager.setIsRoutesListFirst(false);
                FragementFarmersDragList.this.getUnits = unitsModels;


            }
        });
        mViewModel.getCycles(isCycles).observe(this, cycles -> {
            if (cycles != null && cycles.size() > 0) {
                prefrenceManager.setIsCyclesListFirst(false);
                FragementFarmersDragList.this.getCycles = cycles;


            }
        });
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);


    }


    private class EditCustomListener implements View.OnClickListener {
        AlertDialog dialog;
        boolean isDummy = false;
        FamerModel farmer;
        UnitsModel unitsModel;
        RoutesModel routesModel;
        Cycles cycles;

        //int type;
        RequestDataCallback requestDataCallback;

        public EditCustomListener(AlertDialog alertDialogAndroid, FamerModel famerModel, UnitsModel u, RoutesModel r, Cycles c) {
            dialog = alertDialogAndroid;
            this.farmer = famerModel;
            this.unitsModel = u;
            this.routesModel = r;
            this.cycles = c;

        }

        @Override
        public void onClick(View v) {


        }
    }


}
