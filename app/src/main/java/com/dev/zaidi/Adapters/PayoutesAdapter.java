package com.dev.zaidi.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.zaidi.Adapters.ViewHolders.PayoutsViewHolder;
import com.dev.zaidi.Models.Payouts;
import com.dev.zaidi.R;
import com.dev.zaidi.Utils.DateTimeUtils;
import com.dev.zaidi.Utils.GeneralUtills;
import com.dev.zaidi.Utils.OnclickRecyclerListener;

import java.util.List;

//import com.dev.zaidi.Trader.Models.Payouts;

public class PayoutesAdapter extends RecyclerView.Adapter<PayoutsViewHolder> {

    private Context context;
    private List<Payouts> modelList;
    private OnclickRecyclerListener listener;
    private boolean hideCounts = false;

    public PayoutesAdapter(Context context, List<Payouts> modelList, OnclickRecyclerListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
        this.hideCounts = false;

    }

    public PayoutesAdapter(Context context, List<Payouts> modelList, OnclickRecyclerListener listener, boolean hideCounts) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
        this.hideCounts = hideCounts;

    }


    @Override
    public PayoutsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.payout_card, parent, false);

        return new PayoutsViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(PayoutsViewHolder holder, int position) {
        Payouts model = modelList.get(position);


        holder.startDate.setText(DateTimeUtils.Companion.getDisplayDate(model.getStartDate(), DateTimeUtils.Companion.getDisplayDatePattern1()));
        holder.endDate.setText(DateTimeUtils.Companion.getDisplayDate(model.getEndDate(), DateTimeUtils.Companion.getDisplayDatePattern1()));


        holder.cycleName.setText(model.getCyclename());
        holder.status.setText(model.getStatusName());


        String vB = model.getBalance();
        String vL = model.getLoanTotal();
        String vO = model.getOrderTotal();
        String vM = model.getMilkTotalLtrs();


        try {
            vB = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vB, 0)));
            vL = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vL, 1)));
            vO = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vO, 1)));
            vM = GeneralUtills.Companion.addCommify(String.valueOf(GeneralUtills.Companion.round(vM, 1)));
        } catch (Exception nm) {
            nm.printStackTrace();
        }

        holder.milkTotal.setText(String.format("%s%s", vM, context.getString(R.string.ltrs)));
        holder.loanTotal.setText(String.format("%s%s", vL, context.getString(R.string.ksh)));
        holder.orderTotal.setText(String.format("%s%s", vO, context.getString(R.string.ksh)));
        holder.balance.setText(String.format("%s%s", vB, context.getString(R.string.ksh)));

        GeneralUtills.Companion.changeCOlor(model.getBalance(), holder.balance, 1);

        holder.approvedCount.setText(model.getApprovedCards());
        holder.unApprovedCount.setText(model.getPendingCards());


        if (model.getStatus() == 1) {
            //  holder.status.setText("Active");
            holder.status.setTextColor(context.getResources().getColor(R.color.green_color_picker));
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.green_color_picker));
            holder.statusview.setBackgroundColor(context.getResources().getColor(R.color.green_color_picker));


        } else if (model.getStatus() == 0) {

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

        if (hideCounts) {
            holder.approvedView.setVisibility(View.GONE);
            holder.pendingView.setVisibility(View.GONE);
        } else {
            holder.approvedView.setVisibility(View.VISIBLE);
            holder.pendingView.setVisibility(View.VISIBLE);
        }


    }


    @Override
    public int getItemCount() {
        return (null != modelList ? modelList.size() : 0);
    }


    public void refresh(List<Payouts> payoutsList) {
        if (payoutsList != null) {
            modelList = payoutsList;
            notifyDataSetChanged();
        }
    }
}
