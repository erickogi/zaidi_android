package com.dev.lishabora.Views.Trader.Fragments;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.RoutesModel;
import com.dev.lishabora.Models.UnitsModel;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.Trader.FarmerConst;
import com.dev.lishaboramobile.R;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.List;
import java.util.Objects;

public class FragmentRoutesUnitDetails extends Fragment implements BlockingStep {

    private View view;
    private TextInputEditText edtRouteName, edtRouteCode, edtUnitName, edtUnitPrice, edtUnitMeasurement;
    private MaterialSpinner spinnerRoute, spinnerUnit;
    private TraderViewModel mViewModel;
    private PrefrenceManager prefrenceManager;
    FamerModel fm = new FamerModel();

    private List<RoutesModel> routesModels;
    private List<UnitsModel> unitsModels;

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_createfarmerroute, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        prefrenceManager = new PrefrenceManager(getContext());
        mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);
        hideKeyboardFrom(Objects.requireNonNull(getContext()), view);

    }


    UnitsModel unitsModel = null;//= unitsModels.get(position);

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

        callback.goToPrevStep();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if (!TextUtils.isEmpty(edtRouteName.getText()) && !TextUtils.isEmpty(edtUnitName.getText())) {
            return null;
        }
        return new VerificationError("Routes & Unit required");

    }

    @Override
    public void onSelected() {

        initViews();
        hideKeyboardFrom(getContext(), view);
    }


    @Override
    public void onError(@NonNull VerificationError error) {

    }

    private boolean verifySpinner(MaterialSpinner spinner) {
        //return spinner.getSelectedIndex() != 0;
        return spinner.isSelected();
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        FarmerConst.getFamerModel().setRoute(edtRouteName.getText().toString());
        FarmerConst.getFamerModel().setRoutename(edtRouteName.getText().toString());
        FarmerConst.getFamerModel().setRoutecode(edtRouteCode.getText().toString());
        FarmerConst.getFamerModel().setUnitcapacity(edtUnitMeasurement.getText().toString());
        FarmerConst.getFamerModel().setUnitname(spinnerUnit.getItems().get(spinnerUnit.getSelectedIndex()).toString());
        FarmerConst.getFamerModel().setUnitcode(edtUnitName.getText().toString());
        FarmerConst.getFamerModel().setUnitcapacity(edtUnitMeasurement.getText().toString());
        FarmerConst.getFamerModel().setUnitprice(edtUnitPrice.getText().toString());

        hideKeyboardFrom(Objects.requireNonNull(getContext()), view);
        callback.goToNextStep();

    }

    private void setEditData(FamerModel fm) {
        //FarmerConst.getFamerModel().setUnitprice(edtUnitPrice.getText().toString());

        edtRouteName.setText(fm.getRoutename());
        edtRouteCode.setText(fm.getRoutecode());

        edtUnitMeasurement.setText(fm.getUnitcapacity());
        edtUnitName.setText(fm.getUnitname());
        //   edtUnitPrice.setText(fm.getUnitprice());


    }

    private void initViews() {
        spinnerUnit = view.findViewById(R.id.spinnerUnit);
        spinnerRoute = view.findViewById(R.id.spinnerRoute);
        edtUnitName = view.findViewById(R.id.edt_unit_names);
        edtUnitPrice = view.findViewById(R.id.edt_unit_price);
        edtUnitMeasurement = view.findViewById(R.id.edt_unit_size);
        edtRouteName = view.findViewById(R.id.edt_route_names);
        edtRouteCode = view.findViewById(R.id.edt_route_code);



        hideKeyboardFrom(Objects.requireNonNull(getContext()), view);

        initData();
        initActions();



    }

    private void initActions() {

        try {
            spinnerUnit.setOnItemSelectedListener((view, position, id, item) -> {
                //   if (position != 0) {
                unitsModel = unitsModels.get(position);
                edtUnitName.setText("" + unitsModel.getUnit());
                edtUnitMeasurement.setText(unitsModel.getUnitcapacity());
//                edtUnitPrice.setText(unitsModel.getUnitprice());
//                if (edtUnitPrice.getText().equals("") || TextUtils.isEmpty(edtUnitPrice.getText())) {
//
//                    edtUnitPrice.setText("0");
//
//                }

            });

            edtUnitPrice.setOnClickListener(view -> {

                if (unitsModel == null || TextUtils.isEmpty(edtUnitName.getText().toString())) {
                    spinnerUnit.expand();
                    hideKeyboardFrom(Objects.requireNonNull(getContext()), view);
                }

            });
        } catch (Exception nm) {
            nm.printStackTrace();
        }
        try {
            spinnerRoute.setOnItemSelectedListener((view, position, id, item) -> {
                RoutesModel routesModel = routesModels.get(position);
                edtRouteName.setText(routesModel.getRoute());
                edtRouteCode.setText(routesModel.getCode());
            });
        } catch (Exception nm) {
            nm.printStackTrace();
        }
    }

    private void initData() {

        fm = new FamerModel();
        if (FarmerConst.getCreateFarmerIntentType() == 1) {
            fm = FarmerConst.getFamerModel();
            if (fm != null) {
                setEditData(fm);
            }
        }

        mViewModel.getRoutes(false).observe(this, routesModels -> {
            if (routesModels != null && routesModels.size() > 0) {
                prefrenceManager.setIsRoutesListFirst(false);

                FragmentRoutesUnitDetails.this.routesModels = routesModels;
                String routes[] = new String[routesModels.size()];

                //routes[0] = "Choose Route";
                for (int a = 0; a < routesModels.size(); a++) {
                    routes[a] = routesModels.get(a).getRoute();

                }
                spinnerRoute.setItems(routes);

                if (fm != null) {
                    if (spinnerRoute.getItems() != null) {
                        List<String> items = spinnerRoute.getItems();
                        try {
                            for (int a = 0; a < items.size(); a++) {
                                if (items.get(a).contains(fm.getRoutename())) {
                                    spinnerRoute.setSelectedIndex(a);
                                }
                            }
                        } catch (Exception nm) {
                            nm.printStackTrace();
                        }
                    }
                }

            }

        });
        mViewModel.getUnits(false).observe(this, unitsModels -> {
            if (unitsModels != null && unitsModels.size() > 0) {
                prefrenceManager.setIsRoutesListFirst(false);
                FragmentRoutesUnitDetails.this.unitsModels = unitsModels;
                String units[] = new String[unitsModels.size()];

                //units[0] = "Choose Unit ";
                for (int a = 0; a < unitsModels.size(); a++) {
                    units[a] = unitsModels.get(a).getUnit();

                }
                spinnerUnit.setItems(units);

                if (fm != null) {
                    if (spinnerUnit.getItems() != null) {

                        try {
                            List<String> items2 = spinnerUnit.getItems();
                            for (int a = 0; a < items2.size(); a++) {
                                if (items2.get(a).contains(fm.getUnitname())) {
                                    spinnerUnit.setSelectedIndex(a);
                                }
                            }
                        } catch (Exception nm) {
                            nm.printStackTrace();
                        }
                    }
                }

            }
        });


    }

}
