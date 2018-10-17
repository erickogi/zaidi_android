package com.dev.lishabora.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.lishabora.Adapters.ViewHolders.NotificationViewHolder;
import com.dev.lishabora.Models.Notifications;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;

import java.util.List;

//import com.dev.lishaboramobile.Trader.Models.Notifications;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationViewHolder> {

    private Context context;
    private List<Notifications> modelList;
    private OnclickRecyclerListener listener;
    private boolean isChk = false;

    public NotificationsAdapter(Context context, List<Notifications> modelList, OnclickRecyclerListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
        this.isChk = false;

    }

    public NotificationsAdapter(Context context, List<Notifications> modelList, OnclickRecyclerListener listener, boolean isChk) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;
        this.isChk = isChk;

    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card, parent, false);

        return new NotificationViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        Notifications notification = modelList.get(position);


        holder.title.setText(notification.getTitle());
        holder.message.setText(notification.getMessage());
        holder.txtDate.setText("");


    }


    @Override
    public int getItemCount() {
        return (null != modelList ? modelList.size() : 0);
    }


    public void refresh(List<Notifications> filteredNotifications) {
        modelList = filteredNotifications;
        notifyDataSetChanged();

    }
}
