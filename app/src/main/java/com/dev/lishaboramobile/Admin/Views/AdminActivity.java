package com.dev.lishaboramobile.Admin.Views;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.dev.lishaboramobile.Admin.Adapters.ViewPagerAdapter;
import com.dev.lishaboramobile.Admin.Models.ChartModel;
import com.dev.lishaboramobile.Admin.Models.LVModel;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminActivity extends AppCompatActivity {
    private static Fragment fragment = null;
    ViewPagerAdapter adapter;
    String id;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentAdminDashboard fragmentAdminDashboard;

    private FragmentFarmersTab fragmentFarmersTab;
    private FragmentTradersTab fragmentTradersTab;
    private FragmentProductsTab fragmentProductsTab;
    private FragmentLoansTab fragmentLoansTab;
    private FragmentMilkTab fragmentMilkTab;
   // private FragmentFarmersTab fragmentFarmersTab;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        viewPager = findViewById(R.id.viewpager);
        viewPager.setVisibility(View.GONE);
        viewPager.setOffscreenPageLimit(3);


        tabLayout = findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
       // setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);



       // setupTabIcons();
         setUpMainFragment();


    }
    private void setupTabIcons() {

        Objects.requireNonNull(tabLayout.getTabAt(0)).setText("Traders");
        Objects.requireNonNull(tabLayout.getTabAt(1)).setText("Farmers");
        Objects.requireNonNull(tabLayout.getTabAt(2)).setText("Products");
        Objects.requireNonNull(tabLayout.getTabAt(3)).setText("Milk");
        Objects.requireNonNull(tabLayout.getTabAt(4)).setText("Loans");
        //tabLayout.getTabAt(2).setText("Chart");


    }
    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());


        fragmentAdminDashboard = new FragmentAdminDashboard();
        fragmentFarmersTab = new FragmentFarmersTab();
        fragmentTradersTab = new FragmentTradersTab();
        fragmentProductsTab = new FragmentProductsTab();
        fragmentMilkTab = new FragmentMilkTab();
        fragmentLoansTab = new FragmentLoansTab();

        Bundle args = new Bundle();
        args.putString("key_id", id);
        fragmentAdminDashboard.setArguments(args);

        adapter.addFragment(fragmentTradersTab, "Traders");
        adapter.addFragment(fragmentFarmersTab, "Farmers");
        adapter.addFragment(fragmentProductsTab, "Products");
        adapter.addFragment(fragmentMilkTab, "Milk");
        adapter.addFragment(fragmentLoansTab, "Loans");

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


    private void setUpMainFragment() {
        fragment = new FragmentAdminDashboard();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment, "fragmentMain").commit();

    }


    void setUpView() {
        if (fragment != null) {
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment)
                    .addToBackStack(null).commit();
        }

    }

    void popOutFragments() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }


}
