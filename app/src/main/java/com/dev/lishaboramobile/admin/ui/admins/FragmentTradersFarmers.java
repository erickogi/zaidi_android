package com.dev.lishaboramobile.admin.ui.admins;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.lishaboramobile.Farmer.Models.FamerModel;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Trader.Models.TraderModel;
import com.dev.lishaboramobile.Views.Trader.ui.Routes;
import com.dev.lishaboramobile.admin.models.ProductsModel;

public class FragmentTradersFarmers extends Fragment {
    private View view;
    private AdminsViewModel mViewModel;
    private TraderModel traderModel;
    private Routes routes;
    private ProductsModel productsModel;
    private FamerModel famerModel;


    public static FragmentTradersFarmers newInstance(FamerModel famerModel) {
        FragmentTradersFarmers fragmentTradersProducts = new FragmentTradersFarmers();
        Bundle bundle = new Bundle();
        bundle.putSerializable("farmers", famerModel);
        fragmentTradersProducts.setArguments(bundle);
        return fragmentTradersProducts;
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

    }

    @Override
    public void onStart() {
        super.onStart();
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

    public void update(ProductsModel productsModel) {
    }
}
