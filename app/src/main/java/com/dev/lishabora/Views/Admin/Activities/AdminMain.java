package com.dev.lishabora.Views.Admin.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dev.lishabora.Adapters.ViewPagerAdapter;
import com.dev.lishabora.Views.Login.ResetPassword;
import com.dev.lishabora.Views.Trader.Fragments.FragmentTraderProfile;
import com.dev.lishaboramobile.R;

public class AdminMain extends AppCompatActivity {
    private static ViewPager viewPager;
    SearchView mSearchView;
    private TabLayout tabLayout;

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
            startActivity(new Intent(AdminMain.this, ResetPassword.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentTraderProfile(), "profile");
        adapter.addFragment(new com.dev.lishabora.Views.Admin.Fragments.Tf.FragementFarmersList(), "farmers");
        adapter.addFragment(new com.dev.lishabora.Views.Admin.Fragments.Tf.FragmentProductList(), "farmers");
        adapter.addFragment(new com.dev.lishabora.Views.Admin.Fragments.Tf.FragmentRoutes(), "farmers");
        adapter.addFragment(new com.dev.lishabora.Views.Admin.Fragments.Tf.FragmentPayouts(), "farmers");

        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {

        tabLayout.getTabAt(0).setText("Profile");
        tabLayout.getTabAt(1).setText("Farmers");
        tabLayout.getTabAt(2).setText("Products");
        tabLayout.getTabAt(3).setText("Routes");
        tabLayout.getTabAt(4).setText("Payouts");
        //tabLayout.getTabAt(5).setText("Loans");
        //tabLayout.getTabAt(6).setText("Orders");


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
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
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

    }

}
