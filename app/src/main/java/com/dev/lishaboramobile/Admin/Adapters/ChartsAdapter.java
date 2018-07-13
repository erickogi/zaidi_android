package com.dev.lishaboramobile.Admin.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.lishaboramobile.Admin.Models.ChartModel;
import com.dev.lishaboramobile.Admin.Models.LVModel;
import com.dev.lishaboramobile.Global.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ChartsAdapter extends RecyclerView.Adapter<ChartsAdapter.MyViewHolder> {

    private Context context;
    private List<ChartModel> modelList;
    private OnclickRecyclerListener listener;

    public ChartsAdapter(Context context, List<ChartModel> modelList, OnclickRecyclerListener listener) {
        this.context = context;
        this.modelList = modelList;
        this.listener = listener;

    }

    @Override
    public ChartsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_item, parent, false);

        return new MyViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(ChartsAdapter.MyViewHolder holder, int position) {
        ChartModel chatPojo = modelList.get(position);
        initChart(chatPojo, holder.chart);
        holder.txtHeader.setText(chatPojo.getTitle());

    }

    private void initChart(ChartModel chartModel, PieChart chart) {


        //  int[] colors = new int[chartModel.getLvModels().size()];
        int colors[] = {R.color.colorPrimary, R.color.red, R.color.orange_color_picker, R.color.blue_color_picker};

        int count = 0;
        List<PieEntry> entries = new ArrayList<>();
        for (int a = 0; a < chartModel.getLvModels().size(); a++) {
            LVModel lvModel = chartModel.getLvModels().get(a);
            entries.add(new PieEntry(lvModel.getValue(), lvModel.getLabel()));
            Log.d("colll", lvModel.getColor());
            //colors[a]= Color.parseColor(lvModel.getColor());
            //count++;
        }


        PieDataSet set = new PieDataSet(entries, "");
        PieData data = new PieData(set);
        set.setValueTextSize(14);


        try {
            set.setColors(colors, context);
        } catch (Exception nm) {
            nm.printStackTrace();
        }

        chart.setData(data);

        Description description = new Description();
        description.setText("");

        chart.setCenterText(chartModel.getTotal());
        chart.setDescription(description);
        chart.setEntryLabelTextSize(12);

        chart.setCenterTextSize(14.5f);
        chart.setUsePercentValues(true);
        chart.setHoleRadius(30);


        chart.invalidate(); // refresh


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
