package com.dev.lishabora.Views.Trader.Fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.Trader.Activities.CreateFarmerActivity;
import com.dev.lishabora.Views.Trader.FarmerConst;
import com.dev.lishaboramobile.R;

import static android.app.Activity.RESULT_OK;


public class FragmentFarmerProfile extends Fragment {
    FamerModel famerModel;
    TextView txtName, txtCode, txtPhone, txtRoute, txtCycle, txtUnit, txtMilk, txtLoan, txtOrder, txtBalance, txtTime;

    private View mView;
    private TraderViewModel traderViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setHasOptionsMenu(true);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        MenuItem mEdit = menu.findItem(R.id.action_edit);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_edit:
                Intent intent = new Intent(getActivity(), CreateFarmerActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("farmer", famerModel);

                startActivityForResult(intent, 1001);
                return true;


            default:
                break;
        }

        return false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.farmer_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mView = view;
        famerModel = (FamerModel) getArguments().getSerializable("farmer");
        traderViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);

    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {
        txtName = mView.findViewById(R.id.txt_name);
        txtCode = mView.findViewById(R.id.txt_code);
        txtPhone = mView.findViewById(R.id.txt_phone);


        txtRoute = mView.findViewById(R.id.txt_route);
        txtCycle = mView.findViewById(R.id.txt_cycle);
        txtUnit = mView.findViewById(R.id.txt_unit);


        txtMilk = mView.findViewById(R.id.txt_milk);
        txtLoan = mView.findViewById(R.id.txt_loans);
        txtOrder = mView.findViewById(R.id.txt_orders);

        txtBalance = mView.findViewById(R.id.txt_balance);
        txtTime = mView.findViewById(R.id.txt_last_time);


        setUpData();

    }

    void setUpData() {
        if (famerModel != null) {

            traderViewModel.getFarmersByCode(famerModel.getCode()).observe(this, famerModel -> {

                txtName.setText(famerModel.getNames());
                txtCode.setText(famerModel.getCode());
                txtPhone.setText(famerModel.getMobile());
                txtRoute.setText(famerModel.getRoutename());
                txtCycle.setText(famerModel.getCyclename());
                txtUnit.setText(famerModel.getUnitname());
                txtMilk.setText("");
                txtLoan.setText("");
                txtOrder.setText("");
                txtBalance.setText(GeneralUtills.Companion.round(famerModel.getTotalbalance(), 1));
                txtTime.setText(famerModel.getLastCollectionTime());

                try {
                    getActivity().setTitle(famerModel.getNames());
                } catch (Exception nm) {
                    nm.printStackTrace();
                }

            });

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
