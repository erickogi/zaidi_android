package com.dev.lishabora.Views.Admin.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dev.lishabora.Adapters.RoutesAdapter;
import com.dev.lishabora.Models.RoutesModel;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.ViewModels.Admin.AdminsViewModel;
import com.dev.lishaboramobile.R;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class FragmentTradersRoutes extends Fragment {
    private View view;
    private AdminsViewModel mViewModel;
    RoutesAdapter listAdapter;
    private LinkedList<RoutesModel> routesModels;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout linearLayoutEmpty;
    private FloatingActionButton fab;

    public static FragmentTradersRoutes newInstance(List<RoutesModel> routes) {
        FragmentTradersRoutes fragmentTradersRoutes = new FragmentTradersRoutes();
        Bundle bundle = new Bundle();

        LinkedList<RoutesModel> routesModels = new LinkedList<>();
        routesModels.addAll(routes);

        bundle.putSerializable("routes", routesModels);
        fragmentTradersRoutes.setArguments(bundle);
        return fragmentTradersRoutes;
    }

    public void initList() {
        recyclerView = view.findViewById(R.id.recyclerView);
        listAdapter = new RoutesAdapter(getActivity(), routesModels, new OnclickRecyclerListener() {
            @Override
            public void onMenuItem(int position, int menuItem) {

            }

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


    }

    private void populateTraders() {
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        listAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(listAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();

        if (getArguments() != null) {
            routesModels = (LinkedList<RoutesModel>) getArguments().getSerializable("routes");
            initList();
            populateTraders();
        }
    }

    public void update(List<RoutesModel> routesModel) {
        if (routesModels != null && listAdapter != null) {

            this.routesModels.clear();
            this.routesModels.addAll(routesModel);
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trader_routes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        fab = view.findViewById(R.id.fab);
        fab.hide();

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
