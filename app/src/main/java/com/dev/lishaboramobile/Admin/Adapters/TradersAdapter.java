package com.dev.lishaboramobile.Admin.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.lishaboramobile.Global.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Trader.Models.TraderModel;
import com.github.mikephil.charting.charts.PieChart;

import java.lang.ref.WeakReference;
import java.util.List;

public class TradersAdapter extends RecyclerView.Adapter<TradersAdapter.MyViewHolder> {

    private Context context;
    private List<TraderModel> modelList;
    private OnclickRecyclerListener listener;

    public TradersAdapter(Context context, List<TraderModel> modelList, OnclickRecyclerListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;

    }

    @Override
    public TradersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_item, parent, false);

        return new TradersAdapter.MyViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(TradersAdapter.MyViewHolder holder, int position) {
        TraderModel chatPojo = modelList.get(position);


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
            return false;
        }
    }
}
