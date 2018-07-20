package com.dev.lishaboramobile.Views.Trader;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dev.lishaboramobile.Admin.Callbacks.FabCallbacks;
import com.dev.lishaboramobile.Admin.Callbacks.SearchViewCallbacks;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Trader.Data.TraderPrefs;

public class TraderActivity extends AppCompatActivity {
    private static Fragment fragment = null;
    TraderPrefs traderPrefs;
    FloatingActionButton fab;
    SearchView mSearchView;


    void setUpDrawer(Toolbar toolbar, String name, String email) {
        DrawerClass.Companion.getDrawer(email, name, this, toolbar, new DrawerItemListener() {
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
            public void routesSettingsClicked() {

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

            }

            @Override
            public void payoutsClicked() {

            }
        });

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

        traderPrefs = new TraderPrefs(this);
        setUpDrawer(toolbar, traderPrefs.getTraderModel().getMobile(), traderPrefs.getTraderModel().getNames());

        fab = findViewById(R.id.fab);

        setUpMainFragment();


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
