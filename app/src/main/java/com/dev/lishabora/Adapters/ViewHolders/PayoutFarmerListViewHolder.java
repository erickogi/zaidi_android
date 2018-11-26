package com.dev.lishabora.Adapters.ViewHolders;

import android.support.design.button.MaterialButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;

import java.lang.ref.WeakReference;

//import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

public class PayoutFarmerListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    //  public RelativeLayout engineer_background;
    public TextView deleteLayoutTxtApprove;


    public TextView status, id, name, balance, milk, loan, order;
    public RelativeLayout background;
    LinearLayout linear_totals, linear_status;
    public View statusview;
    public View itemVew;
    public MaterialButton btnApprove;
    private WeakReference<OnclickRecyclerListener> listenerWeakReference;


    public PayoutFarmerListViewHolder(View itemView, OnclickRecyclerListener onclickRecyclerListener) {
        super(itemView);
        this.itemVew = itemView;
        listenerWeakReference = new WeakReference<>(onclickRecyclerListener);
        // itemView.setOnClickListener(this);
        // itemView.setOnLongClickListener(this);
        statusview = itemView.findViewById(R.id.status_view);
        //  background = itemView.findViewById(R.id.background);
        // background.setOnLongClickListener(this);

        //  linear_totals = itemView.findViewById(R.id.linear_totals);
        //  linear_status = itemView.findViewById(R.id.linear_status);

//        linear_status.setOnClickListener(this);
//        linear_totals.setOnClickListener(this);

        btnApprove = itemView.findViewById(R.id.btn_approve);

        status = itemView.findViewById(R.id.txt_status);
        id = itemView.findViewById(R.id.txt_id);
        name = itemView.findViewById(R.id.txt_name);
        balance = itemView.findViewById(R.id.txt_balance);


        milk = itemView.findViewById(R.id.txt_milk);
        loan = itemView.findViewById(R.id.txt_loans);
        order = itemView.findViewById(R.id.txt_orders);

//        deleteLayoutTxtApprove = itemView.findViewById(R.id.txt_approve);

    }


    @Override
    public void onClick(View v) {
        // listenerWeakReference.get().onClickListener(getAdapterPosition());
        // listenerWeakReference.get().onMenuItem(getAdapterPosition(), 4);

    }

    @Override
    public boolean onLongClick(View v) {
        // listenerWeakReference.get().onClickListener(getAdapterPosition(), v);

        return true;
    }
}
