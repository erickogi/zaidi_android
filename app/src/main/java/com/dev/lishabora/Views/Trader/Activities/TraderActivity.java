package com.dev.lishabora.Views.Trader.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.dev.lishabora.Adapters.ViewPagerAdapter;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.ViewModels.Admin.AdminsViewModel;
import com.dev.lishabora.Views.Login.Activities.LoginActivity;
import com.dev.lishabora.Views.Login.ResetPassword;
import com.dev.lishabora.Views.Reports.FragmentReports;
import com.dev.lishabora.Views.Reports.HistoryToolBarUI;
import com.dev.lishabora.Views.Reports.Reports;
import com.dev.lishabora.Views.Trader.Fragments.FragementFarmersList;
import com.dev.lishabora.Views.Trader.Fragments.FragmentPayouts;
import com.dev.lishabora.Views.Trader.Fragments.FragmentProductList;
import com.dev.lishabora.Views.Trader.Fragments.FragmentRoutes;
import com.dev.lishabora.Views.Trader.Fragments.FragmentTraderProfile;
import com.dev.lishaboramobile.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Objects;

public class TraderActivity extends AppCompatActivity {
    private static Fragment fragment = null;
    PrefrenceManager traderPrefs;
    FloatingActionButton fab;
    SearchView mSearchView;
    ViewPagerAdapter adapter;
    private AVLoadingIndicatorView avi;
    private AdminsViewModel adminsViewModel;


    private static final String SAMPLE_DB_NAME = "lm_database";
    private static final String SAMPLE_TABLE_NAME = "Info";




    void setUpDrawer(Toolbar toolbar, String name, String email) {
        DrawerClass.Companion.getDrawer(email, name, this, toolbar, new DrawerItemListener() {
            @Override
            public void syncWorksClicked() {
                startActivity(new Intent(TraderActivity.this, SyncWorks.class));

            }

            @Override
            public void productsClicked() {
                fragment = new FragmentProductList();
                popOutFragments();
                setUpView("Products");
            }

            @Override
            public void logOutClicked() {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(TraderActivity.this);
                alertDialog.setMessage("Are sure you want to log out").setCancelable(false).setTitle("Log Out");


                alertDialog.setPositiveButton("Yes", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    new PrefrenceManager(TraderActivity.this).setIsLoggedIn(false, 0);
                    startActivity(new Intent(TraderActivity.this, LoginActivity.class));
                    finish();
                }).setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());

                AlertDialog alertDialogAndroid = alertDialog.create();
                alertDialogAndroid.setCancelable(false);
                alertDialogAndroid.show();



            }

            @Override
            public void helpClicked() {
                //fragment = new FragmentSyncs();

                MyToast.toast("We are working on implementing this  \n sit tight", TraderActivity.this, R.drawable.ic_launcher, Toast.LENGTH_LONG);

            }

            @Override
            public void profileSettingsClicked() {
                fragment = new FragmentTraderProfile();
                popOutFragments();
                setUpView("My Profile");
            }

            @Override
            public void routesSettingsClicked() {

                fragment = new FragmentRoutes();
                popOutFragments();
                setUpView("Routes");


            }

            @Override
            public void notificationsClicked() {
                MyToast.toast("We are working on implementing this  \n sit tight", TraderActivity.this, R.drawable.ic_launcher, Toast.LENGTH_LONG);

////                private void exportDB(){
//                File sd = Environment.getExternalStorageDirectory();
//                File data = Environment.getDataDirectory();
//                FileChannel source = null;
//                FileChannel destination = null;
//                String currentDBPath = "/data/" + "com.dev.lishaboramobile" + "/databases/" + SAMPLE_DB_NAME;
//                String backupDBPath = SAMPLE_DB_NAME + ".db";
//                File currentDB = new File(data, currentDBPath);
//                File backupDB = new File(sd, backupDBPath);
//                try {
//                    source = new FileInputStream(currentDB).getChannel();
//                    destination = new FileOutputStream(backupDB).getChannel();
//                    destination.transferFrom(source, 0, source.size());
//                    source.close();
//                    destination.close();
//                    Toast.makeText(TraderActivity.this, "DB Exported!", Toast.LENGTH_LONG).show();
//                } catch (IOException e) {
//                    Toast.makeText(TraderActivity.this, "DB  not Exported!" + e.toString(), Toast.LENGTH_LONG).show();
//
//                    e.printStackTrace();
//                }
//                // }

            }

            @Override
            public void appSettingsClicked() {
                MyToast.toast("We are working on implementing this  \n sit tight", TraderActivity.this, R.drawable.ic_launcher, Toast.LENGTH_LONG);

            }

            @Override
            public void homeClicked() {

                TraderActivity.this.setTitle("Farmers");
                popOutFragments();
            }

            @Override
            public void analyticsReportsTransactionsClicked() {
                fragment = new FragmentReports();
                popOutFragments();
                setUpView("Reports");
            }

            @Override
            public void payoutsClicked() {
                fragment = new FragmentPayouts();
                popOutFragments();
                setUpView("Payouts");



            }
        });

    }

    void setUpView(String name) {

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.left_out, R.anim.right_enter).
                    replace(R.id.frame_layout, fragment).addToBackStack(null).commit();
        }

        try {
            this.setTitle(name);
        } catch (Exception nm) {
            nm.printStackTrace();
        }

    }

    void popOutFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
        this.setTitle("Farmers");
    }

    private void setUpMainFragment() {

        fragment = new FragementFarmersList();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment, "fragmentMain").commit();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trader);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        adminsViewModel = ViewModelProviders.of(this).get(AdminsViewModel.class);


        traderPrefs = new PrefrenceManager(this);
        setUpDrawer(toolbar, traderPrefs.getTraderModel().getMobile(), traderPrefs.getTraderModel().getNames());

        fab = findViewById(R.id.fab);
        fab.hide();
        if (traderPrefs.isTraderFirstTime()) {
            alertDialogFirstTime();
        }

        setUpMainFragment();


        boolean[] settings = new PrefrenceManager(TraderActivity.this).getSettingStatus();
        for (boolean n : settings) {
            if (!n) {
                setUpSettingsDialog();
                return;
            }
        }


    }

    private void alertDialogFirstTime() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(this));
        alertDialogBuilderUserInput.setTitle("Initial Account Setup ");
        alertDialogBuilderUserInput.setMessage("It seems to be the first time you are logging into the app.. \nTo continue using the app fully, you will need to setup you basic details ,routes , cycles and products information .\n Press continue to do so  thank you.");


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Continue", (dialogBox, id) -> {
                    // ToDo get user input here
                    startActivity(new Intent(TraderActivity.this, FirstTimeLaunch.class));


                })

                .setNegativeButton("Skip for now",
                        (dialogBox, id) -> dialogBox.cancel());

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        alertDialogAndroid.show();


    }

    private void setUpSettingsDialog() {


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
            startActivity(new Intent(TraderActivity.this, ResetPassword.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void milkFragment(View view) {
        Intent intent = new Intent(this, Reports.class);
        intent.putExtra("type", HistoryToolBarUI.TYPE_MILK);
        startActivity(intent);
    }

    public void loansFragment(View view) {
        Intent intent = new Intent(this, Reports.class);
        intent.putExtra("type", HistoryToolBarUI.TYPE_LOAN);
        startActivity(intent);
    }

    public void productsFragment(View view) {
        Intent intent = new Intent(this, Reports.class);
        intent.putExtra("type", HistoryToolBarUI.TYPE_ORDERS);
        startActivity(intent);
    }

    public void totalsFragment(View view) {
        Intent intent = new Intent(this, Reports.class);
        intent.putExtra("type", HistoryToolBarUI.TYPE_ALL);
        startActivity(intent);
    }


}
