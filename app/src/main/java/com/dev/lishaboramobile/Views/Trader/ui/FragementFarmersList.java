package com.dev.lishaboramobile.Views.Trader.ui;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.lishaboramobile.Farmer.Models.FamerModel;
import com.dev.lishaboramobile.Global.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.Global.Utils.RequestDataCallback;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Trader.Models.Cycles;
import com.dev.lishaboramobile.Trader.Models.RPFSearchModel;
import com.dev.lishaboramobile.Trader.Models.RoutesModel;
import com.dev.lishaboramobile.Trader.Models.UnitsModel;
import com.dev.lishaboramobile.Views.Trader.CreateFarmerActivity;
import com.dev.lishaboramobile.Views.Trader.FarmerController;
import com.dev.lishaboramobile.Views.Trader.FarmerViewModel;
import com.dev.lishaboramobile.Views.Trader.FarmersAdapter;
import com.dev.lishaboramobile.Views.Trader.TraderViewModel;
import com.dev.lishaboramobile.login.PrefrenceManager;
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

public class FragementFarmersList extends Fragment {
    FarmersAdapter listAdapter;
    Gson gson = new Gson();
    FarmerController farmersController;
    List<Object> objects;
    LinkedList<FamerModel> famerModels;
    LinkedList<FamerModel> filteredFamerModels;
    boolean isArchived = false;
    boolean isDummy = false;
    FarmerViewModel famersViewModel;
    ViewModel viewModel;
    private View view;
    private Context context;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout linearLayoutEmpty;
    private int entity;
    private ProgressDialog progressDialog;
    private ActionMode mActionMode;
    private SwipeRefreshLayout swipe_refresh_layout;
    private LinearLayout empty_layout;
    private TextView emptyTxt, txt_network_state;
    private ChipGroup chipGroup;
    private Chip activeChip, allChip, deletedChip, archivedChip, syncedChip, dummyChip, unsyncedChip;
    private AVLoadingIndicatorView avi;
    private boolean isConnected;
    private String filterText = "";
    FloatingActionButton fab;
    MaterialSpinner spinner1, spinner2;
    List<UnitsModel> getUnits = new LinkedList<>();
    private SearchView searchView;
    List<RoutesModel> getRoutess = new LinkedList<>();
    List<Cycles> getCycles = new LinkedList<>();
    // private AdminsViewModel mViewModel;
    private TraderViewModel mViewModel;
    private PrefrenceManager prefrenceManager;
    private List<RoutesModel> routesModels;



    private int FarmerDel, FarmerDummy, TraderSynched, FarmerArchive, All;

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
        listAdapter = new FarmersAdapter(getActivity(), filteredFamerModels, new OnclickRecyclerListener() {
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
                popupMenu(adapterPosition, view, filteredFamerModels.get(adapterPosition));

            }
        });


    }


    public void update(List<FamerModel> famerModels) {

        Log.d("ReTr", "update started");

        if (this.famerModels != null && listAdapter != null) {
            Log.d("ReTr", "update started");

            this.famerModels.clear();
            this.famerModels.addAll(famerModels);
            filterFarmers();
            //listAdapter.notifyDataSetChanged();


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
            default:
                break;
        }

        return false;
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

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viehw) {
                FragementFarmersList.this.createFarmers();
            }
        });


        spinner1 = view.findViewById(R.id.spinner1);
        spinner2 = view.findViewById(R.id.spinner2);
        spinner2.setVisibility(View.GONE);

        spinner1.setItems("Filter Automatically", "By Route", "Chronologically", "Manually", "Alphabetically", "By Account Status");
        spinner1.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view1, position, id, item) -> {
            if (position == 1 || position == 5) {
                spinner2.setVisibility(View.VISIBLE);
                setUpSpinner2(position);
            } else {
                spinner2.setVisibility(View.GONE);
            }
        });

        if (prefrenceManager.isRoutesListFirstTime() || prefrenceManager.isCycleListFirstTime() || prefrenceManager.isUnitListFirstTime()) {
            initDataOffline(false, false, false);
        }



    }

    private void setUpSpinner2(int pos) {
        switch (pos) {
            case 0:
                filterFarmers();
                break;
            case 2:
                filterFarmers();
                break;
            case 3:
                filterFarmers();
                break;

            case 1:
                getRoutes();
                //spinner2.setItems(getRoutes());
                setUpSpinner2Listner();
                break;
            case 5:
                spinner2.setItems("All", "Active", "Archived", "In-Active", "Dummy");
                setUpSpinner2Listner();
                break;

            case 4:
                filterFarmersAlpahbetically();
            default:


        }
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
                    FragementFarmersList.this.routesModels = routesModels;
                    String routes[] = new String[routesModels.size()];
                    // routes[0] = "Choose Route";

                    for (int a = 0; a < routesModels.size(); a++) {
                        routes[a] = routesModels.get(a).getRoute();

                    }

                    spinner2.setItems(routes);
                    filterFarmers();
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
                    FragementFarmersList.this.routesModels = routesModels;
                    String routes[] = new String[routesModels.size()];
                    // routes[0] = "Choose Route";

                    for (int a = 0; a < routesModels.size(); a++) {
                        routes[a] = routesModels.get(a).getRoute();

                    }

                    spinner2.setItems(routes);
                    filterFarmers();
                }
                //spinner2.setItems(routesModels);
            });
        }
    }

    private void createFarmers() {
        startActivity(new Intent(getActivity(), CreateFarmerActivity.class));
    }

    void getFarmers(boolean isOnline) {
        if (avi != null) {
            startAnim();
        }
        if (famersViewModel != null) {

        } else {
            snack("FamersViewModel is null");
        }
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

        mViewModel.getFarmers(getTraderFarmeroductsObject(), prefrenceManager.isFarmerListFirstTime()).observe(FragementFarmersList.this, new Observer<List<FamerModel>>() {
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

        if (filteredFamerModels.size() < 1) {
            //snack("No data");
        }

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

        String route = "";
        if (spinner1.getSelectedIndex() == 1) {
            if (spinner2.getItems() != null) {
                route = spinner2.getItems().get(spinner2.getSelectedIndex()).toString();
            }
            if (route != null && route.length() > 0) {

            } else {
                route = "";
            }
        }




        filteredFamerModels.clear();
        if (famerModels != null && famerModels.size() > 0) {
            for (FamerModel famerModel : famerModels) {
                if (famerModel.getCode().toLowerCase().contains(filterText) ||
                        famerModel.getMobile().toLowerCase().contains(filterText) ||
                        famerModel.getNames().toLowerCase().contains(filterText)) {
                    if (famerModel.getRoutename() != null) {
                        if (famerModel.getRoutename().toLowerCase().contains(route.toLowerCase())) {
                            filteredFamerModels.add(famerModel);
                        }
                    } else {
                        filteredFamerModels.add(famerModel);

                    }
                }

            }
            listAdapter.notifyDataSetChanged();
        }

    }

    private void filterFarmersAlpahbetically() {


        filteredFamerModels = famerModels;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//          //  Collections.sort(filteredFamerModels, Comparator.comparing(FamerModel::getNames));
//        }else {
        Collections.sort(filteredFamerModels, (v1, v2) -> v1.getNames().compareTo(v2.getNames()));
        // }
        listAdapter.notifyDataSetChanged();


    }

    @Override
    public void onStart() {
        super.onStart();
        famerModels = new LinkedList<>();
        filteredFamerModels = new LinkedList<>();

        try {
            Objects.requireNonNull(getActivity()).setTitle("Farmers");
        } catch (Exception nm) {
            nm.printStackTrace();
        }


        initList();
        populateTraders();
        getFarmers();



    }

    @Override
    public void onResume() {
        super.onResume();
        if (farmersController == null) {
            farmersController = new FarmerController(context);

        }
        if (famerModels == null) {
            famerModels = new LinkedList<>();
        }
        if (filteredFamerModels == null) {
            filteredFamerModels = new LinkedList<>();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        farmersController = null;

    }

    @Override
    public void onStop() {
        super.onStop();
        farmersController = null;
//        ((TraderActivity) Objects.requireNonNull(getActivity())).fabButton(false, R.drawable.ic_add_black_24dp, () -> {

        //   });


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

        if (famerModel.getDeleted() == 1) {
            popupMenu.getMenu().getItem(0).setVisible(false);
        }
        if (famerModel.getArchived() == 1) {
            isArchived = true;
            popupMenu.getMenu().getItem(1).setTitle("Un-Archive");
        }
        if (famerModel.getDummy() == 1) {
            isDummy = true;
            popupMenu.getMenu().getItem(2).setTitle("Remove from dummy");
        }


        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.delete:


                    famerModel.setStatus("Deleted");
                    famerModel.setDeleted(1);
                    //update(famerModel);


                    break;

                case R.id.archive:

                    if (isArchived) {
                        famerModel.setStatus("Active");
                        famerModel.setArchived(0);
                    } else {
                        famerModel.setStatus("Archived");
                        famerModel.setArchived(1);
                    }
                    // update(famerModel);

                    break;

                case R.id.dummy:

                    if (isDummy) {
                        famerModel.setStatus("Active");
                        famerModel.setDummy(0);
                    } else {
                        famerModel.setStatus("Dummy");
                        famerModel.setDummy(1);
                    }

                    // update(famerModel);
                    break;
                case R.id.edit:


                    Log.d("farmerdialog", "edit clicked");
                    editTrader(famerModel, getUnits(), getCycles(), getRoutess(), true);
                    break;
                case R.id.view:

                    editTrader(famerModel, getUnits(), getCycles(), getRoutess(), false);

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
            alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
            alertDialogBuilderUserInput.setTitle(famerModel.getNames() + " " + famerModel.getCode());


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
                spinnerRoute.setEnabled(false);
                edtUnitName.setEnabled(false);
                edtUnitPrice.setEnabled(false);
                edtUnitMeasurement.setEnabled(false);
                edtRouteName.setEnabled(false);
                edtRouteCode.setEnabled(false);
                spinner.setSelected(false);
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
                    .setCancelable(false)
                    .setPositiveButton("Update", (dialogBox, id) -> {
                        // ToDo get user input here


                    })

                    .setNegativeButton("Dismiss",
                            (dialogBox, id) -> dialogBox.cancel());

            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.setCancelable(false);
            alertDialogAndroid.show();

            Button theButton = alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE);
            theButton.setOnClickListener(new EditCustomListener(alertDialogAndroid, famerModel, unitsModel, routesModel, cyclesM));

        } else {
            Log.d("farmerdialog", "context nulll edit clicked");

        }

    }

    private void initDataOffline(boolean isRoutesFirst, boolean isUnitFirst, boolean isCycles) {

        TraderViewModel mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);
        mViewModel.getRoutes(isRoutesFirst).observe(this, routesModels -> {
            if (routesModels != null && routesModels.size() > 0) {
                prefrenceManager.setIsRoutesListFirst(false);
                this.getRoutess = routesModels;

            }

        });
        mViewModel.getUnits(isUnitFirst).observe(this, unitsModels -> {
            if (unitsModels != null && unitsModels.size() > 0) {
                prefrenceManager.setIsRoutesListFirst(false);
                FragementFarmersList.this.getUnits = unitsModels;


            }
        });
        mViewModel.getCycles(isCycles).observe(this, cycles -> {
            if (cycles != null && cycles.size() > 0) {
                prefrenceManager.setIsCyclesListFirst(false);
                FragementFarmersList.this.getCycles = cycles;


            }
        });
    }


//    private void update(FamerModel famerModel) {
//
////        listAdapter.notifyDataSetChanged();
////
////        if (farmersController != null) {
////            farmersController.startAnim();
////        }
////
////        if (famersViewModel != null) {
////            try {
////                famersViewModel.updateTrader(new JSONObject(gson.toJson(traderModel)), true).observe(this, responseModel -> {
////                    if (farmersController != null) {
////                        farmersController.stopAnim();
////                        if (responseModel != null) {
////                            farmersController.snack(responseModel.getResultDescription());
////                        }
////                        if (responseModel != null) {
////                            if (responseModel.getResultCode() != 0) {
////                                farmersController.dismissDialog();
////                                famersViewModel.refresh(getSearchObject(), true);
////                            }
////                        }
////                    }
////
////                });
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
////        }
//
//
//    }

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
