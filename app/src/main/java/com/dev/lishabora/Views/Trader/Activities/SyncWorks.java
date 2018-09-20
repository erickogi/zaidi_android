package com.dev.lishabora.Views.Trader.Activities;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.dev.lishabora.Adapters.SyncSAdapter;
import com.dev.lishabora.Models.SyncModel;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishaboramobile.R;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static com.dev.lishabora.Models.SyncModel.farmerDateComparatorDesc;


public class SyncWorks extends AppCompatActivity {
    TraderViewModel traderViewModel;
    private View view;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private SyncSAdapter listAdapter;
    private List<SyncModel> syncWorks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_works);
        traderViewModel = ViewModelProviders.of(SyncWorks.this).get(TraderViewModel.class);
        traderViewModel.fetchByStatus(0).observe(this, syncModels -> {
            syncWorks = syncModels;
            //delete(syncModels);
            initList(syncModels);
        });


    }

    private void delete(List<SyncModel> syncModels) {
        for (SyncModel s : syncModels) {
            traderViewModel.deleteSync(s);
        }
    }

    public void initList(List<SyncModel> syncModels) {
        recyclerView = findViewById(R.id.recyclerView);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (syncModels != null) {
            Collections.sort(syncModels, farmerDateComparatorDesc);
        }


        listAdapter = new SyncSAdapter(this, syncModels, new OnclickRecyclerListener() {
            @Override
            public void onSwipe(int adapterPosition, int direction) {


            }

            @Override
            public void onClickListener(int position) {


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
        recyclerView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();


    }

    public void syncNow(View view) {
        Snackbar.make(view, "We are working on implementing sync sit tight", Snackbar.LENGTH_LONG).show();


    }

    public void deleteNow(View view) {
        if (syncWorks != null) {
            delete(syncWorks);
        }
    }
}
