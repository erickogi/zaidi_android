package com.dev.lishabora.Views.Trader.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.Views.Trader.Fragments.FragmentPayoutColloectionsList;
import com.dev.lishabora.Views.Trader.Fragments.FragmentPayoutFarmersList;
import com.dev.lishabora.Views.Trader.PayoutConstants;
import com.dev.lishaboramobile.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class Payouts extends AppCompatActivity {
    private MaterialSpinner spinner;
    private RelativeLayout lspinner;
    private static Fragment fragment = null;
    private com.dev.lishabora.Models.Payouts payouts;
    SearchView mSearchView;


    public TextView status, startDate, cycleName, endDate, milkTotal, loanTotal, orderTotal, balance, approvedCount, unApprovedCount;
    public RelativeLayout background;
    public View statusview;

    //    public static void setSpinner(int v) {
//        try {
//            spinner.setVisibility(v);
//
//        } catch (Exception nm) {
//            nm.printStackTrace();
//        }
////    }
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payout, menu);
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
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payouts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        payouts = new com.dev.lishabora.Models.Payouts();
        if (getIntent() != null) {
            payouts = (com.dev.lishabora.Models.Payouts) getIntent().getSerializableExtra("data");
        } else {
            payouts = PayoutConstants.getPayouts();
        }



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());


        fab.hide();


        lspinner = findViewById(R.id.linear_spinner);
        spinner = findViewById(R.id.spinner);

        setUpMainFragment();
        setTitle("Payouts");
        setSpinner();

        initCardHeader();



    }

    private void setSpinner() {
        try {
            String items[] = {"Payout Collections List", "Farmers List"};
            spinner.setItems(items);
            spinner.setSelectedIndex(0);


            spinner.setOnItemSelectedListener((view, position, id, item) -> {
                if (position == 0) {
                    Fragment fragment = new FragmentPayoutColloectionsList();
                    Bundle args = new Bundle();
                    args.putSerializable("data", payouts);
                    fragment.setArguments(args);

                    LinearLayout linearLayoutAmPm = findViewById(R.id.linear_farmers_titles);
                    linearLayoutAmPm.setVisibility(View.GONE);

                    LinearLayout linearLayout = findViewById(R.id.linear_collection_titles);
                    linearLayout.setVisibility(View.VISIBLE);

                    popOutFragments();
                    setUpView(fragment);

                    //mSearchView.setVisibility(View.GONE);



                } else {

                    Fragment fragment = new FragmentPayoutFarmersList();
                    Bundle args = new Bundle();
                    args.putSerializable("data", payouts);
                    fragment.setArguments(args);

                    LinearLayout linearLayoutAmPm = findViewById(R.id.linear_farmers_titles);
                    linearLayoutAmPm.setVisibility(View.VISIBLE);

                    LinearLayout linearLayout = findViewById(R.id.linear_collection_titles);
                    linearLayout.setVisibility(View.GONE);


                    popOutFragments();
                    setUpView(fragment);

                }
            });

        } catch (Exception nm) {
            nm.printStackTrace();
        }
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

    public void initCardHeader() {
        background = findViewById(R.id.background);
        startDate = findViewById(R.id.txt_date_start);
        endDate = findViewById(R.id.txt_date_end);


        cycleName = findViewById(R.id.txt_cycle);

        milkTotal = findViewById(R.id.txt_milk_totals);
        loanTotal = findViewById(R.id.txt_loans_total);
        orderTotal = findViewById(R.id.txt_orders_total);

        approvedCount = findViewById(R.id.txt_approved_farmers);
        unApprovedCount = findViewById(R.id.txt_pending_farmers);
        balance = findViewById(R.id.txt_Bal_out);

        if (payouts != null) {
            setCardHeaderData(payouts);
        }
        //setSpinner();

    }

    public void setCardHeaderData(com.dev.lishabora.Models.Payouts model) {
        startDate.setText(model.getStartDate());
        endDate.setText(model.getEndDate());
        cycleName.setText(model.getCyclename());


        milkTotal.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getMilkTotalLtrs(), 1), this.getString(R.string.ltrs)));
        loanTotal.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getLoanTotal(), 1), this.getString(R.string.ksh)));
        orderTotal.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getOrderTotal(), 1), this.getString(R.string.ksh)));
        balance.setText(String.format("%s %s", GeneralUtills.Companion.round(model.getBalance(), 1), this.getString(R.string.ksh)));
        GeneralUtills.Companion.changeCOlor(model.getBalance(), balance, 1);


        approvedCount.setText(model.getApprovedCards());
        unApprovedCount.setText(model.getPendingCards());


        if (model.getStatus() == 1) {
            // status.setText("Active");
            background.setBackgroundColor(this.getResources().getColor(R.color.green_color_picker));


        } else if (model.getStatus() == 0) {
            background.setBackgroundColor(this.getResources().getColor(R.color.red));

        } else {
            background.setBackgroundColor(this.getResources().getColor(R.color.blue_color_picker));

        }

    }



}
