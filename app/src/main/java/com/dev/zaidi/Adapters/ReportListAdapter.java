package com.dev.zaidi.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.zaidi.Adapters.ViewHolders.ReportListViewHolder;
import com.dev.zaidi.Models.Reports.ReportListModel;
import com.dev.zaidi.R;
import com.dev.zaidi.Utils.DateTimeUtils;
import com.dev.zaidi.Utils.OnclickRecyclerListener;

import java.util.List;

//import com.dev.lishabora.Models.ReportListModel;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListViewHolder> {

    private Context context;
    private List<ReportListModel> modelList;
    private OnclickRecyclerListener listener;

    public ReportListAdapter(Context context, List<ReportListModel> modelList, OnclickRecyclerListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;

    }

    @Override
    public ReportListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_list_card, parent, false);

        return new ReportListViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(ReportListViewHolder holder, int position) {
        ReportListModel model = modelList.get(position);
        holder.day.setText(model.getDay());
        holder.date.setText(DateTimeUtils.Companion.getDisplayDate(model.getDate(), DateTimeUtils.Companion.getFormatSmall()));
        if (model.getValue1() == null) {
            holder.value1.setVisibility(View.GONE);
        } else {
            holder.value1.setText(model.getValue1());
        }
        holder.value2.setText(model.getValue2());


    }

    public void refresh(List<ReportListModel> models) {
        modelList = models;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (null != modelList ? modelList.size() : 0);
    }


}
