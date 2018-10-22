package com.dev.lishabora.Adapters.ViewHolders;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;

import java.lang.ref.WeakReference;

public class LoanOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public TextView status, farmer, amount, installment, balance, installmentbal, txtDate;
    public View itemVew;
    private WeakReference<OnclickRecyclerListener> listenerWeakReference;


    public LoanOrderViewHolder(View itemView, OnclickRecyclerListener onclickRecyclerListener) {
        super(itemView);
        this.itemVew = itemView;
        listenerWeakReference = new WeakReference<>(onclickRecyclerListener);

        txtDate = itemView.findViewById(R.id.txt_date);

        status = itemView.findViewById(R.id.txt_status);

        balance = itemView.findViewById(R.id.txt_balance);

        farmer = itemView.findViewById(R.id.txt_farmer);

        amount = itemView.findViewById(R.id.txt_amount);

        installment = itemView.findViewById(R.id.txt_installment);

        installmentbal = itemView.findViewById(R.id.txt_pending_installments);

        installmentbal.setVisibility(View.GONE);


        itemView.setOnClickListener(this);
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
