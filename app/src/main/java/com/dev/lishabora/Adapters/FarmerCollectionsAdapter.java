package com.dev.lishabora.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.lishabora.Adapters.ViewHolders.FarmerCollectionViewHolder;
import com.dev.lishabora.Models.DayCollectionModel;
import com.dev.lishabora.Utils.AdvancedOnclickRecyclerListener;
import com.dev.lishaboramobile.R;

import java.util.List;


public class FarmerCollectionsAdapter extends RecyclerView.Adapter<FarmerCollectionViewHolder> {

    private Context context;
    private List<DayCollectionModel> modelList;
    private AdvancedOnclickRecyclerListener listener;
    private boolean isChk = false;

    public FarmerCollectionsAdapter(Context context, List<DayCollectionModel> modelList, AdvancedOnclickRecyclerListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
        this.isChk = false;

    }

    public FarmerCollectionsAdapter(Context context, List<DayCollectionModel> modelList, AdvancedOnclickRecyclerListener listener, boolean isChk) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
        this.isChk = isChk;

    }

    public void refresh(List<DayCollectionModel> collections) {
        this.modelList.clear();
        this.modelList.addAll(collections);
        notifyDataSetChanged();
    }

    @Override
    public FarmerCollectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.payout_farmers_collections_card, parent, false);

        return new FarmerCollectionViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(FarmerCollectionViewHolder holder, int position) {
        DayCollectionModel model = modelList.get(position);

        holder.day.setText(model.getDay());
        holder.date.setText(model.getDate());


        holder.milkTotalAm.setText(model.getMilkAm());
        if (!model.getMilkAm().equals("0.0")) {
            holder.milkTotalAm.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.milkTotalAm.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.milkTotalAm.setTypeface(Typeface.DEFAULT);

            holder.milkTotalAm.setTextColor(context.getResources().getColor(R.color.black));

        }
        holder.milkTotalPm.setText(model.getMilkPm());
        if (!model.getMilkPm().equals("0.0")) {
            holder.milkTotalPm.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.milkTotalPm.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.milkTotalPm.setTypeface(Typeface.DEFAULT);

            holder.milkTotalPm.setTextColor(context.getResources().getColor(R.color.black));

        }


        holder.loanTotalAm.setText(model.getLoanAm());
        if (!model.getLoanAm().equals("0.0")) {
            holder.loanTotalAm.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.loanTotalAm.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.loanTotalAm.setTypeface(Typeface.DEFAULT);

            holder.loanTotalAm.setTextColor(context.getResources().getColor(R.color.black));

        }
        holder.loanTotalPm.setText(model.getLoanPm());
        if (!model.getLoanPm().equals("0.0")) {
            holder.loanTotalPm.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.loanTotalPm.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            holder.loanTotalPm.setTypeface(Typeface.DEFAULT);

            holder.loanTotalPm.setTextColor(context.getResources().getColor(R.color.black));

        }
        holder.orderTotalAm.setText(model.getOrderAm());
        if (!model.getOrderAm().equals("0.0")) {
            holder.orderTotalAm.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.orderTotalAm.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.orderTotalAm.setTypeface(Typeface.DEFAULT);

            holder.orderTotalAm.setTextColor(context.getResources().getColor(R.color.black));

        }
        holder.orderTotalPm.setText(model.getOrderPm());
        if (!model.getOrderPm().equals("0.0")) {
            holder.orderTotalPm.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.orderTotalPm.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.orderTotalPm.setTypeface(Typeface.DEFAULT);

            holder.orderTotalPm.setTextColor(context.getResources().getColor(R.color.black));

        }


    }


    @Override
    public int getItemCount() {
        return (null != modelList ? modelList.size() : 0);
    }


}
