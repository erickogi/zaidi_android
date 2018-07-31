package com.dev.lishaboramobile.Views.Trader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.lishaboramobile.Farmer.Models.FamerModel;
import com.dev.lishaboramobile.Global.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;
import com.github.mikephil.charting.charts.PieChart;

import java.lang.ref.WeakReference;
import java.util.List;

public class FarmersAdapter extends RecyclerView.Adapter<FarmerViewHolder> {

    private Context context;
    private List<FamerModel> modelList;
    private OnclickRecyclerListener listener;

    public FarmersAdapter(Context context, List<FamerModel> modelList, OnclickRecyclerListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;

    }

    @Override
    public FarmerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.farmer_card, parent, false);

        return new FarmerViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(FarmerViewHolder holder, int position) {
        FamerModel farmer = modelList.get(position);

        holder.balance.setText(farmer.getTotalbalance());
        holder.id.setText(farmer.getCode());
        holder.name.setText(farmer.getNames());
        holder.cycle.setText(farmer.getCyclename());
        holder.route.setText("" + farmer.getRoutename());

        String status = "";

        if (farmer.getArchived() == 0 && farmer.getDeleted() == 0 && farmer.getDummy() == 0) {
            status = "Active";
            holder.status.setText(status);
            holder.status.setTextColor(context.getResources().getColor(R.color.green_color_picker));
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.green_color_picker));
            holder.statusview.setBackgroundColor(context.getResources().getColor(R.color.green_color_picker));
//
//
        } else {
            StringBuilder stringBuilder = new StringBuilder(status);
            if (farmer.getDeleted() == 1) {
                //stringBuilder.setLength(0);
                stringBuilder.append("|Deleted");
            }
            if (farmer.getArchived() == 1) {
                stringBuilder.append("|Archived");
            }
            if (farmer.getDummy() == 1) {
                stringBuilder.append("|Dummy");

            }


            holder.status.setText(stringBuilder.toString());
            holder.status.setTextColor(context.getResources().getColor(R.color.red));
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.red));
            holder.statusview.setBackgroundColor(context.getResources().getColor(R.color.red));
//
        }
//
//        if(farmer.getSynced()==0){
//            holder.status.setTextColor(context.getResources().getColor(R.color.accent));
//        }else {
//            holder.status.setTextColor(context.getResources().getColor(R.color.green_color_picker));
//
//        }
//
//
//        if(farmer.getArchived()==1){
//            holder.status.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
//        }else {
//            holder.status.setTextColor(context.getResources().getColor(R.color.green_color_picker));
//
//        }
//
//        if(farmer.getDeleted()==1){
//            holder.status.setTextColor(context.getResources().getColor(R.color.red));
//        }else {
//            holder.status.setTextColor(context.getResources().getColor(R.color.green_color_picker));
//
//        }


        //farmer.getSynced()==1||farmer.getArchived()==1)

    }


    @Override
    public int getItemCount() {
        return (null != modelList ? modelList.size() : 0);
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        PieChart chart;
        TextView txtHeader;
        private LinearLayout linearLayout;
        private WeakReference<OnclickRecyclerListener> listenerWeakReference;

        public MyViewHolder(View itemView, OnclickRecyclerListener listener) {
            super(itemView);
            txtHeader = itemView.findViewById(R.id.txt_header);
            chart = itemView.findViewById(R.id.chartFarmers);
            listenerWeakReference = new WeakReference<>(listener);

            chart.setOnClickListener(this::onClick);

        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {


            if (v.getId() == R.id.chartFarmers) {
                listenerWeakReference.get().onClickListener(getAdapterPosition());
            }
        }

        /**
         * Called when a view has been clicked and held.
         *
         * @param v The view that was clicked and held.
         * @return true if the callback consumed the long click, false otherwise.
         */
        @Override
        public boolean onLongClick(View v) {
            listenerWeakReference.get().onClickListener(getAdapterPosition(), v);
            return false;
        }
    }


}
