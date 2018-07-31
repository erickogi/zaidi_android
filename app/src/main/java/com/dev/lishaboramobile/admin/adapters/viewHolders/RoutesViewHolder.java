package com.dev.lishaboramobile.admin.adapters.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.lishaboramobile.Global.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;

import java.lang.ref.WeakReference;

public class RoutesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    //  public RelativeLayout engineer_background;

    public TextView status, name, famerscount;
    private WeakReference<OnclickRecyclerListener> listenerWeakReference;
    public RelativeLayout background;
    public View statusview;


    public RoutesViewHolder(View itemView, OnclickRecyclerListener onclickRecyclerListener) {
        super(itemView);
        listenerWeakReference = new WeakReference<>(onclickRecyclerListener);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        statusview = itemView.findViewById(R.id.status_view);
        background = itemView.findViewById(R.id.background);

        status = itemView.findViewById(R.id.txt_status);
        name = itemView.findViewById(R.id.txt_name);
        famerscount = itemView.findViewById(R.id.txt_route_farmers);

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
