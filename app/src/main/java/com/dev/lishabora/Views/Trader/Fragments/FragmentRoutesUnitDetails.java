package com.dev.lishabora.Views.Trader.Fragments;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
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

        callback.goToNextStep();

    }

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
        if (verifySpinner(spinnerRoute) && verifySpinner(spinnerUnit)) {
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
        return spinner.getSelectedIndex() != 0;
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

        if (FarmerConst.getCreateFarmerIntentType() == 1) {
            FamerModel fm = FarmerConst.getFamerModel();
            if (fm != null) {
                setEditData(fm);
            }
        }

    }

    private void setEditData(FamerModel fm) {
        FarmerConst.getFamerModel().setUnitprice(edtUnitPrice.getText().toString());

        edtRouteName.setText(fm.getRoutename());
        edtRouteCode.setText(fm.getRoutecode());

        edtUnitMeasurement.setText(fm.getUnitcapacity());
        edtUnitName.setText(fm.getUnitcode());
        edtUnitPrice.setText(fm.getUnitprice());


    }

    private void initActions() {
        spinnerUnit.setOnItemSelectedListener((view, position, id, item) -> {
            if (position != 0) {
                UnitsModel unitsModel = unitsModels.get(position - 1);
                edtUnitName.setText("" + unitsModel.getCode());
                edtUnitMeasurement.setText(unitsModel.getUnitcapacity());
                edtUnitPrice.setText(unitsModel.getUnitprice());
            }
        });
        spinnerRoute.setOnItemSelectedListener((view, position, id, item) -> {
            if (position != 0) {
                RoutesModel routesModel = routesModels.get(position - 1);
                edtRouteName.setText(routesModel.getRoute());
                edtRouteCode.setText(routesModel.getCode());
            }
        });
    }

    private void initData() {

        mViewModel.getRoutes(false).observe(this, routesModels -> {
            if (routesModels != null && routesModels.size() > 0) {
                prefrenceManager.setIsRoutesListFirst(false);

                FragmentRoutesUnitDetails.this.routesModels = routesModels;
                String routes[] = new String[routesModels.size() + 1];

                routes[0] = "Choose Route";
                for (int a = 0; a < routesModels.size(); a++) {
                    routes[a + 1] = routesModels.get(a).getRoute();

                }
                spinnerRoute.setItems(routes);
            }

        });
        mViewModel.getUnits(prefrenceManager.isUnitListFirstTime()).observe(this, unitsModels -> {
            if (unitsModels != null && unitsModels.size() > 0) {
                prefrenceManager.setIsRoutesListFirst(false);
                FragmentRoutesUnitDetails.this.unitsModels = unitsModels;
                String units[] = new String[unitsModels.size() + 1];

                units[0] = "Choose Unit ";
                for (int a = 0; a < unitsModels.size(); a++) {
                    units[a + 1] = unitsModels.get(a).getUnit();

                }
                spinnerUnit.setItems(units);

            }
        });
    }

}
