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
import android.widget.RelativeLayout;

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


    //    public static void setSpinner(int v) {
//        try {
//            spinner.setVisibility(v);
//
//        } catch (Exception nm) {
//            nm.printStackTrace();
//        }
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


        return super.onOptionsItemSelected(item);
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

                    popOutFragments();
                    setUpView(fragment);

                    //mSearchView.setVisibility(View.GONE);



                } else {

                    Fragment fragment = new FragmentPayoutFarmersList();
                    Bundle args = new Bundle();
                    args.putSerializable("data", payouts);
                    fragment.setArguments(args);

                    //mSearchView.setVisibility(View.VISIBLE);


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


}
