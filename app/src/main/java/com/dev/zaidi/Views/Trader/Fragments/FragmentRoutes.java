package com.dev.zaidi.Views.Trader.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Point;
import android.graphics.Rect;
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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.zaidi.Adapters.RoutesAdapter;
import com.dev.zaidi.Application;
import com.dev.zaidi.Models.FamerModel;
import com.dev.zaidi.Models.RPFSearchModel;
import com.dev.zaidi.Models.ResponseModel;
import com.dev.zaidi.Models.RoutesModel;
import com.dev.zaidi.R;
import com.dev.zaidi.Repos.Trader.FarmerRepo;
import com.dev.zaidi.Utils.DateTimeUtils;
import com.dev.zaidi.Utils.GeneralUtills;
import com.dev.zaidi.Utils.Logs;
import com.dev.zaidi.Utils.MaterialIntro;
import com.dev.zaidi.Utils.MyToast;
import com.dev.zaidi.Utils.OnclickRecyclerListener;
import com.dev.zaidi.Utils.PrefrenceManager;
import com.dev.zaidi.ViewModels.Trader.TraderViewModel;
import com.getkeepsafe.taptargetview.TapTarget;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class FragmentRoutes extends Fragment {
    LinkedList<RoutesModel> filteredRoutesModels;
    private LinkedList<RoutesModel> routesModels;

    Gson gson = new Gson();
    RoutesAdapter listAdapter;
    FloatingActionButton fab;
    private View view;
    private TraderViewModel mViewModel;
    private AVLoadingIndicatorView avi;
    private String filterText = "";
    private SearchView searchView;
    private ImageButton helpView;

    private LinearLayout empty_layout;
    private TextView emptyTxt, txt_network_state;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout linearLayoutEmpty;

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
        mViewModel.getRoutes(false).observe(FragmentRoutes.this, routesModels -> {
            avi.smoothToHide();
            update(routesModels);
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



    void showIntro() {

        // We load a drawable and create a location to show a tap target here
        // We need the display to get the width and height at this point in time
        final Display display = getActivity().getWindowManager().getDefaultDisplay();

//        final Drawable droid = ContextCompat.getDrawable(getContext(), R.drawable.ic_launcher);
//        // Tell our droid buddy where we want him to appear
//        final Rect droidTarget = new Rect(0, 0, droid.getIntrinsicWidth() * 2, droid.getIntrinsicHeight() * 2);
//        // Using deprecated methods makes you look way cool
//        droidTarget.offset(display.getWidth() / 2, display.getHeight() / 2);
//

        int canvasW = display.getWidth();
        int canvasH = display.getHeight();
        Point centerOfCanvas = new Point(canvasW / 2, canvasH / 2);
        int rectW = 10;
        int rectH = 10;
        int left = centerOfCanvas.x - (rectW / 2);
        int top = centerOfCanvas.y - (rectH / 2);
        int right = centerOfCanvas.x + (rectW / 2);
        int bottom = centerOfCanvas.y + (rectH / 2);
        Rect rect = new Rect(left, top, right, bottom);

        List<TapTarget> targets = new ArrayList<>();
        targets.add(TapTarget.forView(fab, "Click the + button to add a new route ", getContext().getResources().getString(R.string.dismiss_intro)).cancelable(false).id(9).transparentTarget(true));
        targets.add(TapTarget.forBounds(rect, "Long click on a route to edit or delete it. \n *A route with existing farmers cannot be deleted*", getContext().getResources().getString(R.string.dismiss_intro)).cancelable(false).id(10).transparentTarget(true));
        targets.add(TapTarget.forView(helpView, "Click here to see this introduction again", getContext().getResources().getString(R.string.dismiss_intro)).cancelable(false).id(11).transparentTarget(true));
        targets.add(TapTarget.forView(searchView, "Search for a route by its name", getContext().getResources().getString(R.string.dismiss_intro)).cancelable(false).id(12).transparentTarget(true));

        MaterialIntro.Companion.showIntroSequence(getActivity(), targets);
        new PrefrenceManager(getContext()).setRoutesFragmentIntroShown(true);

    }


    public void createRoute() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_add_route, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        alertDialogBuilderUserInput.setView(mView);


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
        lTitle.setVisibility(View.VISIBLE);
        txtTitle.setVisibility(View.VISIBLE);
        imgIcon.setVisibility(View.VISIBLE);
        imgIcon.setImageResource(R.drawable.ic_add_black_24dp);
        txtTitle.setText("Add New Route");

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


    public void initList() {
        recyclerView = view.findViewById(R.id.recyclerView);
        listAdapter = new RoutesAdapter(getActivity(), filteredRoutesModels, new OnclickRecyclerListener() {
            @Override
            public void onMenuItem(int position, int menuItem) {

            }

            @Override
            public void onSwipe(int adapterPosition, int direction) {

            }

            @Override
            public void onClickListener(int position) {
                editRoute(filteredRoutesModels.get(position), position);

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
                        MyToast.toast("A route with existing farmers cannot be deleted", getContext());

                    } else {

                        routesModel.setStatus(0);

                        avi.smoothToShow();
                        mViewModel.deleteRoute(routesModel, null, false).observe(FragmentRoutes.this, responseModel -> {
                            avi.smoothToHide();
                            MyToast.toast(responseModel.getResultDescription(), getContext());
                        });
                    }

                    break;

                case R.id.archive:


                    break;

                case R.id.dummy:


                    // update(famerModel);
                    break;
                case R.id.edit:


                    Logs.Companion.d("farmerdialog", "edit clicked");
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


        btnNeutral.setVisibility(View.VISIBLE);
        btnNeutral.setText("Delete");
        btnNeutral.setTextColor(getContext().getResources().getColor(R.color.red));

        lTitle.setVisibility(View.VISIBLE);
        txtTitle.setVisibility(View.VISIBLE);
        imgIcon.setVisibility(View.VISIBLE);
        imgIcon.setImageResource(R.drawable.ic_edit_24dp);
        txtTitle.setText("Edit Route");
        txtTitle.setTextColor(getContext().getResources().getColor(R.color.black));

        btnPositive.setOnClickListener(new EditCustomListener(alertDialogAndroid, routesModel));
        btnNeutral.setOnClickListener(view -> {

            FarmerRepo f = new FarmerRepo(Application.context);
            int count = f.getNoOfRows(routesModel.getCode());
            if (routesModel.getFarmers() < 1 && count < 1) {
                mViewModel.deleteRoute(routesModel, null, false).observe(FragmentRoutes.this, responseModel -> {
                    avi.smoothToHide();
                    MyToast.toast(responseModel.getResultDescription(), getContext());
                    alertDialogAndroid.dismiss();
                });
            } else {
                MyToast.errorToast("Route has farmers,\n CANNOT BE DELETED", getContext());
            }
        });
        btnNegative.setOnClickListener(view -> alertDialogAndroid.dismiss());


    }


    public void update(List<RoutesModel> routesModels) {

        Logs.Companion.d("ReTr", "routes started");

        if (this.routesModels != null && listAdapter != null) {
            Logs.Companion.d("ReTr", "routes started");

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);

        //inflater.inflate(R.menu.menu_main, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);

        searchView = (SearchView) mSearch.getActionView();

        searchView.setVisibility(View.GONE);

        MenuItem mHelp = menu.findItem(R.id.action_help);

        helpView = (ImageButton) mHelp.getActionView();
        helpView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_help));
        helpView.setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
        if (!new PrefrenceManager(getContext()).isRoutesFragmentIntroShown()) {
            showIntro();
        }
        helpView.setOnClickListener(v -> showIntro());

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

                    mViewModel.getAllByRouteCode(routesModel.getCode()).observe(FragmentRoutes.this, new Observer<List<FamerModel>>() {
                        @Override
                        public void onChanged(@Nullable List<FamerModel> famerModels) {
                            if (famerModels != null) {
                                for (FamerModel famerModel : famerModels) {
                                    famerModel.setRoutename(routesModel.getRoute());
                                    mViewModel.updateFarmer(famerModel, false, true);
                                }
                                dialog.dismiss();

                            } else {
                                dialog.dismiss();

                            }
                        }
                    });
                }
            });

            // mViewModel.updateFarmerRoute(routesModel.getCode())


        }

    }

}
