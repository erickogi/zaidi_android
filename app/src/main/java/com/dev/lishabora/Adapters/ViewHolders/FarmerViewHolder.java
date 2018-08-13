package com.dev.lishabora.Adapters.ViewHolders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import java.lang.ref.WeakReference;

public class FarmerViewHolder extends AbstractDraggableItemViewHolder implements View.OnClickListener, View.OnLongClickListener {
    //  public RelativeLayout engineer_background;

    public TextView status, id, name, cycle, balance, famerscount, route, txtDate;
    private WeakReference<OnclickRecyclerListener> listenerWeakReference;
    public RelativeLayout background;
    public View statusview;
    public View itemVew;
    public LinearLayout lBig;
    public View dragHandle;


    public FarmerViewHolder(View itemView, OnclickRecyclerListener onclickRecyclerListener) {
        super(itemView);
        this.itemVew = itemView;
        listenerWeakReference = new WeakReference<>(onclickRecyclerListener);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        lBig = itemView.findViewById(R.id.l_big);
        lBig.setOnLongClickListener(this);
        statusview = itemView.findViewById(R.id.status_view);
        background = itemView.findViewById(R.id.background);
        background.setOnLongClickListener(this);

        txtDate = itemView.findViewById(R.id.txt_date);

        status = itemView.findViewById(R.id.txt_status);
        id = itemView.findViewById(R.id.txt_id);
        name = itemView.findViewById(R.id.txt_name);
        cycle = itemView.findViewById(R.id.txt_cycle);
        route = itemView.findViewById(R.id.txt_route);
        balance = itemView.findViewById(R.id.txt_balance);

    }

    public FarmerViewHolder(View itemView) {
        super(itemView);
        dragHandle = itemView.findViewById(R.id.drag_handle);
        this.itemVew = itemView;
        lBig = itemView.findViewById(R.id.l_big);
        statusview = itemView.findViewById(R.id.status_view);
        background = itemView.findViewById(R.id.background);

        txtDate = itemView.findViewById(R.id.txt_date);

        status = itemView.findViewById(R.id.txt_status);
        id = itemView.findViewById(R.id.txt_id);
        name = itemView.findViewById(R.id.txt_name);
        cycle = itemView.findViewById(R.id.txt_cycle);
        route = itemView.findViewById(R.id.txt_route);
        balance = itemView.findViewById(R.id.txt_balance);

        //lBig.setVisibility(View.GONE);

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
