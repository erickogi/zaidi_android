package com.dev.zaidi.Adapters.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.zaidi.R;
import com.dev.zaidi.Utils.OnclickRecyclerListener;

import java.lang.ref.WeakReference;

public class ReportListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    //  public RelativeLayout engineer_background;

    public TextView day, date, value1, value2;
    public LinearLayout background;
    private WeakReference<OnclickRecyclerListener> listenerWeakReference;


    public ReportListViewHolder(View itemView, OnclickRecyclerListener onclickRecyclerListener) {
        super(itemView);
        listenerWeakReference = new WeakReference<>(onclickRecyclerListener);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        background = itemView.findViewById(R.id.background);

        day = itemView.findViewById(R.id.txt_day);
        date = itemView.findViewById(R.id.txt_date);
        value1 = itemView.findViewById(R.id.txt_value1);
        value2 = itemView.findViewById(R.id.txt_value2);

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
