package com.dev.lishabora.Views.Trader.Fragments;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishabora.Adapters.RoutesAdapter;
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
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class TraderRoutesInfoFragment extends Fragment implements BlockingStep {
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

    public void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {

            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void getRoutes() {
        avi.smoothToShow();
        avi.setVisibility(View.VISIBLE);

        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);

        }
        mViewModel.getRoutes(false).observe(TraderRoutesInfoFragment.this, routesModels -> {
            avi.smoothToHide();
            update(routesModels);
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trader_routes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        initViews();
        hideKeyboardFrom(Objects.requireNonNull(getContext()), getView());


    }

    void initViews() {
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
        avi = mView.findViewById(R.id.avi);
        alertDialogBuilderUserInput
                .setCancelable(false);
//
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
//    private void editRoute(RoutesModel routesModel, int pos) {
//        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
//        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_add_route, null);
//        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
//        alertDialogBuilderUserInput.setView(mView);
////        alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
////        alertDialogBuilderUserInput.setTitle("Route");
//
//
//        avi = mView.findViewById(R.id.avi);
//
//        TextInputEditText name;
//        CheckBox chkDummy;
//
//        name = mView.findViewById(R.id.edt_rout_names);
//        chkDummy = mView.findViewById(R.id.chk_dummy);
//        chkDummy.setVisibility(View.GONE);
//
//        name.setText(routesModel.getRoute());
//        try {
//            name.setSelection(routesModel.getRoute().length());
//        } catch (Exception nm) {
//            nm.printStackTrace();
//        }
//
//
//        alertDialogBuilderUserInput
//                .setCancelable(false);
////                .setPositiveButton("Save", (dialogBox, id) -> {
////                    // ToDo get user input here
////
////
////                })
////
////                .setNegativeButton("Dismiss",
////                        (dialogBox, id) -> dialogBox.cancel());
//
//        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
//        alertDialogAndroid.setCancelable(false);
//        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//
//        alertDialogAndroid.show();
//
////        Button theButton = alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE);
////        theButton.setOnClickListener(new EditCustomListener(alertDialogAndroid,routesModel));
//
//
//        MaterialButton btnPositive, btnNegative, btnNeutral;
//        TextView txtTitle;
//        LinearLayout lTitle;
//        ImageView imgIcon;
//        btnPositive = mView.findViewById(R.id.btn_positive);
//        btnNegative = mView.findViewById(R.id.btn_negative);
//        btnNeutral = mView.findViewById(R.id.btn_neutral);
//        txtTitle = mView.findViewById(R.id.txt_title);
//        lTitle = mView.findViewById(R.id.linear_title);
//        imgIcon = mView.findViewById(R.id.img_icon);
//
//
//        btnNeutral.setVisibility(View.VISIBLE);
//        btnNeutral.setText("Delete");
//        btnNeutral.setBackgroundColor(getContext().getResources().getColor(R.color.red));
//        lTitle.setVisibility(View.GONE);
//        txtTitle.setVisibility(View.VISIBLE);
//        imgIcon.setVisibility(View.VISIBLE);
//        imgIcon.setImageResource(R.drawable.ic_add_black_24dp);
//        txtTitle.setText("Route");
//
//        btnPositive.setOnClickListener(new FragmentRoutes.EditCustomListener(alertDialogAndroid, routesModel));
//        btnNeutral.setOnClickListener(view -> {
//
//            FarmerRepo f = new FarmerRepo(Application.context);
//            int count = f.getNoOfRows(routesModel.getCode());
//            if (routesModel.getFarmers() < 1 && count < 1) {
//                mViewModel.deleteRoute(routesModel, null, false).observe(FragmentRoutes.this, responseModel -> {
//                    avi.smoothToHide();
//                    MyToast.toast(responseModel.getResultDescription(), getContext(), R.drawable.ic_launcher, Toast.LENGTH_LONG);
//                    alertDialogAndroid.dismiss();
//                });
//            } else {
//                MyToast.toast("Route has farmers,\n CANNOT BE DELETED", getContext(), R.drawable.ic_error_outline_black_24dp, Toast.LENGTH_LONG);
//            }
//        });
//        btnNegative.setOnClickListener(view -> alertDialogAndroid.dismiss());
//
//
//    }

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
                // popupMenu(adapterPosition, view, filteredRoutesModels.get(adapterPosition));

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
                        mViewModel.deleteRoute(routesModel, null, false).observe(TraderRoutesInfoFragment.this, responseModel -> {
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

        btnPositive.setOnClickListener(new TraderRoutesInfoFragment.EditCustomListener(alertDialogAndroid, routesModel));
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


    private void populate() {
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        listAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(listAdapter);

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

    private void initData() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

        callback.complete();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

        callback.goToPrevStep();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {
        Objects.requireNonNull(getActivity()).setTitle("Routes");

        initData();
        if (routesModels == null) {
            routesModels = new LinkedList<>();
        }
        if (filteredRoutesModels == null) {
            filteredRoutesModels = new LinkedList<>();
        }

        initList();
        populate();
        getRoutes();


    }


    @Override
    public void onError(@NonNull VerificationError error) {


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
            mViewModel.createRoute(routesModel, jsonObject, false).observe(TraderRoutesInfoFragment.this, new Observer<ResponseModel>() {
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
            mViewModel.updateRoute(routesModel, jsonObject, false).observe(TraderRoutesInfoFragment.this, new Observer<ResponseModel>() {
                @Override
                public void onChanged(@Nullable ResponseModel responseModel) {
                    avi.smoothToHide();
                    dialog.dismiss();
                }
            });


        }

    }
}
