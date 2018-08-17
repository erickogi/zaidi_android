package com.dev.lishabora.Adapters.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;

import java.lang.ref.WeakReference;

public class CollectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public TextView date, day, milkTotalAm, milkTotalPm, loanTotalAm, loanTotalPm, orderTotalAm, orderTotalPm;
    public RelativeLayout background;
    public View statusview;
    private WeakReference<OnclickRecyclerListener> listenerWeakReference;


    public CollectionViewHolder(View itemView, OnclickRecyclerListener onclickRecyclerListener) {
        super(itemView);
        listenerWeakReference = new WeakReference<>(onclickRecyclerListener);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        statusview = itemView.findViewById(R.id.status_view);
        background = itemView.findViewById(R.id.background);
        date = itemView.findViewById(R.id.txt_date);
        day = itemView.findViewById(R.id.txt_day);
        milkTotalAm = itemView.findViewById(R.id.txt_am_milk);
        milkTotalPm = itemView.findViewById(R.id.txt_pm_milk);
        loanTotalAm = itemView.findViewById(R.id.txt_am_loans);
        loanTotalPm = itemView.findViewById(R.id.txt_pm_loans);
        orderTotalAm = itemView.findViewById(R.id.txt_am_orders);
        orderTotalPm = itemView.findViewById(R.id.txt_pm_orders);


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
