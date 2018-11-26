package com.dev.lishabora.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.lishabora.Adapters.ViewHolders.PayoutFarmerListViewHolder;
import com.dev.lishabora.Models.PayoutFarmersCollectionModel;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishaboramobile.R;

import java.util.List;


public class PayoutFarmersAdapter extends RecyclerView.Adapter<PayoutFarmerListViewHolder> {

    private Context context;
    private List<PayoutFarmersCollectionModel> modelList;
    private OnclickRecyclerListener listener;

    public PayoutFarmersAdapter(Context context, List<PayoutFarmersCollectionModel> modelList, OnclickRecyclerListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
    }

    public PayoutFarmersAdapter(Context context, List<PayoutFarmersCollectionModel> modelList, OnclickRecyclerListener listener, boolean isChk) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;

    }

    @Override
    public PayoutFarmerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.payout_farmerslist_card, parent, false);

        return new PayoutFarmerListViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(PayoutFarmerListViewHolder holder, int position) {
        PayoutFarmersCollectionModel model = modelList.get(position);



        holder.id.setText(model.getFarmercode());
        holder.name.setText(model.getFarmername());
        holder.status.setText(model.getStatusName());


        String vB = model.getBalance();
        String vL = model.getLoanTotal();
        String vO = model.getOrderTotal();
        String vM = model.getMilktotalKsh();
        String vML = model.getMilktotalLtrs();


        try {
            vB = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vB, 0)));
            vL = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vL, 0)));
            vO = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vO, 0)));
            vM = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vM, 0)));
            vML = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vML, 0)));
        } catch (Exception nm) {
            nm.printStackTrace();
        }


        holder.balance.setText(String.format("%s %s", vB, context.getString(R.string.ksh)));
        GeneralUtills.Companion.changeCOlor(model.getBalance(), holder.balance, 1);

        holder.milk.setText(String.format("%s %s", vML, context.getString(R.string.ltrs)));
        if (!model.getMilktotal().equals("0.0")) {
            holder.milk.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.milk.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.milk.setTypeface(Typeface.DEFAULT);
            holder.milk.setTextColor(context.getResources().getColor(R.color.black));

        }


        holder.loan.setText(String.format("%s %s", vL, context.getString(R.string.ksh)));
        if (!model.getLoanTotal().equals("0.0")) {

            holder.loan.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.loan.setTypeface(Typeface.DEFAULT_BOLD);

        } else {

            holder.loan.setTypeface(Typeface.DEFAULT);
            holder.loan.setTextColor(context.getResources().getColor(R.color.black));

        }

        holder.order.setText(String.format("%s %s", vO, context.getString(R.string.ksh)));
        if (!model.getOrderTotal().equals("0.0")) {
            holder.order.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.order.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.order.setTypeface(Typeface.DEFAULT);

            holder.order.setTextColor(context.getResources().getColor(R.color.black));

        }

        if (model.getCardstatus() == 1) {
            //  holder.status.setText("Active");
            holder.status.setTextColor(context.getResources().getColor(R.color.green_color_picker));
            // holder.background.setBackgroundColor(context.getResources().getColor(R.color.green_color_picker));
            holder.statusview.setBackgroundColor(context.getResources().getColor(R.color.green_color_picker));

            holder.btnApprove.setVisibility(View.GONE);

        } else if (model.getCardstatus() == 0) {


            holder.btnApprove.setVisibility(View.VISIBLE);

            //  holder.status.setText("Deleted");
            holder.status.setTextColor(context.getResources().getColor(R.color.red));
            // holder.background.setBackgroundColor(context.getResources().getColor(R.color.red));
            holder.statusview.setBackgroundColor(context.getResources().getColor(R.color.red));

        } else {
            // holder.status.setText("In-Active");
            holder.status.setTextColor(context.getResources().getColor(R.color.blue_color_picker));
            // holder.background.setBackgroundColor(context.getResources().getColor(R.color.blue_color_picker));
            holder.statusview.setBackgroundColor(context.getResources().getColor(R.color.blue_color_picker));

        }
        if (model.getPayoutStatus() == 1) {
            holder.btnApprove.setVisibility(View.GONE);
        } else {
            holder.btnApprove.setVisibility(View.VISIBLE);
            CommonFuncs.setCardActionStatus(model, holder.btnApprove, model.getLoanTotal(), model.getOrderTotal());

        }


    }


    @Override
    public int getItemCount() {
        return (null != modelList ? modelList.size() : 0);
    }


    public void refresh(List<PayoutFarmersCollectionModel> dayCollectionModels) {
        modelList = dayCollectionModels;
        notifyDataSetChanged();
    }
}
