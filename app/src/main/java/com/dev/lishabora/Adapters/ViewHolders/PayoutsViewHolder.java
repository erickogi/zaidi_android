package com.dev.lishabora.Adapters.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;

import java.lang.ref.WeakReference;

public class PayoutsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    //  public RelativeLayout engineer_background;

    public TextView status, startDate, cycleName, endDate, milkTotal, loanTotal, orderTotal, balance, approvedCount, unApprovedCount;
    public RelativeLayout background;
    public LinearLayout approvedView, pendingView;
    public View statusview;
    private WeakReference<OnclickRecyclerListener> listenerWeakReference;


    public PayoutsViewHolder(View itemView, OnclickRecyclerListener onclickRecyclerListener) {
        super(itemView);
        listenerWeakReference = new WeakReference<>(onclickRecyclerListener);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        statusview = itemView.findViewById(R.id.status_view);
        background = itemView.findViewById(R.id.background);
        startDate = itemView.findViewById(R.id.txt_date_start);
        endDate = itemView.findViewById(R.id.txt_date_end);
        status = itemView.findViewById(R.id.txt_status);

        approvedView = itemView.findViewById(R.id.linear_approved_count);
        pendingView = itemView.findViewById(R.id.linear_pending_count);



        cycleName = itemView.findViewById(R.id.txt_cycle);

        milkTotal = itemView.findViewById(R.id.txt_milk_totals);
        loanTotal = itemView.findViewById(R.id.txt_loans_total);
        orderTotal = itemView.findViewById(R.id.txt_orders_total);

        approvedCount = itemView.findViewById(R.id.txt_approved_farmers);
        unApprovedCount = itemView.findViewById(R.id.txt_pending_farmers);
        balance = itemView.findViewById(R.id.txt_Bal_out);


    }

    @Override
    public void onClick(View v) {
        listenerWeakReference.get().onClickListener(getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View v) {
        listenerWeakReference.get().onClickListener(getAdapterPosition(), v);

        return true;
    }
}
