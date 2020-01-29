package com.dev.zaidi.Adapters.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dev.zaidi.R;
import com.dev.zaidi.Utils.OnclickRecyclerListener;

import java.lang.ref.WeakReference;

//import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

public class PayoutFarmersPayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    //  public RelativeLayout engineer_background;
    public TextView deleteLayoutTxtApprove;


    public TextView name, balance, milk, deductions;
    public View itemVew;
    public CheckBox chk;
    private WeakReference<OnclickRecyclerListener> listenerWeakReference;


    public PayoutFarmersPayViewHolder(View itemView, OnclickRecyclerListener onclickRecyclerListener) {
        super(itemView);
        this.itemVew = itemView;
        listenerWeakReference = new WeakReference<>(onclickRecyclerListener);
        name = itemView.findViewById(R.id.txt_name);
        balance = itemView.findViewById(R.id.txt_balance);
        deductions = itemView.findViewById(R.id.txt_deductions);
        chk = itemView.findViewById(R.id.approve);
        chk.setOnClickListener(this::onClick);


        milk = itemView.findViewById(R.id.txt_milk);

    }


    @Override
    public void onClick(View v) {
        listenerWeakReference.get().onClickListener(getAdapterPosition());
        // listenerWeakReference.get().onMenuItem(getAdapterPosition(), 4);

    }

    @Override
    public boolean onLongClick(View v) {
        listenerWeakReference.get().onClickListener(getAdapterPosition(), v);

        return true;
    }
}
