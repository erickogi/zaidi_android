package com.dev.lishaboramobile.admin.ui.admins;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.lishaboramobile.Farmer.Models.FamerModel;
import com.dev.lishaboramobile.Global.Models.ResponseModel;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Trader.Models.RPFSearchModel;
import com.dev.lishaboramobile.Trader.Models.RoutesModel;
import com.dev.lishaboramobile.Trader.Models.TraderModel;
import com.dev.lishaboramobile.admin.adapters.ViewPagerAdapter;
import com.dev.lishaboramobile.admin.models.ProductsModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.Objects;

public class FragmentTradersBasicProfile extends Fragment {
    private View view;
    private AdminsViewModel mViewModel;
    Gson gson = new Gson();

    ViewPagerAdapter adapter;
    TextView txtNames, txtCode, txtBussinessName, txtDefaultPayment, txtStatus, txtSynched;
    private AVLoadingIndicatorView avi;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentTradersProducts fragmentTradersProducts;
    private FragmentTradersRoutes fragmentTradersRoutes;
    private FragmentTradersFarmers fragmentTradersFarmers;
    private RoutesModel routesModel;
    private ProductsModel productsModel;
    private TraderModel traderModel;

    public static FragmentTradersBasicProfile newInstance(TraderModel traderModel) {
        FragmentTradersBasicProfile fragmentTradersBasicProfile = new FragmentTradersBasicProfile();
        Bundle bundle = new Bundle();
        bundle.putSerializable("trader", traderModel);
        fragmentTradersBasicProfile.setArguments(bundle);
        return fragmentTradersBasicProfile;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_traders_profile, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AdminsViewModel.class);

        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AdminsViewModel.class);

        this.view = view;



    }

    void initView() {
        txtNames = view.findViewById(R.id.txt_trader_name);
        txtBussinessName = view.findViewById(R.id.txt_business_name);
        txtCode = view.findViewById(R.id.txt_trader_code);
        txtDefaultPayment = view.findViewById(R.id.txt_default);
        txtStatus = view.findViewById(R.id.txt_status);
        txtSynched = view.findViewById(R.id.txt_last_synched);
        avi = view.findViewById(R.id.avi);


        viewPager = view.findViewById(R.id.viewpager);
        viewPager.setVisibility(View.VISIBLE);
        viewPager.setOffscreenPageLimit(4);


        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setVisibility(View.VISIBLE);
        initData();
        setupTabIcons();


    }

    void initData() {
        if (traderModel != null) {
            txtNames.setText(traderModel.getNames());
            txtSynched.setText(traderModel.getSynctime());
            txtCode.setText(traderModel.getCode());
            txtBussinessName.setText(traderModel.getBusinessname());
            txtDefaultPayment.setText(traderModel.getDefaultpaymenttype());


        }

        //getRoutes();
        //getProducts();
        setupViewPager(viewPager, routesModel, productsModel);

    }

    public void initPager() {


    }

    private boolean isFetched() {
        return productsModel != null && routesModel != null;
    }

    private JSONObject getTraderRoutesObject() {


        RPFSearchModel rpfSearchModel = new RPFSearchModel();
        rpfSearchModel.setEntitycode(traderModel.getCode());
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(rpfSearchModel));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONObject getTraderProductsObject() {
        RPFSearchModel rpfSearchModel = new RPFSearchModel();
        rpfSearchModel.setEntitycode(traderModel.getCode());
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(rpfSearchModel));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void getProducts() {
        avi.smoothToShow();
        mViewModel.getTraderProductsModels(getTraderProductsObject(), true).observe(FragmentTradersBasicProfile.this, new Observer<ResponseModel>() {
            @Override
            public void onChanged(@Nullable ResponseModel responseModel) {
                avi.smoothToHide();
                snack(responseModel.getResultDescription());
                JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                Type listType = new TypeToken<LinkedList<ProductsModel>>() {
                }.getType();
                productsModel = gson.fromJson(jsonArray, listType);


                fragmentTradersProducts.update(productsModel);

            }
        });
    }

    private void getRoutes() {
        avi.smoothToShow();
        mViewModel.getTraderRoutesModels(getTraderRoutesObject(), true).observe(FragmentTradersBasicProfile.this, new Observer<ResponseModel>() {
            @Override
            public void onChanged(@Nullable ResponseModel responseModel) {
                avi.smoothToHide();
                snack(responseModel.getResultDescription());
                JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                Type listType = new TypeToken<LinkedList<RoutesModel>>() {
                }.getType();
                routesModel = gson.fromJson(jsonArray, listType);

                fragmentTradersRoutes.update(routesModel);


            }
        });
    }


    private void setupTabIcons() {

        Objects.requireNonNull(tabLayout.getTabAt(0)).setText("Products");
        Objects.requireNonNull(tabLayout.getTabAt(1)).setText("Routes");
        Objects.requireNonNull(tabLayout.getTabAt(2)).setText("Farmers");


    }

    private void setupViewPager(ViewPager viewPager, RoutesModel routesModel, ProductsModel productsModel) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());


        fragmentTradersProducts = FragmentTradersProducts.newInstance(productsModel);
        fragmentTradersRoutes = FragmentTradersRoutes.newInstance(routesModel);
        fragmentTradersFarmers = FragmentTradersFarmers.newInstance(new FamerModel());


        adapter.addFragment(fragmentTradersProducts, "Products");
        adapter.addFragment(fragmentTradersRoutes, "Routes");
        adapter.addFragment(fragmentTradersFarmers, "Farmers");

        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);

        View root = tabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.black));
            drawable.setSize(2, 1);
            //((LinearLayout) root).setDividerPadding(10);
            //((LinearLayout) root).setDividerDrawable(drawable);
        }

    }

    private void snack(String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }




    @Override
    public void onStart() {
        super.onStart();

        if (getArguments() != null) {
            traderModel = (TraderModel) getArguments().getSerializable("data");
        }

        initView();
        initPager();


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
