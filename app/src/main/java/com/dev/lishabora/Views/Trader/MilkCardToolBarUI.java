package com.dev.lishabora.Views.Trader;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishaboramobile.R;

public class MilkCardToolBarUI extends RelativeLayout {

    private boolean isApproved;
    private boolean isPastApprovalDay;
    private int payoutNumber;
    private FamerModel famerModel;

    private TextView txtFarmersName, txtFarmesCode, txtPayoutNumber;
    private TextView txtStartDate, txtEndDate;

    private TextView txtMilkTotal, txtLoanTotal, txtOrderTotal, txtBalance;
    private RelativeLayout rlTotals;
    private String orderTotal, loanTotal, milkTotal;
    private String milkTotalKsh;

    public MilkCardToolBarUI(Context context) {
        super(context);
        init(context);
    }

    public MilkCardToolBarUI(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MilkCardToolBarUI(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public boolean isPastApprovalDay() {
        return isPastApprovalDay;
    }

    public void setPastApprovalDay(boolean pastApprovalDay) {
        isPastApprovalDay = pastApprovalDay;
    }

    public int getPayoutNumber() {
        return payoutNumber;
    }

    public void setPayoutNumber(int payoutNumber) {
        this.payoutNumber = payoutNumber;
    }

    public FamerModel getFamerModel() {
        return famerModel;
    }

    public void setFamerModel(FamerModel famerModel) {
        this.famerModel = famerModel;
    }

    private void init(Context context) {
        View.inflate(context, R.layout.farmer_milk_card_toolbar_ui, this);
        if (isInEditMode()) {
            return;
        }

        txtFarmersName = findViewById(R.id.txt_name);
        txtFarmesCode = findViewById(R.id.txt_code);
        txtPayoutNumber = findViewById(R.id.txt_payout_no);


        txtStartDate = findViewById(R.id.txt_card_start);
        txtEndDate = findViewById(R.id.txt_card_end);

        txtMilkTotal = findViewById(R.id.txt_milk);
        txtLoanTotal = findViewById(R.id.txt_loans);
        txtOrderTotal = findViewById(R.id.txt_orders);
        txtBalance = findViewById(R.id.txt_balance);

        rlTotals = findViewById(R.id.background);


        initializeDefaults();
    }

    public void initializeDefaults() {
        setBackgroundColor(getResources().getColor(R.color.colorPrimary));


    }

    public void show(String balance) {

        txtBalance.setText(String.format("%s %s", GeneralUtills.Companion.round(balance, 1), getResources().getString(R.string.ksh)));
        GeneralUtills.Companion.changeCOlor(balance, txtBalance, 1);


    }

    public void show(String milkTotals, String loaTotals, String orderTotal, String balance, FamerModel famerModel, Payouts payouts, boolean isApproved, boolean isPast) {


        setApproved(isApproved);
        setFamerModel(famerModel);
        setPastApprovalDay(isPast);
        setPayoutNumber(payouts.getPayoutnumber());

        setLoanTotal(loaTotals);
        setOrderTotal(orderTotal);
        setMilkTotal(orderTotal);


        txtBalance.setText(String.format("%s %s", GeneralUtills.Companion.round(balance, 1), getResources().getString(R.string.ksh)));
        GeneralUtills.Companion.changeCOlor(balance, txtBalance, 1);

        txtMilkTotal.setText(String.format("%s %s", GeneralUtills.Companion.round(milkTotals, 1), getResources().getString(R.string.ltrs)));
        txtLoanTotal.setText(String.format("%s %s", GeneralUtills.Companion.round(loaTotals, 1), getResources().getString(R.string.ltrs)));
        txtOrderTotal.setText(String.format("%s %s", GeneralUtills.Companion.round(orderTotal, 1), getResources().getString(R.string.ltrs)));

        txtFarmersName.setText(famerModel.getNames());
        txtFarmesCode.setText(famerModel.getCode());

        txtPayoutNumber.setText("" + payouts.getPayoutnumber());
        txtStartDate.setText(DateTimeUtils.Companion.getDisplayDate(payouts.getStartDate(), DateTimeUtils.Companion.getDisplayDatePattern1()));
        txtEndDate.setText(DateTimeUtils.Companion.getDisplayDate(payouts.getEndDate(), DateTimeUtils.Companion.getDisplayDatePattern1()));

        if (isApproved) {
            rlTotals.setBackgroundColor(MilkCardToolBarUI.this.getResources().getColor(R.color.green_color_picker));
        } else {
            rlTotals.setBackgroundColor(MilkCardToolBarUI.this.getResources().getColor(R.color.red));

        }



    }

    public void updateMilk(String v) {
        txtMilkTotal.setText(String.format("%s %s", GeneralUtills.Companion.round(v, 1), getResources().getString(R.string.ltrs)));
        setMilkTotal(v);

    }

    public void updateLoan(String v) {
        txtLoanTotal.setText(String.format("%s %s", GeneralUtills.Companion.round(v, 1), getResources().getString(R.string.ksh)));
        setLoanTotal(v);

    }

    public void updateOrder(String v) {

        txtOrderTotal.setText(String.format("%s %s", GeneralUtills.Companion.round(v, 1), getResources().getString(R.string.ksh)));
        setOrderTotal(v);
    }

    public void updateBalance(String v) {
        txtBalance.setText(v);

    }

    public String getOrderTotal() {
        if (orderTotal != null) {
            return orderTotal;
        } else {
            return "0";
        }
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getLoanTotal() {
        if (loanTotal != null) {
            return loanTotal;
        } else {
            return "0";
        }
    }

    public void setLoanTotal(String loanTotal) {
        this.loanTotal = loanTotal;
    }

    public String getMilkTotal() {
        if (milkTotal != null) {
            return milkTotal;
        } else {
            return "0";
        }
    }

    public void setMilkTotal(String milkTotal) {
        this.milkTotal = milkTotal;
    }

    public String getMilkTotalKsh() {
        return milkTotalKsh;
    }

    public void setMilkTotalKsh(String milkTotalKsh) {
        this.milkTotalKsh = milkTotalKsh;
    }

    public void setOnPayNoClickListener(OnClickListener clickListener) {
        txtPayoutNumber.setOnClickListener(clickListener);
    }


}
