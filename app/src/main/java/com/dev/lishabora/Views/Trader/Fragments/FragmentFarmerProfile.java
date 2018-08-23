package com.dev.lishabora.Views.Trader.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.lishabora.Models.Cycles;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.RoutesModel;
import com.dev.lishabora.Models.UnitsModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.Trader.Activities.CreateFarmerActivity;
import com.dev.lishabora.Views.Trader.FarmerConst;
import com.dev.lishaboramobile.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class FragmentFarmerProfile extends Fragment {
    FamerModel famerModel;
    List<UnitsModel> unitsModels;
    List<Cycles> cycles;
    List<RoutesModel> routesModels;
    TextInputEditText edtNames, edtMobile;
    MaterialSpinner defaultPayment;
    TraderViewModel mViewModel;
    MaterialSpinner spinner;
    TextInputEditText edtRouteName, edtRouteCode, edtUnitName, edtUnitPrice, edtUnitMeasurement;
    LinearLayout lcycle;
    TextView txtStartDay, txtEndDay, txtCycle;
    Button btnEdit;
    private View mView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_farmer_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mView = view;
        famerModel = (FamerModel) getArguments().getSerializable("farmer");


    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {
        txtStartDay = mView.findViewById(R.id.starts);
        txtEndDay = mView.findViewById(R.id.ends);
        txtCycle = mView.findViewById(R.id.txt_cycle);
        edtNames = mView.findViewById(R.id.edt_farmer_names);
        edtMobile = mView.findViewById(R.id.edt_farmer_phone);

        edtUnitName = mView.findViewById(R.id.edt_unit_names);
        edtUnitPrice = mView.findViewById(R.id.edt_unit_price);
        edtUnitMeasurement = mView.findViewById(R.id.edt_unit_size);
        edtRouteName = mView.findViewById(R.id.edt_route_names);
        edtRouteCode = mView.findViewById(R.id.edt_route_code);
        btnEdit = mView.findViewById(R.id.btn_edit);


        setUpData();
        btnEdit.setOnClickListener(view -> {

            Intent intent = new Intent(getActivity(), CreateFarmerActivity.class);
            intent.putExtra("type", 1);
            intent.putExtra("farmer", famerModel);

            startActivityForResult(intent, 1001);
        });
    }

    void setUpData() {
        if (famerModel != null) {
            edtNames.setText(famerModel.getNames());
            edtMobile.setText(famerModel.getMobile());
            edtUnitPrice.setText("" + famerModel.getUnitprice());
            edtUnitMeasurement.setText("" + famerModel.getUnitcapacity());
            edtRouteName.setText("" + famerModel.getRoutename());
            edtUnitName.setText("" + famerModel.getUnitname());
            edtRouteCode.setText("" + famerModel.getRoutecode());

            try {
                txtStartDay.setText(famerModel.getCycleStartDay());
                txtEndDay.setText(famerModel.getCycleStartEndDay());
                txtCycle.setText(famerModel.getCyclename());
            } catch (Exception nm) {
                nm.printStackTrace();
            }

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            if (resultCode == RESULT_OK) {
                famerModel = (FamerModel) data.getSerializableExtra("farmer_back");
                FarmerConst.setFamerModel(famerModel);
                setUpData();
            }
        }
    }
}
