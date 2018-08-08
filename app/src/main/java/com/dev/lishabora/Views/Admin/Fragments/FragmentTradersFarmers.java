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

import com.dev.lishabora.Adapters.FarmersAdapter;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.ProductsModel;
import com.dev.lishabora.Models.Trader.TraderModel;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.ViewModels.Admin.AdminsViewModel;
import com.dev.lishabora.Views.Trader.Fragments.FragmentRoutes;
import com.dev.lishaboramobile.R;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class FragmentTradersFarmers extends Fragment {
    private View view;
    private AdminsViewModel mViewModel;
    private TraderModel traderModel;
    FarmersAdapter listAdapter;
    private FloatingActionButton fab;
    private ProductsModel productsModel;
    private FamerModel famerModel;
    private FragmentRoutes fragmentRoutes;
    private LinkedList<FamerModel> famerModels;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout linearLayoutEmpty;

    public static FragmentTradersFarmers newInstance(List<FamerModel> famerModel) {
        FragmentTradersFarmers fragmentTradersProducts = new FragmentTradersFarmers();
        Bundle bundle = new Bundle();

        LinkedList<FamerModel> famerModels = new LinkedList<>();
        famerModels.addAll(famerModel);

        bundle.putSerializable("farmers", famerModels);

        fragmentTradersProducts.setArguments(bundle);
        return fragmentTradersProducts;
    }

    public void initList() {
        recyclerView = view.findViewById(R.id.recyclerView);
        listAdapter = new FarmersAdapter(getActivity(), famerModels, new OnclickRecyclerListener() {
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
            famerModels = (LinkedList<FamerModel>) getArguments().getSerializable("farmers");
            initList();
            populateTraders();
        }
    }

    public void update(List<FamerModel> famerModels) {
        if (this.famerModels != null && listAdapter != null) {

            this.famerModels.clear();
            this.famerModels.addAll(famerModels);
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
        return inflater.inflate(R.layout.fragment_trader_products, container, false);
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
