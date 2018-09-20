package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.lishabora.Models.Trader.TraderModel;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.Trader.Activities.FirstTimeLaunch;
import com.dev.lishaboramobile.R;

public class FragmentTraderProfile extends Fragment {

    TextView txtName, txtCode, txtPhone, txtBussinesName, txtRoutes, txtProducts, txtStartDate, txtEndDate;
    TraderViewModel traderViewModel;
    private View view;
    private PrefrenceManager prefrenceManager;
    private TraderModel traderModel;
    private MaterialButton btnEdit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.trader_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        traderViewModel = ViewModelProviders.of(FragmentTraderProfile.this).get(TraderViewModel.class);

        prefrenceManager = new PrefrenceManager(getContext());
        traderModel = prefrenceManager.getTraderModel();

        btnEdit = view.findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), FirstTimeLaunch.class);
                i.putExtra("trader", traderModel);
                startActivity(i);
            }
        });

        txtName = view.findViewById(R.id.txt_name);
        txtCode = view.findViewById(R.id.txt_code);
        txtPhone = view.findViewById(R.id.txt_phone);
        txtBussinesName = view.findViewById(R.id.txt_business_name);


        txtRoutes = view.findViewById(R.id.txt_routes);
        txtProducts = view.findViewById(R.id.txt_products);


        txtStartDate = view.findViewById(R.id.txt_date_start);
        txtEndDate = view.findViewById(R.id.txt_date_end);


        txtName.setText(traderModel.getNames());
        txtCode.setText(traderModel.getCode());
        txtPhone.setText(traderModel.getMobile());
        txtBussinesName.setText(traderModel.getBusinessname());

        txtStartDate.setText(traderModel.getCycleStartDay());
        txtEndDate.setText(traderModel.getCycleEndDay());


        traderViewModel.getProductsCountLive().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                txtProducts.setText("" + integer);
            }
        });
        traderViewModel.getRoutesCountLive().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                txtRoutes.setText("" + integer);
            }
        });


    }
}
