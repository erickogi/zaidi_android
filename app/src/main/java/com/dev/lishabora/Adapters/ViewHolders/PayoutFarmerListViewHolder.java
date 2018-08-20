package com.dev.lishabora.Adapters.ViewHolders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import java.lang.ref.WeakReference;

public class PayoutFarmerListViewHolder extends AbstractDraggableItemViewHolder implements View.OnClickListener, View.OnLongClickListener {
    //  public RelativeLayout engineer_background;

    public TextView status, id, name, balance, milk, loan, order;
    public RelativeLayout background;
    LinearLayout linear_totals, linear_status;
    public View statusview;
    public View itemVew;
    private WeakReference<OnclickRecyclerListener> listenerWeakReference;


    public PayoutFarmerListViewHolder(View itemView, OnclickRecyclerListener onclickRecyclerListener) {
        super(itemView);
        this.itemVew = itemView;
        listenerWeakReference = new WeakReference<>(onclickRecyclerListener);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        statusview = itemView.findViewById(R.id.status_view);
        background = itemView.findViewById(R.id.background);
        background.setOnLongClickListener(this);

        linear_totals = itemView.findViewById(R.id.linear_totals);
        linear_status = itemView.findViewById(R.id.linear_status);

        linear_status.setOnClickListener(this);
        linear_totals.setOnClickListener(this);


        status = itemView.findViewById(R.id.txt_status);
        id = itemView.findViewById(R.id.txt_id);
        name = itemView.findViewById(R.id.txt_name);
        balance = itemView.findViewById(R.id.txt_balance);


        milk = itemView.findViewById(R.id.txt_milk);
        loan = itemView.findViewById(R.id.txt_loans);
        order = itemView.findViewById(R.id.txt_orders);


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
