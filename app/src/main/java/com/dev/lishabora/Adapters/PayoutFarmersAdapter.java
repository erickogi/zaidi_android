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
import com.dev.lishaboramobile.R;

import java.util.List;


public class PayoutFarmersAdapter extends RecyclerView.Adapter<PayoutFarmerListViewHolder> {

    private Context context;
    private List<PayoutFarmersCollectionModel> modelList;
    private OnclickRecyclerListener listener;
    private boolean isChk = false;

    public PayoutFarmersAdapter(Context context, List<PayoutFarmersCollectionModel> modelList, OnclickRecyclerListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
        this.isChk = false;

    }

    public PayoutFarmersAdapter(Context context, List<PayoutFarmersCollectionModel> modelList, OnclickRecyclerListener listener, boolean isChk) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
        this.isChk = isChk;

    }

    @Override
    public PayoutFarmerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.payout_farmerslist_card, parent, false);

        return new PayoutFarmerListViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(PayoutFarmerListViewHolder holder, int position) {
        PayoutFarmersCollectionModel model = modelList.get(position);

        holder.balance.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getBalance(), 1), context.getString(R.string.ksh)));
        holder.id.setText(model.getFarmercode());
        holder.name.setText(model.getFarmername());

        holder.status.setText(model.getStatusName());


        holder.milk.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getMilktotalLtrs(), 1), context.getString(R.string.ltrs)));
        if (!model.getMilktotal().equals("0.0")) {
            holder.milk.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.milk.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.milk.setTypeface(Typeface.DEFAULT);

            holder.milk.setTextColor(context.getResources().getColor(R.color.black));

        }


        holder.loan.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getLoanTotal(), 1), context.getString(R.string.ksh)));
        if (!model.getLoanTotal().equals("0.0")) {
            holder.loan.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.loan.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.loan.setTypeface(Typeface.DEFAULT);

            holder.loan.setTextColor(context.getResources().getColor(R.color.black));

        }

        holder.order.setText(String.format("%s %s", model.getOrderTotal(), context.getString(R.string.ksh)));
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
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.green_color_picker));
            holder.statusview.setBackgroundColor(context.getResources().getColor(R.color.green_color_picker));


        } else if (model.getCardstatus() == 0) {

            //  holder.status.setText("Deleted");
            holder.status.setTextColor(context.getResources().getColor(R.color.red));
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.red));
            holder.statusview.setBackgroundColor(context.getResources().getColor(R.color.red));

        } else {
            // holder.status.setText("In-Active");
            holder.status.setTextColor(context.getResources().getColor(R.color.blue_color_picker));
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.blue_color_picker));
            holder.statusview.setBackgroundColor(context.getResources().getColor(R.color.blue_color_picker));

        }


    }


    @Override
    public int getItemCount() {
        return (null != modelList ? modelList.size() : 0);
    }


}
