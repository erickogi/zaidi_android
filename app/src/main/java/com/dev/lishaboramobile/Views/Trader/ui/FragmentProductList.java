package com.dev.lishaboramobile.Views.Trader.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishaboramobile.Global.Models.ResponseModel;
import com.dev.lishaboramobile.Global.Utils.DateTimeUtils;
import com.dev.lishaboramobile.Global.Utils.MyToast;
import com.dev.lishaboramobile.Global.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.Trader.Models.ProductSubscriptionModel;
import com.dev.lishaboramobile.Trader.Models.RPFSearchModel;
import com.dev.lishaboramobile.Trader.Models.TraderModel;
import com.dev.lishaboramobile.admin.adapters.ProductsAdapter;
import com.dev.lishaboramobile.admin.models.ProductsModel;
import com.dev.lishaboramobile.admin.ui.admins.AdminsViewModel;
import com.dev.lishaboramobile.login.PrefrenceManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class FragmentProductList extends Fragment {
    LinkedList<ProductsModel> AllproductsModel;
    LinkedList<ProductsModel> filteredProductsModel;
    Gson gson = new Gson();
    ProductsAdapter listAdapter;
    ProductsAdapter listAdapterAll = null;
    private View view;
    private AdminsViewModel mViewModel;
    private LinkedList<ProductsModel> productsModel;
    private LinkedList<ProductsModel> productsModelAll;
    private AVLoadingIndicatorView avi;
    private AVLoadingIndicatorView davi;
    private String filterText = "";
    private FloatingActionButton fab;
    private SearchView searchView;
    private LinearLayout empty_layout;
    private TextView emptyTxt, txt_network_state;
    private boolean launchDialog;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout linearLayoutEmpty;

    //    private void populateList() {
//        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(mStaggeredLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//
//
//        listAdapter.notifyDataSetChanged();
//        recyclerView.setAdapter(listAdapter);
//
//    }
    @Override
    public void onStart() {
        super.onStart();
        if (productsModel == null) {
            productsModel = new LinkedList<>();
        }
        if (filteredProductsModel == null) {
            filteredProductsModel = new LinkedList<>();
        }

        initList();
        populateList();
        getProducts();


        try {
            Objects.requireNonNull(getActivity()).setTitle("Products");
        } catch (Exception nm) {
            nm.printStackTrace();
        }

    }

    private void getProducts() {
        avi.smoothToShow();
        avi.setVisibility(View.VISIBLE);

        if (mViewModel == null) {
            mViewModel = ViewModelProviders.of(this).get(AdminsViewModel.class);

        }
        mViewModel.getTraderProductsModels(getTraderProductsObject(), true).observe(FragmentProductList.this, new Observer<ResponseModel>() {
            @Override
            public void onChanged(@Nullable ResponseModel responseModel) {
                avi.smoothToHide();
                //snack(responseModel.getResultDescription());
                JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                Type listType = new TypeToken<LinkedList<ProductsModel>>() {
                }.getType();
                // productsModel = ;
                Log.d("ReTrUp", "routes update called");
                update(gson.fromJson(jsonArray, listType));


            }
        });
    }

    private JSONObject getTraderProductsObject() {


        RPFSearchModel rpfSearchModel = new RPFSearchModel();
        rpfSearchModel.setEntitycode(new PrefrenceManager(getActivity()).getTraderModel().getCode());
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(rpfSearchModel));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trader_routes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view
            , @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        mViewModel = ViewModelProviders.of(this).get(AdminsViewModel.class);


        recyclerView = view.findViewById(R.id.recyclerView);
        empty_layout = view.findViewById(R.id.empty_layout);
        emptyTxt = view.findViewById(R.id.empty_text);
        avi = view.findViewById(R.id.avi);
        txt_network_state = view.findViewById(R.id.txt_network_state);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // subscribeProduct();
                launchDialog = true;
                if (productsModelAll == null || productsModelAll.size() < 1) {
                    getAllProducts();

                } else {

                    subscribeProduct(sort(productsModel, productsModelAll));
                }
            }
        });


    }

    LinkedList<ProductsModel> sort(LinkedList<ProductsModel> a, LinkedList<ProductsModel> b) {
        LinkedList<ProductsModel> sortedList = new LinkedList<>();

        sortedList.addAll(a);
        sortedList.addAll(b);


        HashMap<String, ProductsModel> myImageHashMap = new HashMap<>();

        for (ProductsModel image : sortedList) {
            String path = image.getNames() + "" + String.valueOf(image.getId());
            myImageHashMap.put(path, image);
        }

        LinkedList<ProductsModel> myImages = new LinkedList<>();

        for (String key : myImageHashMap.keySet()) {

            ProductsModel myImage = myImageHashMap.get(key);
            myImages.add(myImage);

        }
        return myImages;
    }

    private void subscribeProduct(LinkedList<ProductsModel> productsModels) {

        Log.d("ReTrReqd", " Dialog is has called");
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_all_products_list, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
        alertDialogBuilderUserInput.setTitle("Products");


        davi = mView.findViewById(R.id.avi);


        RecyclerView recyclerView = mView.findViewById(R.id.recyclerView);

        StaggeredGridLayoutManager mStaggeredLayoutManager;
        LinkedList<ProductsModel> selected = new LinkedList<>();


        listAdapterAll = new ProductsAdapter(getActivity(), productsModels, new OnclickRecyclerListener() {
            @Override
            public void onClickListener(int position) {

                if (productsModels.get(position).isSelected()) {
                    productsModels.get(position).setSelected(false);
                    selected.remove(productsModels.get(position));
                    listAdapterAll.notifyItemChanged(position, productsModels.get(position));

                } else {
                    selected.add(productsModels.get(position));
                    productsModels.get(position).setSelected(true);
                    listAdapterAll.notifyItemChanged(position, productsModels.get(position));

                }
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

            }
        }, true);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        listAdapterAll.notifyDataSetChanged();
        recyclerView.setAdapter(listAdapterAll);


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Done", (dialogBox, id) -> {
                    // ToDo get user input here


                })

                .setNegativeButton("Dismiss",
                        (dialogBox, id) -> dialogBox.cancel());

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.show();
        launchDialog = false;

        Button theButton = alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CustomListener(alertDialogAndroid, selected));


    }

    private void populateList() {
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        listAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(listAdapter);
        //emptyState(listAdapter.getItemCount() > 0, "We couldn't find any farmers records", empty_layout, null, emptyTxt);

    }

    private void emptyState(boolean listHasData, String text, LinearLayout empty_layout, ImageView empty_image, TextView emptyTxt) {
        if (listHasData) {
            //  empty_layout.setVisibility(View.GONE);
        } else {
            //  empty_layout.setVisibility(View.VISIBLE);
            if (text != null) {
                //     emptyTxt.setText(text);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filterText = s;
                filterProducts();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterText = s;
                filterProducts();

                return true;
            }
        });

    }

    public void initList() {
        recyclerView = view.findViewById(R.id.recyclerView);
        listAdapter = new ProductsAdapter(getActivity(), filteredProductsModel, new OnclickRecyclerListener() {
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

            }
        });


    }

    public void update(List<ProductsModel> productsModel) {

        Log.d("ReTr", "update started");

        if (this.productsModel != null && listAdapter != null) {
            Log.d("ReTr", "update started");

            this.productsModel.clear();
            this.productsModel.addAll(productsModel);
            filterProducts();
            //listAdapter.notifyDataSetChanged();


        }
    }

    private void filterProducts() {
        filteredProductsModel.clear();
        if (productsModel != null && productsModel.size() > 0) {
            for (ProductsModel productsModel : productsModel) {
                if (productsModel.getNames().toLowerCase().contains(filterText) ||
                        productsModel.getCostprice().toLowerCase().contains(filterText) ||
                        productsModel.getSellingprice().toLowerCase().contains(filterText)) {
                    filteredProductsModel.add(productsModel);
                }

            }
            listAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);

        //inflater.inflate(R.menu.menu_main, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        searchView = (SearchView) mSearch.getActionView();

        searchView.setVisibility(View.GONE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Not implemented here
                return false;
            case R.id.action_search:
                // Do Fragment menu item stuff here
                return true;
            default:
                break;
        }

        return false;
    }

    private JSONObject getSearchObject() {
        Gson gson = new Gson();

        ProductsModel f = new ProductsModel();

        try {
            return new JSONObject(gson.toJson(f));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;


    }

    private void getAllProducts() {


        if (mViewModel != null) {

            avi.smoothToShow();

            mViewModel.getProductsModels(getSearchObject(), true).observe(this, responseModel -> {

                Gson gson = new Gson();

                avi.smoothToHide();


                if (responseModel.getResultCode() == 1 && responseModel.getData() != null) {
                    JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                    Type listType = new TypeToken<LinkedList<ProductsModel>>() {
                    }.getType();
                    productsModelAll = gson.fromJson(jsonArray, listType);
                    if (productsModel == null || productsModel.size() > 0) {
                        Log.d("ReTrReqd", " Dialog is has init");

                        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
                        if (launchDialog) {
                            subscribeProduct(productsModelAll);
                        } else {
                            getProducts();
                        }
                    } else {
                        if (launchDialog) {
                            Log.d("ReTrReqd", " Dialog is has init2");
                            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
                            subscribeProduct(sort(productsModel, productsModelAll));
                        } else {
                            getProducts();
                        }
                    }

                } else if (responseModel.getResultCode() == 2) {
                    productsModelAll.clear();

                } else {


                }

            });


        }
    }

    private class CustomListener implements View.OnClickListener {
        AlertDialog dialog;
        boolean isActive = true;
        int isAct = 1;
        private LinkedList<ProductsModel> selectedProducts = new LinkedList<>();

        public CustomListener(AlertDialog alertDialogAndroid, LinkedList<ProductsModel> selectedProducts) {
            dialog = alertDialogAndroid;
            this.selectedProducts = selectedProducts;

        }

        @Override
        public void onClick(View v) {


            if (selectedProducts != null && selectedProducts.size() > 0) {

                LinkedList<ProductSubscriptionModel> productSubscriptionModels = new LinkedList<>();
                PrefrenceManager prefrenceManager = new PrefrenceManager(getActivity());
                TraderModel traderModel = prefrenceManager.getTraderModel();
                for (ProductsModel s : selectedProducts) {
                    ProductSubscriptionModel productSubscriptionModel = new ProductSubscriptionModel();
                    productSubscriptionModel.setEntitycode(traderModel.getCode());
                    productSubscriptionModel.setProductcode(s.getId() + "");
                    productSubscriptionModel.setStatus("1");
                    productSubscriptionModel.setSubscribed(1);
                    productSubscriptionModel.setSynctime("");
                    productSubscriptionModel.setTransactontime(DateTimeUtils.Companion.getNow());
                    productSubscriptionModel.setTransactedby("" + traderModel.getApikey());

                    productSubscriptionModels.add(productSubscriptionModel);

                }
                davi.smoothToShow();
                JSONArray jsonObject = null;
                try {
                    String element = gson.toJson(
                            productSubscriptionModels,
                            new TypeToken<ArrayList<ProductSubscriptionModel>>() {
                            }.getType());

                    jsonObject = new JSONArray(element);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mViewModel.subscribeProducts(jsonObject, true).observe(FragmentProductList.this, responseModel -> {
                    davi.smoothToHide();
                    //  snack(responseModel.getResultDescription());
                    if (responseModel.getResultCode() == 1) {
                        dialog.dismiss();
                        mViewModel.refreshProducts(getTraderProductsObject(), true);
                    } else {
                        MyToast.toast(responseModel.getResultDescription(), getActivity(), R.drawable.ic_launcher, Toast.LENGTH_LONG);
                    }
                });


            } else {
                Log.d("ReTrReq", " Dialog is has closed nu");

                // dialog.dismiss();
            }


        }

    }


}
