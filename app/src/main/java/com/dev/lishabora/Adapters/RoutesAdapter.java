package com.dev.lishabora.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.lishabora.Adapters.ViewHolders.RoutesViewHolder;
import com.dev.lishabora.Models.RoutesModel;
import com.dev.lishabora.Repos.Trader.FarmerRepo;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;

import java.util.List;

//import com.dev.lishabora.Models.RoutesModel;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesViewHolder> {

    private Context context;
    private List<RoutesModel> modelList;
    private OnclickRecyclerListener listener;

    public RoutesAdapter(Context context, List<RoutesModel> modelList, OnclickRecyclerListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;

    }

    @Override
    public RoutesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_card, parent, false);

        return new RoutesViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(RoutesViewHolder holder, int position) {
        RoutesModel routesModel = modelList.get(position);
        FarmerRepo f = new FarmerRepo(context);
        holder.name.setText(routesModel.getRoute());
        holder.famerscount.setText("" + f.getNoOfRows(routesModel.getCode()));
        holder.status.setText("" + routesModel.getStatus());

        String status = "";

        if (routesModel.getStatus() == 1) {
            holder.status.setText("Active");
            holder.status.setTextColor(context.getResources().getColor(R.color.green_color_picker));
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.green_color_picker));
            holder.statusview.setBackgroundColor(context.getResources().getColor(R.color.green_color_picker));
//
//
        } else {

            holder.status.setText("In Active");
            holder.status.setTextColor(context.getResources().getColor(R.color.red));
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.red));
            holder.statusview.setBackgroundColor(context.getResources().getColor(R.color.red));
//
        }


    }


    @Override
    public int getItemCount() {
        return (null != modelList ? modelList.size() : 0);
    }


}
