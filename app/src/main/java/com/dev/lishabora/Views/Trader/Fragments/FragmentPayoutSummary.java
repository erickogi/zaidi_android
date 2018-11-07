package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.PayoutFarmersCollectionModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.ViewModels.Trader.BalncesViewModel;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishabora.Views.Trader.PayoutConstants;
import com.dev.lishaboramobile.R;

import java.util.List;

import static com.dev.lishabora.Views.CommonFuncs.setPayoutActionStatus;

public class FragmentPayoutSummary extends Fragment {
    public TextView status, startDate, cycleName, endDate, milkTotal, loanTotal, orderTotal, balance, approvedCount, unApprovedCount;
    public RelativeLayout background;
    public View statusview;
    public View view;
    private Payouts payouts;
    TextView txtApprovalStatus;
    private PayoutsVewModel payoutsVewModel;
    private BalncesViewModel balncesViewModel;
    private MaterialButton btnApprove;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.payout_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);
        balncesViewModel = ViewModelProviders.of(this).get(BalncesViewModel.class);

        payouts = new Payouts();
        if (getArguments() != null) {
            payouts = (Payouts) getArguments().getSerializable("data");
        } else {
            payouts = PayoutConstants.getPayouts();
        }
        initCardHeader();

        btnApprove = view.findViewById(R.id.btn_approve);
        txtApprovalStatus = view.findViewById(R.id.txt_approval_status);
        btnApprove.setVisibility(View.GONE);


        if (CommonFuncs.canApprovePayout(payouts)) {
            btnApprove.setVisibility(View.VISIBLE);
        } else {
            btnApprove.setVisibility(View.GONE);
        }


        btnApprove.setOnClickListener(view1 -> {

            if (CommonFuncs.allCollectionsAreApproved(payoutsVewModel, payouts)) {
                payouts.setStatus(1);
                payouts.setStatusName("Approved");
                payoutsVewModel.updatePayout(payouts);
                setCardHeaderData(payouts);
            } else {
                MyToast.toast("Some farmer cards in this payout are not approved yet", FragmentPayoutSummary.this.getContext(), R.drawable.ic_error_outline_black_24dp, Toast.LENGTH_LONG);
            }

        });
        setPayoutActionStatus(payouts, getContext(), btnApprove, txtApprovalStatus);

    }

    private void loadFarmers() {


        try {
            payoutsVewModel.getFarmersByCycle("" + payouts.getCycleCode()).observe(this, famerModels -> {
                if (famerModels != null) {


                    try {
                        loadCollectionPayouts(famerModels);
                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }

                } else {

                }
            });
        } catch (Exception nm) {
            nm.printStackTrace();
        }
    }

    private void loadCollectionPayouts(List<FamerModel> famerModels) {

        payoutsVewModel.getCollectionByDateByPayout("" + payouts.getCode()).observe(this, collections -> {
            if (collections != null) {


                try {
                    setUpFarmerCollectionList(famerModels, collections);
                } catch (Exception nm) {
                    nm.printStackTrace();
                }
            }
        });


    }

    private void setUpFarmerCollectionList(List<FamerModel> famerModels, List<Collection> collections) {


        Double totalBalance = 0.0;
        Double totalMilk = 0.0;
        Double totalOrders = 0.0;
        Double totalLoans = 0.0;

        for (FamerModel famerModel : famerModels) {


            PayoutFarmersCollectionModel p = CommonFuncs.getFarmersCollectionModel(famerModel, collections, payouts, balncesViewModel);


            if (p != null) {

                try {
                    totalBalance = totalBalance + Double.valueOf(p.getBalance());

                } catch (Exception nm) {
                    nm.printStackTrace();
                }
                try {
                    totalMilk = totalMilk + Double.valueOf(p.getMilktotalKsh());

                } catch (Exception nm) {
                    nm.printStackTrace();
                }
                try {
                    totalLoans = totalLoans + Double.valueOf(p.getLoanTotal());

                } catch (Exception nm) {
                    nm.printStackTrace();
                }
                try {
                    totalOrders = totalOrders + Double.valueOf(p.getOrderTotal());

                } catch (Exception nm) {
                    nm.printStackTrace();
                }

            }
        }

        setData(totalBalance, totalMilk, totalLoans, totalOrders);


    }

    private void setData(Double totalBalance, Double totalMilk, Double totalLoans, Double totalOrders) {

        String vB = String.valueOf(totalBalance);
        String vL = String.valueOf(totalLoans);
        String vO = String.valueOf(totalOrders);
        String vM = String.valueOf(totalMilk);


        try {
            vB = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vB, 0)));
            vL = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vL, 0)));
            vO = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vO, 0)));
            vM = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vM, 0)));
        } catch (Exception nm) {
            nm.printStackTrace();
        }


        milkTotal.setText(String.format("%s %s", vM, this.getString(R.string.ksh)));
        loanTotal.setText(String.format("%s %s", vL, this.getString(R.string.ksh)));
        orderTotal.setText(String.format("%s %s", vO, this.getString(R.string.ksh)));
        balance.setText(String.format("%s %s", vB, this.getString(R.string.ksh)));


        GeneralUtills.Companion.changeCOlor(String.valueOf(totalBalance), balance, 1);


    }


    public void setCardHeaderData(com.dev.lishabora.Models.Payouts model) {
        startDate.setText(DateTimeUtils.Companion.getDisplayDate(model.getStartDate(), DateTimeUtils.Companion.getDisplayDatePattern1()));
        endDate.setText(DateTimeUtils.Companion.getDisplayDate(model.getEndDate(), DateTimeUtils.Companion.getDisplayDatePattern1()));
        cycleName.setText(model.getCyclename());


        try {
            milkTotal.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getMilkTotalLtrs(), 1), this.getString(R.string.ltrs)));
            loanTotal.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getLoanTotal(), 1), this.getString(R.string.ksh)));
            orderTotal.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getOrderTotal(), 1), this.getString(R.string.ksh)));
            balance.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getBalance(), 1), this.getString(R.string.ksh)));


            GeneralUtills.Companion.changeCOlor(model.getBalance(), balance, 1);

        } catch (Exception nm) {
            nm.printStackTrace();
        }

        approvedCount.setText(model.getApprovedCards());
        unApprovedCount.setText(model.getPendingCards());


        if (model.getStatus() == 1) {

            background.setBackgroundColor(this.getResources().getColor(R.color.green_color_picker));


        } else if (model.getStatus() == 0) {
            background.setBackgroundColor(this.getResources().getColor(R.color.red));

        } else {
            background.setBackgroundColor(this.getResources().getColor(R.color.blue_color_picker));

        }
        try {
            loadFarmers();
        } catch (Exception nm) {
            nm.printStackTrace();
        }

    }


    public void initCardHeader() {
        background = view.findViewById(R.id.background);
        startDate = view.findViewById(R.id.txt_date_start);
        endDate = view.findViewById(R.id.txt_date_end);


        cycleName = view.findViewById(R.id.txt_cycle);

        milkTotal = view.findViewById(R.id.txt_milk_totals);
        loanTotal = view.findViewById(R.id.txt_loans_total);
        orderTotal = view.findViewById(R.id.txt_orders_total);

        approvedCount = view.findViewById(R.id.txt_approved_farmers);
        unApprovedCount = view.findViewById(R.id.txt_pending_farmers);
        balance = view.findViewById(R.id.txt_Bal_out);

        if (payouts != null) {
            setCardHeaderData(payouts);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}
