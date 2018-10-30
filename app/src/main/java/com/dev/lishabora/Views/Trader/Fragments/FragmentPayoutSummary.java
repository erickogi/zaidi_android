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

import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishabora.Views.Trader.PayoutConstants;
import com.dev.lishaboramobile.R;

import static com.dev.lishabora.Views.CommonFuncs.setPayoutActionStatus;

public class FragmentPayoutSummary extends Fragment {
    public TextView status, startDate, cycleName, endDate, milkTotal, loanTotal, orderTotal, balance, approvedCount, unApprovedCount;
    public RelativeLayout background;
    public View statusview;
    public View view;
    private Payouts payouts;
    TextView txtApprovalStatus;
    private PayoutsVewModel payoutsVewModel;
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

    public void setCardHeaderData(com.dev.lishabora.Models.Payouts model) {
        startDate.setText(DateTimeUtils.Companion.getDisplayDate(model.getStartDate(), DateTimeUtils.Companion.getDisplayDatePattern1()));
        endDate.setText(DateTimeUtils.Companion.getDisplayDate(model.getEndDate(), DateTimeUtils.Companion.getDisplayDatePattern1()));
        cycleName.setText(model.getCyclename());


        milkTotal.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getMilkTotalLtrs(), 1), this.getString(R.string.ltrs)));
        loanTotal.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getLoanTotal(), 1), this.getString(R.string.ksh)));
        orderTotal.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getOrderTotal(), 1), this.getString(R.string.ksh)));
        balance.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getBalance(), 1), this.getString(R.string.ksh)));
        GeneralUtills.Companion.changeCOlor(model.getBalance(), balance, 1);


        approvedCount.setText(model.getApprovedCards());
        unApprovedCount.setText(model.getPendingCards());


        if (model.getStatus() == 1) {
            // status.setText("Active");
            background.setBackgroundColor(this.getResources().getColor(R.color.green_color_picker));


        } else if (model.getStatus() == 0) {
            background.setBackgroundColor(this.getResources().getColor(R.color.red));

        } else {
            background.setBackgroundColor(this.getResources().getColor(R.color.blue_color_picker));

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
