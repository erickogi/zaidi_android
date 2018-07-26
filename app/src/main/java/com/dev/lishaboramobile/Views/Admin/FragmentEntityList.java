package com.dev.lishaboramobile.Views.Admin;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.lishaboramobile.Global.AppConstants;
import com.dev.lishaboramobile.Global.Models.FetchTraderModel;
import com.dev.lishaboramobile.Global.Utils.NetworkUtils;
import com.dev.lishaboramobile.Global.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Trader.Models.TraderModel;
import com.dev.lishaboramobile.admin.TradersController;
import com.dev.lishaboramobile.admin.TradersViewModel;
import com.dev.lishaboramobile.admin.adapters.TradersAdapter;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FragmentEntityList extends Fragment {

    Chip chipAll;
    Chip chipDummy;
    Chip chipDelete;
    Chip chipArchive;


    TradersAdapter listAdapter;
    Gson gson = new Gson();
    TradersController tradersController;
    List<Object> objects;
    LinkedList<TraderModel> traderModels;
    LinkedList<TraderModel> filteredTraderModels;
    boolean isArchived = false;
    boolean isDummy = false;
    TradersViewModel tradersViewModel;
    ViewModel viewModel;
    private View view;
    private Context context;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout linearLayoutEmpty;
    private int entity;
    private ProgressDialog progressDialog;
    private ActionMode mActionMode;
    private SwipeRefreshLayout swipe_refresh_layout;
    private LinearLayout empty_layout;
    private TextView emptyTxt, txt_network_state;
    private String filterText = "";
    private ChipGroup chipGroup;
    private Chip activeChip, allChip, deletedChip, archivedChip, syncedChip, dummyChip, unsyncedChip;
    private AVLoadingIndicatorView avi;
    private boolean isConnected;

    private int TraderDel, TraderDummy, TraderSynched, TraderArchive, All;
    private ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.entity_list_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete:

                    mode.finish();
                    break;
                case R.id.archive:

                    mode.finish();

                    break;
                case R.id.dummy:


                    mode.finish();

                    break;
                default:
                    return false;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            mActionMode = null;
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            entity = getArguments().getInt("Entity");
        }

        switch (entity) {
            case AppConstants.TRADER:

                return inflater.inflate(R.layout.fragment_entities_list, container, false);

            //break;

            default:
                return inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

            //initTrader();
        }

    }

    private void disablechips() {
        TraderDel = 0;
        TraderArchive = 0;
        TraderDummy = 0;
        All = 0;

    }

    private void setDefault() {
        //TraderDel=0;TraderArchive=0;TraderDummy=0;All=1;
        //chipAll.setChecked(true);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        recyclerView = view.findViewById(R.id.recyclerView);
        empty_layout = view.findViewById(R.id.empty_layout);
        emptyTxt = view.findViewById(R.id.empty_text);
        avi = view.findViewById(R.id.avi);
        txt_network_state = view.findViewById(R.id.txt_network_state);
        chipGroup = view.findViewById(R.id.chip_group);
        swipe_refresh_layout = view.findViewById(R.id.swipeRefreshView);
        chipAll = view.findViewById(R.id.chip_all);
        chipDummy = view.findViewById(R.id.chip_dummy);
        chipDelete = view.findViewById(R.id.chip_deleted);
        chipArchive = view.findViewById(R.id.chip_archived);
        chipAll.setChecked(true);
        disablechips();

        All = 1;

        chipDelete.setOnCheckedChangeListener((compoundButton, b) -> {

            disablechips();

            if (chipDelete.isChecked()) {
                TraderDel = 1;
            } else {
                TraderDel = 0;
            }
            if (tradersViewModel != null && tradersController != null) {
                startAnim();
                tradersViewModel.refresh(getSearchObject(), true);
            }
        });
        chipArchive.setOnCheckedChangeListener((compoundButton, b) -> {
            disablechips();

            if (chipArchive.isChecked()) {
                TraderArchive = 1;
            } else {
                TraderArchive = 0;
            }
            if (tradersViewModel != null && tradersController != null) {
                startAnim();
                tradersViewModel.refresh(getSearchObject(), true);
            }
        });
        chipAll.setOnCheckedChangeListener((compoundButton, b) -> {
            disablechips();

            if (b) {

                All = 1;


            } else {

                All = 0;
            }

            if (tradersViewModel != null && tradersController != null) {
                startAnim();
                /// emptyState(true,linearLayoutEmpty,);
                tradersViewModel.refresh(getSearchObject(), true);
            }
        });
        chipDummy.setOnCheckedChangeListener((compoundButton, b) -> {
            disablechips();

            if (chipDummy.isChecked()) {
                TraderDummy = 1;
            } else {
                setDefault();
                TraderDummy = 0;
            }

            if (tradersViewModel != null && tradersController != null) {
                startAnim();
                tradersViewModel.refresh(getSearchObject(), true);
            }
        });

        chipGroup.setOnCheckedChangeListener((chipGroup, i) -> {


        });


        ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnectedToInternet -> {
                    // do something with isConnectedToInternet value
                    isConnected = isConnectedToInternet;

                    if (isConnectedToInternet) {
                        try {
                            txt_network_state.setText("Connected to internet");
                            txt_network_state.setTextColor(getActivity().getResources().getColor(R.color.green_color_picker));
                        } catch (Exception nm) {
                            nm.printStackTrace();
                        }

                    } else {

                        try {
                            txt_network_state.setText("Not connected to internet");
                            txt_network_state.setTextColor(getActivity().getResources().getColor(R.color.red));
                        } catch (Exception vb) {
                            vb.printStackTrace();
                        }

                    }
                });

    }


    void getTradersv() {
        if (avi != null) {
            startAnim();
        }
        if (tradersViewModel != null) {


            tradersViewModel.getTraderModels(getSearchObject(), true).observe(this, responseModel -> {

                Gson gson = new Gson();
                if (avi != null) {
                    stopAnim();
                }
                if (swipe_refresh_layout.isRefreshing()) {
                    swipe_refresh_layout.setRefreshing(false);
                }

                if (responseModel.getResultCode() == 1 && responseModel.getData() != null) {
                    JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                    Type listType = new TypeToken<LinkedList<TraderModel>>() {
                    }.getType();
                    traderModels = gson.fromJson(jsonArray, listType);
                    filterTraders();
                    populateTraders();
                } else if (responseModel.getResultCode() == 2) {
                    traderModels.clear();
                    populateTraders();
                } else {


                    snack(responseModel.getResultDescription());
                }

            });


        }
    }

    private void initTrader() {

        if (NetworkUtils.Companion.isConnectionFast(Objects.requireNonNull(getActivity()))) {
            listAdapter = new TradersAdapter(getActivity(), filteredTraderModels, new OnclickRecyclerListener() {
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
                public void onClickListener(int adapterPosition, @NotNull View view) {
                    popupMenu(adapterPosition, view, filteredTraderModels.get(adapterPosition));

                }
            });

            getTradersv();

        } else {
            emptyState(false, "You are not connected to the internet ...", empty_layout, null, emptyTxt);
        }


        swipe_refresh_layout.setOnRefreshListener(() -> {
            startAnim();
            tradersViewModel.refresh(getSearchObject(), true);

        });



        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {


            }
        });


    }

    private JSONObject getSearchObject() {

        FetchTraderModel f = new FetchTraderModel();


        f.setArchived(TraderArchive);
        f.setDeleted(TraderDel);
        f.setAll(All);
        f.setDummy(TraderDummy);
        try {
            return new JSONObject(gson.toJson(f));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;


    }

    private void populateTraders() {
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (filteredTraderModels.size() < 1) {
            snack("No data");
        }

        listAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(listAdapter);
        emptyState(listAdapter.getItemCount() > 0, "We couldn't find any traders records", empty_layout, null, emptyTxt);

    }

    private void emptyState(boolean listHasData, String text, LinearLayout empty_layout, ImageView empty_image, TextView emptyTxt) {
        if (listHasData) {
            empty_layout.setVisibility(View.GONE);
        } else {
            empty_layout.setVisibility(View.VISIBLE);
            if (text != null) {
                emptyTxt.setText(text);
            }
        }
    }


    private void filterTraders() {

        filteredTraderModels.clear();
//        if(traderModels!=null&&traderModels.size()>0){
        for (TraderModel traderModel : traderModels) {
            if (traderModel.getCode().toLowerCase().contains(filterText) ||
                    traderModel.getMobile().toLowerCase().contains(filterText) ||
                    traderModel.getNames().toLowerCase().contains(filterText)) {
//
//                       if(traderModel.getDeleted()==TraderDel&&traderModel.getArchived()==TraderArchive&&traderModel.getDummy()==TraderDummy) {
                filteredTraderModels.add(traderModel);
//                       }
            }
//            }
        }
        listAdapter.notifyDataSetChanged();

    }

    @Override
    public void onStart() {
        super.onStart();
        traderModels = new LinkedList<>();
        tradersController = new TradersController(getContext());
        filteredTraderModels = new LinkedList<>();

        try {
            Objects.requireNonNull(getActivity()).setTitle("Traders");
        } catch (Exception nm) {
            nm.printStackTrace();
        }

        if (getArguments() != null) {
            entity = getArguments().getInt("Entity");
        }

        switch (entity) {
            case AppConstants.TRADER:

                tradersViewModel = ViewModelProviders.of(this).get(TradersViewModel.class);

                initTrader();

                break;

            default:
                initTrader();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (tradersController == null) {
            tradersController = new TradersController(context);

        }
        if (traderModels == null) {
            traderModels = new LinkedList<>();
        }
        if (filteredTraderModels == null) {
            filteredTraderModels = new LinkedList<>();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // tradersController = null;

    }

    @Override
    public void onStop() {
        super.onStop();
        tradersController = null;



    }

    private void snack(String msg) {
        if (view != null) {
            Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();

        }
        Log.d("SnackMessage", msg);
    }


    void startAnim() {
        avi.show();

    }

    void stopAnim() {
        avi.hide();

    }

    private void popupMenu(int pos, View view, TraderModel traderModel) {
        PopupMenu popupMenu = new PopupMenu(Objects.requireNonNull(getContext()), view);
        popupMenu.inflate(R.menu.entity_list_menu);

        if (traderModel.getDeleted() == 1) {
            popupMenu.getMenu().getItem(0).setVisible(false);
        }
        if (traderModel.getArchived() == 1) {
            isArchived = true;
            popupMenu.getMenu().getItem(1).setTitle("Un-Archive");
        }
        if (traderModel.getDummy() == 1) {
            isDummy = true;
            popupMenu.getMenu().getItem(2).setTitle("Remove from dummy");
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.delete:


                    traderModel.setStatus("Deleted");
                    traderModel.setDeleted(1);
                    update(traderModel);


                    break;

                case R.id.archive:

                    if (isArchived) {
                        traderModel.setStatus("Active");
                        traderModel.setArchived(0);
                    } else {
                        traderModel.setStatus("Archived");
                        traderModel.setArchived(1);
                    }
                    update(traderModel);

                    break;

                case R.id.dummy:

                    if (isDummy) {
                        traderModel.setStatus("Active");
                        traderModel.setDummy(0);
                    } else {
                        traderModel.setStatus("Dummy");
                        traderModel.setDummy(1);
                    }

                    update(traderModel);
                    break;
                case R.id.edit:

                    editTrader(traderModel);
                    break;
                default:
            }
            return false;
        });
        popupMenu.show();
    }

    private void editTrader(TraderModel traderModel) {

//
//        tradersController.editTrader(traderModel, requestData -> {
//            if (tradersViewModel != null) {
//                tradersViewModel.updateTrader(requestData, true).observe(FragmentEntityList.this, responseModel -> {
//                    if (tradersController != null) {
//                        tradersController.stopAnim();
//                        if (responseModel != null) {
//                            tradersController.snack(responseModel.getResultDescription());
//                        }
//                        if (responseModel != null) {
//                            if (responseModel.getResultCode() != 0) {
//                                tradersController.dismissDialog();
//                            }
//                        }
//                    }
//
//                });
//            }
//        });
    }

    private void update(TraderModel traderModel) {

        listAdapter.notifyDataSetChanged();

        if (tradersController != null) {
            tradersController.startAnim();
        }

        if (tradersViewModel != null) {
            try {
                tradersViewModel.updateTrader(new JSONObject(gson.toJson(traderModel)), true).observe(this, responseModel -> {
                    if (tradersController != null) {
                        tradersController.stopAnim();
                        if (responseModel != null) {
                            tradersController.snack(responseModel.getResultDescription());
                        }
                        if (responseModel != null) {
                            if (responseModel.getResultCode() != 0) {
                                tradersController.dismissDialog();
                                tradersViewModel.refresh(getSearchObject(), true);
                            }
                        }
                    }

                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


}
