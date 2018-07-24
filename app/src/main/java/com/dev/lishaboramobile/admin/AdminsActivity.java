package com.dev.lishaboramobile.admin;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dev.lishaboramobile.Admin.Models.AdminModel;
import com.dev.lishaboramobile.BottomNav.AHBottomNavigation;
import com.dev.lishaboramobile.BottomNav.AHBottomNavigationItem;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Views.Admin.FragmentAdminDashboard;
import com.dev.lishaboramobile.Views.Trader.AdminDrawerClass;
import com.dev.lishaboramobile.Views.Trader.AdminDrawerItemListener;
import com.dev.lishaboramobile.Views.Trader.FragementFarmersList;
import com.dev.lishaboramobile.admin.ui.admins.AdminsTradersListFragment;
import com.dev.lishaboramobile.login.PrefrenceManager;

public class AdminsActivity extends AppCompatActivity {
    AHBottomNavigationItem item2;
    AHBottomNavigation bottomNavigation;
    SearchView mSearchView;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, AdminsTradersListFragment.newInstance())
                    .commitNow();
        }
        PrefrenceManager prefrenceManager = new PrefrenceManager(this);
        AdminModel adminModel = prefrenceManager.getAdmin();
        setUpDrawer(toolbar, adminModel.getNames(), adminModel.getEmail());
        bottomNav();
    }

    void setUpDrawer(Toolbar toolbar, String name, String email) {
        AdminDrawerClass.Companion.getDrawer(email, name, this, toolbar, new AdminDrawerItemListener() {
            @Override
            public void logOutClicked() {

            }

            @Override
            public void helpClicked() {

            }

            @Override
            public void profileSettingsClicked() {

            }


            @Override
            public void notificationsClicked() {

            }

            @Override
            public void appSettingsClicked() {

            }

            @Override
            public void homeClicked() {

            }

            @Override
            public void analyticsReportsTransactionsClicked() {
                fragment = new FragmentAdminDashboard();
                setFragment();
            }

            @Override
            public void payoutsClicked() {

            }
        });

    }

    void popOutFragments() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }

    void setFragment() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment, "fragmentMain").addToBackStack("").commit();
    }

    private void bottomNav() {
        bottomNavigation = findViewById(R.id.bottom_navigation);

// Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Traders", R.drawable.ic_home_black_24dp, R.color.white);
        item2 = new AHBottomNavigationItem("Products", R.drawable.ic_shopping_cart_black_24dp, R.color.white);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Metrics", R.drawable.ic_timeline, R.color.white);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Orders", R.drawable.ic_dehaze_black_24dp, R.color.white);


// Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);

// Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#09413b"));

// Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(true);

// Enable the translation of the FloatingActionButton
        //bottomNavigation.manageFloatingActionButtonBehavior(floatingActionButton);

// Change colors
        // bottomNavigation.setAccentColor(Color.parseColor("#FFFFFF"));
        bottomNavigation.setInactiveColor(Color.parseColor("#FFFFFF"));

// Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);

// Display color under navigation bar (API 21+)
// Don't forget these lines in your style-v21
// <item name="android:windowTranslucentNavigation">true</item>
// <item name="android:fitsSystemWindows">true</item>
        // bottomNavigation.setTranslucentNavigationEnabled(true);

// Manage titles
        // bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        // bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        // bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

// Use colored navigation with circle reveal effect
        //bottomNavigation.setColored(true);

// Set current item programmatically
        bottomNavigation.setCurrentItem(0);
        // bottomNavigation.s

// Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));

// Add or remove notification for each item

//        int count=getCount(DbConstants.TABLE_SAVED_TITLES,
//                DbConstants.IMAGE_STATUS, String.valueOf(DbConstants.saved));
//
//        if(count>0) {
//            bottomNavigation.setNotification(String.valueOf(count), 1);
//        }

// OR
//        AHNotification notification = new AHNotification.Builder()
//                .setText("1")
//                .setBackgroundColor(ContextCompat.getColor(DemoActivity.this, R.color.color_notification_back))
//                .setTextColor(ContextCompat.getColor(DemoActivity.this, R.color.color_notification_text))
//                .build();
//        bottomNavigation.setNotification(notification, 1);

// Enable / disable item & set disable color
        // bottomNavigation.enableItemAtPosition(2);
        // bottomNavigation.disableItemAtPosition(2);
        // bottomNavigation.setItemDisableColor(Color.parseColor("#3A000000"));

// Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                Bundle bundle = new Bundle();
                switch (position) {
                    case 0:
                        fragment = new AdminsTradersListFragment();
                        popOutFragments();
                        bundle.putInt("type", 0);
                        fragment.setArguments(bundle);
                        setFragment();
                        break;
                    case 1:
                        popOutFragments();
                        fragment = new FragementFarmersList();
                        bundle.putInt("type", 1);
                        fragment.setArguments(bundle);
                        setFragment();
                        break;
                    case 2:
                        fragment = new FragmentAdminDashboard();
                        popOutFragments();
                        bundle.putInt("type", 2);
                        fragment.setArguments(bundle);
                        setFragment();
                        break;
                }
                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                // Manage the new y position
            }
        });

        bottomNavigation.setNotification("4", 3);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) mSearch.getActionView();

        mSearchView.setVisibility(View.GONE);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_search) {
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

}
