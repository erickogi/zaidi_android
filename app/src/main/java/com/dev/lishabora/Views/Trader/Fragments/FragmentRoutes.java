package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.FloatingActionButton;
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
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishabora.Adapters.RoutesAdapter;
import com.dev.lishabora.Models.RPFSearchModel;
import com.dev.lishabora.Models.ResponseModel;
import com.dev.lishabora.Models.RoutesModel;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class FragmentRoutes extends Fragment {
    LinkedList<RoutesModel> filteredRoutesModels;
    Gson gson = new Gson();
    RoutesAdapter listAdapter;
    FloatingActionButton fab;
    private View view;
    private TraderViewModel mViewModel;
    private LinkedList<RoutesModel> routesModels;
    private AVLoadingIndicatorView avi;
    private String filterText = "";
    private SearchView searchView;
    private LinearLayout empty_layout;
    private TextView emptyTxt, txt_network_state;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout linearLayoutEmpty;

    //    private void populateTraders() {
//        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(mStaggeredLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//
//
//        listAdapter.notifyDataSetChanged();
//        recyclerView.setAdapter(listAdapter);
//
//    }
    @Override
    public void onStart() {
        super.onStart();
        if (routesModels == null) {
            routesModels = new LinkedList<>();
        }
        if (filteredRoutesModels == null) {
            filteredRoutesModels = new LinkedList<>();
        }

        initList();
        populateTraders();
        getRoutes();


        try {
            Objects.requireNonNull(getActivity()).setTitle("Routes");
        } catch (Exception nm) {
            nm.printStackTrace();
        }

    }

    private void getRoutes() {
        avi.smoothToShow();
        avi.setVisibility(View.VISIBLE);

        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);

        }
        mViewModel.getRoutes(new PrefrenceManager(getContext()).isRoutesListFirstTime()).observe(FragmentRoutes.this, new Observer<List<RoutesModel>>() {
            @Override
            public void onChanged(@Nullable List<RoutesModel> routesModels) {
                avi.smoothToHide();
                update(routesModels);
            }
        });
    }

    private JSONObject getTraderRoutesObject() {


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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trader_routes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view
            , @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);


        recyclerView = view.findViewById(R.id.recyclerView);
        empty_layout = view.findViewById(R.id.empty_layout);
        emptyTxt = view.findViewById(R.id.empty_text);
        avi = view.findViewById(R.id.avi);
        txt_network_state = view.findViewById(R.id.txt_network_state);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRoute();
            }
        });


    }


    public void createRoute() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_add_route, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        alertDialogBuilderUserInput.setView(mView);
//        alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
//        alertDialogBuilderUserInput.setTitle("Route");


        avi = mView.findViewById(R.id.avi);


        alertDialogBuilderUserInput
                .setCancelable(false);
//                .setPositiveButton("Save", (dialogBox, id) -> {
//                    // ToDo get user input here
//
//
//                })
//
//                .setNegativeButton("Dismiss",
//                        (dialogBox, id) -> dialogBox.cancel());

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        alertDialogAndroid.show();

//        Button theButton = alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE);
//        theButton.setOnClickListener(new CustomListener(alertDialogAndroid));


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
        lTitle.setVisibility(View.GONE);
        txtTitle.setVisibility(View.VISIBLE);
        imgIcon.setVisibility(View.VISIBLE);
        imgIcon.setImageResource(R.drawable.ic_add_black_24dp);
        txtTitle.setText("Route");

        btnPositive.setOnClickListener(new CustomListener(alertDialogAndroid));
        btnNeutral.setOnClickListener(view -> {

        });
        btnNegative.setOnClickListener(view -> alertDialogAndroid.dismiss());

    }

    private void populateTraders() {
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        listAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(listAdapter);
        //emptyState(listAdapter.getItemCount() > 0, "We couldn't find any farmers records", empty_layout, null, emptyTxt);

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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filterText = s;
                filterRoutes();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterText = s;
                filterRoutes();

                return true;
            }
        });

    }

    public void initList() {
        recyclerView = view.findViewById(R.id.recyclerView);
        listAdapter = new RoutesAdapter(getActivity(), filteredRoutesModels, new OnclickRecyclerListener() {
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

                popupMenu(adapterPosition, view, filteredRoutesModels.get(adapterPosition));
            }
        });


    }

    private void popupMenu(int pos, View view, RoutesModel routesModel) {
        PopupMenu popupMenu = new PopupMenu(Objects.requireNonNull(getContext()), view);
        popupMenu.inflate(R.menu.route_list_menu);


        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.delete:

                    if (mViewModel.noOfFarmersPerRoute(routesModel.getCode()) > 0) {
                        MyToast.toast("A route with existing farmers cannot be deleted", getContext(), R.drawable.ic_launcher, Toast.LENGTH_LONG);

                    } else {

                        routesModel.setStatus(0);

                        avi.smoothToShow();
                        mViewModel.deleteRoute(routesModel, null, false).observe(FragmentRoutes.this, responseModel -> {
                            avi.smoothToHide();
                            MyToast.toast(responseModel.getResultDescription(), getContext(), R.drawable.ic_launcher, Toast.LENGTH_LONG);
                        });
                    }

                    break;

                case R.id.archive:


                    break;

                case R.id.dummy:


                    // update(famerModel);
                    break;
                case R.id.edit:


                    Log.d("farmerdialog", "edit clicked");
                    editRoute(routesModel, pos);
                    break;

                default:
            }
            return false;
        });
        popupMenu.show();
    }

    private void editRoute(RoutesModel routesModel, int pos) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_add_route, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        alertDialogBuilderUserInput.setView(mView);
//        alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
//        alertDialogBuilderUserInput.setTitle("Route");


        avi = mView.findViewById(R.id.avi);

        TextInputEditText name;
        CheckBox chkDummy;

        name = mView.findViewById(R.id.edt_rout_names);
        chkDummy = mView.findViewById(R.id.chk_dummy);
        chkDummy.setVisibility(View.GONE);

        name.setText(routesModel.getRoute());
        try {
            name.setSelection(routesModel.getRoute().length());
        } catch (Exception nm) {
            nm.printStackTrace();
        }


        alertDialogBuilderUserInput
                .setCancelable(false);
//                .setPositiveButton("Save", (dialogBox, id) -> {
//                    // ToDo get user input here
//
//
//                })
//
//                .setNegativeButton("Dismiss",
//                        (dialogBox, id) -> dialogBox.cancel());

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        alertDialogAndroid.show();

//        Button theButton = alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE);
//        theButton.setOnClickListener(new EditCustomListener(alertDialogAndroid,routesModel));


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
        lTitle.setVisibility(View.GONE);
        txtTitle.setVisibility(View.VISIBLE);
        imgIcon.setVisibility(View.VISIBLE);
        imgIcon.setImageResource(R.drawable.ic_add_black_24dp);
        txtTitle.setText("Route");

        btnPositive.setOnClickListener(new EditCustomListener(alertDialogAndroid, routesModel));
        btnNeutral.setOnClickListener(view -> {

        });
        btnNegative.setOnClickListener(view -> alertDialogAndroid.dismiss());


    }


    public void update(List<RoutesModel> routesModels) {

        Log.d("ReTr", "routes started");

        if (this.routesModels != null && listAdapter != null) {
            Log.d("ReTr", "routes started");

            this.routesModels.clear();
            this.routesModels.addAll(routesModels);
            filterRoutes();
            //listAdapter.notifyDataSetChanged();


        } else {
            filteredRoutesModels.clear();
            routesModels.clear();


        }
    }

    private void filterRoutes() {
        filteredRoutesModels.clear();
        if (routesModels != null && routesModels.size() > 0) {
            for (RoutesModel routesModel : routesModels) {
                if (routesModel.getCode().toLowerCase().contains(filterText) ||
                        routesModel.getRoute().toLowerCase().contains(filterText) ||
                        routesModel.getEntityname().toLowerCase().contains(filterText)) {
                    filteredRoutesModels.add(routesModel);
                }

            }
            listAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(listAdapter.getItemCount() - 1);
        } else {
            listAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

    private class CustomListener implements View.OnClickListener {
        AlertDialog dialog;
        boolean isActive = true;
        int isAct = 1;

        public CustomListener(AlertDialog alertDialogAndroid) {
            dialog = alertDialogAndroid;

        }

        @Override
        public void onClick(View v) {
            TextInputEditText name;
            CheckBox chkDummy;

            name = dialog.findViewById(R.id.edt_rout_names);
            chkDummy = dialog.findViewById(R.id.chk_dummy);


            if (name.getText().toString().isEmpty()) {
                name.setError("Required");
                name.requestFocus();
                avi.smoothToHide();
                return;
            }


            if (chkDummy != null && !chkDummy.isChecked()) {
                isActive = false;
                isAct = 2;
            }
            PrefrenceManager prefrenceManager = new PrefrenceManager(getContext());


            RoutesModel routesModel = new RoutesModel();
            routesModel.setCode(new GeneralUtills(getContext()).getRandon(9999, 1000) + "");
            routesModel.setEntity("Trader");
            routesModel.setEntitycode(prefrenceManager.getTraderModel().getCode());
            routesModel.setEntityname(prefrenceManager.getTraderModel().getNames());
            routesModel.setFarmers(0);
            routesModel.setStatus(isAct);
            routesModel.setRoute(name.getText().toString());
            routesModel.setSynctime(DateTimeUtils.Companion.getNow());
            routesModel.setTransactedby(prefrenceManager.getTraderModel().getApikey());
            routesModel.setTransactioncode(DateTimeUtils.Companion.getNow() + routesModel.getEntitycode());
            routesModel.setTransactiontime(DateTimeUtils.Companion.getNow());

            avi.smoothToShow();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(gson.toJson(routesModel));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mViewModel.createRoute(routesModel, jsonObject, false).observe(FragmentRoutes.this, new Observer<ResponseModel>() {
                @Override
                public void onChanged(@Nullable ResponseModel responseModel) {
                    avi.smoothToHide();
                    dialog.dismiss();
                }
            });


        }

    }

    private class EditCustomListener implements View.OnClickListener {
        AlertDialog dialog;
        boolean isActive = true;
        int isAct = 1;
        RoutesModel routesModel;

        public EditCustomListener(AlertDialog alertDialogAndroid, RoutesModel routesModel) {
            dialog = alertDialogAndroid;
            this.routesModel = routesModel;

        }

        @Override
        public void onClick(View v) {
            TextInputEditText name;
            CheckBox chkDummy;

            name = dialog.findViewById(R.id.edt_rout_names);
            chkDummy = dialog.findViewById(R.id.chk_dummy);


            if (name.getText().toString().isEmpty()) {
                name.setError("Required");
                name.requestFocus();
                avi.smoothToHide();
                return;
            }


            if (chkDummy != null && !chkDummy.isChecked()) {
                isActive = false;
                isAct = 2;
            }
            PrefrenceManager prefrenceManager = new PrefrenceManager(getContext());

            routesModel.setRoute(name.getText().toString());

            avi.smoothToShow();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(gson.toJson(routesModel));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mViewModel.updateRoute(routesModel, jsonObject, false).observe(FragmentRoutes.this, new Observer<ResponseModel>() {
                @Override
                public void onChanged(@Nullable ResponseModel responseModel) {
                    avi.smoothToHide();
                    dialog.dismiss();
                }
            });


        }

    }

}
