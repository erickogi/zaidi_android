package com.dev.lishaboramobile.Admin.Views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dev.lishaboramobile.Admin.Adapters.ChartsAdapter;
import com.dev.lishaboramobile.Admin.Models.ChartModel;
import com.dev.lishaboramobile.Admin.Models.LVModel;
import com.dev.lishaboramobile.Global.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FragmentAdminDashboard extends Fragment {
    ChartsAdapter listAdapter;
    List<ChartModel> chartModels;
    private View view;
    private Context context;
    private Fragment fragment;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout linearLayoutEmpty;

    private static String loadJSONFromAsset(Context activity, String fileName) {
        String json = null;
        try {
            InputStream is = activity.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    void setUpView() {
        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment)
                    .addToBackStack(null).commit();
        }

    }

    void popOutFragments() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        recyclerView = view.findViewById(R.id.recyclerView);

        charts();
    }

    private void charts() {


        List<ChartModel> chartModels = getChartData();
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        listAdapter = new ChartsAdapter(getActivity(), chartModels, new OnclickRecyclerListener() {
            @Override
            public void onClickListener(int position) {
                switch (chartModels.get(position).getId()) {
                    case 2:
                        fragment = new FragmentEntityList();
                        Bundle args = new Bundle();
                        fragment.setArguments(args);
                        popOutFragments();
                        setUpView();
                        break;

                    default:
                }
            }

            @Override
            public void onLongClickListener(int position) {

            }

            @Override
            public void onCheckedClickListener(int position) {

            }

            @Override
            public void onMoreClickListener(int position) {

            }

            @Override
            public void onClickListener(int adapterPosition, @NotNull View view) {

            }
        });
        listAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(listAdapter);


    }

    private List<ChartModel> getChartData() {

        return jsonToProduct("analyticsdata.json");
    }

    @Override
    public void onStart() {
        super.onStart();
        this.context = getContext();
        if (context == null) {
            context = getActivity();
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

    private List<ChartModel> jsonToProduct(String fileName) {
        List<ChartModel> productModels = new ArrayList<>();

        Gson gson = new Gson();
        String js = loadJSONFromAsset(getActivity(), fileName);

        try {

            JSONObject jsonObject = new JSONObject(js);
            JSONArray jsonArray = jsonObject.optJSONArray("data");

            for (int a = 0; a < jsonArray.length(); a++) {
                JSONObject jsonObject1 = jsonArray.optJSONObject(a);
                ChartModel chartModel = new ChartModel();
                chartModel.setTitle(jsonObject1.optString("title"));
                chartModel.setDescription(jsonObject1.optString("description"));
                chartModel.setTotal(jsonObject1.optString("total"));
                JSONArray jsonArray1 = jsonObject1.optJSONArray("lvModels");
                List<LVModel> lvModels = new ArrayList<>();
                for (int b = 0; b < jsonArray1.length(); b++) {
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(b);
                    LVModel lvModel = new LVModel();
                    lvModel.setColor(jsonObject2.optString("color"));
                    lvModel.setLabel(jsonObject2.optString("label"));
                    lvModel.setValue(jsonObject2.optInt("value"));
                    lvModels.add(lvModel);

                }
                chartModel.setLvModels(lvModels);
                productModels.add(chartModel);
            }
            //String data=jsonObject.getJSONArray("data").getString(0);
            //Log.d("dada",data);
            // productModels = gson.fromJson(js, new TypeToken<List<ChartModel>>(){}.getType());


        } catch (Exception e) {
            e.printStackTrace();
        }


        return productModels;
    }
}
