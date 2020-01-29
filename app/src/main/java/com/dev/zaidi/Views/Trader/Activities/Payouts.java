package com.dev.zaidi.Views.Trader.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.zaidi.R;
import com.dev.zaidi.Views.Trader.Fragments.FragmentPayoutColloectionsList;
import com.dev.zaidi.Views.Trader.Fragments.FragmentPayoutFarmersList;
import com.dev.zaidi.Views.Trader.Fragments.FragmentPayoutSummary;
import com.dev.zaidi.Views.Trader.PayoutConstants;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

public class Payouts extends AppCompatActivity {
    private MaterialSpinner spinner;
    private RelativeLayout lspinner;
    private com.dev.zaidi.Models.Payouts payouts;
    SearchView mSearchView;
    static Fragment page;

    private static Fragment fragment = null;

    private static ViewPager viewPager;
    private TabLayout tabLayout;

    public TextView status, startDate, cycleName, endDate, milkTotal, loanTotal, orderTotal, balance, approvedCount, unApprovedCount;
    public RelativeLayout background;
    public View statusview;


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
        int id = item.getItemId();
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void setupViewPager(ViewPager viewPager) {


        Fragment fragment = new FragmentPayoutFarmersList();
        Bundle args = new Bundle();
        args.putSerializable("data", payouts);
        fragment.setArguments(args);

        Fragment fragment1 = new FragmentPayoutColloectionsList();
        args.putSerializable("data", payouts);

        fragment.setArguments(args);

        Fragment fragment2 = new FragmentPayoutSummary();
        args.putSerializable("data", payouts);

        fragment.setArguments(args);



        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(fragment2, "all");
        adapter.addFragment(fragment1, "sum");
        adapter.addFragment(fragment, "add");



        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {

        tabLayout.getTabAt(0).setText("Summary");
        tabLayout.getTabAt(1).setText("Collections");
        tabLayout.getTabAt(2).setText("Farmers");


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payouts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        payouts = new com.dev.zaidi.Models.Payouts();
        if (getIntent() != null) {
            payouts = (com.dev.zaidi.Models.Payouts) getIntent().getSerializableExtra("data");
        } else {
            payouts = PayoutConstants.getPayouts();
        }
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();





        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());


        fab.hide();


        //lspinner = findViewById(R.id.linear_spinner);
        spinner = findViewById(R.id.spinner);

        //setUpMainFragment();
        setTitle("Payouts");
        setSpinner();

        //  initCardHeader();



    }

    private void setSpinner() {

    }


    public void setUpView(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()//.setCustomAnimations(R.anim.left_out, R.anim.right_enter)
                    .
                            replace(R.id.frame_layout, fragment)
                    .addToBackStack(null).commit();
        }

    }

    public void popOutFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }
    private void setUpMainFragment() {

        //fragment = new FragmentPayouts();

        fragment = new FragmentPayoutColloectionsList();
        Bundle args = new Bundle();
        args.putSerializable("data", payouts);
        fragment.setArguments(args);

        popOutFragments();
        //setUpView(fragment);
        try {
            // mSearchView.setVisibility(View.GONE);
        } catch (Exception nm) {
            nm.printStackTrace();
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment, "fragmentMain").commit();

    }
//    public void setCardHeaderData(com.dev.lishabora.Models.Payouts model) {
//        startDate.setText(DateTimeUtils.Companion.getDisplayDate(model.getStartDate(), DateTimeUtils.Companion.getDisplayDatePattern1()));
//        endDate.setText(DateTimeUtils.Companion.getDisplayDate(model.getEndDate(), DateTimeUtils.Companion.getDisplayDatePattern1()));
//        cycleName.setText(model.getCyclename());
//
//
//        milkTotal.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getMilkTotalLtrs(), 1), this.getString(R.string.ltrs)));
//        loanTotal.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getLoanTotal(), 1), this.getString(R.string.ksh)));
//        orderTotal.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getOrderTotal(), 1), this.getString(R.string.ksh)));
//        balance.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getBalance(), 1), this.getString(R.string.ksh)));
//        GeneralUtills.Companion.changeCOlor(model.getBalance(), balance, 1);
//
//
//        approvedCount.setText(model.getApprovedCards());
//        unApprovedCount.setText(model.getPendingCards());
//
//
//        if (model.getStatus() == 1) {
//            // status.setText("Active");
//            background.setBackgroundColor(this.getResources().getColor(R.color.green_color_picker));
//
//
//        } else if (model.getStatus() == 0) {
//            background.setBackgroundColor(this.getResources().getColor(R.color.red));
//
//        } else {
//            background.setBackgroundColor(this.getResources().getColor(R.color.blue_color_picker));
//
//        }
//
//    }
//
//
//    public void initCardHeader() {
//        background = findViewById(R.id.background);
//        startDate = findViewById(R.id.txt_date_start);
//        endDate = findViewById(R.id.txt_date_end);
//
//
//        cycleName = findViewById(R.id.txt_cycle);
//
//        milkTotal = findViewById(R.id.txt_milk_totals);
//        loanTotal = findViewById(R.id.txt_loans_total);
//        orderTotal = findViewById(R.id.txt_orders_total);
//
//        approvedCount = findViewById(R.id.txt_approved_farmers);
//        unApprovedCount = findViewById(R.id.txt_pending_farmers);
//        balance = findViewById(R.id.txt_Bal_out);
//
//        if (payouts != null) {
//            setCardHeaderData(payouts);
//        }
//        //setSpinner();
//
//    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }



}
