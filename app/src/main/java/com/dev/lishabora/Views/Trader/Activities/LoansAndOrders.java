package com.dev.lishabora.Views.Trader.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.dev.lishabora.Adapters.FarmersAdapter;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.Utils.RecyclerTouchListener;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.Trader.Fragments.FragmentTraderLoans;
import com.dev.lishabora.Views.Trader.Fragments.FragmentTraderOrders;
import com.dev.lishabora.Views.Trader.OrderConstants;
import com.dev.lishaboramobile.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoansAndOrders extends AppCompatActivity {
    static Fragment page;
    private static ViewPager viewPager;
    SearchView mSearchView;
    private TabLayout tabLayout;
    FarmersAdapter listAdapterP;
    AlertDialog alertDialogAndroid;
    List<Integer> unclickableRows, unswipeableRows;
    private TraderViewModel traderViewModel;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private RecyclerTouchListener onTouchListener;
    private void setupViewPager(ViewPager viewPager) {


        Fragment fragment = new FragmentTraderLoans();
        Bundle args = new Bundle();
        args.putSerializable("data", "");
        fragment.setArguments(args);

        Fragment fragment1 = new FragmentTraderOrders();
        args.putSerializable("data", "");

        fragment.setArguments(args);


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(fragment, "loans");
        adapter.addFragment(fragment1, "orders");

        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {

        tabLayout.getTabAt(0).setText("Loans");
        tabLayout.getTabAt(1).setText("Orders");


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loans_and_orders);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        traderViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.hide();
        fab.setOnClickListener(view -> give());

    }

    private void give() {
        int id = viewPager.getCurrentItem();
        traderViewModel.getFarmerByStatusRoute(50, "").observe(this, famerModels -> {

            if (famerModels != null && famerModels.size() > 0) {
                displayFarmers(id, famerModels);
            } else {
                MyToast.errorToast("You have no farmers", LoansAndOrders.this);
            }
        });

    }

    private void displayFarmers(int id, List<FamerModel> famerModels) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.fragment_recycler_view, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(this));
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
        alertDialogBuilderUserInput.setTitle("Farmers");

        RecyclerView recyclerView = mView.findViewById(R.id.recyclerView);
        unclickableRows = new ArrayList<>();
        unswipeableRows = new ArrayList<>();


        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        listAdapterP = new FarmersAdapter(this, famerModels, new OnclickRecyclerListener() {
            @Override
            public void onClickListener(int position) {

            }

            @Override
            public void onLongClickListener(int position) {

            }

            @Override
            public void onCheckedClickListener(int position) {

            }

            @Override
            public void onMoreClickListener(int position) {

            }

            @Override
            public void onMenuItem(int position, int menuItem) {

            }

            @Override
            public void onClickListener(int adapterPosition, @NotNull View view) {

            }

            @Override
            public void onSwipe(int adapterPosition, int direction) {

            }
        }, null);


        recyclerView.setAdapter(listAdapterP);

        listAdapterP.notifyDataSetChanged();
        onTouchListener = new RecyclerTouchListener(this, recyclerView);
        onTouchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        alertDialogAndroid.dismiss();
                        if (id == 0) {

                        } else {
                            OrderConstants.setFamerModel(famerModels.get(position));
                            Intent intent2 = new Intent(LoansAndOrders.this, GiveOrder.class);
                            intent2.putExtra("farmer", famerModels.get(position));
                            startActivity(intent2);
                        }
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {

                    }
                })
                .setLongClickable(true, position -> {

                });
        alertDialogBuilderUserInput
                .setCancelable(true);

        alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(true);
        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        try {
            alertDialogAndroid.show();
        } catch (Exception NM) {
            NM.printStackTrace();
        }

    }

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
