package com.dev.lishabora.Views.Trader.Fragments;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.lishabora.Adapters.ProductsAdapter;
import com.dev.lishabora.Models.ProductsModel;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class TraderProductsnfoFragment extends Fragment implements BlockingStep {
    LinkedList<ProductsModel> filteredProductsModel;
    Gson gson = new Gson();
    ProductsAdapter listAdapter;
    ProductsAdapter listAdapterAll = null;
    private View view;
    private TraderViewModel tViewModel;
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

    public void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {

            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trader_routes, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        this.view = view;

        tViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);

        recyclerView = view.findViewById(R.id.recyclerView);
        empty_layout = view.findViewById(R.id.empty_layout);
        emptyTxt = view.findViewById(R.id.empty_text);
        avi = view.findViewById(R.id.avi);
        txt_network_state = view.findViewById(R.id.txt_network_state);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(view1 -> {
            // subscribeProduct();
            launchDialog = true;
            if (productsModelAll == null || productsModelAll.size() < 1) {
                getAllProducts();

            } else {

                subscribeProduct(sort(productsModel, productsModelAll));
            }
        });
        hideKeyboardFrom(Objects.requireNonNull(getContext()), getView());


    }

    private void initData() {
        hideKeyboardFrom(Objects.requireNonNull(getContext()), getView());

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
            public void onSwipe(int adapterPosition, int direction) {

            }

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

        callback.complete();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

        callback.goToPrevStep();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {
        initData();
        if (productsModel == null) {
            productsModel = new LinkedList<>();
        }
        if (filteredProductsModel == null) {
            filteredProductsModel = new LinkedList<>();
        }


        initList();
        populateList();
        getProducts();


    }


    @Override
    public void onError(@NonNull VerificationError error) {


    }

    private void getProducts() {
        avi.smoothToShow();
        avi.setVisibility(View.VISIBLE);


        tViewModel.getProducts(false).observe(TraderProductsnfoFragment.this, productsModels -> {
            avi.smoothToHide();
            update(productsModel);


        });

    }


    public void initList() {
        recyclerView = view.findViewById(R.id.recyclerView);
        listAdapter = new ProductsAdapter(getActivity(), filteredProductsModel, new OnclickRecyclerListener() {
            @Override
            public void onSwipe(int adapterPosition, int direction) {

            }

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

        if (listAdapter != null) {
            Log.d("ReTr", "update started" + productsModel.size());

            this.productsModel.clear();
            this.productsModel.addAll(productsModel);
            filterProducts();


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


        avi.smoothToShow();

        tViewModel.getProductsModels(getSearchObject(), true).observe(this, responseModel -> {

            Gson gson = new Gson();

            avi.smoothToHide();


            if (responseModel.getResultCode() == 1 && responseModel.getData() != null) {
                JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                Type listType = new TypeToken<LinkedList<ProductsModel>>() {
                }.getType();
                subscribeProduct(gson.fromJson(jsonArray, listType));


            }

        });


    }

    private class CustomListener implements View.OnClickListener {
        AlertDialog dialog;

        private List<ProductsModel> selectedProducts;

        public CustomListener(AlertDialog alertDialogAndroid, List<ProductsModel> selectedProducts) {
            dialog = alertDialogAndroid;
            this.selectedProducts = selectedProducts;

        }

        @Override
        public void onClick(View v) {


            if (selectedProducts != null && selectedProducts.size() > 0) {
                Log.d("createproducts", " products" + selectedProducts.size() + selectedProducts.get(0).getNames());

                tViewModel.createProducts(selectedProducts, false).observe(TraderProductsnfoFragment.this, responseModel -> {
                    avi.smoothToHide();
                    dialog.dismiss();
                    if (responseModel != null) {
                        Snackbar.make(view, responseModel.getResultDescription(), Snackbar.LENGTH_LONG).show();
                    }
                });

            } else {

                // dialog.dismiss();
            }


        }

    }


}
