package com.dev.lishaboramobile.Views.Trader;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.lishaboramobile.Admin.Callbacks.SearchViewCallbacks;
import com.dev.lishaboramobile.Farmer.Models.FamerModel;
import com.dev.lishaboramobile.Global.Models.FetchTraderModel;
import com.dev.lishaboramobile.Global.Utils.NetworkUtils;
import com.dev.lishaboramobile.Global.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FragementFarmersList extends Fragment {
    FarmersAdapter listAdapter;
    Gson gson = new Gson();
    FarmerController farmersController;
    List<Object> objects;
    LinkedList<FamerModel> famerModels;
    LinkedList<FamerModel> filteredFamerModels;
    boolean isArchived = false;
    boolean isDummy = false;
    FarmerViewModel famersViewModel;
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
    private ChipGroup chipGroup;
    private Chip activeChip, allChip, deletedChip, archivedChip, syncedChip, dummyChip, unsyncedChip;
    private AVLoadingIndicatorView avi;
    private boolean isConnected;
    private String filterText = "";
    private int FarmerDel, FarmerDummy, TraderSynched, FarmerArchive, All;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_farmers_list, container, false);

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

    void getFarmers(boolean isOnline) {
        if (avi != null) {
            startAnim();
        }
        if (famersViewModel != null) {


            famersViewModel.getFarmerModels(getSearchObject(), isOnline).observe(this, responseModel -> {

                if (avi != null) {
                    stopAnim();
                }


                if (responseModel != null) {

                    famerModels.clear();
                    famerModels.addAll(responseModel);
                    filterFarmers();
                    populateTraders();
                } else {
                    //famerModels.clear();
                    populateTraders();
                }


            });


        } else {
            snack("FamersViewModel is null");
        }
    }

    private void initTrader() {

        if (NetworkUtils.Companion.isConnectionFast(Objects.requireNonNull(getActivity()))) {
            listAdapter = new FarmersAdapter(getActivity(), filteredFamerModels, new OnclickRecyclerListener() {
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
                    popupMenu(adapterPosition, view, filteredFamerModels.get(adapterPosition));

                }
            });

            getFarmers(true);

        } else {
            emptyState(false, "You are not connected to the internet ...", empty_layout, null, emptyTxt);
        }


//        swipe_refresh_layout.setOnRefreshListener(() -> {
//            startAnim();
//            famersViewModel.refreshAll(getSearchObject(), true);
//
//        });


        ((TraderActivity) getActivity()).fabButton(true, R.drawable.ic_add_black_24dp, () -> {
            if (isConnected) {

                if (farmersController != null) {

                }

            } else {
                snack("You have to be connected to the internet");
            }
        });
        ((TraderActivity) getActivity()).searchAble(true, "Search farmers", new SearchViewCallbacks() {
            @Override
            public void onQueryTextChange(String search) {
                // snack(search);
                filterText = search;
                filterFarmers();

            }

            @Override
            public void onQueryTextSubmit(String search) {
                // snack(search);
                filterText = search;
                filterFarmers();
            }
        });


    }

    private JSONObject getSearchObject() {

        FetchTraderModel f = new FetchTraderModel();


        f.setArchived(FarmerArchive);
        f.setDeleted(FarmerDel);
        f.setAll(All);
        f.setDummy(FarmerDummy);
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

        if (filteredFamerModels.size() < 1) {
            snack("No data");
        }

        listAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(listAdapter);
        emptyState(listAdapter.getItemCount() > 0, "We couldn't find any farmers records", empty_layout, null, emptyTxt);

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


    private void filterFarmers() {

        filteredFamerModels.clear();
        if (famerModels != null && famerModels.size() > 0) {
            for (FamerModel famerModel : famerModels) {
                if (famerModel.getCode().toLowerCase().contains(filterText) ||
                        famerModel.getMobile().toLowerCase().contains(filterText) ||
                        famerModel.getNames().toLowerCase().contains(filterText)) {
//
//                       if(traderModel.getDeleted()==FarmerDel&&traderModel.getArchived()==FarmerArchive&&traderModel.getDummy()==FarmerDummy) {
                    filteredFamerModels.add(famerModel);
//                       }
                }
//            }
            }
            listAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        famerModels = new LinkedList<>();
        farmersController = new FarmerController(getContext());
        filteredFamerModels = new LinkedList<>();

        try {
            Objects.requireNonNull(getActivity()).setTitle("Farmers");
        } catch (Exception nm) {
            nm.printStackTrace();
        }


        famersViewModel = ViewModelProviders.of(this).get(FarmerViewModel.class);
        initTrader();


    }

    @Override
    public void onResume() {
        super.onResume();
        if (farmersController == null) {
            farmersController = new FarmerController(context);

        }
        if (famerModels == null) {
            famerModels = new LinkedList<>();
        }
        if (filteredFamerModels == null) {
            filteredFamerModels = new LinkedList<>();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        farmersController = null;

    }

    @Override
    public void onStop() {
        super.onStop();
        farmersController = null;
        ((TraderActivity) Objects.requireNonNull(getActivity())).fabButton(false, R.drawable.ic_add_black_24dp, () -> {

        });


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

    private void popupMenu(int pos, View view, FamerModel famerModel) {
        PopupMenu popupMenu = new PopupMenu(Objects.requireNonNull(getContext()), view);
        popupMenu.inflate(R.menu.entity_list_menu);

        if (famerModel.getDeleted() == 1) {
            popupMenu.getMenu().getItem(0).setVisible(false);
        }
        if (famerModel.getArchived() == 1) {
            isArchived = true;
            popupMenu.getMenu().getItem(1).setTitle("Un-Archive");
        }
        if (famerModel.getDummy() == 1) {
            isDummy = true;
            popupMenu.getMenu().getItem(2).setTitle("Remove from dummy");
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.delete:


                    famerModel.setStatus("Deleted");
                    famerModel.setDeleted(1);
                    update(famerModel);


                    break;

                case R.id.archive:

                    if (isArchived) {
                        famerModel.setStatus("Active");
                        famerModel.setArchived(0);
                    } else {
                        famerModel.setStatus("Archived");
                        famerModel.setArchived(1);
                    }
                    update(famerModel);

                    break;

                case R.id.dummy:

                    if (isDummy) {
                        famerModel.setStatus("Active");
                        famerModel.setDummy(0);
                    } else {
                        famerModel.setStatus("Dummy");
                        famerModel.setDummy(1);
                    }

                    update(famerModel);
                    break;
                case R.id.edit:

                    editTrader(famerModel);
                    break;
                default:
            }
            return false;
        });
        popupMenu.show();
    }

    private void editTrader(FamerModel famerModel) {

//        farmersController.editTrader(traderModel, requestData -> {
//            if (famersViewModel != null) {
//                famersViewModel.updateTrader(requestData, true).observe(FragmentEntityList.this, responseModel -> {
//                    if (farmersController != null) {
//                        farmersController.stopAnim();
//                        if (responseModel != null) {
//                            farmersController.snack(responseModel.getResultDescription());
//                        }
//                        if (responseModel != null) {
//                            if (responseModel.getResultCode() != 0) {
//                                farmersController.dismissDialog();
//                            }
//                        }
//                    }
//
//                });
//            }
//        });
    }

    private void update(FamerModel famerModel) {

//        listAdapter.notifyDataSetChanged();
//
//        if (farmersController != null) {
//            farmersController.startAnim();
//        }
//
//        if (famersViewModel != null) {
//            try {
//                famersViewModel.updateTrader(new JSONObject(gson.toJson(traderModel)), true).observe(this, responseModel -> {
//                    if (farmersController != null) {
//                        farmersController.stopAnim();
//                        if (responseModel != null) {
//                            farmersController.snack(responseModel.getResultDescription());
//                        }
//                        if (responseModel != null) {
//                            if (responseModel.getResultCode() != 0) {
//                                farmersController.dismissDialog();
//                                famersViewModel.refresh(getSearchObject(), true);
//                            }
//                        }
//                    }
//
//                });
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }


    }

}
