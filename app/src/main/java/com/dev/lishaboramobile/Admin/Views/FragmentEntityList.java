package com.dev.lishaboramobile.Admin.Views;

import android.app.ProgressDialog;
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

import com.dev.lishaboramobile.Admin.Adapters.TradersAdapter;
import com.dev.lishaboramobile.Admin.Callbacks.CreateTraderCallbacks;
import com.dev.lishaboramobile.Admin.Callbacks.FabCallbacks;
import com.dev.lishaboramobile.Admin.Callbacks.SearchViewCallbacks;
import com.dev.lishaboramobile.Admin.Callbacks.TraderCallbacks;
import com.dev.lishaboramobile.Admin.Controllers.TradersController;
import com.dev.lishaboramobile.Global.AppConstants;
import com.dev.lishaboramobile.Global.Models.ResponseModel;
import com.dev.lishaboramobile.Global.Utils.NetworkUtils;
import com.dev.lishaboramobile.Global.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Trader.Models.TraderModel;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FragmentEntityList extends Fragment {
    TradersAdapter listAdapter;
    TradersController tradersController;
    List<Object> objects;
    LinkedList<TraderModel> traderModels;
    LinkedList<TraderModel> filteredTraderModels;
    boolean isArchived = false;
    private View view;
    private Context context;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout linearLayoutEmpty;
    private int entity;
    boolean isDummy = false;
    private ProgressDialog progressDialog;
    private ActionMode mActionMode;
    private SwipeRefreshLayout swipe_refresh_layout;
    private LinearLayout empty_layout;
    private TextView emptyTxt, txt_network_state;
    private ChipGroup chipGroup;
    private Chip activeChip, allChip, deletedChip, archivedChip, syncedChip, dummyChip, unsyncedChip;
    private AVLoadingIndicatorView avi;
    private int TraderDel, TraderDummy, TraderSynched, TraderArchive;
    private boolean isConnected;
    private String filterText = "";
    private ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater menuInflater = mode.getMenuInflater();
            menuInflater.inflate(R.menu.traders_list_menu, menu);
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

        // View view=R.layout.fragment_entities_list;
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


        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {

            }
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

    private void initTrader() {
        swipe_refresh_layout.setOnRefreshListener(() -> {

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
                        if (filteredTraderModels.get(adapterPosition).getDeleted() == 0) {
                            popupMenu(adapterPosition, view, filteredTraderModels.get(adapterPosition));
                        }
                        {
                            snack("Account is deleted .....");
                        }
                    }
                });
                swipe_refresh_layout.setRefreshing(false);

                getTraders();


            } else {
                emptyState(false, "You are not connected to the internet ...", empty_layout, null, emptyTxt);
            }


        });

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
                    if (filteredTraderModels.get(adapterPosition).getDeleted() == 0) {
                        popupMenu(adapterPosition, view, filteredTraderModels.get(adapterPosition));
                    }
                    {
                        snack("Account is deleted .....");
                    }
                }
            });

            getTraders();


        } else {
            emptyState(false, "You are not connected to the internet ...", empty_layout, null, emptyTxt);
        }


        ((AdminActivity) getActivity()).fabButton(true, R.drawable.ic_add_black_24dp, new FabCallbacks() {
            @Override
            public void onClick() {
                if (isConnected) {

                    tradersController.createTrader(new CreateTraderCallbacks() {
                        @Override
                        public void success(ResponseModel responseModel) {
                            getTraders();
                        }

                        @Override
                        public void error(String response) {

                        }

                        @Override
                        public void startProgressDialog() {

                        }

                        @Override
                        public void stopProgressDialog() {

                        }

                        @Override
                        public void updateProgressDialog(String message) {

                        }
                    });
                } else {
                    snack("You have to be connected to the internet");
                }
            }
        });
        ((AdminActivity) getActivity()).searchAble(true, "Search traders", new SearchViewCallbacks() {
            @Override
            public void onQueryTextChange(String search) {
                // snack(search);
                filterText = search;
                filterTraders();

            }

            @Override
            public void onQueryTextSubmit(String search) {
                // snack(search);
                filterText = search;
                filterTraders();
            }
        });

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {


            }
        });


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

    private LinkedList<TraderModel> getTraders() {

        Gson gson = new Gson();
        if (tradersController != null) {
            tradersController.getTraderModels(0, 0, 0, 0, new TraderCallbacks() {
                @Override
                public void success(ResponseModel responseModel) {
                    if (avi != null) {
                        stopAnim();
                    }
                    JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();


                    //Log.d("sdf",jsonArray.getAsString());
                    Type listType = new TypeToken<LinkedList<TraderModel>>() {
                    }.getType();
                    traderModels = gson.fromJson(jsonArray, listType);
                    filterTraders();
                    populateTraders();


                    //snack("success");
                }

                @Override
                public void error(String response) {

                    if (avi != null) {
                        stopAnim();
                    }
                    filterTraders();
                    snack(response);
                    populateTraders();
                }

                @Override
                public void startProgressDialog() {

                    if (avi != null) {
                        startAnim();
                    }
                }

                @Override
                public void stopProgressDialog() {
                    if (avi != null) {
                        stopAnim();
                    }
                }

                @Override
                public void updateProgressDialog(String message) {

                    //snack(message);
                }
            });
        } else {

        }
        //populateTraders();
        return traderModels;
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

        getActivity().setTitle("Traders");

        if (getArguments() != null) {
            entity = getArguments().getInt("Entity");
        }

        switch (entity) {
            case AppConstants.TRADER:
                initTrader();

                break;

            default:
                initTrader();
        }

        //Toast.makeText(getActivity(), "Started", Toast.LENGTH_SHORT).show();
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
        tradersController = null;

    }

    @Override
    public void onStop() {
        super.onStop();
        tradersController = null;
    }

    private void snack(String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();

        Log.d("SnackMessage", msg);
    }

    private void createDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(true);
    }

    void startAnim() {
        avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {
        avi.hide();
        // or avi.smoothToHide();
    }

    private void popupMenu(int pos, View view, TraderModel traderModel) {
        PopupMenu popupMenu = new PopupMenu(Objects.requireNonNull(getContext()), view);
        popupMenu.inflate(R.menu.traders_list_menu);

//        popupMenu.getMenu().getItem(0).setVisible(false);
//        popupMenu.getMenu().getItem(1).setVisible(false);
//        popupMenu.getMenu().getItem(2).setVisible(false);

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

        tradersController.editTrader(traderModel, new CreateTraderCallbacks() {
            @Override
            public void success(ResponseModel responseModel) {
                getTraders();
            }

            @Override
            public void error(String response) {

            }

            @Override
            public void startProgressDialog() {

            }

            @Override
            public void stopProgressDialog() {

            }

            @Override
            public void updateProgressDialog(String message) {

            }
        });
    }

    private void update(TraderModel traderModel) {

        listAdapter.notifyDataSetChanged();
        startAnim();
        tradersController.upTrader(traderModel, new TraderCallbacks() {
            @Override
            public void success(ResponseModel responseModel) {
                stopAnim();
                snack(responseModel.getResultDescription());
                if (responseModel.getResultCode() == 1) {
                    populateTraders();
                }
            }

            @Override
            public void error(String response) {

                stopAnim();
                snack(response);

            }

            @Override
            public void startProgressDialog() {

                startAnim();
            }

            @Override
            public void stopProgressDialog() {

                stopAnim();
            }

            @Override
            public void updateProgressDialog(String message) {

            }
        });
    }


}
