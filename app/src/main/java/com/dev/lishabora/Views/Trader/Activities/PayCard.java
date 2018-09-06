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
import android.text.TextUtils;
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
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.LoanModel;
import com.dev.lishabora.Models.MilkModel;
import com.dev.lishabora.Models.OrderModel;
import com.dev.lishabora.Models.PayoutFarmersCollectionModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Utils.AdvancedOnclickRecyclerListener;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

import static com.dev.lishabora.Views.CommonFuncs.getBalance;
import static com.dev.lishabora.Views.CommonFuncs.getCollectionIdAm;
import static com.dev.lishabora.Views.CommonFuncs.getCollectionIdPm;

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
    private List<DayCollectionModel> dayCollectionModels;
    private MaterialButton btnApprove, btnBack;
    private TextView txtApprovalStatus;

    private LinearLayout empty_layout;
    private Payouts payouts;
    private PayoutsVewModel payoutsVewModel;

    private List<Collection> collections;
    private boolean firstDone = false;
    private List<DayCollectionModel> liveModel;
    BottomSheetBehavior sheetBehavior;
    private AVLoadingIndicatorView avi;
    private SearchView searchView;
    Double milkKsh = 0.0;
    private FamerModel famerModel;




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
        payoutsVewModel.getPayoutsByPayoutNumber(payout).observe(this, payouts -> {
            if (payouts != null) {
                setUpDayCollectionsModel(payouts, collections);
            }
        });

    }

    private String filterText = "";

    private void setUpList(List<DayCollectionModel> dayCollectionModels) {
        this.dayCollectionModels = dayCollectionModels;
        this.liveModel = dayCollectionModels;

        listAdapter.refresh(dayCollectionModels, payoutfarmermodel.getStatus() == 0);



    }

    private void setUpDayCollectionsModel(Payouts payouts, List<Collection> collections) {


        List<DaysDates> daysDates = DateTimeUtils.Companion.getDaysAndDatesBtnDates(payouts.getStartDate(), payouts.getEndDate());

        List<DayCollectionModel> dayCollectionModels = new LinkedList<>();
        for (DaysDates d : daysDates) {


            MilkModel milkModelAm = CommonFuncs.getMilk(d.getDate(), "AM", collections);
            MilkModel milkModelPm = CommonFuncs.getMilk(d.getDate(), "PM", collections);


            String milkAm = milkModelAm.getUnitQty();
            String milkPm = milkModelPm.getUnitQty();

            LoanModel loanModelAm = CommonFuncs.getLoan(d.getDate(), "AM", collections);
            LoanModel loanModelPm = CommonFuncs.getLoan(d.getDate(), "PM", collections);


            String loanAm = loanModelAm.getLoanAmount();
            String laonPm = loanModelPm.getLoanAmount();

            OrderModel orderModelAm = CommonFuncs.getOrder(d.getDate(), "AM", collections);
            OrderModel orderModelPm = CommonFuncs.getOrder(d.getDate(), "PM", collections);


            String orderAm = orderModelAm.getOrderAmount();
            String orderPm = orderModelPm.getOrderAmount();


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
                            orderPm,
                            collectionIdAm,
                            collectionIdPm,
                            milkModelAm, loanModelAm, orderModelAm,
                            milkModelPm, loanModelPm, orderModelPm

                    )

            );







        }

        setUpList(dayCollectionModels);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_card);
        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);
        famerModel = (FamerModel) getIntent().getSerializableExtra("farmer");


        initList();
        btnApprove = findViewById(R.id.btn_approve);
        btnApprove.setVisibility(View.GONE);
        txtApprovalStatus = findViewById(R.id.txt_approval_status);


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

        if (data != null && !data.equals("null")) {

            Gson gson = new Gson();
            Type listType = new TypeToken<LinkedList<PayoutFarmersCollectionModel>>() {
            }.getType();
            setBottom();
            initBottomList(gson.fromJson(data, listType));

        } else {
            layoutBottomSheet.setVisibility(View.GONE);
        }



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
                payoutsVewModel.getFarmerByCode(models.get(position).getFarmercode()).observe(PayCard.this, famerModel -> {
                    if (famerModel != null) {
                        PayCard.this.famerModel = famerModel;
                        setData(models.get(position));
                    } else {
                        Timber.tag("farmerCilcked").d("clicked " + position + "  eRROR Farmern Not found");

                    }

                });




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


    private void setBalance(Double milkKsh) {
        balance.setText(getBalance(String.valueOf(milkKsh), loan.getText().toString(), order.getText().toString()));
    }

    private void setData(PayoutFarmersCollectionModel model) {
        this.payoutfarmermodel = model;
        //  balance.setText("" + model.getBalance());

        id.setText(model.getFarmercode());
        name.setText(model.getFarmername());
        status.setText(model.getStatusName());

        setTitle("" + model.getFarmername() + "        ID " + model.getFarmercode());


        milk.setText(String.format("%s %s", model.getMilktotal(), this.getString(R.string.ltrs)));
        if (!model.getMilktotal().equals("0.0")) {
            milk.setTextColor(this.getResources().getColor(R.color.colorPrimary));
            milk.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            milk.setTypeface(Typeface.DEFAULT);

            milk.setTextColor(this.getResources().getColor(R.color.black));

        }


        loan.setText(String.format("%s %s", model.getLoanTotal(), this.getString(R.string.ksh)));
        if (!model.getLoanTotal().equals("0.0")) {
            loan.setTextColor(this.getResources().getColor(R.color.colorPrimary));
            loan.setTypeface(Typeface.DEFAULT_BOLD);

        } else {
            loan.setTypeface(Typeface.DEFAULT);

            loan.setTextColor(this.getResources().getColor(R.color.black));

        }

        order.setText(String.format("%s %s", model.getOrderTotal(), this.getString(R.string.ksh)));
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


        payoutsVewModel.getSumOfMilkForPayoutLtrs(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> {
            milk.setText(String.valueOf(integer));
            setBalance(milkKsh);
        });
        payoutsVewModel.getSumOfMilkForPayoutKsh(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> {
            milkKsh = integer;
            setBalance(milkKsh);
        });
        payoutsVewModel.getSumOfLoansForPayout(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> {
            loan.setText(String.valueOf(integer));
            setBalance(milkKsh);
        });
        payoutsVewModel.getSumOfOrdersForPayout(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> {
            order.setText(String.valueOf(integer));
            setBalance(milkKsh);
        });


        btnApprove.setOnClickListener(view -> approve(payouts, model));


        if (model.getStatus() == 0 && (DateTimeUtils.Companion.getToday().equals(payouts.getEndDate())
                || DateTimeUtils.Companion.isPastLastDay(payouts.getEndDate()))) {
            btnApprove.setVisibility(View.VISIBLE);
            txtApprovalStatus.setVisibility(View.GONE);
            btnBack.setVisibility(View.GONE);


        } else if (model.getStatus() == 1) {
            txtApprovalStatus.setText("Approved");
            txtApprovalStatus.setVisibility(View.VISIBLE);
            txtApprovalStatus.setTextColor(this.getResources().getColor(R.color.colorPrimary));


            if (payouts.getStatus() == 0) {
                btnApprove.setVisibility(View.GONE);
                btnBack.setVisibility(View.VISIBLE);
                btnBack.setText("Cancel Approval");
                txtApprovalStatus.setVisibility(View.VISIBLE);

            } else {
                btnBack.setVisibility(View.GONE);
                btnApprove.setVisibility(View.GONE);
                txtApprovalStatus.setText("Approved");
                txtApprovalStatus.setTextColor(this.getResources().getColor(R.color.colorPrimary));

                txtApprovalStatus.setVisibility(View.VISIBLE);
            }
        } else if (model.getStatus() == 0 && (!DateTimeUtils.Companion.getToday().equals(payouts.getEndDate())
                || !DateTimeUtils.Companion.isPastLastDay(payouts.getEndDate()))) {
            txtApprovalStatus.setText("Pending");
            txtApprovalStatus.setTextColor(this.getResources().getColor(R.color.red));

            txtApprovalStatus.setVisibility(View.VISIBLE);
            btnBack.setVisibility(View.GONE);
            btnApprove.setVisibility(View.GONE);


        }


    }

    public void editValue(int adapterPosition, int time, int type, View editable, DayCollectionModel dayCollectionModel) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_edit_collection, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(this));
        alertDialogBuilderUserInput.setView(mView);


        avi = mView.findViewById(R.id.avi);
        TextInputEditText edtVL = mView.findViewById(R.id.edt_value);
        TextView txt = mView.findViewById(R.id.txt_desc);

        String ti = "";
        if (time == 1) {
            ti = " AM";
        } else {
            ti = " PM";
        }
        String tp = "";
        if (type == 1) {
            tp = " Milk collection";
        } else if (type == 2) {
            tp = " Loan";
        } else {
            tp = " Order ";
        }
        txt.setText(" Editing " + tp + "  For  " + dayCollectionModel.getDate() + "  " + ti);



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
        Collection ctoUpdate;


        if (time == 1) {
            if (dayCollectionModel.getCollectionIdAm() != 0) {
                collection = payoutsVewModel.getCollectionByIdOne(dayCollectionModel.getCollectionIdAm());
                ctoUpdate = CommonFuncs.updateCollection(s, time, type, dayCollectionModel, collection, payouts, famerModel);






            } else {
                Collection c = new Collection();
                c = CommonFuncs.updateCollection(s, time, type, dayCollectionModel, null, payouts, famerModel);
                //                c.setCycleCode(payouts.getCycleCode());
//                c.setFarmerCode(payoutfarmermodel.getFarmercode());
//                c.setFarmerName(payoutfarmermodel.getFarmername());
//                c.setCycleId(payouts.getCycleCode());
//                c.setDayName(dayCollectionModel.getDay());
//                c.setLoanAmountGivenOutPrice(dayCollectionModel.getLoanAm());
//                c.setDayDate(dayCollectionModel.getDate());
//                c.setTimeOfDay("AM");
//                c.setMilkCollected(dayCollectionModel.getMilkAm());
//                c.setOrderGivenOutPrice(dayCollectionModel.getOrderAm());
//
//                c.setLoanId("");
//                c.setOrderId("");
//                c.setSynced(0);
//                c.setSynced(false);
//                c.setApproved(0);
//
//                c.setPayoutnumber(dayCollectionModel.getPayoutNumber());
//                c.setCycleStartedOn(payouts.getStartDate());
//
//                if (type == 1) {
//                    c.setMilkCollected(s);
//
//                } else if (type == 2) {
//                    c.setLoanAmountGivenOutPrice(s);
//
//                } else if (type == 3) {
//                    c.setOrderGivenOutPrice(s);
//
//                }
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
                ctoUpdate = CommonFuncs.updateCollection(s, time, type, dayCollectionModel, collection, payouts, famerModel);

            } else {


                Collection c = new Collection();
                c = CommonFuncs.updateCollection(s, time, type, dayCollectionModel, null, payouts, famerModel);

                //                c.setCycleCode(payoutfarmermodel.getCycleCode());
//                c.setFarmerCode(payoutfarmermodel.getFarmercode());
//                c.setFarmerName(payoutfarmermodel.getFarmername());
//                c.setCycleId(payoutfarmermodel.getCycleCode());
//                c.setDayName(dayCollectionModel.getDay());
//                c.setLoanAmountGivenOutPrice(dayCollectionModel.getLoanPm());
//                c.setDayDate(dayCollectionModel.getDate());
//                c.setTimeOfDay("PM");
//                c.setMilkCollected(dayCollectionModel.getMilkPm());
//                c.setOrderGivenOutPrice(dayCollectionModel.getOrderPm());
//
//                c.setLoanId("");
//                c.setOrderId("");
//                c.setSynced(0);
//                c.setSynced(false);
//                c.setApproved(0);
//
//                c.setPayoutnumber(dayCollectionModel.getPayoutNumber());
//                c.setCycleStartedOn(payouts.getStartDate());
//
//
//                if (type == 1) {
//                    c.setMilkCollected(s);
//
//                } else if (type == 2) {
//                    c.setLoanAmountGivenOutPrice(s);
//
//                } else if (type == 3) {
//                    c.setOrderGivenOutPrice(s);
//
//                }
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
            //            if (type == 1) {
//                MilkModel milkModel;
//                milkModel = time == 1 ? dayCollectionModel.getMilkModelAm() : dayCollectionModel.getMilkModelPm();
//                milkModel.setUnitQty(s);
//                collection.setMilkCollected(s);
//                collection.setMilkCollectedValueKsh(milkModel.getValueKsh());
//                collection.setMilkCollectedValueLtrs(milkModel.getValueLtrs());
//                collection.setMilkDetails(new Gson().toJson(milkModel));
//                payoutsVewModel.updateCollection(collection);
//                a.dismiss();
//            } else if (type == 2) {
//                LoanModel loanModel;
//                loanModel = time == 1 ? dayCollectionModel.getLoanModelAm() : dayCollectionModel.getLoanModelPm();
//                loanModel.setLoanAmount(s);
//                collection.setLoanAmountGivenOutPrice(s);
//                collection.setLoanDetails(new Gson().toJson(loanModel));
//                payoutsVewModel.updateCollection(collection);
//                a.dismiss();
//            } else if (type == 3) {
//                OrderModel orderModel;
//                orderModel = time == 1 ? dayCollectionModel.getOrderModelAm() : dayCollectionModel.getOrderModelPm();
//                orderModel.setOrderAmount(s);
//
//
//
//                collection.setOrderGivenOutPrice(s);
//                collection.setLoanDetails(new Gson().toJson(orderModel));
//
            payoutsVewModel.updateCollection(ctoUpdate);
            a.dismiss();
            //}
        } else {
            Timber.d("Our coll is null" + dayCollectionModel.getCollectionIdAm() + "  " + dayCollectionModel.getCollectionIdPm());
        }

//        if (collection != null) {
//            if (type == 1) {
//                collection.setMilkCollected(s);
//                payoutsVewModel.updateCollection(collection);
//                a.dismiss();
//            } else if (type == 2) {
//                collection.setLoanAmountGivenOutPrice(s);
//                payoutsVewModel.updateCollection(collection);
//                a.dismiss();
//            } else if (type == 3) {
//                collection.setOrderGivenOutPrice(s);
//                payoutsVewModel.updateCollection(collection);
//                a.dismiss();
//            }
//        } else {
//            Log.d("collectionche0", "Our coll is null" + dayCollectionModel.getCollectionIdAm() + "  " + dayCollectionModel.getCollectionIdPm());
//        }
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

        }, false);


        recyclerView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();


    }


    @Override
    public void onBackPressed() {


        super.onBackPressed();


    }




}
