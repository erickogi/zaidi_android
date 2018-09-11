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
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishaboramobile.R;

import java.util.List;


public class FarmerCollectionsAdapter extends RecyclerView.Adapter<FarmerCollectionViewHolder> {

    private Context context;
    private List<DayCollectionModel> modelList;
    private AdvancedOnclickRecyclerListener listener;
    private boolean isEditable = true;

    public FarmerCollectionsAdapter(Context context, List<DayCollectionModel> modelList, AdvancedOnclickRecyclerListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
        this.isEditable = true;

    }

    public FarmerCollectionsAdapter(Context context, List<DayCollectionModel> modelList, AdvancedOnclickRecyclerListener listener, boolean isEditable) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
        this.isEditable = isEditable;


    }

    public void refresh(List<DayCollectionModel> collections) {
        this.modelList.clear();
        this.modelList.addAll(collections);
        notifyDataSetChanged();
    }

    public void refresh(List<DayCollectionModel> collections, boolean isEditable) {
        this.modelList.clear();
        this.isEditable = isEditable;
        this.modelList.addAll(collections);
        notifyDataSetChanged();
    }

    @Override
    public FarmerCollectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.payout_farmers_collections_card1, parent, false);

        return new FarmerCollectionViewHolder(itemView, listener, isEditable);
    }


    @Override
    public void onBindViewHolder(FarmerCollectionViewHolder holder, int position) {
        DayCollectionModel model = modelList.get(position);
        holder.day.setText(model.getDay());
        holder.date.setText(DateTimeUtils.Companion.getDisplayDate(model.getDate(), DateTimeUtils.Companion.getDisplayDatePattern1()));

        if (DateTimeUtils.Companion.isPastLastDay(model.getDate(), 1)) {
            holder.background_linear.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            holder.background_linear.setBackgroundColor(context.getResources().getColor(R.color.divider));

        }

        holder.milkTotalAm.setText(String.format("%s %s", model.getMilkAm(), context.getString(R.string.ltrs)));
        if (!model.getMilkAm().equals("0.0")) {
            holder.milkTotalAm.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.milkTotalAm.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.milkTotalAm.setTypeface(Typeface.DEFAULT);
            holder.milkTotalAm.setTextColor(context.getResources().getColor(R.color.black));

        }
        holder.milkTotalPm.setText(String.format("%s %s", model.getMilkPm(), context.getString(R.string.ltrs)));
        if (!model.getMilkPm().equals("0.0")) {
            holder.milkTotalPm.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.milkTotalPm.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.milkTotalPm.setTypeface(Typeface.DEFAULT);
            holder.milkTotalPm.setTextColor(context.getResources().getColor(R.color.black));

        }

        holder.loanTotalPm.setText(String.format("%s %s", model.getLoan(), context.getString(R.string.ksh)));

        if (!model.getLoan().equals("0.0")) {
            holder.loanTotalPm.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.loanTotalPm.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.loanTotalPm.setTypeface(Typeface.DEFAULT);

            holder.loanTotalPm.setTextColor(context.getResources().getColor(R.color.black));

        }

        holder.orderTotalPm.setText(String.format("%s %s", model.getOrder(), context.getString(R.string.ksh)));

        if (!model.getOrder().equals("0.0")) {
            holder.orderTotalPm.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.orderTotalPm.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.orderTotalPm.setTypeface(Typeface.DEFAULT);

            holder.orderTotalPm.setTextColor(context.getResources().getColor(R.color.black));

        }


        if (DateTimeUtils.Companion.isPastLastDay(model.getDate(), 1) || isEditable) {
            holder.milkTotalPm.setEnabled(true);
            holder.milkTotalAm.setEnabled(true);
            holder.loanTotalAm.setEnabled(true);
            holder.loanTotalPm.setEnabled(true);
            holder.orderTotalAm.setEnabled(true);
            holder.orderTotalPm.setEnabled(true);


        } else {
            holder.milkTotalPm.setEnabled(false);
            holder.milkTotalAm.setEnabled(false);
            holder.loanTotalAm.setEnabled(false);
            holder.loanTotalPm.setEnabled(false);
            holder.orderTotalAm.setEnabled(false);
            holder.orderTotalPm.setEnabled(false);


        }

    }


    @Override
    public int getItemCount() {
        return (null != modelList ? modelList.size() : 0);
    }


}
