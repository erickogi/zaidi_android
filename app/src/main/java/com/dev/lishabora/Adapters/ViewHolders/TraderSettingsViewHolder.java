package com.dev.lishabora.Adapters.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;

import java.lang.ref.WeakReference;

public class TraderSettingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    //  public RelativeLayout engineer_background;

    public TextView title, subtitle;
    public Button btnSki;
    public ImageView icon;
    private WeakReference<OnclickRecyclerListener> listenerWeakReference;


    public TraderSettingsViewHolder(View itemView, OnclickRecyclerListener onclickRecyclerListener) {
        super(itemView);
        listenerWeakReference = new WeakReference<>(onclickRecyclerListener);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        title = itemView.findViewById(R.id.title);
        subtitle = itemView.findViewById(R.id.subtitle);
        icon = itemView.findViewById(R.id.icon);
        btnSki = itemView.findViewById(R.id.btn_skip);
        btnSki.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_skip:
                listenerWeakReference.get().onClickListener(getAdapterPosition(), v);
                break;
            default:
                listenerWeakReference.get().onClickListener(getAdapterPosition());

        }

    }

    @Override
    public boolean onLongClick(View v) {
        listenerWeakReference.get().onClickListener(getAdapterPosition(), v);

        return true;
    }
}
