package com.dev.lishaboramobile.admin.ui.admins;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.PopupMenu;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishaboramobile.Global.Models.ResponseModel;
import com.dev.lishaboramobile.Global.Utils.DateTimeUtils;
import com.dev.lishaboramobile.Global.Utils.MyToast;
import com.dev.lishaboramobile.Global.Utils.NetworkUtils;
import com.dev.lishaboramobile.Global.Utils.OnclickRecyclerListener;
import com.dev.lishaboramobile.Global.Utils.RequestDataCallback;
import com.dev.lishaboramobile.R;
import com.dev.lishaboramobile.admin.adapters.ProductsAdapter;
import com.dev.lishaboramobile.admin.models.ProductsModel;
import com.dev.lishaboramobile.login.PrefrenceManager;
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
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AdminProductsFragment extends Fragment {
    LinkedList<ProductsModel> productsModel;
    LinkedList<ProductsModel> filteredProductsModels;
    FloatingActionButton fab;

    private boolean isConnected;
    private View view;
    private Context context;
    private RecyclerView recyclerView;
    ProductsAdapter listAdapter;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout linearLayoutEmpty;
    private LinearLayout empty_layout;
    private TextView emptyTxt, txt_network_state;
    private String filterText = "";
    private AVLoadingIndicatorView avi;
    private AdminsViewModel mViewModel;
    private SearchView searchView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;


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

    private void filterProducts() {
        filteredProductsModels.clear();
//        if(productsModel!=null&&productsModel.size()>0){
        for (ProductsModel productsModel : productsModel) {
            if (productsModel.getNames().toLowerCase().contains(filterText)) {

                filteredProductsModels.add(productsModel);

            }

        }

        listAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admins_fragment_products, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AdminsViewModel.class);

        // TODO: Use the ViewModel
    }

    void initWIdgets() {
        recyclerView = view.findViewById(R.id.recyclerView);
        empty_layout = view.findViewById(R.id.empty_layout);
        emptyTxt = view.findViewById(R.id.empty_text);
        avi = view.findViewById(R.id.avi);
        txt_network_state = view.findViewById(R.id.txt_network_state);


        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(view -> createProduct());


    }

    @Override
    public void onStart() {
        super.onStart();
        if (context == null) {
            context = getActivity();
        }
        initWIdgets();
        productsModel = new LinkedList<>();
        filteredProductsModels = new LinkedList<>();
        initConnectivityListener();
        initProductsList();

    }

    public void createProduct() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_add_product, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(context));
        alertDialogBuilderUserInput.setView(mView);
//        alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
//        alertDialogBuilderUserInput.setTitle("Product");


        avi = mView.findViewById(R.id.avi);


        TextInputEditText edtNames, edtMobile;
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
//        theButton.setOnClickListener(new CustomListener(alertDialogAndroid, true));

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
        txtTitle.setText("Product");

        btnPositive.setOnClickListener(new CustomListener(alertDialogAndroid, true));
        btnNegative.setOnClickListener(view -> alertDialogAndroid.dismiss());




    }

    public void updateProduct(ProductsModel productsModel) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_add_product, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(context));
        alertDialogBuilderUserInput.setView(mView);
//        alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
//        alertDialogBuilderUserInput.setTitle("Product");


        avi = mView.findViewById(R.id.avi);


        TextInputEditText name, cost, selling;
        CheckBox chkDummy;
        cost = mView.findViewById(R.id.edt_product_cost_price);
        name = mView.findViewById(R.id.edt_product_names);
        selling = mView.findViewById(R.id.edt_product_selling_prices);
        chkDummy = mView.findViewById(R.id.chk_active);

        name.setText(productsModel.getNames());
        cost.setText(productsModel.getCostprice());
        selling.setText(productsModel.getSellingprice());

        if (productsModel.getStatus() == 2) {
            chkDummy.setChecked(true);
        }


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
//        theButton.setOnClickListener(new CustomListener(alertDialogAndroid, false));

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
        txtTitle.setText("Edit Product");

        btnPositive.setOnClickListener(new CustomListener(alertDialogAndroid, false));
        btnNegative.setOnClickListener(view -> alertDialogAndroid.dismiss());



    }


    void initConnectivityListener() {
        ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnectedToInternet -> {
                    // do something with isConnectedToInternet value
                    isConnected = isConnectedToInternet;

                    if (isConnectedToInternet) {
                        try {
                            fab.setImageResource(android.R.drawable.ic_input_add);

                            //txt_network_state.setText("Connected to internet");
                            //txt_network_state.setTextColor(getActivity().getResources().getColor(R.color.green_color_picker));
                        } catch (Exception nm) {
                            nm.printStackTrace();
                        }

                    } else {

                        try {
                            fab.setImageResource(R.drawable.ic_add_red_24dp);

                            // txt_network_state.setText("Not connected to internet");
                            // txt_network_state.setTextColor(getActivity().getResources().getColor(R.color.red));
                        } catch (Exception vb) {
                            vb.printStackTrace();
                        }

                    }
                });
    }

    void initProductsList() {
        if (NetworkUtils.Companion.isConnectionFast(Objects.requireNonNull(getActivity()))) {
            listAdapter = new ProductsAdapter(getActivity(), filteredProductsModels, new OnclickRecyclerListener() {
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
                    popupMenu(adapterPosition, view, filteredProductsModels.get(adapterPosition));

                }
            });

            getProducts();

        } else {
            emptyState(false, "You are not connected to the internet ...", empty_layout, null, emptyTxt);
        }

    }

    private void popupMenu(int pos, View view, ProductsModel productsModel) {
        PopupMenu popupMenu = new PopupMenu(Objects.requireNonNull(getContext()), view);
        popupMenu.inflate(R.menu.product_list_menu);


        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.delete:


                    productsModel.setStatus(0);
                    updateProductstatus(productsModel);

                    break;

                case R.id.archive:

                    productsModel.setStatus(2);
                    updateProductstatus(productsModel);

                    break;


                case R.id.edit:

                    updateProduct(productsModel);
                    break;
                default:
            }
            return false;
        });
        popupMenu.show();


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

    private void getProducts() {

        if (avi != null) {
            startAnim();
        }
        if (mViewModel != null) {


            mViewModel.getProductsModels(getSearchObject(), true).observe(this, responseModel -> {

                Gson gson = new Gson();
                if (avi != null) {
                    stopAnim();
                }


                if (responseModel.getResultCode() == 1 && responseModel.getData() != null) {
                    JsonArray jsonArray = gson.toJsonTree(responseModel.getData()).getAsJsonArray();
                    Type listType = new TypeToken<LinkedList<ProductsModel>>() {
                    }.getType();
                    productsModel = gson.fromJson(jsonArray, listType);
                    filterProducts();
                    populateProducts();
                } else if (responseModel.getResultCode() == 2) {
                    productsModel.clear();
                    populateProducts();
                } else {


                    snack(responseModel.getResultDescription());
                }

            });


        }
    }

    private void populateProducts() {
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (filteredProductsModels.size() < 1) {
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

    private void snack(String msg) {
        if (view != null) {
            Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();

        }
        if (context != null) {
            MyToast.toast(msg, context, R.drawable.ic_launcher, Toast.LENGTH_LONG);
        }
        Log.d("SnackMessage", msg);
    }

    void startAnim() {
        avi.show();

    }

    void stopAnim() {
        avi.hide();

    }

    private void updateProductstatus(ProductsModel productsModel) {
        startAnim();
        Gson gson = new Gson();

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(productsModel));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mViewModel.updateProduct(jsonObject, true).observe(AdminProductsFragment.this, responseModel -> {
            stopAnim();
            snack(responseModel.getResultDescription());
            if (responseModel.getResultCode() == 1) {
                mViewModel.refreshProducts(getSearchObject(), true);
            }
        });
    }

    private class CustomListener implements View.OnClickListener {
        AlertDialog dialog;
        boolean isDummy = false;
        //int type;
        RequestDataCallback requestDataCallback;
        private boolean isNew;

        public CustomListener(AlertDialog alertDialogAndroid, boolean isNew) {
            dialog = alertDialogAndroid;
            this.isNew = isNew;

        }

        @Override
        public void onClick(View v) {
            TextInputEditText name, cost, selling;
            CheckBox chkDummy;
            cost = dialog.findViewById(R.id.edt_product_cost_price);
            name = dialog.findViewById(R.id.edt_product_names);
            selling = dialog.findViewById(R.id.edt_product_selling_prices);
            chkDummy = dialog.findViewById(R.id.chk_active);


            if (name.getText().toString().isEmpty()) {
                name.setError("Required");
                name.requestFocus();
                stopAnim();
                return;
            }


            if (selling.getText().toString().isEmpty()) {
                selling.setError("Required");
                selling.requestFocus();
                stopAnim();
                return;
            }
            if (cost.getText().toString().isEmpty()) {
                cost.setError("Required");
                cost.requestFocus();
                stopAnim();
                return;
            }


            int status = 0;
            if (chkDummy.isChecked()) {
                isDummy = true;
                status = 1;
            }


            ProductsModel productsModel = new ProductsModel();
            productsModel.setNames(name.getText().toString());
            productsModel.setCostprice(cost.getText().toString());
            productsModel.setSellingprice(selling.getText().toString());
            productsModel.setStatus(status);
            productsModel.setTransactedby(new PrefrenceManager(context).getAdmin().getApikey());
            productsModel.setTransactiontime(DateTimeUtils.Companion.getNow());


            startAnim();
            Gson gson = new Gson();

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(gson.toJson(productsModel));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (isNew) {
                mViewModel.createProduct(jsonObject, true).observe(AdminProductsFragment.this, new Observer<ResponseModel>() {
                    @Override
                    public void onChanged(@Nullable ResponseModel responseModel) {
                        stopAnim();
                        snack(responseModel.getResultDescription());
                        if (responseModel.getResultCode() == 1) {
                            dialog.dismiss();
                            mViewModel.refreshProducts(getSearchObject(), true);
                        }
                    }
                });
            } else {
                mViewModel.updateProduct(jsonObject, true).observe(AdminProductsFragment.this, new Observer<ResponseModel>() {
                    @Override
                    public void onChanged(@Nullable ResponseModel responseModel) {
                        stopAnim();
                        snack(responseModel.getResultDescription());
                        if (responseModel.getResultCode() == 1) {
                            dialog.dismiss();
                            mViewModel.refreshProducts(getSearchObject(), true);
                        }
                    }
                });
            }


        }

    }


}
