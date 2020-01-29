package com.dev.zaidi.Adapters.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.zaidi.R;
import com.dev.zaidi.Utils.OnclickRecyclerListener;

import java.lang.ref.WeakReference;

//import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

public class MonthsFarmerCollViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    //  public RelativeLayout engineer_background;

    public TextView month, balance, milk, loan, order;
    public LinearLayout milkSection, loanSection, orderSection;
    public RelativeLayout background;
    public View statusview;
    public View itemVew;
    LinearLayout linear_totals, linear_status;
    private WeakReference<OnclickRecyclerListener> listenerWeakReference;


    public MonthsFarmerCollViewHolder(View itemView, OnclickRecyclerListener onclickRecyclerListener) {
        super(itemView);
        this.itemVew = itemView;
        listenerWeakReference = new WeakReference<>(onclickRecyclerListener);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        statusview = itemView.findViewById(R.id.status_view);
        background = itemView.findViewById(R.id.background);
        background.setOnLongClickListener(this);

        linear_totals = itemView.findViewById(R.id.linear_totals);
        linear_totals.setOnClickListener(this);


        month = itemView.findViewById(R.id.txt_month);
        balance = itemView.findViewById(R.id.txt_balance);


        milk = itemView.findViewById(R.id.txt_milk);
        loan = itemView.findViewById(R.id.txt_loans);
        order = itemView.findViewById(R.id.txt_orders);


        milkSection = itemView.findViewById(R.id.milk_section);
        loanSection = itemView.findViewById(R.id.loan_section);
        orderSection = itemView.findViewById(R.id.order_section);
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
