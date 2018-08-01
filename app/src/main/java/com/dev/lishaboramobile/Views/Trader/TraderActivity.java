package com.dev.lishaboramobile.Views.Trader;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishaboramobile.Global.Models.ResponseModel;
import com.dev.lishaboramobile.Global.Utils.DateTimeUtils;
import com.dev.lishaboramobile.Global.Utils.MyToast;
import com.dev.lishaboramobile.Global.Utils.RequestDataCallback;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Trader.Data.TraderPrefs;
import com.dev.lishaboramobile.Trader.Models.TraderModel;
import com.dev.lishaboramobile.Views.Trader.ui.FragementFarmersList;
import com.dev.lishaboramobile.Views.Trader.ui.FragmentProductList;
import com.dev.lishaboramobile.Views.Trader.ui.FragmentRoutes;
import com.dev.lishaboramobile.admin.adapters.ViewPagerAdapter;
import com.dev.lishaboramobile.admin.ui.ResetPassword;
import com.dev.lishaboramobile.admin.ui.admins.AdminsViewModel;
import com.dev.lishaboramobile.login.LoginActivity;
import com.dev.lishaboramobile.login.PrefrenceManager;
import com.dev.lishaboramobile.login.ui.login.LoginController;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class TraderActivity extends AppCompatActivity {
    private static Fragment fragment = null;
    TraderPrefs traderPrefs;
    FloatingActionButton fab;
    SearchView mSearchView;
    ViewPagerAdapter adapter;
    private AVLoadingIndicatorView avi;
    private AdminsViewModel adminsViewModel;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private FragementFarmersList farmersList;
    private FragmentProductList productsList;
    private FragmentPayouts payouts;







    void setUpDrawer(Toolbar toolbar, String name, String email) {
        DrawerClass.Companion.getDrawer(email, name, this, toolbar, new DrawerItemListener() {
            @Override
            public void productsClicked() {
                fragment = new FragmentProductList();
                popOutFragments();
                setUpView();
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

            }

            @Override
            public void profileSettingsClicked() {
                editTrader(new PrefrenceManager(TraderActivity.this).getTraderModel());

            }

            @Override
            public void routesSettingsClicked() {

                fragment = new FragmentRoutes();
                popOutFragments();
                setUpView();


            }

            @Override
            public void notificationsClicked() {

            }

            @Override
            public void appSettingsClicked() {

            }

            @Override
            public void homeClicked() {

                popOutFragments();
            }

            @Override
            public void analyticsReportsTransactionsClicked() {

            }

            @Override
            public void payoutsClicked() {

            }
        });

    }

    void setUpView() {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.left_out, R.anim.right_enter)
                    .
                            replace(R.id.frame_layout, fragment)
                    .addToBackStack(null).commit();
        }

    }

    void popOutFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
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


        traderPrefs = new TraderPrefs(this);
        setUpDrawer(toolbar, traderPrefs.getTraderModel().getMobile(), traderPrefs.getTraderModel().getNames());

        fab = findViewById(R.id.fab);
        fab.hide();
//
        setUpMainFragment();
//        viewPager = findViewById(R.id.viewpager);
//        viewPager.setVisibility(View.VISIBLE);
//        viewPager.setOffscreenPageLimit(4);
//
//
//        tabLayout = findViewById(R.id.tabs);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//        tabLayout.setupWithViewPager(viewPager);
//
//        tabLayout.setVisibility(View.VISIBLE);
//
//        setupViewPager(viewPager);
//        setupTabIcons();

        boolean[] settings = new PrefrenceManager(TraderActivity.this).getSettingStatus();
        for (boolean n : settings) {
            if (!n) {
                setUpSettingsDialog();
                return;
            }
        }


    }

    private void setUpSettingsDialog() {


    }

    private void setupTabIcons() {

        Objects.requireNonNull(tabLayout.getTabAt(0)).setText("Farmers");
        Objects.requireNonNull(tabLayout.getTabAt(1)).setText("Payouts");
        Objects.requireNonNull(tabLayout.getTabAt(2)).setText("Products");


    }

    private void setupViewPager(ViewPager viewPager) {
//        adapter = new ViewPagerAdapter(getSupportFragmentManager());
//
//
//        farmersList = new FragementFarmersList();
//        payouts = new FragmentPayouts();
//        productsList = new FragmentProductList();
//
//
//        adapter.addFragment(basicProfile, "Profile");
//        adapter.addFragment(payouts, "Payouts");
//        adapter.addFragment(products, "Products");
//
//        viewPager.setOffscreenPageLimit(1);
//        viewPager.setAdapter(adapter);
//
//        View root = tabLayout.getChildAt(0);
//        if (root instanceof LinearLayout) {
//            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//            GradientDrawable drawable = new GradientDrawable();
//            drawable.setColor(getResources().getColor(R.color.black));
//            drawable.setSize(2, 1);
//            //((LinearLayout) root).setDividerPadding(10);
//            //((LinearLayout) root).setDividerDrawable(drawable);
//        }

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


    public void editTrader(TraderModel traderModel) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(TraderActivity.this);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_add_trader, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(TraderActivity.this));
        alertDialogBuilderUserInput.setView(mView);
//        alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
//        alertDialogBuilderUserInput.setTitle("Trader");


        avi = mView.findViewById(R.id.avi);


        TextInputEditText edtNames, edtMobile;
        edtMobile = mView.findViewById(R.id.edt_traders_phone);
        edtNames = mView.findViewById(R.id.edt_traders_names);

        edtMobile.setText(traderModel.getMobile());
        edtNames.setText(traderModel.getNames());

        CheckBox chk = mView.findViewById(R.id.chk_dummy);
        chk.setVisibility(View.GONE);


        alertDialogBuilderUserInput
                .setCancelable(false);
//                .setPositiveButton("Update", (dialogBox, id) -> {
//                    // ToDo get user input here
//
//
//                })
//
//                .setNegativeButton("Dismiss",
//                        (dialogBox, id) -> dialogBox.cancel());

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        alertDialogAndroid.show();

//        Button theButton = alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE);
//        theButton.setOnClickListener(new EditCustomListener(alertDialogAndroid, traderModel));

        MaterialButton btnPositive, btnNegative, btnNeutral;
        TextView txtTitle;
        LinearLayout lTitle;
        ImageView imgIcon;
        btnPositive = mView.findViewById(R.id.btn_positive);
        btnNegative = mView.findViewById(R.id.btn_negative);
        btnNeutral = mView.findViewById(R.id.btn_neutral);
        txtTitle = mView.findViewById(R.id.txt_title);
        lTitle = mView.findViewById(R.id.linear_title);
        imgIcon = mView.findViewById(R.id.img_icon);


        btnNeutral.setVisibility(View.GONE);
        lTitle.setVisibility(View.GONE);
        txtTitle.setVisibility(View.VISIBLE);
        imgIcon.setVisibility(View.VISIBLE);
        imgIcon.setImageResource(R.drawable.ic_add_black_24dp);
        txtTitle.setText("Trader");

        btnPositive.setOnClickListener(new EditCustomListener(alertDialogAndroid, traderModel));
        btnNeutral.setOnClickListener(view -> {

        });
        btnNegative.setOnClickListener(view -> alertDialogAndroid.dismiss());




    }

    private class EditCustomListener implements View.OnClickListener {
        AlertDialog dialog;
        boolean isDummy = false;
        TraderModel traderModel;
        //int type;
        RequestDataCallback requestDataCallback;

        public EditCustomListener(AlertDialog alertDialogAndroid, TraderModel traderModel) {
            dialog = alertDialogAndroid;
            this.traderModel = traderModel;

        }

        @Override
        public void onClick(View v) {
            TextInputEditText name, phone, bussinessname;
            Spinner spinnerPayment;
            CheckBox chkDummy;
            Gson gson = new Gson();

            spinnerPayment = dialog.findViewById(R.id.spinner);
            bussinessname = dialog.findViewById(R.id.edt_traders_business_name);

            name = dialog.findViewById(R.id.edt_traders_names);
            phone = dialog.findViewById(R.id.edt_traders_phone);
            chkDummy = dialog.findViewById(R.id.chk_dummy);

            if (name.getText().toString().isEmpty()) {
                name.setError("Required");
                name.requestFocus();
                avi.smoothToHide();
                return;
            }


            if (phone.getText().toString().isEmpty()) {
                phone.setError("Required");
                phone.requestFocus();
                avi.smoothToHide();

                return;
            }
            String bussines = "";


            if (!bussinessname.getText().toString().isEmpty()) {
                bussines = bussinessname.getText().toString();
            }

            if (!LoginController.isValidPhoneNumber(phone.getText().toString())) {
                phone.requestFocus();
                avi.smoothToHide();

                phone.setError("Invalid Phone number");
            }

            String defaultPayment = "";

            if (spinnerPayment != null && spinnerPayment.getSelectedItemPosition() == 0) {

                spinnerPayment.requestFocus();

                avi.smoothToHide();

                //phone.setError("Invalid Phone number");
            }


            if (chkDummy.isChecked()) {
                isDummy = true;
            }


            traderModel.setDefaultpaymenttype(spinnerPayment.getSelectedItem().toString());
            traderModel.setNames(name.getText().toString());
            traderModel.setMobile(phone.getText().toString());
            traderModel.setTransactiontime(DateTimeUtils.Companion.getNowslong());
            traderModel.setBusinessname(bussines);


            avi.smoothToShow();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(gson.toJson(traderModel));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            adminsViewModel.updateTrader(jsonObject, true).observe(TraderActivity.this, new Observer<ResponseModel>() {
                @Override
                public void onChanged(@Nullable ResponseModel responseModel) {
                    avi.smoothToHide();
                    //snack(responseModel.getResultDescription());
                    if (responseModel.getResultCode() == 1) {
                        dialog.dismiss();
                        new PrefrenceManager(TraderActivity.this).setLoggedUser(traderModel);
                        MyToast.toast(responseModel.getResultDescription(), TraderActivity.this, R.drawable.ic_launcher, Toast.LENGTH_LONG);
                        //mViewModel.refresh(getSearchObject(), true);
                    } else {
                        MyToast.toast(responseModel.getResultDescription(), TraderActivity.this, R.drawable.ic_launcher, Toast.LENGTH_LONG);

                    }
                }
            });


        }


    }

}
