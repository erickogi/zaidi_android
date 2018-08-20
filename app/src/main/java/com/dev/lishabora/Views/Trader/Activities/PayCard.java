package com.dev.lishabora.Views.Trader.Activities;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.lishabora.Adapters.FarmerCollectionsAdapter;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.DayCollectionModel;
import com.dev.lishabora.Models.DaysDates;
import com.dev.lishabora.Models.PayoutFarmersCollectionModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Utils.AdvancedOnclickRecyclerListener;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishaboramobile.R;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class PayCard extends AppCompatActivity {
    public TextView status, id, name, balance, milk, loan, order;
    public RelativeLayout background;
    public View statusview;
    public View itemVew;
    Collection c = new Collection();
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

    private void setUpList(List<DayCollectionModel> dayCollectionModels) {
        this.dayCollectionModels = dayCollectionModels;
        this.liveModel = dayCollectionModels;
        listAdapter.refresh(dayCollectionModels);


//        listAdapter.notify();
        // initList();
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

            dayCollectionModels.add(new DayCollectionModel(payouts.getPayoutnumber(),
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
            for (Collection c : collections) {

                if (c.getDayDate().contains(date) && c.getTimeOfDay().equals("AM")) {
                    return c.getId();
                }
            }

        }
        return 0;
    }

    private int getCollectionIdPm(String date, List<Collection> collections) {
        double orderTotal = 0.0;
        if (collections != null) {
            for (Collection c : collections) {

                if (c.getDayDate().contains(date) && c.getTimeOfDay().equals("PM")) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_card);
        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);

        initList();
        statusview = findViewById(R.id.status_view);
        background = findViewById(R.id.background);
        save = findViewById(R.id.save_btn);
        save.setVisibility(View.GONE);
        save.setOnClickListener(view -> {

            update(liveModel);
        });


        status = findViewById(R.id.txt_status);
        id = findViewById(R.id.txt_id);
        name = findViewById(R.id.txt_name);
        balance = findViewById(R.id.txt_balance);


        milk = findViewById(R.id.txt_milk);
        loan = findViewById(R.id.txt_loans);
        order = findViewById(R.id.txt_orders);
        PayoutFarmersCollectionModel model = (PayoutFarmersCollectionModel) getIntent().getSerializableExtra("data");

        payoutsVewModel.getSumOfMilkForPayout(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> milk.setText(String.valueOf(integer)));
        payoutsVewModel.getSumOfLoansForPayout(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> loan.setText(String.valueOf(integer)));
        payoutsVewModel.getSumOfOrdersForPayout(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> order.setText(String.valueOf(integer)));
        setData(model);

    }

    private void update(List<DayCollectionModel> liveModel) {
        if (liveModel != null && liveModel.size() > 0) {
            for (DayCollectionModel dayCollectionModel : liveModel) {
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

            }
            for (DayCollectionModel dayCollectionModel1 : liveModel) {
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

            }
        }
        save.setVisibility(View.GONE);

    }

    private void setData(PayoutFarmersCollectionModel model) {
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
            public void onEditTextChanged(int adapterPosition, int time, int type, Editable editable) {
                if (editable != null && editable.length() > 0) {


                    save.setVisibility(View.VISIBLE);
                    switch (type) {
                        case 1:
                            updateMilk(editable, adapterPosition, time);
                            break;
                        case 2:
                            updateLoan(editable, adapterPosition, time);
                            break;
                        case 3:
                            updateOrder(editable, adapterPosition, time);
                            break;
                        default:
                    }


                }


            }

        });
        recyclerView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();


    }

    private void updateOrder(Editable editable, int adapterPosition, int time) {

        if (time == 1) {
            liveModel.get(adapterPosition).setOrderAm(editable.toString());
//            payoutsVewModel.getCollectionById(dayCollectionModels.get(adapterPosition).getCollectionIdAm()).observe(this, new Observer<Collection>() {
//                @Override
//                public void onChanged(@Nullable Collection collection) {
//                    if (collection != null) {
//                        c=collection;
//                        c.setOrderGivenOutPrice(editable.toString());
//                        payoutsVewModel.updateCollection(c);
//                    }
//                }
//            });
        } else {
            liveModel.get(adapterPosition).setOrderPm(editable.toString());
//            payoutsVewModel.getCollectionById(dayCollectionModels.get(adapterPosition).getCollectionIdPm()).observe(this, new Observer<Collection>() {
//                @Override
//                public void onChanged(@Nullable Collection collection) {
//                    if (collection != null) {
//                        c=collection;
//                        c.setOrderGivenOutPrice(editable.toString());
//                        payoutsVewModel.updateCollection(c);
//                    }
//                }
//            });
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


}
