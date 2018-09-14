package com.dev.lishabora.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.lishabora.Adapters.ViewHolders.CollectionViewHolder;
import com.dev.lishabora.Models.DayCollectionModel;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;

import java.util.List;


public class CollectionsAdapter extends RecyclerView.Adapter<CollectionViewHolder> {

    private Context context;
    private List<DayCollectionModel> modelList;
    private OnclickRecyclerListener listener;
    private boolean isChk = false;

    public CollectionsAdapter(Context context, List<DayCollectionModel> modelList, OnclickRecyclerListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
        this.isChk = false;

    }

    public CollectionsAdapter(Context context, List<DayCollectionModel> modelList, OnclickRecyclerListener listener, boolean isChk) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
        this.isChk = isChk;

    }

    @Override
    public CollectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.payout_collections_card1, parent, false);

        return new CollectionViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(CollectionViewHolder holder, int position) {
        DayCollectionModel model = modelList.get(position);

        holder.day.setText(model.getDay());


        holder.date.setText(DateTimeUtils.Companion.getDisplayDate(model.getDate(), DateTimeUtils.Companion.getDisplayDatePattern1()));

        if (DateTimeUtils.Companion.isPastLastDay(model.getDate(), 1)) {
            holder.background_linear.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            holder.background_linear.setBackgroundColor(context.getResources().getColor(R.color.divider));

        }


        holder.milkTotalAm.setText(GeneralUtills.Companion.round(model.getMilkAmLtrs(), 1));
        if (!model.getMilkAm().equals("0.0")) {
            holder.milkTotalAm.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.milkTotalAm.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.milkTotalAm.setTypeface(Typeface.DEFAULT);

            holder.milkTotalAm.setTextColor(context.getResources().getColor(R.color.black));

        }
        holder.milkTotalPm.setText(GeneralUtills.Companion.round(model.getMilkPmLtrs(), 1));
        if (!model.getMilkPm().equals("0.0")) {
            holder.milkTotalPm.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.milkTotalPm.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.milkTotalPm.setTypeface(Typeface.DEFAULT);

            holder.milkTotalPm.setTextColor(context.getResources().getColor(R.color.black));

        }


        holder.loanTotalPm.setText(GeneralUtills.Companion.round(model.getLoan(), 1));

        if (!model.getLoan().equals("0.0")) {
            holder.loanTotalPm.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.loanTotalPm.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.loanTotalPm.setTypeface(Typeface.DEFAULT);

            holder.loanTotalPm.setTextColor(context.getResources().getColor(R.color.black));

        }

        holder.orderTotalPm.setText(model.getOrder());

        if (!model.getOrder().equals("0.0")) {
            holder.orderTotalPm.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.orderTotalPm.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.orderTotalPm.setTypeface(Typeface.DEFAULT);

            holder.orderTotalPm.setTextColor(context.getResources().getColor(R.color.black));

        }


        if (!DateTimeUtils.Companion.isPastLastDay(model.getDate(), 1)) {
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
//
//
//        holder.loanTotalAm.setText(model.getLoanAm());
//        if (!model.getLoanAm().equals("0.0")) {
//            holder.loanTotalAm.setTextColor(context.getResources().getColor(R.color.colorPrimary));
//            holder.loanTotalAm.setTypeface(Typeface.DEFAULT_BOLD);
//
//        } else {
//            holder.loanTotalAm.setTypeface(Typeface.DEFAULT);
//
//            holder.loanTotalAm.setTextColor(context.getResources().getColor(R.color.black));
//
//        }
//        holder.loanTotalPm.setText(model.getLoanPm());
//        if (!model.getLoanPm().equals("0.0")) {
//            holder.loanTotalPm.setTextColor(context.getResources().getColor(R.color.colorPrimary));
//            holder.loanTotalPm.setTypeface(Typeface.DEFAULT_BOLD);
//        } else {
//            holder.loanTotalPm.setTypeface(Typeface.DEFAULT);
//
//            holder.loanTotalPm.setTextColor(context.getResources().getColor(R.color.black));
//
//        }
//        holder.orderTotalAm.setText(model.getOrderAm());
//        if (!model.getOrderAm().equals("0.0")) {
//            holder.orderTotalAm.setTextColor(context.getResources().getColor(R.color.colorPrimary));
//            holder.orderTotalAm.setTypeface(Typeface.DEFAULT_BOLD);
//
//        } else {
//            holder.orderTotalAm.setTypeface(Typeface.DEFAULT);
//
//            holder.orderTotalAm.setTextColor(context.getResources().getColor(R.color.black));
//
//        }
//        holder.orderTotalPm.setText(model.getOrderPm());
//        if (!model.getOrderPm().equals("0.0")) {
//            holder.orderTotalPm.setTextColor(context.getResources().getColor(R.color.colorPrimary));
//            holder.orderTotalPm.setTypeface(Typeface.DEFAULT_BOLD);
//
//        } else {
//            holder.orderTotalPm.setTypeface(Typeface.DEFAULT);
//
//            holder.orderTotalPm.setTextColor(context.getResources().getColor(R.color.black));
//
//        }


    }


    @Override
    public int getItemCount() {
        return (null != modelList ? modelList.size() : 0);
    }


}
