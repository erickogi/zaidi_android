package com.dev.lishabora.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.lishabora.Adapters.ViewHolders.LoanOrderViewHolder;
import com.dev.lishabora.Application;
import com.dev.lishabora.Models.Trader.FarmerLoansTable;
import com.dev.lishabora.Models.Trader.FarmerOrdersTable;
import com.dev.lishabora.Repos.Trader.FarmerRepo;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;

import java.util.List;


public class LoansOrdersAdapter extends RecyclerView.Adapter<LoanOrderViewHolder>

{
    private Context context;
    private List<FarmerLoansTable> modelListLoans;
    private List<FarmerOrdersTable> modelListOrders;
    private OnclickRecyclerListener listener;


    public LoansOrdersAdapter(Context context, List<FarmerLoansTable> modelListLoans, List<FarmerOrdersTable> modelListOrders, OnclickRecyclerListener listener) {
        this.context = context;

        this.modelListLoans = modelListLoans;
        this.modelListOrders = modelListOrders;
        this.listener = listener;

    }


    @Override
    public LoanOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trader_loanorder_card, parent, false);

        return new LoanOrderViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(LoanOrderViewHolder holder, int position) {

        if (modelListLoans != null) {


            FarmerLoansTable model = modelListLoans.get(position);
            holder.txtDate.setText(DateTimeUtils.Companion.getDisplayDate(model.getTimestamp()));
            holder.farmer.setText(new FarmerRepo(Application.context).getFramerByCodeOne(model.getFarmerCode()).getNames());


            String vA = model.getLoanAmount();
            String vI = model.getInstallmentAmount();
            String vB = String.valueOf((Double.valueOf(model.getLoanAmount())) - Double.valueOf(model.getLoanAmountPaid()));


            try {
                vA = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vA, 0)));
                vI = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vI, 0)));
                vB = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vB, 0)));
            } catch (Exception nm) {
                nm.printStackTrace();
            }


            holder.amount.setText(String.format("%s %s", vA, context.getString(R.string.ksh)));
            holder.installment.setText(String.format("%s %s", vI + " Per ", context.getString(R.string.ksh)));
            holder.balance.setText(String.valueOf(String.format("%s %s", vB, context.getString(R.string.ksh))));





            if (model.getStatus() == 1) {
                holder.status.setText("Paid In Full");
                holder.status.setTextColor(context.getResources().getColor(R.color.green_color_picker));
            } else {
                holder.status.setText("Pending Full Payment");
                holder.status.setTextColor(context.getResources().getColor(R.color.red));

            }


        } else {
            FarmerOrdersTable model = modelListOrders.get(position);
            holder.txtDate.setText(DateTimeUtils.Companion.getDisplayDate(model.getTimestamp()));
            holder.farmer.setText(new FarmerRepo(Application.context).getFramerByCodeOne(model.getFarmerCode()).getNames());


            String vA = model.getOrderAmount();
            String vI = model.getInstallmentAmount();
            String vB = String.valueOf((Double.valueOf(model.getOrderAmount())) - Double.valueOf(model.getOrderAmountPaid()));


            try {
                vA = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vA, 0)));
                vI = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vI, 0)));
                vB = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vB, 0)));
            } catch (Exception nm) {
                nm.printStackTrace();
            }


            holder.amount.setText(String.format("%s %s", vA, context.getString(R.string.ksh)));
            holder.installment.setText(String.format("%s %s", vI + " Per ", context.getString(R.string.ksh)));
            holder.balance.setText(String.valueOf(String.format("%s %s", vB, context.getString(R.string.ksh))));








            if (model.getStatus() == 1) {
                holder.status.setText("Paid In Full");
                holder.status.setTextColor(context.getResources().getColor(R.color.green_color_picker));

            } else {

                holder.status.setText("Pending Full Payment");
                holder.status.setTextColor(context.getResources().getColor(R.color.red));

            }
        }


    }


    @Override
    public int getItemCount() {

        if (modelListOrders == null && modelListLoans == null) {
            return 3;
        } else if (modelListLoans != null) {

            return modelListLoans.size();
        } else {
            return modelListOrders.size();
        }

    }

}
