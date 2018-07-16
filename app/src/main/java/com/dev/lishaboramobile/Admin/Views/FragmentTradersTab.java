package com.dev.lishaboramobile.Admin.Views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.lishaboramobile.Admin.Models.ChartModel;
import com.dev.lishaboramobile.Admin.Models.LVModel;
import com.dev.lishaboramobile.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class FragmentTradersTab extends Fragment {
    private View view;
    PieChart chart;
    ChartModel chartModel;
    DashboardController dashboardController;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_traders_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view=view;
        chart = view.findViewById(R.id.chartFarmers);



    }

    private void initChart(ChartModel chartModel, PieChart chart) {


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
            set.setColors(colors, getActivity());
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

        chart.setRotationEnabled(false);



        chart.invalidate(); // refresh


    }

    @Override
    public void onStart() {
        super.onStart();

         dashboardController=new DashboardController(getContext());

        chartModel=dashboardController.getChartData(DashboardController.TRADERS);
        if(chartModel!=null){
            initChart(chartModel,chart);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
