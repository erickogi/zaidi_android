package com.dev.lishabora.Views.Trader.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.Trader.FarmerConst;
import com.dev.lishabora.Views.Trader.Fragments.FragmentCurrentFarmerPayout;
import com.dev.lishabora.Views.Trader.Fragments.FragmentFarmerHistory;
import com.dev.lishabora.Views.Trader.Fragments.FragmentFarmerProfile;
import com.dev.lishaboramobile.R;

public class FarmerProfile extends AppCompatActivity {
    public static Fragment fragment = null;
    AHBottomNavigationItem item2;
    AHBottomNavigation bottomNavigation;
    private FamerModel famerModel;
    private TraderViewModel traderViewModel;


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        traderViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);

        famerModel = (FamerModel) getIntent().getSerializableExtra("farmer");
        FarmerConst.setFamerModel(famerModel);

        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        bundle.putSerializable("farmer", FarmerConst.getFamerModel());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        fragment = new FragmentFarmerProfile();
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment, "fragmentMain").commit();

        Intent i = getIntent();
        try {
            if (i != null && i.getStringExtra("type").equals("history")) {
                bottomNavigation.setCurrentItem(1);

                popOutFragments();
                fragment = new FragmentFarmerHistory();

                fragment.setArguments(bundle);
                setFragment();
            }
        } catch (Exception nm) {
            nm.printStackTrace();
        }
        bottomNav(famerModel);
    }

    private void bottomNav(FamerModel famerModel) {
        bottomNavigation = findViewById(R.id.bottom_navigation);

// Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Profile", R.drawable.ic_account_circle_black_24dp, R.color.white);
        item2 = new AHBottomNavigationItem("History", R.drawable.ic_history_black_24dp, R.color.white);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Card", R.drawable.ic_timeline, R.color.white);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#0d5c53"));
        bottomNavigation.setBehaviorTranslationEnabled(true);
        bottomNavigation.setInactiveColor(Color.parseColor("#FFFFFF"));
        bottomNavigation.setForceTint(true);
        bottomNavigation.setCurrentItem(0);
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));

        bottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {
            Bundle bundle = new Bundle();
            switch (position) {
                case 0:
                    fragment = new FragmentFarmerProfile();
                    popOutFragments();
                    bundle.putInt("type", 0);
                    bundle.putSerializable("farmer", FarmerConst.getFamerModel());
                    fragment.setArguments(bundle);
                    setFragment();
                    break;
                case 1:
                    popOutFragments();
                    fragment = new FragmentFarmerHistory();
                    bundle.putInt("type", 1);
                    bundle.putSerializable("farmer", FarmerConst.getFamerModel());

                    fragment.setArguments(bundle);
                    setFragment();
                    break;

                case 2:
                    popOutFragments();
                    fragment = new FragmentCurrentFarmerPayout();
                    bundle.putInt("type", 1);
                    bundle.putSerializable("farmer", FarmerConst.getFamerModel());

                    fragment.setArguments(bundle);
                    setFragment();
                    break;


                default:
            }
            return true;
        });
        bottomNavigation.setOnNavigationPositionListener(y -> {
            // Manage the new y position
        });


    }


    void setFragment() {
        // fragment = new FragmentSearch();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment, "fragmentMain").commit();
    }

    void popOutFragments() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }

}
