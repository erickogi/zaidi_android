package com.dev.lishabora.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.lishabora.Adapters.ViewHolders.TraderSettingsViewHolder;
import com.dev.lishabora.Models.Trader.TraderSettingsModel;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;

import java.util.List;

//import com.dev.lishaboramobile.Trader.Models.TraderSettingsModel;

public class TradersSettingsAdapter extends RecyclerView.Adapter<TraderSettingsViewHolder> {

    private Context context;
    private List<TraderSettingsModel> modelList;
    private OnclickRecyclerListener listener;

    public TradersSettingsAdapter(Context context, List<TraderSettingsModel> modelList, OnclickRecyclerListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;

    }

    @Override
    public TraderSettingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_settings_dialog, parent, false);

        return new TraderSettingsViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(TraderSettingsViewHolder holder, int position) {
        TraderSettingsModel traderSettingsModel = modelList.get(position);

        holder.title.setText(traderSettingsModel.getTitle());
        holder.subtitle.setText(traderSettingsModel.getSubtitle());
        holder.icon.setImageResource(traderSettingsModel.getIcon());


    }


    @Override
    public int getItemCount() {
        return (null != modelList ? modelList.size() : 0);
    }


}
