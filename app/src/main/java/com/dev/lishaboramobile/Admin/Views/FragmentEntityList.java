package com.dev.lishaboramobile.Admin.Views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dev.lishaboramobile.Admin.Adapters.TradersAdapter;
import com.dev.lishaboramobile.Global.AppConstants;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Trader.Models.TraderModel;

import java.util.List;

public class FragmentEntityList extends Fragment {
    TradersAdapter listAdapter;
    List<TraderModel> traderModels;
    private View view;
    private Context context;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout linearLayoutEmpty;
    private int entity;

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
        entity = getArguments().getInt("Entity");

        switch (entity) {
            case AppConstants.TRADER:
                initTrader();

                break;

            default:
        }


    }

    private void initTrader() {


    }
}
