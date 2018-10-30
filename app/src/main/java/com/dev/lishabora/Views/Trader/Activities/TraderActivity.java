package com.dev.lishabora.Views.Trader.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishabora.Adapters.ViewPagerAdapter;
import com.dev.lishabora.Application;
import com.dev.lishabora.Database.LMDatabase;
import com.dev.lishabora.Models.ResponseModel;
import com.dev.lishabora.Models.ResponseObject;
import com.dev.lishabora.Models.Trader.TraderModel;
import com.dev.lishabora.Network.ApiConstants;
import com.dev.lishabora.Network.Request;
import com.dev.lishabora.TrackerService;
import com.dev.lishabora.Utils.Jobs.Evernote.PayoutCheckerJob;
import com.dev.lishabora.Utils.Jobs.Evernote.UpSyncJob;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.Utils.ResponseCallback;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishabora.Views.Login.Activities.LoginActivity;
import com.dev.lishabora.Views.Login.ResetPassword;
import com.dev.lishabora.Views.Reports.FragmentReports;
import com.dev.lishabora.Views.Reports.HistoryToolBarUI;
import com.dev.lishabora.Views.Reports.Reports;
import com.dev.lishabora.Views.Trader.Fragments.CollectMilk;
import com.dev.lishabora.Views.Trader.Fragments.FragementFarmersList;
import com.dev.lishabora.Views.Trader.Fragments.FragmentNotifications;
import com.dev.lishabora.Views.Trader.Fragments.FragmentPayouts;
import com.dev.lishabora.Views.Trader.Fragments.FragmentProductList;
import com.dev.lishabora.Views.Trader.Fragments.FragmentRoutes;
import com.dev.lishabora.Views.Trader.Fragments.FragmentTraderProfile;
import com.dev.lishaboramobile.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.mikepenz.materialdrawer.Drawer;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.dev.lishabora.Application.collectMilk;

public class TraderActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST = 1;

    private static Fragment fragment = null;
    PrefrenceManager traderPrefs;
    FloatingActionButton fab;
    private ProgressDialog progressDialog;
    private GoogleApiClient googleApiClient;


    SearchView mSearchView;
    ViewPagerAdapter adapter;
    private AVLoadingIndicatorView avi;

    private TraderViewModel viewModel;

    private Drawer d;
    private static final String SAMPLE_DB_NAME = "lm_database";
    private static final String SAMPLE_TABLE_NAME = "Info";

    private void settings() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext()).addApi(LocationServices.API).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

// **************************

            builder.setAlwaysShow(true); // this is the key ingredient


            PendingResult result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            //                @Override
//                public void onResult(@NonNull Result result) {
//
//                }
//  @Override
            result.setResultCallback((ResultCallback<LocationSettingsResult>) results -> {

                final Status status = results.getStatus();

                final LocationSettingsStates state = results.getLocationSettingsStates();

                switch (status.getStatusCode())

                {

                    case LocationSettingsStatusCodes.SUCCESS:

                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:


                        try {

                            status.startResolutionForResult(TraderActivity.this, 1000);

                        } catch (IntentSender.SendIntentException e)

                        {

                        }

                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                        break;

                }

            });
        }
    }

    private void startTrackerService() {
        startService(new Intent(this, TrackerService.class));
        // finish();
    }

    private void setUp() {
        // Check GPS is enabled
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (lm != null && !lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
            //finish();
            settings();

        }

        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startTrackerService();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start the service when the permission is granted
            startTrackerService();
        } else {
            //finish();
        }
    }

    private void listenOnSyncStatus() {
        TextView txt_network_state = d.getFooter().findViewById(R.id.txt_network_state);

        viewModel.getTrader(traderPrefs.getCode()).observe(this, traderModel -> {

            if (traderModel != null) {
                if (traderModel.getSynchingStatus() == 1) {
                    txt_network_state.setVisibility(View.VISIBLE);
                    txt_network_state.setText("Syncing data ....");
                } else if (traderModel.getSynchingStatus() == 2) {
                    txt_network_state.setVisibility(View.VISIBLE);
                    if (Application.isConnected) {
                        txt_network_state.setText(traderModel.getLastsynchingMessage());
                    } else {
                        txt_network_state.setText("No internet sync failed");

                    }


                } else {
                    txt_network_state.setVisibility(View.GONE);

                }
            }


        });
    }


    void setUpDrawer(Toolbar toolbar, String name, String email) {
        d = DrawerClass.Companion.getDrawer("0", email, name, this, toolbar, new DrawerItemListener() {

            @Override
            public void syncDue(Application.hasSynced a) {
                CommonFuncs.syncDue(TraderActivity.this, a.getDays());
            }
            @Override
            public void wrongTime() {
                CommonFuncs.timeIs(TraderActivity.this);
            }

            @Override
            public void loansAndOrders() {
                startActivity(new Intent(TraderActivity.this, LoansAndOrders.class));

            }

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

                if (Application.canLogOut()) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(TraderActivity.this);
                    alertDialog.setMessage("Are sure you want to log out").setCancelable(false).setTitle("Log Out");


                    alertDialog.setPositiveButton("Yes", (dialogInterface, i) -> {
                        dialogInterface.dismiss();


                        JSONObject jsonObject = new JSONObject();
                        TraderModel traderModel = new PrefrenceManager(TraderActivity.this).getTraderModel();
                        traderModel.setIsLoggedIn(0);
                        try {
                            jsonObject = new JSONObject(new Gson().toJson(traderModel));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        updateTrader(jsonObject);



                    }).setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());

                    AlertDialog alertDialogAndroid = alertDialog.create();
                    alertDialogAndroid.setCancelable(false);
                    alertDialogAndroid.show();

                } else {
                    MyToast.toast("You have un-synced data... ", TraderActivity.this, R.drawable.ic_error_outline_black_24dp, Toast.LENGTH_LONG);
                    UpSyncJob.scheduleExact();

                }


            }

            @Override
            public void helpClicked() {
                Application.syncChanges();
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
                fragment = new FragmentNotifications();
                popOutFragments();
                setUpView("Notifications");
                // MyToast.toast("We are working on implementing this  \n sit tight", TraderActivity.this, R.drawable.ic_launcher, Toast.LENGTH_LONG);
//
//                private void exportDB(){
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
        listenOnSyncStatus();
        viewModel.getTrader(traderPrefs.getCode()).observe(this, traderModel -> {
            if (traderModel != null) {
                DrawerClass.Companion.observeChangesInProfile(traderModel);
            }


        });
        viewModel.getNotifications(0).observe(this, notifications -> {
            if (notifications != null) {
                DrawerClass.Companion.observeChangesInNotifications(notifications.size());

            } else {
                DrawerClass.Companion.observeChangesInNotifications(0);
            }
        });

        //  DrawerClass.Companion.setOpened(true);
    }

    public void updateTrader(JSONObject jsonObject) {
        progressDialog = new ProgressDialog(TraderActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Logging you out");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.show();

        Request.Companion.getResponse(ApiConstants.Companion.getUpdateTrader(), jsonObject, "",
                new ResponseCallback() {
                    @Override
                    public void response(ResponseModel responseModel) {
                        progressDialog.dismiss();
                        if (responseModel.getResultCode() == 1) {
                            logOut();

                        } else {
                            MyToast.toast(responseModel.getResultDescription(), TraderActivity.this, R.drawable.ic_error_outline_black_24dp, Toast.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void response(ResponseObject responseModel) {
                        progressDialog.dismiss();

                        if (responseModel.getResultCode() == 1) {
                            logOut();
                        } else {
                            MyToast.toast(responseModel.getResultDescription(), TraderActivity.this, R.drawable.ic_error_outline_black_24dp, Toast.LENGTH_LONG);
                        }
                    }
                });

    }

    public void logOut() {
        LMDatabase lmDatabase = LMDatabase.getDatabase(TraderActivity.this);
        lmDatabase.clearAllTables();
        new PrefrenceManager(TraderActivity.this).setIsLoggedIn(false, 0);


        startActivity(new Intent(TraderActivity.this, LoginActivity.class));
        finish();

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


    public void intNoti() {
        Intent i = getIntent();
        try {
            if (i != null && i.getStringExtra("type").equals("notification_fragment")) {
                fragment = new FragmentNotifications();
                popOutFragments();
                setUpView("Notifications");
            }
        } catch (Exception nm) {
            nm.printStackTrace();
        }
        try {
            PayoutCheckerJob.schedulePeriodic();
        } catch (Exception nm) {
            nm.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        setUp();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        try {

            if (collectMilk != null) {
                collectMilk.onDestroy();
            }
            super.onDestroy();

        } catch (Exception nm) {
            nm.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trader);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewModel = ViewModelProviders.of(this).get(TraderViewModel.class);


        traderPrefs = new PrefrenceManager(this);
        setUpDrawer(toolbar, traderPrefs.getTraderModel().getMobile(), traderPrefs.getTraderModel().getNames());

        fab = findViewById(R.id.fab);
        fab.hide();

        setUpMainFragment();


        observeSyncDown();


        collectMilk = new CollectMilk(TraderActivity.this, this, true);

//        initConnectivityListener();


        boolean[] settings = new PrefrenceManager(TraderActivity.this).getSettingStatus();
        for (boolean n : settings) {
            if (!n) {
                setUpSettingsDialog();
                return;
            }
        }


        intNoti();

    }

    private void observeSyncDown() {

        progressDialog = new ProgressDialog(TraderActivity.this);
        viewModel.observeSyncDown().observe(this, syncDownObserver -> {
            if (syncDownObserver != null) {
                if (syncDownObserver.getStatus() == 0) {
                    if (!progressDialog.isShowing()) {
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("Syncing Down your saved data . .. Please wait ");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                    } else {
                        if (progressDialog != null) {

                            progressDialog.dismiss();
                        }
                    }

                } else {
                    if (progressDialog != null) {

                        progressDialog.dismiss();
                    }
                }
            }
        });


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

    @Override
    public void onBackPressed() {

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        if (f instanceof FragementFarmersList) {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit");
            builder.setMessage("Are You Sure?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    TraderActivity.super.onBackPressed();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();            //additional code
        } else {
            popOutFragments();
        }


    }
}
