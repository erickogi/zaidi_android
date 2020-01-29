package com.dev.zaidi.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.zaidi.Adapters.ViewHolders.SyncViewHolder;
import com.dev.zaidi.Models.SyncModel;
import com.dev.zaidi.R;
import com.dev.zaidi.Utils.OnclickRecyclerListener;

import java.util.List;

public class SyncAdapter extends RecyclerView.Adapter<SyncViewHolder> {

    private Context context;
    private List<SyncModel> modelList;
    private OnclickRecyclerListener listener;

    public SyncAdapter(Context context, List<SyncModel> modelList, OnclickRecyclerListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;

    }

    @Override
    public SyncViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.synce_card, parent, false);

        return new SyncViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(SyncViewHolder holder, int position) {
        SyncModel model = modelList.get(position);

        holder.txt_action.setText(model.getActionTypeName());
        holder.txt_entity.setText(model.getEntityTypeName());
        holder.txt_model.setText(model.getObject());
        holder.txt_timestamp.setText(model.getTimeStamp());
        holder.txt_syn_time.setText(model.getSyncTime());
        holder.txt_syn_status.setText("" + model.getSyncStatus());

    }


    @Override
    public int getItemCount() {
        return (null != modelList ? modelList.size() : 0);
    }


}
