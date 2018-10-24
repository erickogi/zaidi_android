package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishabora.Adapters.ProductsAdapter;
import com.dev.lishabora.Models.ProductsModel;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

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
    private TraderViewModel mViewModel;
    private LinkedList<ProductsModel> productsModel;
    private List<ProductsModel> productsModelAll;
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
        mViewModel.getProductsModels(1).observe(FragmentProductList.this, productsModels -> {
            avi.smoothToHide();
            update(productsModels);


        });

    }

    private void emptyState(boolean listHasData) {
        LinearLayout empty_layout;
        empty_layout = view.findViewById(R.id.empty_layout);

        if (listHasData) {
            empty_layout.setVisibility(View.GONE);
        } else {
            empty_layout.setVisibility(View.VISIBLE);

        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trader_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view
            , @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        mViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);


        recyclerView = view.findViewById(R.id.recyclerView);
        empty_layout = view.findViewById(R.id.empty_layout);
        emptyTxt = view.findViewById(R.id.empty_text);
        avi = view.findViewById(R.id.avi);
        txt_network_state = view.findViewById(R.id.txt_network_state);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(view1 -> {
            // subscribeProduct();
            launchDialog = true;
//            if (productsModelAll == null || productsModelAll.size() < 1) {
            getAllProducts();
//
//                subscribeProduct(sort(productsModel, productsModelAll));
//
//            } else {

                subscribeProduct(sort(productsModel, productsModelAll));
            //   }
        });


    }

    LinkedList<ProductsModel> sort(LinkedList<ProductsModel> a, List<ProductsModel> b) {
        LinkedList<ProductsModel> sortedList = new LinkedList<>();

        try {
            sortedList.addAll(a);
        } catch (Exception nm) {

        }
        try {
            sortedList.addAll(b);
        } catch (Exception nm) {

        }


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

    private void subscribeProduct(List<ProductsModel> productsModels) {

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
            public void onMenuItem(int position, int menuItem) {

            }

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

//
//        alertDialogBuilderUserInput
//                .setCancelable(false)
//                .setPositiveButton("Done", (dialogBox, id) -> {
//                    // ToDo get user input here
//
//
//                })
//
//                .setNegativeButton("Dismiss",
//                        (dialogBox, id) -> dialogBox.cancel());
//
//        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
//        alertDialogAndroid.setCancelable(false);
//        alertDialogAndroid.show();
//        launchDialog = false;
//
//        Button theButton = alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE);
//        theButton.setOnClickListener(new CustomListener(alertDialogAndroid, selected));
        alertDialogBuilderUserInput
                .setCancelable(false);

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        alertDialogAndroid.show();


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
        btnNeutral.setText("Delete");

        btnNeutral.setBackgroundColor(this.getResources().getColor(R.color.red));
        lTitle.setVisibility(View.GONE);
        txtTitle.setVisibility(View.VISIBLE);
        imgIcon.setVisibility(View.VISIBLE);
        imgIcon.setImageResource(R.drawable.ic_add_black_24dp);
        txtTitle.setText("Route");

//        btnNeutral.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                OrderConstants.getProductOrderModels().remove(pos);
//
//                alertDialogAndroid.dismiss();
//
//                refreshList();
//            }
//        });

        btnPositive.setOnClickListener(new CustomListener(alertDialogAndroid, selected));

        btnNegative.setOnClickListener(view -> alertDialogAndroid.dismiss());


    }

    private void populateList() {
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        listAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(listAdapter);
        emptyState(listAdapter.getItemCount() > 0);
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
            public void onMenuItem(int position, int menuItem) {

            }

            @Override
            public void onSwipe(int adapterPosition, int direction) {

            }

            @Override
            public void onClickListener(int position) {


                try {
                    editProduct(productsModel.get(position));
                } catch (Exception nm) {
                    nm.printStackTrace();
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
        });


    }

    public void update(List<ProductsModel> productsModel) {


        if (this.productsModel != null && listAdapter != null) {

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
            listAdapter.refresh(filteredProductsModel);
            emptyState(listAdapter.getItemCount() > 0);


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

            mViewModel.getProductsModels(0).observe(this, (List<ProductsModel> responseModel) -> {

                Gson gson = new Gson();

                avi.smoothToHide();


                if (responseModel != null) {
                    productsModelAll = responseModel;
                    if (productsModel == null || productsModel.size() > 0) {

                        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
                        if (launchDialog) {
                            // subscribeProduct(responseModel);
                        } else {
                            getProducts();
                        }
                    } else {
                        if (launchDialog) {
                            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
                            //  subscribeProduct(sort(productsModel, productsModelAll));
                        } else {
                            getProducts();
                        }
                    }
                }



            });


        }
    }

    private void editProduct(ProductsModel model) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_edit_product, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        alertDialogBuilderUserInput.setView(mView);
//        alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
//        alertDialogBuilderUserInput.setTitle("Route");


        avi = mView.findViewById(R.id.avi);

        TextInputEditText name, buyPrice, sellingPrice;

        name = mView.findViewById(R.id.edt_product_names);
        buyPrice = mView.findViewById(R.id.edt_product_cost_price);
        sellingPrice = mView.findViewById(R.id.edt_product_selling_prices);

        name.setText(model.getNames());
        buyPrice.setText(model.getBuyingprice());
        sellingPrice.setText(model.getSellingprice());


        alertDialogBuilderUserInput
                .setCancelable(false);
//                .setPositiveButton("Save", (dialogBox, id) -> {
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
//        theButton.setOnClickListener(new EditCustomListener(alertDialogAndroid,routesModel));


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


        btnNeutral.setVisibility(View.VISIBLE);
        btnNeutral.setText("Delete");

        btnNeutral.setBackgroundColor(getContext().getResources().getColor(R.color.red));
        lTitle.setVisibility(View.GONE);
        txtTitle.setVisibility(View.VISIBLE);
        imgIcon.setVisibility(View.VISIBLE);
        imgIcon.setImageResource(R.drawable.ic_add_black_24dp);
        txtTitle.setText("Route");

        btnPositive.setOnClickListener(new EditCustomListener(alertDialogAndroid, model));
        btnNeutral.setOnClickListener(view -> {
            mViewModel.deleteProduct(model, false).observe(FragmentProductList.this, responseModel -> {
                avi.smoothToHide();
                MyToast.toast(responseModel.getResultDescription(), getContext(), R.drawable.ic_launcher, Toast.LENGTH_LONG);
                alertDialogAndroid.dismiss();
            });
        });
        btnNegative.setOnClickListener(view -> alertDialogAndroid.dismiss());


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

                for (int a = 0; a < selectedProducts.size(); a++) {
                    selectedProducts.get(a).setSubscribed("1");
                }


                mViewModel.createProducts(selectedProducts, false).observe(FragmentProductList.this, responseModel -> {

                    dialog.dismiss();

                });

            } else {

                dialog.dismiss();


            }
        }

    }

    private class EditCustomListener implements View.OnClickListener {
        AlertDialog dialog;
        boolean isActive = true;
        int isAct = 1;
        ProductsModel model;

        public EditCustomListener(AlertDialog alertDialogAndroid, ProductsModel model) {
            dialog = alertDialogAndroid;
            this.model = model;

        }

        @Override
        public void onClick(View v) {
            TextInputEditText name, buyPrice, sellingPrice;

            name = dialog.findViewById(R.id.edt_product_names);
            buyPrice = dialog.findViewById(R.id.edt_product_cost_price);
            sellingPrice = dialog.findViewById(R.id.edt_product_selling_prices);

            name = dialog.findViewById(R.id.edt_rout_names);


            if (sellingPrice.getText().toString().isEmpty()) {
                sellingPrice.setError("Required");
                sellingPrice.requestFocus();
                avi.smoothToHide();
                return;
            }

            model.setSellingprice(sellingPrice.getText().toString());


            avi.smoothToShow();

            mViewModel.updateProduct(model, false).observe(FragmentProductList.this, responseModel -> {
                avi.smoothToHide();
                dialog.dismiss();
            });


        }

    }


}
