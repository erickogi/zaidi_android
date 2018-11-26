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

        String vL = model.getLoan();
        String vO = model.getOrder();
        String vMKA = model.getMilkAmKsh();
        String vMKP = model.getMilkPmKsh();
        String vMLA = model.getMilkAmLtrs();
        String vMLP = model.getMilkPmLtrs();


        try {
            vL = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vL, 0)));
            vO = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vO, 0)));

            vMKA = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vMKA, 0)));
            vMKP = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vMKP, 0)));
            vMLA = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vMLA, 0)));
            vMLP = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vMLP, 0)));
        } catch (Exception nm) {
            nm.printStackTrace();
        }


        holder.milkTotalAm.setText(String.format("%s %s", vMLA, context.getString(R.string.ltrs)));
        if (!model.getMilkAm().equals("0.0")) {
            holder.milkTotalAm.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.milkTotalAm.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.milkTotalAm.setTypeface(Typeface.DEFAULT);

            holder.milkTotalAm.setTextColor(context.getResources().getColor(R.color.black));

        }
        holder.milkTotalPm.setText(String.format("%s %s", vMLP, context.getString(R.string.ltrs)));
        if (!model.getMilkPm().equals("0.0")) {
            holder.milkTotalPm.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.milkTotalPm.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.milkTotalPm.setTypeface(Typeface.DEFAULT);

            holder.milkTotalPm.setTextColor(context.getResources().getColor(R.color.black));

        }


        holder.loanTotalPm.setText(String.format("%s %s", vL, context.getString(R.string.ksh)));

        if (!model.getLoan().equals("0.0")) {
            holder.loanTotalPm.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.loanTotalPm.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.loanTotalPm.setTypeface(Typeface.DEFAULT);

            holder.loanTotalPm.setTextColor(context.getResources().getColor(R.color.black));

        }

        holder.orderTotalPm.setText(String.format("%s %s", vO, context.getString(R.string.ltrs)));

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


    }


    @Override
    public int getItemCount() {
        return (null != modelList ? modelList.size() : 0);
    }


}
