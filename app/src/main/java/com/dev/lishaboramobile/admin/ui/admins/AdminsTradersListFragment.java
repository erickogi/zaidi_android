package com.dev.lishaboramobile.admin.ui.admins;


import android.arch.lifecycle.Observer;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dev.lishaboramobile.Global.Models.FetchTraderModel;
import com.dev.lishaboramobile.Global.Models.ResponseModel;
import com.dev.lishaboramobile.Global.Utils.DateTimeUtils;
import com.dev.lishaboramobile.Global.Utils.GeneralUtills;
import com.dev.lishaboramobile.Global.Utils.NetworkUtils;
import com.dev.lishaboramobile.Global.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.Global.Utils.RequestDataCallback;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Trader.Models.TraderModel;
import com.dev.lishaboramobile.admin.TradersController;
import com.dev.lishaboramobile.admin.adapters.TradersAdapter;
import com.dev.lishaboramobile.login.PrefrenceManager;
import com.dev.lishaboramobile.login.ui.login.LoginController;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class AdminsTradersListFragment extends Fragment {

    Chip chipAll;
    Chip chipDummy;
    Chip chipDelete;
    Chip chipArchive;
    FloatingActionButton fab;
    TradersAdapter listAdapter;
    Gson gson = new Gson();
    TradersController tradersController;
    List<Object> objects;
    LinkedList<TraderModel> traderModels;
    LinkedList<TraderModel> filteredTraderModels;
    boolean isArchived = false;
    boolean isDummy = false;
    private ChipGroup chipGroup;
    private Chip activeChip, allChip, deletedChip, archivedChip, syncedChip, dummyChip, unsyncedChip;
    private View view;
    private Context context;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout linearLayoutEmpty;

    private LinearLayout empty_layout;
    private TextView emptyTxt, txt_network_state;
    private String filterText = "";
    private AVLoadingIndicatorView avi;

    private boolean isConnected;
    private MaterialSpinner spinner;

    private int TraderDel, TraderDummy, TraderSynched, TraderArchive, All;



    private AdminsViewModel mViewModel;
    private SearchView searchView;

    public static AdminsTradersListFragment newInstance() {
        return new AdminsTradersListFragment();
    }

    private void disablechips() {
        TraderDel = 0;
        TraderArchive = 0;
        TraderDummy = 0;
        All = 0;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;


    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filterText = s;
                filterTraders(spinner.getSelectedIndex());

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterText = s;
                filterTraders(spinner.getSelectedIndex());

                return true;
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admins_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AdminsViewModel.class);

        // TODO: Use the ViewModel
    }

    void initWIdgets() {
        recyclerView = view.findViewById(R.id.recyclerView);
        empty_layout = view.findViewById(R.id.empty_layout);
        emptyTxt = view.findViewById(R.id.empty_text);
        avi = view.findViewById(R.id.avi);
        txt_network_state = view.findViewById(R.id.txt_network_state);
        spinner = view.findViewById(R.id.spinner);


        chipGroup = view.findViewById(R.id.chip_group);
        chipAll = view.findViewById(R.id.chip_all);
        chipDummy = view.findViewById(R.id.chip_dummy);
        chipDelete = view.findViewById(R.id.chip_deleted);
        chipArchive = view.findViewById(R.id.chip_archived);


        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(view -> createTrader());

        spinner.setItems("All", "Active", "Deleted", "Archived", "Dummy");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                filterTraders(position);
            }
        });


        chipAll.setChecked(true);
        disablechips();

        All = 1;

//        chipDelete.setOnCheckedChangeListener((compoundButton, b) -> {
//
//            disablechips();
//
//            if (chipDelete.isChecked()) {
//                TraderDel = 1;
//            } else {
//                TraderDel = 0;
//            }
//            if (mViewModel != null) {
//                startAnim();
//                mViewModel.refresh(getSearchObject(), true);
//            }
//        });
//        chipArchive.setOnCheckedChangeListener((compoundButton, b) -> {
//            disablechips();
//
//            if (chipArchive.isChecked()) {
//                TraderArchive = 1;
//            } else {
//                TraderArchive = 0;
//            }
//            if (mViewModel != null) {
//                startAnim();
//                mViewModel.refresh(getSearchObject(), true);
//            }
//        });
//        chipAll.setOnCheckedChangeListener((compoundButton, b) -> {
//            disablechips();
//
//            if (b) {
//
//                All = 1;
//
//
//            } else {
//
//                All = 0;
//            }
//
//            if (mViewModel != null) {
//                startAnim();
//                /// emptyState(true,linearLayoutEmpty,);
//                mViewModel.refresh(getSearchObject(), true);
//            }
//        });
//        chipDummy.setOnCheckedChangeListener((compoundButton, b) -> {
//            disablechips();
//
//            if (chipDummy.isChecked()) {
//                TraderDummy = 1;
//            } else {
//                // setDefault();
//                TraderDummy = 0;
//            }
//
//            if (mViewModel != null && tradersController != null) {
//                startAnim();
//                mViewModel.refresh(getSearchObject(), true);
//            }
//        });
//
//        chipGroup.setOnCheckedChangeListener((chipGroup, i) -> {


        //  });


    }

    void initConnectivityListener() {
        ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnectedToInternet -> {
                    // do something with isConnectedToInternet value
                    isConnected = isConnectedToInternet;

                    if (isConnectedToInternet) {
                        try {
                            txt_network_state.setText("Connected to internet");
                            txt_network_state.setTextColor(getActivity().getResources().getColor(R.color.green_color_picker));
                        } catch (Exception nm) {
                            nm.printStackTrace();
                        }

                    } else {

                        try {
                            txt_network_state.setText("Not connected to internet");
                            txt_network_state.setTextColor(getActivity().getResources().getColor(R.color.red));
                        } catch (Exception vb) {
                            vb.printStackTrace();
                        }

                    }
                });
    }

    void initTradersList() {
        if (NetworkUtils.Companion.isConnectionFast(Objects.requireNonNull(getActivity()))) {
            listAdapter = new TradersAdapter(getActivity(), filteredTraderModels, new OnclickRecyclerListener() {
                @Override
                public void onClickListener(int position) {

                    Intent intent = new Intent(getActivity(), AdimTraderProfile.class);
                    intent.putExtra("data", filteredTraderModels.get(position));
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
                    popupMenu(adapterPosition, view, filteredTraderModels.get(adapterPosition));

                }
            });

            getTraders();

        } else {
            emptyState(false, "You are not connected to the internet ...", empty_layout, null, emptyTxt);
        }

    }

    private JSONObject getSearchObject() {

        FetchTraderModel f = new FetchTraderModel();


        f.setArchived(0);
        f.setDeleted(0);
        f.setAll(1);
        f.setDummy(0);
        try {
            return new JSONObject(gson.toJson(f));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;


    }

    private void populateTraders() {
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (filteredTraderModels.size() < 1) {
            snack("No data");
        }

        listAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(listAdapter);
        emptyState(listAdapter.getItemCount() > 0, "We couldn't find any traders records", empty_layout, null, emptyTxt);

    }


    private void emptyState(boolean listHasData, String text, LinearLayout empty_layout, ImageView empty_image, TextView emptyTxt) {
        if (listHasData) {
            empty_layout.setVisibility(View.GONE);
        } else {
            empty_layout.setVisibility(View.VISIBLE);
            if (text != null) {
                emptyTxt.setText(text);
            }
        }
    }


    private void filterTraders(int a) {
        //spinner.setItems("All","Active","Deleted","Archived","Dummy");

        int alll = 0, deleted = 0, archived = 0, all = 0, dumy = 0;


        if (a == 0) {
            alll = 1;
            deleted = 0;
            archived = 0;
            dumy = 0;

        } else if (a == 1) {
            alll = 0;
            deleted = 0;
            archived = 0;
            dumy = 0;

        } else if (a == 2) {
            alll = 0;
            deleted = 1;
            archived = 0;
            dumy = 0;

        } else if (a == 3) {
            alll = 0;
            deleted = 0;
            archived = 1;
            dumy = 0;

        } else if (a == 4) {
            alll = 0;
            deleted = 0;
            archived = 0;
            dumy = 1;

        }


        filteredTraderModels.clear();
        for (TraderModel traderModel : traderModels) {
            if (traderModel.getCode().toLowerCase().contains(filterText) ||
                    traderModel.getMobile().toLowerCase().contains(filterText) ||
                    traderModel.getNames().toLowerCase().contains(filterText)) {

                if (alll == 1) {
                    filteredTraderModels.add(traderModel);

                } else {
                    if (traderModel.getDeleted() == deleted && traderModel.getArchived() == archived && traderModel.getDummy() == dumy) {
                        filteredTraderModels.add(traderModel);
                    }
                }

//
            }
//            }
        }
        listAdapter.notifyDataSetChanged();


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

    private void popupMenu(int pos, View view, TraderModel traderModel) {
        PopupMenu popupMenu = new PopupMenu(Objects.requireNonNull(getContext()), view);
        popupMenu.inflate(R.menu.entity_list_menu);

        if (traderModel.getDeleted() == 1) {
            popupMenu.getMenu().getItem(0).setVisible(false);
        }
        if (traderModel.getArchived() == 1) {
            isArchived = true;
            popupMenu.getMenu().getItem(1).setTitle("Un-Archive");
        }
        if (traderModel.getDummy() == 1) {
            isDummy = true;
            popupMenu.getMenu().getItem(2).setTitle("Remove from dummy");
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.delete:


                    traderModel.setStatus("Deleted");
                    traderModel.setDeleted(1);
                    update(traderModel);


                    break;

                case R.id.archive:

                    if (isArchived) {
                        traderModel.setStatus("Active");
                        traderModel.setArchived(0);
                    } else {
                        traderModel.setStatus("Archived");
                        traderModel.setArchived(1);
                    }
                    update(traderModel);

                    break;

                case R.id.dummy:

                    if (isDummy) {
                        traderModel.setStatus("Active");
                        traderModel.setDummy(0);
                    } else {
                        traderModel.setStatus("Dummy");
                        traderModel.setDummy(1);
                    }

                    update(traderModel);
                    break;
                case R.id.edit:

                    editTrader(traderModel);
                    break;
                default:
            }
            return false;
        });
        popupMenu.show();


    }


    private void update(TraderModel traderModel) {
        listAdapter.notifyDataSetChanged();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(traderModel));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mViewModel.updateTrader(jsonObject, true).observe(this, new Observer<ResponseModel>() {
            @Override
            public void onChanged(@Nullable ResponseModel responseModel) {
                mViewModel.refresh(getSearchObject(), true);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (context == null) {
            context = getActivity();
        }
        initWIdgets();
        traderModels = new LinkedList<>();
        filteredTraderModels = new LinkedList<>();
        initConnectivityListener();
        initTradersList();

    }

    void getTraders() {
        if (avi != null) {
            startAnim();
        }
        if (mViewModel != null) {


            mViewModel.getTraderModels(getSearchObject(), true).observe(this, responseModel -> {

                Gson gson = new Gson();
                if (avi != null) {
                    stopAnim();
                }


                if (responseModel.getResultCode() == 1 && responseModel.getData() != null) {
                    JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                    Type listType = new TypeToken<LinkedList<TraderModel>>() {
                    }.getType();
                    traderModels = gson.fromJson(jsonArray, listType);
                    filterTraders(spinner.getSelectedIndex());
                    populateTraders();
                } else if (responseModel.getResultCode() == 2) {
                    traderModels.clear();
                    populateTraders();
                } else {


                    traderModels.clear();
                    populateTraders();
                    snack(responseModel.getResultDescription());
                }

            });


        }
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

    public void createTrader() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_add_trader, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(context));
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
        alertDialogBuilderUserInput.setTitle("Trader");


        avi = mView.findViewById(R.id.avi);


        TextInputEditText edtNames, edtMobile;
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Save", (dialogBox, id) -> {
                    // ToDo get user input here


                })

                .setNegativeButton("Dismiss",
                        (dialogBox, id) -> dialogBox.cancel());

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.show();

        Button theButton = alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CustomListener(alertDialogAndroid));


    }

    private class CustomListener implements View.OnClickListener {
        AlertDialog dialog;
        boolean isDummy = false;
        //int type;
        RequestDataCallback requestDataCallback;

        public CustomListener(AlertDialog alertDialogAndroid) {
            dialog = alertDialogAndroid;

        }

        @Override
        public void onClick(View v) {
            TextInputEditText name, phone, bussinessname;
            CheckBox chkDummy;
            Spinner spinnerPayment;
            spinnerPayment = dialog.findViewById(R.id.spinner);
            bussinessname = dialog.findViewById(R.id.edt_traders_business_name);


            name = dialog.findViewById(R.id.edt_traders_names);
            phone = dialog.findViewById(R.id.edt_traders_phone);
            chkDummy = dialog.findViewById(R.id.chk_dummy);


            if (name.getText().toString().isEmpty()) {
                name.setError("Required");
                name.requestFocus();
                stopAnim();
                return;
            }


            if (phone.getText().toString().isEmpty()) {
                phone.setError("Required");
                phone.requestFocus();
                stopAnim();
                return;
            }

            if (!LoginController.isValidPhoneNumber(phone.getText().toString())) {
                stopAnim();
                snack("Invalid phone number ");
                phone.requestFocus();
                phone.setError("Invalid Phone number");
                return;
            }
            String defaultPayment = "";
            String bussines = "";


            if (!bussinessname.getText().toString().isEmpty()) {
                bussines = bussinessname.getText().toString();
            }

            if (spinnerPayment != null && spinnerPayment.getSelectedItemPosition() == 0) {

                snack("Invalid payment option ");
                spinnerPayment.requestFocus();

                stopAnim();
                //phone.setError("Invalid Phone number");
            }


            if (chkDummy.isChecked()) {
                isDummy = true;
            }


            //  AdminPrefs adminPrefs = new AdminPrefs(context);
            TraderModel traderModel = new TraderModel();
            //if(type==ISCREATE) {
            traderModel.setId(0);
            traderModel.setCode("" + new GeneralUtills(context).getRandon(9000, 1000));
            traderModel.setEntity("Admin");
            traderModel.setEntityname("LishaBora");
            traderModel.setBusinessname(bussines);
            traderModel.setDefaultpaymenttype(spinnerPayment.getSelectedItem().toString());


            traderModel.setEntitycode(new PrefrenceManager(context).getAdmin().getCode());

            traderModel.setTransactioncode(DateTimeUtils.Companion.getNow());
            traderModel.setNames(name.getText().toString());
            traderModel.setMobile(phone.getText().toString());
            traderModel.setPassword("password");
            traderModel.setBalance("0");
            traderModel.setApikey("");
            traderModel.setFirebasetoken("");
            traderModel.setStatus("Active");
            traderModel.setTransactiontime(DateTimeUtils.Companion.getNow());
            traderModel.setSynctime(DateTimeUtils.Companion.getNow());
            traderModel.setTransactedby(new PrefrenceManager(context).getAdmin().getApikey());


            traderModel.setArchived(0);
            traderModel.setDeleted(0);
            traderModel.setSynced(0);
            traderModel.setDummy(0);


            traderModel.setIsdeleted(false);
            traderModel.setIsarchived(false);
            traderModel.setIsdummy(isDummy);


            startAnim();

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(gson.toJson(traderModel));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mViewModel.createTrader(jsonObject, true).observe(AdminsTradersListFragment.this, new Observer<ResponseModel>() {
                @Override
                public void onChanged(@Nullable ResponseModel responseModel) {
                    stopAnim();
                    snack(responseModel.getResultDescription());
                    if (responseModel.getResultCode() == 1) {
                        dialog.dismiss();
                        mViewModel.refresh(getSearchObject(), true);
                    }
                }
            });


        }

    }

    public void editTrader(TraderModel traderModel) {
        if (context != null) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
            View mView = layoutInflaterAndroid.inflate(R.layout.dialog_add_trader, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(context));
            alertDialogBuilderUserInput.setView(mView);
            alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
            alertDialogBuilderUserInput.setTitle("Trader");


            avi = mView.findViewById(R.id.avi);


            TextInputEditText edtNames, edtMobile, edtBussinesName;
            Spinner spinner;
            spinner = mView.findViewById(R.id.spinner);
            edtMobile = mView.findViewById(R.id.edt_traders_phone);
            edtNames = mView.findViewById(R.id.edt_traders_names);
            edtBussinesName = mView.findViewById(R.id.edt_traders_business_name);

            edtBussinesName.setText(traderModel.getBusinessname());

            edtMobile.setText(traderModel.getMobile());
            edtNames.setText(traderModel.getNames());

            CheckBox chk = mView.findViewById(R.id.chk_dummy);
            chk.setVisibility(View.GONE);


            switch (traderModel.getDefaultpaymenttype().toLowerCase()) {
                case "mpesa":
                    spinner.setSelection(2, true);
                    break;

                case "cash":
                    spinner.setSelection(1, true);

                    break;

                case "bank":
                    spinner.setSelection(3, true);

                    break;

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
            theButton.setOnClickListener(new EditCustomListener(alertDialogAndroid, traderModel));

        }

    }


    private class EditCustomListener implements View.OnClickListener {
        AlertDialog dialog;
        boolean isDummy = false;
        TraderModel traderModel;
        //int type;
        RequestDataCallback requestDataCallback;

        public EditCustomListener(AlertDialog alertDialogAndroid, TraderModel traderModel) {
            dialog = alertDialogAndroid;
            this.traderModel = traderModel;

        }

        @Override
        public void onClick(View v) {
            TextInputEditText name, phone, bussinessname;
            Spinner spinnerPayment;
            CheckBox chkDummy;

            spinnerPayment = dialog.findViewById(R.id.spinner);
            bussinessname = dialog.findViewById(R.id.edt_traders_business_name);

            name = dialog.findViewById(R.id.edt_traders_names);
            phone = dialog.findViewById(R.id.edt_traders_phone);
            chkDummy = dialog.findViewById(R.id.chk_dummy);

            if (name.getText().toString().isEmpty()) {
                name.setError("Required");
                name.requestFocus();
                stopAnim();
                return;
            }


            if (phone.getText().toString().isEmpty()) {
                phone.setError("Required");
                phone.requestFocus();
                stopAnim();
                return;
            }
            String bussines = "";


            if (!bussinessname.getText().toString().isEmpty()) {
                bussines = bussinessname.getText().toString();
            }

            if (!LoginController.isValidPhoneNumber(phone.getText().toString())) {
                snack("Invalid phone number ");
                phone.requestFocus();
                stopAnim();
                phone.setError("Invalid Phone number");
            }

            String defaultPayment = "";

            if (spinnerPayment != null && spinnerPayment.getSelectedItemPosition() == 0) {

                snack("Invalid payment option ");
                spinnerPayment.requestFocus();

                stopAnim();
                //phone.setError("Invalid Phone number");
            }


            if (chkDummy.isChecked()) {
                isDummy = true;
            }


            traderModel.setDefaultpaymenttype(spinnerPayment.getSelectedItem().toString());
            traderModel.setNames(name.getText().toString());
            traderModel.setMobile(phone.getText().toString());
            traderModel.setTransactiontime(DateTimeUtils.Companion.getNowslong());
            traderModel.setBusinessname(bussines);


            startAnim();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(gson.toJson(traderModel));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mViewModel.updateTrader(jsonObject, true).observe(AdminsTradersListFragment.this, new Observer<ResponseModel>() {
                @Override
                public void onChanged(@Nullable ResponseModel responseModel) {
                    stopAnim();
                    snack(responseModel.getResultDescription());
                    if (responseModel.getResultCode() == 1) {
                        dialog.dismiss();
                        mViewModel.refresh(getSearchObject(), true);
                    }
                }
            });


        }

    }


}
