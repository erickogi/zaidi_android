package com.dev.lishaboramobile.Views.Trader;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dev.lishaboramobile.Global.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;

import java.lang.ref.WeakReference;

public class FarmerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    //  public RelativeLayout engineer_background;

    public TextView status, id, name, cycle, balance, famerscount;
    private WeakReference<OnclickRecyclerListener> listenerWeakReference;


    public FarmerViewHolder(View itemView, OnclickRecyclerListener onclickRecyclerListener) {
        super(itemView);
        listenerWeakReference = new WeakReference<>(onclickRecyclerListener);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        status = itemView.findViewById(R.id.txt_status);
        id = itemView.findViewById(R.id.txt_id);
        name = itemView.findViewById(R.id.txt_name);
        cycle = itemView.findViewById(R.id.txt_cycle);
        balance = itemView.findViewById(R.id.txt_balance);

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
