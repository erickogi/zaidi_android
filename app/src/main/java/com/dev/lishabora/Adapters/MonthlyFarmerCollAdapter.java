package com.dev.lishabora.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.lishabora.Adapters.ViewHolders.MonthsFarmerCollViewHolder;
import com.dev.lishabora.Models.FarmerHistoryByDateModel;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;

import java.util.List;

import static com.dev.lishaboramobile.R.string.ksh;


public class MonthlyFarmerCollAdapter extends RecyclerView.Adapter<MonthsFarmerCollViewHolder> {

    private Context context;
    private List<FarmerHistoryByDateModel> modelList;
    private OnclickRecyclerListener listener;
    private boolean isChk = false;

    public MonthlyFarmerCollAdapter(Context context, List<FarmerHistoryByDateModel> modelList, OnclickRecyclerListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
        this.isChk = false;

    }

    public MonthlyFarmerCollAdapter(Context context, List<FarmerHistoryByDateModel> modelList, OnclickRecyclerListener listener, boolean isChk) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
        this.isChk = isChk;

    }

    @Override
    public MonthsFarmerCollViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.month_farmercollection_card, parent, false);

        return new MonthsFarmerCollViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(MonthsFarmerCollViewHolder holder, int position) {
        FarmerHistoryByDateModel model = modelList.get(position);

        holder.month.setText(model.getMonthsDates().getMonthName());


        holder.milk.setText(String.format("%s %s", model.getMilktotal(), context.getString(R.string.ltrs)));
        if (!model.getMilktotal().equals("0.0")) {
            holder.milk.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.milk.setTypeface(Typeface.DEFAULT_BOLD);


        } else {
            holder.milk.setTypeface(Typeface.DEFAULT);
            holder.milk.setTextColor(context.getResources().getColor(R.color.black));

        }


        holder.loan.setText(String.format("%s %s", model.getLoanTotal(), context.getString(ksh)));
        if (!model.getLoanTotal().equals("0.0")) {

            holder.loan.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.loan.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.loan.setTypeface(Typeface.DEFAULT);

            holder.loan.setTextColor(context.getResources().getColor(R.color.black));

        }

        holder.order.setText(String.format("%s %s", model.getOrderTotal(), context.getString(ksh)));
        if (!model.getOrderTotal().equals("0.0")) {
            holder.order.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.order.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            holder.order.setTypeface(Typeface.DEFAULT);

            holder.order.setTextColor(context.getResources().getColor(R.color.black));

        }


    }


    @Override
    public int getItemCount() {
        return (null != modelList ? modelList.size() : 0);
    }


}
