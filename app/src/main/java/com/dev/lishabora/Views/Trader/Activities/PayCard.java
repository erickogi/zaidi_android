package com.dev.lishabora.Views.Trader.Activities;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishabora.Adapters.FarmerCollectionsAdapter;
import com.dev.lishabora.Adapters.PayoutFarmersAdapter;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.DayCollectionModel;
import com.dev.lishabora.Models.DaysDates;
import com.dev.lishabora.Models.PayoutFarmersCollectionModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Utils.AdvancedOnclickRecyclerListener;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class PayCard extends AppCompatActivity {
    public TextView status, id, name, balance, milk, loan, order;
    public RelativeLayout background;
    public View statusview;
    public View itemVew;
    Collection c = new Collection();
    PayoutFarmersCollectionModel payoutfarmermodel;
    LinearLayout layoutBottomSheet;

    private Button save;
    private FarmerCollectionsAdapter listAdapter;
    private Context context;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private LinearLayout empty_layout;
    private Payouts payouts;
    private PayoutsVewModel payoutsVewModel;
    private List<DayCollectionModel> dayCollectionModels;
    private List<Collection> collections;
    private boolean firstDone = false;
    private List<DayCollectionModel> liveModel;
    BottomSheetBehavior sheetBehavior;
    private AVLoadingIndicatorView avi;
    private SearchView searchView;
    private MaterialButton btnApprove, btnBack;



    private void loadCollections(String payoutNumber, String farmerCode) {
        payoutsVewModel.getCollectionByDateByPayoutByFarmer(payoutNumber, farmerCode).observe(this, collections -> {
            if (collections != null) {
                PayCard.this.collections = collections;
                //setUpDayCollectionsModel();
                getPayout(payoutNumber, collections);
            }
        });
    }

    private void getPayout(String payout, List<Collection> collections) {
        payoutsVewModel.getPayoutsByPayoutNumber(payout).observe(this, new Observer<Payouts>() {
            @Override
            public void onChanged(@Nullable Payouts payouts) {
                setUpDayCollectionsModel(payouts, collections);
            }
        });

    }

    private String filterText = "";

    private void setUpList(List<DayCollectionModel> dayCollectionModels) {
        this.dayCollectionModels = dayCollectionModels;
        this.liveModel = dayCollectionModels;
        listAdapter.refresh(dayCollectionModels);



    }

    private void setUpDayCollectionsModel(Payouts payouts, List<Collection> collections) {


        List<DaysDates> daysDates = DateTimeUtils.Companion.getDaysAndDatesBtnDates(payouts.getStartDate(), payouts.getEndDate());

        List<DayCollectionModel> dayCollectionModels = new LinkedList<>();
        for (DaysDates d : daysDates) {
            String milkAm = getMilk(d.getDate(), "AM", collections);
            String milkPm = getMilk(d.getDate(), "PM", collections);
            String loanAm = getLoan(d.getDate(), "AM", collections);
            String laonPm = getLoan(d.getDate(), "PM", collections);
            String orderAm = getOrder(d.getDate(), "AM", collections);
            String orderPm = getOrder(d.getDate(), "PM", collections);
            int collectionIdAm = getCollectionIdAm(d.getDate(), collections);
            int collectionIdPm = getCollectionIdPm(d.getDate(), collections);

            dayCollectionModels.add(new DayCollectionModel(
                    payouts.getPayoutnumber(),
                    d.getDay(),
                    d.getDate(),
                    milkAm,
                    milkPm,
                    loanAm,
                    laonPm,
                    orderAm,
                    orderPm, collectionIdAm, collectionIdPm));
        }

        setUpList(dayCollectionModels);

    }

    private int getCollectionIdAm(String date, List<Collection> collections) {
        if (collections != null) {
            Log.d("collectisdid", "Am Called  " + date
                    + "" + collections);

            for (Collection c : collections) {

                if (c.getDayDate().contains(date) && c.getTimeOfDay().equals("AM")) {
                    Log.d("collectisdid", "AM " + c.getId());
                    return c.getId();
                }
            }

        }
        return 0;
    }

    private String getOrder(String date, String ampm, List<Collection> collections) {
        double orderTotal = 0.0;
        if (collections != null) {
            for (Collection c : collections) {

                if (c.getDayDate().contains(date) && c.getTimeOfDay().equals(ampm)) {
                    try {
                        orderTotal += Double.valueOf(c.getOrderGivenOutPrice());
                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }
                }
            }

        }
        return String.valueOf(orderTotal);

    }

    private String getLoan(String date, String ampm, List<Collection> collections) {
        double loanTotal = 0.0;
        if (collections != null) {
            for (Collection c : collections) {
                if (c.getDayDate().contains(date) && c.getTimeOfDay().equals(ampm)) {
                    try {
                        loanTotal += Double.valueOf(c.getLoanAmountGivenOutPrice());
                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }
                }
            }
        }
        return String.valueOf(loanTotal);


    }

    private String getMilk(String date, String ampm, List<Collection> collections) {
        double milkTotal = 0.0;
        if (collections != null) {
            for (Collection c : collections) {
                if (c.getDayDate().equals(date) && c.getTimeOfDay().equals(ampm)) {
                    try {
                        milkTotal += Double.valueOf(c.getMilkCollected());
                        Log.d("CollectionsVsDate", " Date : " + date + "  Time " + ampm + "\nColDate : " + c.getDayDate() + "  ColTime " + c.getTimeOfDay() + "\n Milk " + c.getMilkCollected());

                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }
                }
            }
        }
        return String.valueOf(milkTotal);


    }

    private int getCollectionIdPm(String date, List<Collection> collections) {
        if (collections != null) {
            Log.d("collectisdid", "Pm Called ");

            for (Collection c : collections) {

                if (c.getDayDate().contains(date) && c.getTimeOfDay().equals("PM")) {
                    Log.d("collectisdid", "PM " + c.getId());

                    return c.getId();
                }
            }

        }
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_card);
        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);

        initList();

        btnApprove = findViewById(R.id.btn_approve);
        btnApprove.setVisibility(View.GONE);

        btnBack = findViewById(R.id.btn_back);

        btnBack.setVisibility(View.GONE);

        statusview = findViewById(R.id.status_view);
        background = findViewById(R.id.background);
        save = findViewById(R.id.save_btn);
        save.setVisibility(View.GONE);
        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        // save.setOnClickListener(view -> update(liveModel));


        status = findViewById(R.id.txt_status);
        id = findViewById(R.id.txt_id);
        name = findViewById(R.id.txt_name);
        balance = findViewById(R.id.txt_balance);
        searchView = findViewById(R.id.search);


        milk = findViewById(R.id.txt_milk);
        loan = findViewById(R.id.txt_loans);
        order = findViewById(R.id.txt_orders);
        PayoutFarmersCollectionModel model = (PayoutFarmersCollectionModel) getIntent().getSerializableExtra("data");
        payouts = (Payouts) getIntent().getSerializableExtra("payout");


        setData(model);

        String data = getIntent().getStringExtra("farmers");

        Gson gson = new Gson();
//        JsonArray jsonArray = gson.toJsonTree(data).getAsJsonArray();
        Type listType = new TypeToken<LinkedList<PayoutFarmersCollectionModel>>() {
        }.getType();

        setBottom();
        initBottomList(gson.fromJson(data, listType));


    }

    private void approve(Payouts payouts, PayoutFarmersCollectionModel model) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PayCard.this);
        alertDialog.setMessage("Confirm that you wish to approve " + model.getFarmername() + "'s " + payouts.getCyclename() + " Collection card").setCancelable(false).setTitle("Approve " + model.getFarmername() + " Card");


        alertDialog.setPositiveButton("Yes", (dialogInterface, i) -> {

            payoutsVewModel.approveFarmersPayoutCard(model.getFarmercode(), model.getPayoutNumber());
            model.setStatus(1);
            model.setStatusName("Approved");
            setData(model);


            dialogInterface.dismiss();

        }).setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());

        AlertDialog alertDialogAndroid = alertDialog.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.show();

    }

    private void searchInit() {


    }

    public void initBottomList(List<PayoutFarmersCollectionModel> models) {

        RecyclerView recyclerView = findViewById(R.id.recyclerViewbottom);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        PayoutFarmersAdapter listAdapter = new PayoutFarmersAdapter(this, models, new OnclickRecyclerListener() {
            @Override
            public void onSwipe(int adapterPosition, int direction) {


            }

            @Override
            public void onClickListener(int position) {
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

                setData(models.get(position));

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
        recyclerView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();

        searchView.setVisibility(View.GONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filterText = s;


                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterText = s;

                return true;
            }
        });


    }


    private void setBottom() {
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }

    private String getBalance(String milkTotal, String loanTotal, String orderTotal) {
        double balance = 0.0;
        try {
            balance = Double.valueOf(milkTotal);
        } catch (Exception nm) {
            nm.printStackTrace();
        }
        try {
            return String.valueOf(Double.valueOf(milkTotal) - (Double.valueOf(loanTotal) + Double.valueOf(orderTotal)));

        } catch (Exception nm) {
            nm.printStackTrace();
        }
        return String.valueOf(balance);
    }

    private void update(List<DayCollectionModel> liveModel) {
        if (liveModel != null && liveModel.size() > 0) {

            for (DayCollectionModel dayCollectionModel : liveModel) {
                if (dayCollectionModel.getCollectionIdAm() != 0) {
                    payoutsVewModel.getCollectionById(dayCollectionModel.getCollectionIdAm()).observe(this, new Observer<Collection>() {
                        @Override
                        public void onChanged(@Nullable Collection collection) {
                            if (collection != null) {

                                collection.setOrderGivenOutPrice(dayCollectionModel.getOrderAm());
                                collection.setMilkCollected(dayCollectionModel.getMilkAm());
                                collection.setLoanAmountGivenOutPrice(dayCollectionModel.getLoanAm());
                                payoutsVewModel.updateCollection(collection);
                            }
                        }
                    });

                } else {

                }
            }
            for (DayCollectionModel dayCollectionModel1 : liveModel) {
                if (dayCollectionModel1.getCollectionIdPm() != 0) {
                    payoutsVewModel.getCollectionById(dayCollectionModel1.getCollectionIdPm()).observe(this, new Observer<Collection>() {
                        @Override
                        public void onChanged(@Nullable Collection collection) {
                            if (collection != null) {

                                collection.setOrderGivenOutPrice(dayCollectionModel1.getOrderPm());
                                collection.setMilkCollected(dayCollectionModel1.getMilkPm());
                                collection.setLoanAmountGivenOutPrice(dayCollectionModel1.getLoanPm());
                                payoutsVewModel.updateCollection(collection);
                            }
                        }
                    });

                } else {

                }
            }
        }
        save.setVisibility(View.GONE);

    }

    private void setBalance() {
        balance.setText(getBalance(milk.getText().toString(), loan.getText().toString(), order.getText().toString()));
    }
    private void setData(PayoutFarmersCollectionModel model) {
        this.payoutfarmermodel = model;
        balance.setText(model.getBalance());
        id.setText(model.getFarmercode());
        name.setText(model.getFarmername());
        status.setText(model.getStatusName());

        setTitle("" + model.getFarmername() + "        ID " + model.getFarmercode());


        milk.setText(model.getMilktotal());
        if (!model.getMilktotal().equals("0.0")) {
            milk.setTextColor(this.getResources().getColor(R.color.colorPrimary));
            milk.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            milk.setTypeface(Typeface.DEFAULT);

            milk.setTextColor(this.getResources().getColor(R.color.black));

        }


        loan.setText(model.getLoanTotal());
        if (!model.getLoanTotal().equals("0.0")) {
            loan.setTextColor(this.getResources().getColor(R.color.colorPrimary));
            loan.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            loan.setTypeface(Typeface.DEFAULT);

            loan.setTextColor(this.getResources().getColor(R.color.black));

        }

        order.setText(model.getOrderTotal());
        if (!model.getOrderTotal().equals("0.0")) {
            order.setTextColor(this.getResources().getColor(R.color.colorPrimary));
            order.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            order.setTypeface(Typeface.DEFAULT);

            order.setTextColor(this.getResources().getColor(R.color.black));

        }

        if (model.getStatus() == 1) {
            //  status.setText("Active");
            status.setTextColor(this.getResources().getColor(R.color.green_color_picker));
            background.setBackgroundColor(this.getResources().getColor(R.color.green_color_picker));
            statusview.setBackgroundColor(this.getResources().getColor(R.color.green_color_picker));


        } else if (model.getStatus() == 0) {

            //  status.setText("Deleted");
            status.setTextColor(this.getResources().getColor(R.color.red));
            background.setBackgroundColor(this.getResources().getColor(R.color.red));
            statusview.setBackgroundColor(this.getResources().getColor(R.color.red));

        } else {
            // status.setText("In-Active");
            status.setTextColor(this.getResources().getColor(R.color.blue_color_picker));
            background.setBackgroundColor(this.getResources().getColor(R.color.blue_color_picker));
            statusview.setBackgroundColor(this.getResources().getColor(R.color.blue_color_picker));

        }

        loadCollections("" + model.getPayoutNumber(), model.getFarmercode());


        payoutsVewModel.getSumOfMilkForPayout(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> {
            milk.setText(String.valueOf(integer));
            setBalance();
        });
        payoutsVewModel.getSumOfLoansForPayout(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> {
            loan.setText(String.valueOf(integer));
            setBalance();
        });
        payoutsVewModel.getSumOfOrdersForPayout(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> {
            order.setText(String.valueOf(integer));
            setBalance();
        });


        if (model.getStatus() == 0 && (DateTimeUtils.Companion.getToday().equals(payouts.getEndDate())
                || DateTimeUtils.Companion.isPastLastDay(payouts.getEndDate()))) {
            btnApprove.setVisibility(View.VISIBLE);

        } else {
            btnApprove.setVisibility(View.GONE);
        }
        btnApprove.setOnClickListener(view -> approve(payouts, model));
//        payoutsVewModel.getStatusForFarmerPayout(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> {
//            if(integer!=null) {
//                if (integer == 1) {
//                    //  status.setText("Active");
//                    status.setText("Approved");
//                    status.setTextColor(PayCard.this.getResources().getColor(R.color.green_color_picker));
//                    background.setBackgroundColor(PayCard.this.getResources().getColor(R.color.green_color_picker));
//                    statusview.setBackgroundColor(PayCard.this.getResources().getColor(R.color.green_color_picker));
//
//
//                } else if (integer == 0) {
//                    status.setText("Pending");
//
//                    //  status.setText("Deleted");
//                    status.setTextColor(PayCard.this.getResources().getColor(R.color.red));
//                    background.setBackgroundColor(PayCard.this.getResources().getColor(R.color.red));
//                    statusview.setBackgroundColor(PayCard.this.getResources().getColor(R.color.red));
//
//                } else {
//                    status.setText("Approved");
//
//                    // status.setText("In-Active");
//                    status.setTextColor(PayCard.this.getResources().getColor(R.color.blue_color_picker));
//                    background.setBackgroundColor(PayCard.this.getResources().getColor(R.color.blue_color_picker));
//                    statusview.setBackgroundColor(PayCard.this.getResources().getColor(R.color.blue_color_picker));
//
//                }
//            }
//        });
//

    }

    @Override
    public void onBackPressed() {


        super.onBackPressed();


    }

    public void initList() {
        recyclerView = findViewById(R.id.recyclerView);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        if (dayCollectionModels == null) {
            dayCollectionModels = new LinkedList<>();
        }
        listAdapter = new FarmerCollectionsAdapter(this, dayCollectionModels, new AdvancedOnclickRecyclerListener() {
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

            @Override
            public void onEditTextChanged(int adapterPosition, int time, int type, View editable) {
                editValue(adapterPosition, time, type, editable, dayCollectionModels.get(adapterPosition));


            }

        });
        recyclerView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();


    }

    private void updateOrder(Editable editable, int adapterPosition, int time) {

        if (time == 1) {
            liveModel.get(adapterPosition).setOrderAm(editable.toString());

        } else {
            liveModel.get(adapterPosition).setOrderPm(editable.toString());

        }
    }

    private void updateLoan(Editable editable, int adapterPosition, int time) {
        if (time == 1) {
            liveModel.get(adapterPosition).setLoanAm(editable.toString());

        } else {
            liveModel.get(adapterPosition).setLoanPm(editable.toString());

        }

    }

    private void updateMilk(Editable editable, int adapterPosition, int time) {
        if (time == 1) {
            liveModel.get(adapterPosition).setMilkAm(editable.toString());

//            payoutsVewModel.getCollectionById(dayCollectionModels.get(adapterPosition).getCollectionIdAm()).observe(this, new Observer<Collection>() {
//                @Override
//                public void onChanged(@Nullable Collection collection) {
//                    if (collection != null) {
//                        c=collection;
//                        c.setMilkCollected(editable.toString());
//                        payoutsVewModel.updateCollection(c);
//                    }
//                }
//            });
        } else {
            liveModel.get(adapterPosition).setMilkPm(editable.toString());
//            payoutsVewModel.getCollectionById(dayCollectionModels.get(adapterPosition).getCollectionIdPm()).observe(this, new Observer<Collection>() {
//                @Override
//                public void onChanged(@Nullable Collection collection) {
//                    if (collection != null) {
//                        c=collection;
//                        c.setMilkCollected(editable.toString());
//                        payoutsVewModel.updateCollection(c);
//                    }
//                }
//            });
        }

    }

    public void editValue(int adapterPosition, int time, int type, View editable, DayCollectionModel dayCollectionModel) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_edit_collection, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(this));
        alertDialogBuilderUserInput.setView(mView);
//        alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
//        alertDialogBuilderUserInput.setTitle("Route");


        avi = mView.findViewById(R.id.avi);
        TextInputEditText edtVL = mView.findViewById(R.id.edt_value);


        try {
            if (!((TextView) editable).getText().toString().equals("0.0")) {
                edtVL.setText(((TextView) editable).getText().toString());
                edtVL.setSelection(edtVL.getText().length());
            }
        } catch (Exception nm) {
            nm.printStackTrace();
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
//        theButton.setOnClickListener(new CustomListener(alertDialogAndroid));


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
        txtTitle.setText("Route");

        btnPositive.setOnClickListener(view -> {
            if (TextUtils.isEmpty(edtVL.getText().toString())) {
                updateCollection("0.0", adapterPosition, time, type, dayCollectionModel, alertDialogAndroid);
            }
            updateCollection(edtVL.getText().toString(), adapterPosition, time, type, dayCollectionModel, alertDialogAndroid);

        });
        btnNeutral.setOnClickListener(view -> {

        });
        btnNegative.setOnClickListener(view -> alertDialogAndroid.dismiss());

    }

    private void updateCollection(String s, int adapterPosition, int time, int type, DayCollectionModel dayCollectionModel, AlertDialog a) {

        Collection collection = null;

        if (time == 1) {
            if (dayCollectionModel.getCollectionIdAm() != 0) {
                collection = payoutsVewModel.getCollectionByIdOne(dayCollectionModel.getCollectionIdAm());
            } else {
                Collection c = new Collection();
                c.setCycleCode(payouts.getCycleCode());
                c.setFarmerCode(payoutfarmermodel.getFarmercode());
                c.setFarmerName(payoutfarmermodel.getFarmername());
                c.setCycleId(payouts.getCycleCode());
                c.setDayName(dayCollectionModel.getDay());
                c.setLoanAmountGivenOutPrice(dayCollectionModel.getLoanAm());
                c.setDayDate(dayCollectionModel.getDate());
                c.setTimeOfDay("AM");
                c.setMilkCollected(dayCollectionModel.getMilkAm());
                c.setOrderGivenOutPrice(dayCollectionModel.getOrderAm());

                c.setLoanId("");
                c.setOrderId("");
                c.setSynced(0);
                c.setSynced(false);
                c.setApproved(0);

                c.setPayoutnumber(dayCollectionModel.getPayoutNumber());
                c.setCycleStartedOn(payouts.getStartDate());

                if (type == 1) {
                    c.setMilkCollected(s);

                } else if (type == 2) {
                    c.setLoanAmountGivenOutPrice(s);

                } else if (type == 3) {
                    c.setOrderGivenOutPrice(s);

                }

                payoutsVewModel.createCollections(c).observe(this, responseModel -> {
                    if (responseModel != null) {
                        a.dismiss();
                        MyToast.toast(responseModel.getResultDescription(), PayCard.this, R.drawable.ic_launcher, Toast.LENGTH_LONG);
                    }
                });
                a.dismiss();
                return;


            }

        } else {
            if (dayCollectionModel.getCollectionIdPm() != 0) {
                collection = payoutsVewModel.getCollectionByIdOne(dayCollectionModel.getCollectionIdPm());
            } else {


                Collection c = new Collection();
                c.setCycleCode(payoutfarmermodel.getCycleCode());
                c.setFarmerCode(payoutfarmermodel.getFarmercode());
                c.setFarmerName(payoutfarmermodel.getFarmername());
                c.setCycleId(payoutfarmermodel.getCycleCode());
                c.setDayName(dayCollectionModel.getDay());
                c.setLoanAmountGivenOutPrice(dayCollectionModel.getLoanPm());
                c.setDayDate(dayCollectionModel.getDate());
                c.setTimeOfDay("PM");
                c.setMilkCollected(dayCollectionModel.getMilkPm());
                c.setOrderGivenOutPrice(dayCollectionModel.getOrderPm());

                c.setLoanId("");
                c.setOrderId("");
                c.setSynced(0);
                c.setSynced(false);
                c.setApproved(0);

                c.setPayoutnumber(dayCollectionModel.getPayoutNumber());
                c.setCycleStartedOn(payouts.getStartDate());


                if (type == 1) {
                    c.setMilkCollected(s);

                } else if (type == 2) {
                    c.setLoanAmountGivenOutPrice(s);

                } else if (type == 3) {
                    c.setOrderGivenOutPrice(s);

                }

                payoutsVewModel.createCollections(c).observe(this, responseModel -> {
                    if (responseModel != null) {
                        a.dismiss();
                        MyToast.toast(responseModel.getResultDescription(), PayCard.this, R.drawable.ic_launcher, Toast.LENGTH_LONG);
                    }
                });
                a.dismiss();
                return;
            }

        }
        if (collection != null) {
            if (type == 1) {
                collection.setMilkCollected(s);
                payoutsVewModel.updateCollection(collection);
                a.dismiss();
            } else if (type == 2) {
                collection.setLoanAmountGivenOutPrice(s);
                payoutsVewModel.updateCollection(collection);
                a.dismiss();
            } else if (type == 3) {
                collection.setOrderGivenOutPrice(s);
                payoutsVewModel.updateCollection(collection);
                a.dismiss();
            }
        } else {
            Log.d("collectionche0", "Our coll is null" + dayCollectionModel.getCollectionIdAm() + "  " + dayCollectionModel.getCollectionIdPm());
        }
    }


}
