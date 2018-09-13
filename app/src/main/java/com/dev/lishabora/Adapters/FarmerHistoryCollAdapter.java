package com.dev.lishabora.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.lishabora.Adapters.ViewHolders.MonthsFarmerCollViewHolder;
import com.dev.lishabora.Models.FarmerHistoryByDateModel;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.Views.Trader.FarmerToolBarUI;
import com.dev.lishaboramobile.R;

import java.util.List;

import static com.dev.lishaboramobile.R.string.ksh;
import static com.dev.lishaboramobile.R.string.ltrs;


public class FarmerHistoryCollAdapter extends RecyclerView.Adapter<MonthsFarmerCollViewHolder> {

    private Context context;
    private List<FarmerHistoryByDateModel> modelList;
    private OnclickRecyclerListener listener;
    private int which = 0;

    public FarmerHistoryCollAdapter(Context context, List<FarmerHistoryByDateModel> modelList, OnclickRecyclerListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
        this.which = 999;

    }
//    public MonthlyFarmerCollAdapter(Context context, List<FarmerHistoryByDateModel> modelList, OnclickRecyclerListener listener) {
//        this.context = context;
//        this.modelList = modelList;
//        this.listener = listener;
//        this.isChk = false;
//
//    }

    public FarmerHistoryCollAdapter(Context context, List<FarmerHistoryByDateModel> modelList, OnclickRecyclerListener listener, int which) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
        this.which = which;

    }

    @Override
    public MonthsFarmerCollViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_farmercollection_card, parent, false);

        return new MonthsFarmerCollViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(MonthsFarmerCollViewHolder holder, int position) {
        FarmerHistoryByDateModel model = modelList.get(position);

        holder.month.setText(model.getDate());

        if (which == FarmerToolBarUI.TYPE_ALL) {


            holder.milk.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getMilktotal(), 1), context.getString(R.string.ltrs)));
            if (!model.getMilktotal().equals("0.0")) {
                holder.milk.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.milk.setTypeface(Typeface.DEFAULT_BOLD);


            } else {
                holder.milk.setTypeface(Typeface.DEFAULT);
                holder.milk.setTextColor(context.getResources().getColor(R.color.black));

            }


            holder.loan.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getLoanTotal(), 1), context.getString(ksh)));
            if (!model.getLoanTotal().equals("0.0")) {

                holder.loan.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.loan.setTypeface(Typeface.DEFAULT_BOLD);

            } else {
                holder.loan.setTypeface(Typeface.DEFAULT);

                holder.loan.setTextColor(context.getResources().getColor(R.color.black));

            }

            holder.order.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getOrderTotal(), 1), context.getString(ksh)));
            if (!model.getOrderTotal().equals("0.0")) {
                holder.order.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.order.setTypeface(Typeface.DEFAULT_BOLD);

            } else {
                holder.order.setTypeface(Typeface.DEFAULT);

                holder.order.setTextColor(context.getResources().getColor(R.color.black));

            }
        } else {
            if (which == FarmerToolBarUI.TYPE_MILK) {
                holder.milk.setVisibility(View.GONE);
                holder.loan.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getMilkAm(), 1), context.getString(ltrs)));
                if (!model.getLoanTotal().equals("0.0")) {

                    holder.loan.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    holder.loan.setTypeface(Typeface.DEFAULT_BOLD);

                } else {
                    holder.loan.setTypeface(Typeface.DEFAULT);

                    holder.loan.setTextColor(context.getResources().getColor(R.color.black));

                }

                holder.order.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getMilkPm(), 1), context.getString(ltrs)));
                if (!model.getOrderTotal().equals("0.0")) {
                    holder.order.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    holder.order.setTypeface(Typeface.DEFAULT_BOLD);

                } else {
                    holder.order.setTypeface(Typeface.DEFAULT);

                    holder.order.setTextColor(context.getResources().getColor(R.color.black));

                }


            } else if (which == FarmerToolBarUI.TYPE_LOAN) {
                holder.milk.setVisibility(View.GONE);
                holder.loan.setVisibility(View.GONE);

                holder.order.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getLoanTotal(), 1), context.getString(ksh)));
                if (!model.getOrderTotal().equals("0.0")) {
                    holder.order.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    holder.order.setTypeface(Typeface.DEFAULT_BOLD);

                } else {
                    holder.order.setTypeface(Typeface.DEFAULT);

                    holder.order.setTextColor(context.getResources().getColor(R.color.black));

                }


            } else if (which == FarmerToolBarUI.TYPE_ORDERS) {
                holder.milk.setVisibility(View.GONE);
                holder.loan.setVisibility(View.GONE);

                holder.order.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getOrderTotal(), 1), context.getString(ksh)));
                if (!model.getOrderTotal().equals("0.0")) {
                    holder.order.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    holder.order.setTypeface(Typeface.DEFAULT_BOLD);

                } else {
                    holder.order.setTypeface(Typeface.DEFAULT);

                    holder.order.setTextColor(context.getResources().getColor(R.color.black));

                }


            }
        }


    }


    @Override
    public int getItemCount() {
        return (null != modelList ? modelList.size() : 0);
    }


}
