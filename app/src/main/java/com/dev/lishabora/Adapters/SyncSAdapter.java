package com.dev.lishabora.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.lishabora.Adapters.ViewHolders.SyncSViewHolder;
import com.dev.lishabora.Models.SyncModel;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;

import java.util.List;

public class SyncSAdapter extends RecyclerView.Adapter<SyncSViewHolder> {

    private Context context;
    private List<SyncModel> modelList;
    private OnclickRecyclerListener listener;

    public SyncSAdapter(Context context, List<SyncModel> modelList, OnclickRecyclerListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;

    }

    @Override
    public SyncSViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sync_card, parent, false);

        return new SyncSViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(SyncSViewHolder holder, int position) {
        SyncModel model = modelList.get(position);

        holder.txt_date.setText(model.getTimeStamp());
        holder.txt_entity.setText(model.getEntityTypeName());
        holder.txt_type.setText(model.getActionTypeName());

    }


    @Override
    public int getItemCount() {
        return (null != modelList ? modelList.size() : 0);
    }


}
