package com.dev.lishaboramobile.admin.adapters.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.lishaboramobile.Global.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;

import java.lang.ref.WeakReference;

public class ProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    //  public RelativeLayout engineer_background;

    public TextView status, id, name, selling, cost, famerscount, txtDate;
    public CheckBox chk;
    private WeakReference<OnclickRecyclerListener> listenerWeakReference;
    public RelativeLayout background;
    public View statusview;


    public ProductsViewHolder(View itemView, OnclickRecyclerListener onclickRecyclerListener) {
        super(itemView);
        listenerWeakReference = new WeakReference<>(onclickRecyclerListener);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        statusview = itemView.findViewById(R.id.status_view);
        background = itemView.findViewById(R.id.background);
        txtDate = itemView.findViewById(R.id.txt_date);

        status = itemView.findViewById(R.id.txt_status);
        id = itemView.findViewById(R.id.txt_id);
        name = itemView.findViewById(R.id.txt_name);
        selling = itemView.findViewById(R.id.txt_selling_price);
        cost = itemView.findViewById(R.id.txt_cost_price);
        chk = itemView.findViewById(R.id.chk_add);
        chk.setOnClickListener(this::onClick);

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
