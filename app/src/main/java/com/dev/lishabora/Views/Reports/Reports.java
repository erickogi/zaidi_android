package com.dev.lishabora.Views.Reports;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.dev.lishaboramobile.R;

public class Reports extends AppCompatActivity {
    public static Fragment fragment = null;
    AHBottomNavigation bottomNavigation;
    int type = 1;
    Bundle bundle = new Bundle();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milk_reports);

        type = getIntent().getIntExtra("type", HistoryToolBarUI.TYPE_LOAN);
        bundle.putInt("type", type);


        fragment = new FragmentHistory();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment, "fragmentMain").commit();


        bottomNav();

    }

    private void bottomNav() {
        bottomNavigation = findViewById(R.id.bottom_navigation);

// Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("List", R.drawable.ic_list_black_24dp, R.color.white);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Line", R.drawable.ic_show_chart_black_24dp, R.color.white);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Bar", R.drawable.ic_insert_chart_black_24dp, R.color.white);
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
            switch (position) {
                case 0:
                    fragment = new FragmentHistory();
                    fragment.setArguments(bundle);
                    popOutFragments();
                    fragment.setArguments(bundle);
                    setFragment();
                    break;
                case 1:
                    popOutFragments();
                    fragment = new FragmentChart();


                    fragment.setArguments(bundle);
                    setFragment();
                    break;

                case 2:
                    popOutFragments();
                    fragment = new FragmentBarChart();

                    fragment.setArguments(bundle);
                    setFragment();
                    break;


                default:
            }
            return true;
        });
        bottomNavigation.setOnNavigationPositionListener(y -> {

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
