package com.dev.lishaboramobile.admin.ui.admins;

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

import com.dev.lishaboramobile.Global.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.admin.adapters.ProductsAdapter;
import com.dev.lishaboramobile.admin.models.ProductsModel;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class FragmentTradersProducts extends Fragment {
    private View view;

    private AdminsViewModel mViewModel;
    ProductsAdapter listAdapter;
    FloatingActionButton fab;
    private LinkedList<ProductsModel> productsModels;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout linearLayoutEmpty;

    public static FragmentTradersProducts newInstance(List<ProductsModel> productsModel) {
        FragmentTradersProducts fragmentTradersProducts = new FragmentTradersProducts();
        Bundle bundle = new Bundle();
        LinkedList<ProductsModel> productsModels = new LinkedList<ProductsModel>();
        productsModels.addAll(productsModel);
        bundle.putSerializable("products", productsModels);
        fragmentTradersProducts.setArguments(bundle);
        return fragmentTradersProducts;
    }

    public void initList() {
        recyclerView = view.findViewById(R.id.recyclerView);
        listAdapter = new ProductsAdapter(getActivity(), productsModels, new OnclickRecyclerListener() {
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
            productsModels = (LinkedList<ProductsModel>) getArguments().getSerializable("products");
            initList();
            populateTraders();
        }
    }

    public void update(List<ProductsModel> productsModels) {
        if (productsModels != null && listAdapter != null) {
            this.productsModels.clear();
            this.productsModels.addAll(productsModels);
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
