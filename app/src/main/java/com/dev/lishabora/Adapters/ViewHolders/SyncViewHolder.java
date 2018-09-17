package com.dev.lishabora.Adapters.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;

import java.lang.ref.WeakReference;

public class SyncViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    //  public RelativeLayout engineer_background;

    public TextView txt_action, txt_entity, txt_model, txt_timestamp, txt_syn_time, txt_syn_status;
    public RelativeLayout background;
    public View statusview;
    private WeakReference<OnclickRecyclerListener> listenerWeakReference;

    public SyncViewHolder(View itemView, OnclickRecyclerListener onclickRecyclerListener) {
        super(itemView);
        listenerWeakReference = new WeakReference<>(onclickRecyclerListener);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        txt_action = itemView.findViewById(R.id.txt_action);
        txt_entity = itemView.findViewById(R.id.txt_entity);
        txt_model = itemView.findViewById(R.id.txt_model);
        txt_timestamp = itemView.findViewById(R.id.txt_timestamp);
        txt_syn_time = itemView.findViewById(R.id.txt_syn_time);
        txt_syn_status = itemView.findViewById(R.id.txt_syn_status);
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
