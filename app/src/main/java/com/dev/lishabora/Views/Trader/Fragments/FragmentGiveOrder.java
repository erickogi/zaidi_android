package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.lishabora.Adapters.ProductOrderAdapter;
import com.dev.lishabora.Adapters.ProductsAdapter;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.FarmerHistoryByDateModel;
import com.dev.lishabora.Models.MonthsDates;
import com.dev.lishabora.Models.ProductOrderModel;
import com.dev.lishabora.Models.ProductsModel;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.Trader.OrderConstants;
import com.dev.lishaboramobile.R;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class FragmentGiveOrder extends Fragment implements BlockingStep {
    public TextView status, id, name, balance, milk, loan, order;
    Button btngetOrders;
    ProductOrderAdapter listAdapter;
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

    private String getCollection(String code, String date, TextInputEditText txt) {

        List<Collection> collections = traderViewModel.getCollectionByDateByFarmer(code, date);//.observe(FragementFarmersList.this, collections -> {

        Double tt = 0.0;

        if (txt != null) {
            if (collections != null) {

                for (Collection c : collections) {


                    tt = tt + (Double.valueOf(c.getLoanAmountGivenOutPrice()));


                }

            }
            txt.setText(String.valueOf(tt));
        }


        return String.valueOf(tt);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);
        traderViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_give_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        famerModel = OrderConstants.getFamerModel();


    }

    void popOutFragments() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void initView() {
        id = view.findViewById(R.id.txt_id);
        name = view.findViewById(R.id.txt_name);
        btngetOrders = view.findViewById(R.id.btn_add_product);

        spinnerMonths = view.findViewById(R.id.spinner_months);

        milk = view.findViewById(R.id.txt_milk);
        loan = view.findViewById(R.id.txt_loans);
        order = view.findViewById(R.id.txt_orders);

        payoutsVewModel.getCollectionByFarmer(famerModel.getCode()).observe(this, collections -> {
            if (collections != null && collections.size() > 0) {
                createMonthlyList(collections);
            } else {
                initMonthlyList(null);
            }
        });
        btngetOrders.setOnClickListener(view -> traderViewModel.getProducts(false).observe(FragmentGiveOrder.this, productsModels -> {
            if (productsModels != null) {
                filterList(productsModels);
            }
        }));
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

    private String[] getCollectionsTotals(MonthsDates mds, List<Collection> collections) {
        String cycleCode = "";
        double milk = 0.0;
        double loan = 0.0;
        double order = 0.0;

        for (Collection collection : collections) {
            if (DateTimeUtils.Companion.isInMonth(collection.getDayDate(), mds.getMonthName())) {
                milk = milk + Double.valueOf(collection.getMilkCollected());
                loan = loan + Double.valueOf(collection.getLoanAmountGivenOutPrice());
                order = order + Double.valueOf(collection.getOrderGivenOutPrice());
            }

        }
        double[] totals = {milk, loan, order};


        return new String[]{String.valueOf(totals[0]), String.valueOf(totals[1]), String.valueOf(totals[2]), cycleCode};
    }

    public void initMonthlyList(List<FarmerHistoryByDateModel> models) {

        this.modelsDA = models;


        if (models == null) {
            models = new LinkedList<>();
        }
        String[] months = new String[models.size()];
        for (int a = 0; a < models.size(); a++) {
            months[a] = models.get(a).getMonthsDates().getMonthName() + " ";

        }

        String m = DateTimeUtils.Companion.getMonth(DateTimeUtils.Companion.getToday());

        spinnerMonths.setItems(months);
        int curr = 0;
        for (int a = 0; a < months.length; a++) {
            if (months[a].contains(m)) {
                curr = a;
            }
        }
        spinnerMonths.setSelectedIndex(curr);
        setData(modelsDA.get(curr));
        spinnerMonths.setOnItemSelectedListener((view, position, id, item) -> setData(modelsDA.get(position)));


    }

    private void setData(FarmerHistoryByDateModel farmerHistoryByDateModel) {
        id.setText(famerModel.getCode());
        name.setText(famerModel.getNames());
        milk.setText(farmerHistoryByDateModel.getMilktotal());
        loan.setText(farmerHistoryByDateModel.getLoanTotal());
        order.setText(farmerHistoryByDateModel.getOrderTotal());

    }

    public int[] getApprovedCards(List<Collection> collections, String pcode) {

        int[] statusR = new int[3];
        int farmerStatus = 0;


        List<FamerModel> f = payoutsVewModel.getFarmersByCycleONe(pcode);


        statusR[0] = f.size();


        int approved = 0;

        for (FamerModel famerModel : f) {
            int status = 0;
            int collectionNo = 0;
            for (Collection c : collections) {


                if (c.getFarmerCode().equals(famerModel.getCode())) {


                    collectionNo = collectionNo + 1;

                    try {
                        status += c.getApproved();

                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }
                }


            }

            if (status == collectionNo) {
                approved += 1;
            }


        }
        statusR[1] = approved;
        statusR[2] = statusR[0] - approved;


        return statusR;


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
        if (OrderConstants.getProductOrderModels() == null) {
            return new VerificationError("No Products selected");
        } else {
            return null;
        }
    }

    @Override
    public void onSelected() {

        initView();
        initList();

    }

    @Override
    public void onError(@NonNull VerificationError error) {

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

        Button theButton = alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CustomListener(alertDialogAndroid, selected));


    }

    private void refreshList() {
        listAdapter.refresh(OrderConstants.getProductOrderModels());
    }

    public void initList() {
        recyclerView = view.findViewById(R.id.recyclerView);
        if (OrderConstants.getProductOrderModels() == null) {
            OrderConstants.setProductOrderModels(new LinkedList<>());
        }
        listAdapter = new ProductOrderAdapter(getActivity(), OrderConstants.getProductOrderModels(), new OnclickRecyclerListener() {
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
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_give_product, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        alertDialogBuilderUserInput.setView(mView);
//        alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
//        alertDialogBuilderUserInput.setTitle("Route");


        avi = mView.findViewById(R.id.avi);

        TextInputEditText name, buyPrice, sellingPrice, qty;

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


        btnNeutral.setVisibility(View.GONE);
        btnNeutral.setText("Delete");

        btnNeutral.setBackgroundColor(getContext().getResources().getColor(R.color.red));
        lTitle.setVisibility(View.GONE);
        txtTitle.setVisibility(View.VISIBLE);
        imgIcon.setVisibility(View.VISIBLE);
        imgIcon.setImageResource(R.drawable.ic_add_black_24dp);
        txtTitle.setText("Route");

        btnPositive.setOnClickListener(new EditCustomListener(alertDialogAndroid, model, pos));

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
                Log.d("createproducts", " products" + selectedProducts.size() + selectedProducts.get(0).getNames());


                List<ProductOrderModel> productOrderModels = new LinkedList<>();
                for (ProductsModel p : selectedProducts) {
                    // public ProductOrderModel(int id, int code, String names, String costprice, String buyingprice,
                    // String sellingprice, String allowablediscount, String transactiontime,
                    // String transactedby, String subscribed, String quantity,
                    // String totalprice,
                    // int status,
                    // boolean isSelected)
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


}
