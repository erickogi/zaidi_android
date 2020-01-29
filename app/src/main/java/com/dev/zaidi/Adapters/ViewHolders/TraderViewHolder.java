package com.dev.zaidi.Adapters.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.zaidi.R;
import com.dev.zaidi.Utils.OnclickRecyclerListener;

import java.lang.ref.WeakReference;

public class TraderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    //  public RelativeLayout engineer_background;

    public TextView status, id, name, phone, balance, famerscount, txtDate;
    private WeakReference<OnclickRecyclerListener> listenerWeakReference;

    public RelativeLayout background;
    public View statusview;
    public RelativeLayout caard;

    public TraderViewHolder(View itemView, OnclickRecyclerListener onclickRecyclerListener) {
        super(itemView);
        listenerWeakReference = new WeakReference<>(onclickRecyclerListener);
        //itemView.setOnClickListener(this);
        //itemView.setOnLongClickListener(this);
        txtDate = itemView.findViewById(R.id.txt_date);
        caard = itemView.findViewById(R.id.rl);

        statusview = itemView.findViewById(R.id.status_view);
        background = itemView.findViewById(R.id.background);
        status = itemView.findViewById(R.id.txt_status);
        id = itemView.findViewById(R.id.txt_id);
        name = itemView.findViewById(R.id.txt_name);
        phone = itemView.findViewById(R.id.txt_phone);
        balance = itemView.findViewById(R.id.txt_balance);

       // itemView.setOnClickListener(this);
       // caard.setOnClickListener(this);
       // itemView.setOnLongClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(itemView.getContext(),"Clicked",Toast.LENGTH_LONG).show();
        listenerWeakReference.get().onClickListener(getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View v) {
        listenerWeakReference.get().onClickListener(getAdapterPosition(), v);

        return true;
    }
}
