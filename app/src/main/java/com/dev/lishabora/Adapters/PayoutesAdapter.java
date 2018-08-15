package com.dev.lishabora.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.lishabora.Adapters.ViewHolders.PayoutsViewHolder;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;

import java.util.List;

//import com.dev.lishaboramobile.Trader.Models.Payouts;

public class PayoutesAdapter extends RecyclerView.Adapter<PayoutsViewHolder> {

    private Context context;
    private List<Payouts> modelList;
    private OnclickRecyclerListener listener;
    private boolean isChk = false;

    public PayoutesAdapter(Context context, List<Payouts> modelList, OnclickRecyclerListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
        this.isChk = false;

    }

    public PayoutesAdapter(Context context, List<Payouts> modelList, OnclickRecyclerListener listener, boolean isChk) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
        this.isChk = isChk;

    }

    @Override
    public PayoutsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);

        return new PayoutsViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(PayoutsViewHolder holder, int position) {
        Payouts model = modelList.get(position);

        holder.startDate.setText(model.getStartDate());
        holder.endDate.setText(model.getEndDate());
        holder.cycleName.setText(model.getCyclename());
        holder.status.setText(model.getStatusName());
        holder.milkTotal.setText(model.getMilkTotal());
        holder.loanTotal.setText(model.getLoanTotal());
        holder.orderTotal.setText(model.getOrderTotal());

        holder.balance.setText(model.getBalance());
        holder.approvedCount.setText(model.getApprovedCards());
        holder.unApprovedCount.setText(model.getPendingCards());


        if (model.getStatus() == 1) {
            holder.status.setText("Active");
            holder.status.setTextColor(context.getResources().getColor(R.color.green_color_picker));
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.green_color_picker));
            holder.statusview.setBackgroundColor(context.getResources().getColor(R.color.green_color_picker));


        } else if (model.getStatus() == 0) {

            holder.status.setText("Deleted");
            holder.status.setTextColor(context.getResources().getColor(R.color.red));
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.red));
            holder.statusview.setBackgroundColor(context.getResources().getColor(R.color.red));

        } else {
            holder.status.setText("In-Active");
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
