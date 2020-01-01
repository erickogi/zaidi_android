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
import android.widget.ImageButton;
import android.widget.TextView;

import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.Utils.MaterialIntro;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.ViewModels.Trader.BalncesViewModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.Trader.Activities.CreateFarmerActivity;
import com.dev.lishabora.Views.Trader.FarmerConst;
import com.dev.lishaboramobile.R;
import com.getkeepsafe.taptargetview.TapTarget;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class FragmentFarmerProfile extends Fragment {
    FamerModel famerModel;
    TextView txtName, txtCode, txtPhone, txtRoute, txtCycle, txtUnit, txtMilk, txtLoan, txtOrder, txtBalance, txtTime;

    private View mView;
    private TraderViewModel traderViewModel;
    private BalncesViewModel balncesViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);


    }
    private ImageButton helpView;
    private ImageButton editView;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem mHelp = menu.findItem(R.id.action_help);
        MenuItem mEdit = menu.findItem(R.id.action_edit);

        helpView = (ImageButton) mHelp.getActionView();
        helpView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_help));
        helpView.setBackgroundColor(getContext().getResources().getColor(R.color.transparent));

        helpView.setOnClickListener(v -> showIntro());

        editView = (ImageButton) mEdit.getActionView();
        editView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_edit_black_24dp));
        editView.setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
        editView.setOnClickListener(v -> editProfile());
        if (!new PrefrenceManager(getContext()).isFarmersProfileFragmentIntroShown()) {
            showIntro();
        }

    }

    void showIntro() {
        List<TapTarget> targets = new ArrayList<>();
        targets.add(TapTarget.forView(editView, "Click here to update farmers profile", getContext().getResources().getString(R.string.dismiss_intro)).cancelable(false).id(15).transparentTarget(true));
        targets.add(TapTarget.forView(helpView, "Click here to see this introduction again", getContext().getResources().getString(R.string.dismiss_intro)).cancelable(false).id(16).transparentTarget(true));
        MaterialIntro.Companion.showIntroSequence(getActivity(), targets);
        new PrefrenceManager(getContext()).setFarmersProfileFragmentIntroShown(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                editProfile();
                return true;


            default:
                break;
        }

        return false;
    }

    private void editProfile(){
        Intent intent = new Intent(getActivity(), CreateFarmerActivity.class);
        intent.putExtra("type", 1);
        intent.putExtra("farmer", famerModel);
        startActivityForResult(intent, 1001);
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
        balncesViewModel = ViewModelProviders.of(this).get(BalncesViewModel.class);

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

            traderViewModel.getFarmersByCode(famerModel.getCode()).observe(this, farmerModel -> {

                txtName.setText(farmerModel.getNames());
                txtCode.setText(farmerModel.getCode());
                txtPhone.setText(farmerModel.getMobile());
                txtRoute.setText(farmerModel.getRoutename());
                txtCycle.setText(farmerModel.getCyclename());
                txtUnit.setText(farmerModel.getUnitname());
                txtMilk.setText("");
                txtLoan.setText("");
                txtOrder.setText("");
                txtBalance.setText(GeneralUtills.Companion.round(farmerModel.getTotalbalance(), 1));
                txtTime.setText(famerModel.getLastCollectionTime());
                txtTime.setText(DateTimeUtils.Companion.getDisplayDate(famerModel.getLastCollectionTime()));


                try {
                    getActivity().setTitle(farmerModel.getNames());
                } catch (Exception nm) {
                    nm.printStackTrace();
                }

                String vA = farmerModel.getTotalbalance();


                try {
                    vA = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vA, 0)));
                } catch (Exception nm) {
                    nm.printStackTrace();
                }
                txtBalance.setText(vA);



            });
            try {
//                String bal = "0.0";
//                try {
//                    balncesViewModel.getByFarmerCodeByPayout(famerModel.getCode(), famerModel.getCurrentPayoutCode()).observe(this, farmerBalances -> {
//
//                        Double total = 0.0;
//                        try {
//                            if (farmerBalances.getPayoutStatus() == 0) {
//                                total = total + (Double.valueOf(farmerBalances.getBalanceToPay()));
//                                }
//                            } catch (Exception nm) {
//                                nm.printStackTrace();
//                            }
//
//                        txtBalance.setText(GeneralUtills.Companion.round(String.valueOf(total), 1));
//
//                    });
//                } catch (Exception NM) {
//                    NM.printStackTrace();
//                    Log.d("balls", NM.toString());
//
//                }


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
