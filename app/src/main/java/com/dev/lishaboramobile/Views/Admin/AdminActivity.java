package com.dev.lishaboramobile.Views.Admin;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.dev.lishaboramobile.Admin.Adapters.ViewPagerAdapter;
import com.dev.lishaboramobile.Admin.Callbacks.FabCallbacks;
import com.dev.lishaboramobile.Admin.Callbacks.SearchViewCallbacks;
import com.dev.lishaboramobile.R;

import java.util.Objects;

public class AdminActivity extends AppCompatActivity {
    private static Fragment fragment = null;
    ViewPagerAdapter adapter;
    String id;
    // private FragmentFarmersTab fragmentFarmersTab;
    FloatingActionButton fab;
    SearchView mSearchView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentAdminDashboard fragmentAdminDashboard;
    private FragmentFarmersTab fragmentFarmersTab;
    private FragmentTradersTab fragmentTradersTab;
    private FragmentProductsTab fragmentProductsTab;
    private FragmentLoansTab fragmentLoansTab;
    private FragmentMilkTab fragmentMilkTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
////            }
//        });

        viewPager = findViewById(R.id.viewpager);
        viewPager.setVisibility(View.GONE);
        viewPager.setOffscreenPageLimit(3);


        tabLayout = findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setVisibility(View.GONE);

        // setupTabIcons();
        setUpMainFragment();

        fab.setVisibility(View.GONE);


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

    void searchAble(boolean shouldShow, String hint, SearchViewCallbacks searchViewCallbacks) {
        if (mSearchView != null) {
            if (shouldShow) {
                mSearchView.setVisibility(View.VISIBLE);
                if (hint != null) {
                    mSearchView.setQueryHint(hint);
                    mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            searchViewCallbacks.onQueryTextSubmit(query);
                            return true;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            searchViewCallbacks.onQueryTextChange(newText);
                            return true;
                        }
                    });
                }
            } else {
                mSearchView.setVisibility(View.GONE);
            }
        }


    }

    void fabButton(boolean shouldShow, Integer resource, FabCallbacks fabCallbacks) {
        if (!shouldShow) {
            fab.hide();
        } else {
            fab.show();
            fab.setImageResource(resource);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fabCallbacks.onClick();
                }
            });

        }
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

        return super.onOptionsItemSelected(item);
    }

}
