package com.dev.lishaboramobile.admin.ui.admins;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.dev.lishaboramobile.Admin.Adapters.ViewPagerAdapter;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Trader.Models.TraderModel;

import java.util.Objects;

public class AdimTraderProfile extends AppCompatActivity {
    private static Fragment fragment = null;
    ViewPagerAdapter adapter;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentTradersBasicProfile basicProfile;
    private FragmentTradersPayouts payouts;
    private FragmentTraderMilkCollections milkCollections;
    private FragmentTradersOrders orders;
    private FragmentTradersLoans loans;
    private FragmentTradersProducts products;
    private TraderModel traderModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adim_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            traderModel = (TraderModel) intent.getSerializableExtra("data");
        }

        try {

            this.setTitle(traderModel.getNames() + "   " + traderModel.getCode());
        } catch (Exception nm) {
            nm.printStackTrace();
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        viewPager = findViewById(R.id.viewpager);
        viewPager.setVisibility(View.VISIBLE);
        viewPager.setOffscreenPageLimit(4);


        tabLayout = findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setVisibility(View.VISIBLE);

        setupViewPager(viewPager);
        setupTabIcons();

    }

    private void setupTabIcons() {

        Objects.requireNonNull(tabLayout.getTabAt(0)).setText("Profile");
        Objects.requireNonNull(tabLayout.getTabAt(1)).setText("Payouts");
        Objects.requireNonNull(tabLayout.getTabAt(2)).setText("Products");
        Objects.requireNonNull(tabLayout.getTabAt(3)).setText("Milk");
        Objects.requireNonNull(tabLayout.getTabAt(4)).setText("Loans");
        Objects.requireNonNull(tabLayout.getTabAt(4)).setText("Orders");
        //tabLayout.getTabAt(2).setText("Chart");


    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());


        basicProfile = new FragmentTradersBasicProfile();
        payouts = new FragmentTradersPayouts();
        products = new FragmentTradersProducts();
        milkCollections = new FragmentTraderMilkCollections();
        loans = new FragmentTradersLoans();
        orders = new FragmentTradersOrders();

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", traderModel);
        basicProfile.setArguments(bundle);
        products.setArguments(bundle);
        payouts.setArguments(bundle);
        milkCollections.setArguments(bundle);
        loans.setArguments(bundle);
        orders.setArguments(bundle);


        adapter.addFragment(basicProfile, "Profile");
        adapter.addFragment(payouts, "Payouts");
        adapter.addFragment(products, "Products");
        adapter.addFragment(milkCollections, "Milk");
        adapter.addFragment(loans, "Loans");
        adapter.addFragment(orders, "Orders");

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


}
