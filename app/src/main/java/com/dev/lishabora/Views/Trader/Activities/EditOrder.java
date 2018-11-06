package com.dev.lishabora.Views.Trader.Activities;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishabora.Adapters.ProductOrderAdapter;
import com.dev.lishabora.Adapters.ProductsAdapter;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.DayCollectionModel;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.FarmerHistoryByDateModel;
import com.dev.lishabora.Models.MonthsDates;
import com.dev.lishabora.Models.OrderModel;
import com.dev.lishabora.Models.ProductOrderModel;
import com.dev.lishabora.Models.ProductsModel;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.Utils.InputFilterMinMax;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.Trader.OrderConstants;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wajahatkarim3.easyflipview.EasyFlipView;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.dev.lishabora.Views.CommonFuncs.getCollectionsTotals;

public class EditOrder extends AppCompatActivity {
    public TextView status, id, name, balance, milk, loan, order;
    Button btngetOrders;

    //GIVE PRODUCTS
    ProductOrderAdapter listAdapter;
    //GIVE ORDER
    ImageView imgAdd, imgRemove, imgDelete;
    TextView txtQty, txtPrice;
    TextInputEditText edtAmount, edtDeliveryFee;
    //CUSTOM ALERT
    MaterialButton btnPositive, btnNegative, btnNeutral;
    TextView txtTitle;
    LinearLayout lTitle;
    ImageView imgIcon;
    private EasyFlipView easyFlipView;
    private EasyFlipView.FlipState currentSide;
    private ProductsAdapter listAdapterAll;
    private RecyclerView recyclerView;
    private List<ProductOrderModel> productOrderModels;
    private AVLoadingIndicatorView avi;
    private AVLoadingIndicatorView davi;
    private MaterialSpinner spinnerMonths;
    private FamerModel famerModel;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private PayoutsVewModel payoutsVewModel;
    private TraderViewModel traderViewModel;
    private View view;
    private List<FarmerHistoryByDateModel> modelsDA = new LinkedList<>();
    private DayCollectionModel dayCollectionModel;
    private boolean isEditable;
    private void initViewCustomDialog() {


        btnPositive = findViewById(R.id.btn_positive);
        btnNegative = findViewById(R.id.btn_negative);
        btnNeutral = findViewById(R.id.btn_neutral);
        txtTitle = findViewById(R.id.txt_title);
        lTitle = findViewById(R.id.linear_title);
        imgIcon = findViewById(R.id.img_icon);


        btnNeutral.setVisibility(View.GONE);
        btnPositive.setText("Next");
        lTitle.setVisibility(View.GONE);
        txtTitle.setVisibility(View.VISIBLE);
        imgIcon.setVisibility(View.GONE);
        imgIcon.setImageResource(R.drawable.ic_add_black_24dp);
        txtTitle.setText("Route");



        btnPositive.setOnClickListener(view -> btnPositiveClicked());
        btnNeutral.setOnClickListener(view -> btnNeutralClicked());
        btnNegative.setOnClickListener(view -> btnNegativeClicked());


    }

    private void btnNegativeClicked() {
        setUpClear();
        finish();
    }

    private void btnNeutralClicked() {

        if (currentSide == EasyFlipView.FlipState.BACK_SIDE) {
            String delivery = "0";
            OrderModel orderModel = new OrderModel();
            orderModel.setInstallmentAmount(txtPrice.getText().toString());
            orderModel.setInstallmentNo(txtQty.getText().toString());
            orderModel.setOrderAmount(edtAmount.getText().toString());
            if (!TextUtils.isEmpty(edtDeliveryFee.getText())) {
                delivery = edtDeliveryFee.getText().toString();
            }
            orderModel.setOrderDeliveryFee(delivery);
            orderModel.setProductOrderModels(OrderConstants.getProductOrderModels());

            Gson gson = new Gson();

            String data = gson.toJson(orderModel);
            OrderConstants.setOrderData(data);
            OrderConstants.setOrderModel(orderModel);

            easyFlipView.flipTheView();
        }

    }

    private void btnPositiveClicked() {
        if (currentSide == EasyFlipView.FlipState.FRONT_SIDE) {
            if (OrderConstants.getProductOrderModels() != null) {
                easyFlipView.flipTheView();
            } else {
                Toast.makeText(EditOrder.this, "No products selected", Toast.LENGTH_LONG).show();
            }
        } else {
            String deliveryFee = "0";

            OrderModel orderModel = new OrderModel();
            orderModel.setInstallmentAmount(txtPrice.getText().toString());
            orderModel.setInstallmentNo(txtQty.getText().toString());
            orderModel.setOrderAmount(edtAmount.getText().toString());
            if (!TextUtils.isEmpty(edtDeliveryFee.getText().toString())) {
                deliveryFee = edtDeliveryFee.getText().toString();
            }
            orderModel.setOrderDeliveryFee(deliveryFee);
            orderModel.setProductOrderModels(OrderConstants.getProductOrderModels());

            Gson gson = new Gson();

            String data = gson.toJson(orderModel);
            OrderConstants.setOrderData(data);
            OrderConstants.setOrderModel(orderModel);


            Intent returnIntent = new Intent();
            returnIntent.putExtra("orderData", data);
            returnIntent.putExtra("orderDataModel", orderModel);


            LinkedList<ProductOrderModel> p = new LinkedList<>();
            p.addAll(OrderConstants.getProductOrderModels());

            returnIntent.putExtra("orderDataProducts", p);
            returnIntent.putExtra("dayCollection", dayCollectionModel);


            setResult(RESULT_OK, returnIntent);
            finish();


        }

    }

    private void setUpClear() {
        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);
        traderViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);



        OrderConstants.setProductOrderModels(null);
        OrderConstants.setOrderModel(null);
        OrderConstants.setOrderData(null);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);
        dayCollectionModel = (DayCollectionModel) getIntent().getSerializableExtra("dayCollection");
        isEditable = getIntent().getBooleanExtra("isEditable", true);
        easyFlipView = findViewById(R.id.easyFlipView);
        currentSide = EasyFlipView.FlipState.FRONT_SIDE;

        famerModel = OrderConstants.getFamerModel();

        initViewCustomDialog();
        setUpClear();


        setFrontSideData();
        initDataProducts();

        easyFlipView.setOnFlipListener((easyFlipView, newCurrentSide) -> {
            currentSide = newCurrentSide;

            if (currentSide == EasyFlipView.FlipState.FRONT_SIDE) {
                setFrontSideData();
            } else {
                setBackSideData();
            }


        });


    }


    private void setFrontSideData() {


        btnPositive.setText("Next");
        btnPositive.setVisibility(View.VISIBLE);


        btnNegative.setText("Dismiss");
        btnNegative.setVisibility(View.VISIBLE);


        btnNeutral.setText("Dismiss");
        btnNeutral.setVisibility(View.GONE);


        initView();
        initList();



    }

    private void setBackSideData() {


        btnPositive.setText("Complete");
        btnPositive.setVisibility(View.VISIBLE);

        if (!isEditable) {
            btnPositive.setEnabled(false);
        }


        btnNegative.setText("Dismiss");
        btnNegative.setVisibility(View.VISIBLE);


        btnNeutral.setText("Back");
        btnNeutral.setVisibility(View.VISIBLE);


        initViewOrder();
        initActions();
        initData();


        initDataCompleteOrder();

    }


    /****GIVE PRODUCTS     ****/


    private void initView() {
        id = findViewById(R.id.txt_id);
        name = findViewById(R.id.txt_name);
        btngetOrders = findViewById(R.id.btn_add_product);

        spinnerMonths = findViewById(R.id.spinner_months);

        milk = findViewById(R.id.txt_milk);
        loan = findViewById(R.id.txt_loans);
        order = findViewById(R.id.txt_orders);

        payoutsVewModel.getCollectionByFarmer(famerModel.getCode()).observe(this, collections -> {
            if (collections != null && collections.size() > 0) {
                createMonthlyList(collections);
            } else {
                initMonthlyList(new LinkedList<>());
            }
        });
        btngetOrders.setOnClickListener(view -> traderViewModel.getProducts(false).observe(EditOrder.this, productsModels -> {
            if (productsModels != null) {
                filterList(productsModels);
            }
        }));

        if (!isEditable) {
            btngetOrders.setEnabled(false);
            //btngetOrders.setVisibility(View.GONE);
        }
    }

    private void filterList(List<ProductsModel> productsModels) {

        if (OrderConstants.getProductOrderModels() == null || OrderConstants.getProductOrderModels().size() < 1) {
            subscribeProduct(productsModels);
        } else {
            for (ProductOrderModel p : OrderConstants.getProductOrderModels()) {
                for (int a = 0; a < productsModels.size(); a++) {
                    if (productsModels.get(a).getCode() == p.getCode()) {
                        productsModels.remove(a);
                    }
                }
            }
            subscribeProduct(productsModels);


        }

    }

    private void createMonthlyList(List<Collection> collections) {

        List<MonthsDates> monthsDates = DateTimeUtils.Companion.getMonths(12);
        if (monthsDates != null && monthsDates.size() > 0) {

            LinkedList<FarmerHistoryByDateModel> fmh = new LinkedList<>();

            for (MonthsDates mds : monthsDates) {

                String[] totals = getCollectionsTotals(mds, collections);
                fmh.add(new FarmerHistoryByDateModel(mds, famerModel, totals[0], totals[1], totals[2], totals[3]));

            }
            initMonthlyList(fmh);

        }


    }
    public void initMonthlyList(List<FarmerHistoryByDateModel> models) {

        this.modelsDA = models;

        double milkTotal = 0.0, loanTotal = 0, OrderTotal = 0;
        for (FarmerHistoryByDateModel f : models) {
            milkTotal = milkTotal + Double.valueOf(f.getMilktotal());

            loanTotal = loanTotal + Double.valueOf(f.getLoanTotal());

            OrderTotal = OrderTotal + Double.valueOf(f.getOrderTotal());
        }


        if (models == null) {
            models = new LinkedList<>();
        }
//        String[] months = new String[models.size()];
//        for (int a = 0; a < models.size(); a++) {
//            months[a] = models.get(a).getMonthsDates().getMonthName() + " ";
//
//        }

//        String m = DateTimeUtils.Companion.getMonth(DateTimeUtils.Companion.getToday());
//
//        spinnerMonths.setItems(months);
//        int curr = 0;
//        for (int a = 0; a < months.length; a++) {
//            if (months[a].contains(m)) {
//                curr = a;
//            }
//        }
//        try {
//            spinnerMonths.setSelectedIndex(curr);
//            setData(modelsDA.get(curr));
//        }catch (Exception nm){
//            nm.printStackTrace();
//        }
//        spinnerMonths.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
//                setData(modelsDA.get(position));
//            }

//        });
        int x = 1;
        if (models.size() > 0) {
            x = models.size();
        }

        setData(String.valueOf(milkTotal / x), String.valueOf(loanTotal / x), String.valueOf(OrderTotal / x));

    }

    private void setData(String s, String s1, String s2) {
        id.setText(famerModel.getCode());
        name.setText(famerModel.getNames());
        milk.setText(GeneralUtills.Companion.round(s, 0));
        loan.setText(GeneralUtills.Companion.round(s1, 0));
        order.setText(GeneralUtills.Companion.round(s2, 0));


    }

//    public void initMonthlyList(List<FarmerHistoryByDateModel> models) {
//
//        this.modelsDA = models;
//
//
//        if (models == null) {
//            models = new LinkedList<>();
//        }
//        String[] months = new String[models.size()];
//        for (int a = 0; a < models.size(); a++) {
//            months[a] = models.get(a).getMonthsDates().getMonthName() + " ";
//
//        }
//
//        String m = DateTimeUtils.Companion.getMonth(DateTimeUtils.Companion.getToday());
//
//        spinnerMonths.setItems(months);
//        int curr = 0;
//        for (int a = 0; a < months.length; a++) {
//            if (m != null && months[a].contains(m)) {
//                curr = a;
//            }
//        }
//
//        if (months != null && months.length >= curr && spinnerMonths.getItems() != null && spinnerMonths.getItems().size() > 0) {
//            spinnerMonths.setSelectedIndex(curr);
//        }
//        if (modelsDA != null && modelsDA.size() > 0) {
//            setData(modelsDA.get(curr));
//        }
//        spinnerMonths.setOnItemSelectedListener((view, position, id, item) -> setData(modelsDA.get(position)));
//
//
//    }
//
//    private void setData(FarmerHistoryByDateModel farmerHistoryByDateModel) {
//        id.setText(famerModel.getCode());
//        name.setText(famerModel.getNames());
//        milk.setText(farmerHistoryByDateModel.getMilktotal());
//        loan.setText(farmerHistoryByDateModel.getLoanTotal());
//        order.setText(farmerHistoryByDateModel.getOrderTotal());
//
//    }

    private void subscribeProduct(List<ProductsModel> productsModels) {

        Log.d("ReTrReqd", " Dialog is has called");
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_all_products_list, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(this));
        alertDialogBuilderUserInput.setView(mView);
//        alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
//        alertDialogBuilderUserInput.setTitle("Products");


        davi = mView.findViewById(R.id.avi);


        RecyclerView recyclerView = mView.findViewById(R.id.recyclerView);

        StaggeredGridLayoutManager mStaggeredLayoutManager;
        LinkedList<ProductsModel> selected = new LinkedList<>();


        listAdapterAll = new ProductsAdapter(this, productsModels, new OnclickRecyclerListener() {
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

    private void refreshList() {
        listAdapter.refresh(OrderConstants.getProductOrderModels());
    }

    public void initList() {
        recyclerView = findViewById(R.id.recyclerView);
        if (OrderConstants.getProductOrderModels() == null) {
            OrderConstants.setProductOrderModels(new LinkedList<>());
        }
        listAdapter = new ProductOrderAdapter(this, OrderConstants.getProductOrderModels(), new OnclickRecyclerListener() {
            @Override
            public void onMenuItem(int position, int menuItem) {

            }

            @Override
            public void onSwipe(int adapterPosition, int direction) {

            }

            @Override
            public void onClickListener(int position) {

                editProduct(OrderConstants.getProductOrderModels().get(position), position);
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
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        listAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(listAdapter);


    }

    private void editProduct(ProductOrderModel model, int pos) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_give_product, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(this));
        alertDialogBuilderUserInput.setView(mView);
//        alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
//        alertDialogBuilderUserInput.setTitle("Route");


        avi = mView.findViewById(R.id.avi);

        TextView name, buyPrice;
        TextInputEditText sellingPrice, qty;

        name = mView.findViewById(R.id.edt_product_names);
        buyPrice = mView.findViewById(R.id.edt_product_cost_price);
        sellingPrice = mView.findViewById(R.id.edt_product_selling_prices);
        qty = mView.findViewById(R.id.edt_product_quantity);

        name.setText(model.getNames());
        buyPrice.setText(model.getBuyingprice());
        sellingPrice.setText(model.getTotalprice());
        qty.setText(model.getQuantity());


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


        btnNeutral.setVisibility(View.VISIBLE);
        btnNeutral.setText("Delete");

        btnNeutral.setBackgroundColor(this.getResources().getColor(R.color.red));
        lTitle.setVisibility(View.GONE);
        txtTitle.setVisibility(View.VISIBLE);
        imgIcon.setVisibility(View.VISIBLE);
        imgIcon.setImageResource(R.drawable.ic_add_black_24dp);
        txtTitle.setText("Route");

        btnNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderConstants.getProductOrderModels().remove(pos);

                alertDialogAndroid.dismiss();

                refreshList();
            }
        });

        btnPositive.setOnClickListener(new EditCustomListener(alertDialogAndroid, model, pos));

        btnNegative.setOnClickListener(view -> alertDialogAndroid.dismiss());


    }

    private void initDataProducts() {
        OrderModel orderModel = dayCollectionModel.getOrderModel();
        OrderConstants.setOrderModel(orderModel);
        OrderConstants.setProductOrderModels(orderModel.getProductOrderModels());
        listAdapter.refresh(OrderConstants.getProductOrderModels());

        //getCollection(famerModel.getCode(), DateTimeUtils.Companion.getToday());
    }


    /********COMPLETE ORDER     ******/
    public void calc(View imgAction, View txtQty) {
        String gty = ((TextView) txtQty).getText().toString();

//        double mazx=0;
//
//        try{
//            mazx=Double.valueOf(gty);
//        }catch (Exception nm){
//            nm.printStackTrace();
//        }
//
//        if(mazx>10) {
        double deliveryFee = 0.0;

        if (imgAction.getId() == R.id.img_add) {
            int vq = Integer.valueOf(gty) + 1;
            if (vq <= 10) {
                ((TextView) txtQty).setText(String.valueOf(vq));
            }
            // ((TextView) txtQty).setText(String.valueOf(vq));

        } else {
            int vq = Integer.valueOf(gty);
            if (vq != 1) {
                ((TextView) txtQty).setText(String.valueOf(vq - 1));
            }
        }


        double installmentValue = 0.0;
        if (edtDeliveryFee.getText() != null && !TextUtils.isEmpty(edtDeliveryFee.getText())) {
            deliveryFee = (Double.valueOf(edtDeliveryFee.getText().toString()));
        }
        if (edtAmount.getText() != null && !TextUtils.isEmpty(edtAmount.getText())) {

            double value = Double.valueOf(edtAmount.getText().toString());


            int insNo = Integer.valueOf(((TextView) txtQty).getText().toString());
            if (value > 0.0) {
                installmentValue = ((value + deliveryFee) / insNo);

            }

        }


        txtPrice.setText(String.valueOf(GeneralUtills.Companion.round((installmentValue), 2)));

//        }else {
//            MyToast.toast("You reached maximum value",EditOrder.this,R.drawable.ic_error_outline_black_24dp,Toast.LENGTH_LONG);
//        }

    }

    private void initData() {

        Double dt = 0.0;
        Double delivery = 0.0;
        if (OrderConstants.getProductOrderModels() != null) {

            for (ProductOrderModel p : OrderConstants.getProductOrderModels()) {
                if (p.getTotalprice() != null) {
                    dt = dt + (Double.valueOf(p.getTotalprice()));
                }
            }
        }

        if (OrderConstants.getOrderModel() != null) {
            if (OrderConstants.getOrderModel().getOrderDeliveryFee() != null) {
                delivery = Double.valueOf(OrderConstants.getOrderModel().getOrderDeliveryFee());
            }
        }


        edtDeliveryFee.setText(String.valueOf(delivery));
        edtDeliveryFee.setFilters(new InputFilter[]{new InputFilterMinMax(1, 1000)});

        edtAmount.setText(String.valueOf(dt));

        double installmentValue = 0.0;
        if (!TextUtils.isEmpty(edtDeliveryFee.getText())) {
            delivery = Double.valueOf(edtDeliveryFee.getText().toString());
        }

        edtAmount.getText().toString();
        if (!TextUtils.isEmpty(edtAmount.getText().toString())) {
            double value = Double.valueOf(edtAmount.getText().toString());
            int insNo = Integer.valueOf(txtQty.getText().toString());
            if (value > 0.0) {
                installmentValue = ((value + delivery) / insNo);
            }
        }

        txtPrice.setText(String.valueOf(GeneralUtills.Companion.round((installmentValue), 2)));
    }

    void initViewOrder() {
        id = findViewById(R.id.txt_id);
        name = findViewById(R.id.txt_name);

        edtAmount = findViewById(R.id.edt_value);
        edtDeliveryFee = findViewById(R.id.edt_delivery);



        txtQty = findViewById(R.id.txt_qty);
        txtPrice = findViewById(R.id.txt_installment);

        imgAdd = findViewById(R.id.img_add);
        imgRemove = findViewById(R.id.img_remove);
        if (!isEditable) {
            // btngetOrders.setEnabled(false);
            //btngetOrders.setVisibility(View.GONE);
            edtDeliveryFee.setEnabled(false);
            edtAmount.setEnabled(false);
            imgAdd.setEnabled(false);
            imgRemove.setEnabled(false);

            imgAdd.setClickable(false);
            imgRemove.setClickable(false);
        }

    }

    void initActions() {
        imgAdd.setOnClickListener(view -> calc(imgAdd, txtQty));
        imgRemove.setOnClickListener(view -> calc(imgRemove, txtQty));


        edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                double installmentValue = 0.0;
                double delivery = 0.0;
                if (edtDeliveryFee.getText() != null && !TextUtils.isEmpty(edtDeliveryFee.getText().toString())) {
                    delivery = Double.valueOf(edtDeliveryFee.getText().toString());
                }

                if (editable != null) {

                    if (edtAmount.getText().toString() != null && !TextUtils.isEmpty(edtAmount.getText().toString())) {
                        double value = Double.valueOf(edtAmount.getText().toString());
                        int insNo = Integer.valueOf(txtQty.getText().toString());
                        if (value > 0.0) {
                            installmentValue = ((value + delivery) / insNo);
                        }
                    }


                }

                txtPrice.setText(String.valueOf(GeneralUtills.Companion.round((installmentValue), 2)));
            }
        });
        edtDeliveryFee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                double installmentValue = 0.0;
                double delivery = 0.0;
                if (edtDeliveryFee.getText() != null && !TextUtils.isEmpty(edtDeliveryFee.getText().toString())) {
                    delivery = Double.valueOf(edtDeliveryFee.getText().toString());
                }
                if (editable != null) {

                    if (edtAmount.getText().toString() != null && !TextUtils.isEmpty(edtAmount.getText().toString())) {
                        double value = Double.valueOf(edtAmount.getText().toString());
                        int insNo = Integer.valueOf(txtQty.getText().toString());
                        if (value > 0.0) {
                            installmentValue = ((value + delivery) / insNo);
                        }
                    }

                }
                txtPrice.setText(String.valueOf(GeneralUtills.Companion.round((installmentValue), 2)));
            }
        });


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
                Log.d("createproducts", " products" + selectedProducts.size() + selectedProducts.get(0).getNames());


                List<ProductOrderModel> productOrderModels = new LinkedList<>();
                for (ProductsModel p : selectedProducts) {
                    productOrderModels.add(new ProductOrderModel(
                            p.getId(),
                            p.getCode(),
                            p.getNames(),
                            p.getCostprice(),
                            p.getBuyingprice(),
                            p.getSellingprice(),
                            p.getAllowablediscount(),
                            DateTimeUtils.Companion.getNow(),
                            p.getTransactedby(),
                            p.getSubscribed(),
                            "1",
                            p.getSellingprice(), 1, true));


                }

                OrderConstants.addProductOrders(productOrderModels);
                dialog.dismiss();
                refreshList();

            } else {

                // dialog.dismiss();
            }

        }

    }

    private class EditCustomListener implements View.OnClickListener {
        AlertDialog dialog;
        boolean isActive = true;
        int isAct = 1;
        ProductOrderModel model;
        int pos;

        public EditCustomListener(AlertDialog alertDialogAndroid, ProductOrderModel model, int pos) {
            dialog = alertDialogAndroid;
            this.model = model;
            this.pos = pos;

        }

        @Override
        public void onClick(View v) {
            TextInputEditText name, buyPrice, sellingPrice, qty;

            name = dialog.findViewById(R.id.edt_product_names);
            buyPrice = dialog.findViewById(R.id.edt_product_cost_price);
            sellingPrice = dialog.findViewById(R.id.edt_product_selling_prices);
            qty = dialog.findViewById(R.id.edt_product_quantity);

            name = dialog.findViewById(R.id.edt_rout_names);


            if (sellingPrice.getText().toString().isEmpty()) {
                sellingPrice.setError("Required");
                sellingPrice.requestFocus();
                avi.smoothToHide();
                return;
            }
            if (qty.getText().toString().isEmpty()) {
                qty.setError("Required");
                qty.requestFocus();
                avi.smoothToHide();
                return;
            }

            model.setSellingprice(sellingPrice.getText().toString());
            model.setQuantity(qty.getText().toString());


            OrderConstants.getProductOrderModels().get(pos).setQuantity(model.getQuantity());
            OrderConstants.getProductOrderModels().get(pos).setSellingprice(model.getSellingprice());


            try {
                Double total = (Double.valueOf(model.getQuantity()) * Double.valueOf(model.getSellingprice()));
                OrderConstants.getProductOrderModels().get(pos).setTotalprice(String.valueOf(total));

            } catch (Exception nm) {
                nm.printStackTrace();
            }


            dialog.dismiss();
            refreshList();


        }

    }

    private void initDataCompleteOrder() {

        Double dt = 0.0;
        Double delivery = 0.0;
        if (OrderConstants.getProductOrderModels() != null) {


            for (ProductOrderModel p : OrderConstants.getProductOrderModels()) {
                if (p.getTotalprice() != null) {
                    dt = dt + (Double.valueOf(p.getTotalprice()));
                }
            }
        }

        edtAmount.setText(String.valueOf(dt));
        double installmentValue = 0.0;
        if (!TextUtils.isEmpty(edtDeliveryFee.getText())) {
            delivery = Double.valueOf(edtDeliveryFee.getText().toString());

        }
        if (!TextUtils.isEmpty(Objects.requireNonNull(edtAmount.getText()).toString())) {
            double value = Double.valueOf(edtAmount.getText().toString());
            int insNo = Integer.valueOf(txtQty.getText().toString());
            if (value > 0.0) {
                installmentValue = ((value + delivery) / insNo);
            }
        }


        txtPrice.setText(String.valueOf(GeneralUtills.Companion.round((installmentValue), 2)));

        if (OrderConstants.getOrderModel() != null) {
            OrderModel l = OrderConstants.getOrderModel();

            if (l.getInstallmentNo() != null) {
                txtQty.setText(l.getInstallmentNo());
                if (!TextUtils.isEmpty(edtDeliveryFee.getText())) {
                    delivery = Double.valueOf(edtDeliveryFee.getText().toString());

                }
                try {
                    if (!TextUtils.isEmpty(Objects.requireNonNull(edtAmount.getText()).toString())) {
                        double value = Double.valueOf(edtAmount.getText().toString());
                        int insNo = Integer.valueOf(txtQty.getText().toString());
                        if (value > 0.0) {
                            installmentValue = ((value + delivery) / insNo);
                        }
                    }

                    txtPrice.setText(String.valueOf(GeneralUtills.Companion.round((installmentValue), 2)));

                } catch (Exception nm) {
                    nm.printStackTrace();
                }

            }


        }
    }



}
