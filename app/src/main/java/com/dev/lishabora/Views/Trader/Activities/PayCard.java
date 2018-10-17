package com.dev.lishabora.Views.Trader.Activities;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishabora.Adapters.FarmerCollectionsAdapter;
import com.dev.lishabora.Adapters.PayoutFarmersAdapter;
import com.dev.lishabora.AppConstants;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.DayCollectionModel;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.LoanModel;
import com.dev.lishabora.Models.OrderModel;
import com.dev.lishabora.Models.PayoutFarmersCollectionModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Models.Trader.FarmerLoansTable;
import com.dev.lishabora.Models.Trader.FarmerOrdersTable;
import com.dev.lishabora.Models.Trader.LoanPayments;
import com.dev.lishabora.Models.Trader.OrderPayments;
import com.dev.lishabora.Utils.AdvancedOnclickRecyclerListener;
import com.dev.lishabora.Utils.ApproveFarmerPayCardListener;
import com.dev.lishabora.Utils.CollectionCreateUpdateListener;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.Utils.OnclickRecyclerListener;
import com.dev.lishabora.ViewModels.Trader.BalncesViewModel;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishabora.Views.Trader.MilkCardToolBarUI;
import com.dev.lishabora.Views.Trader.OrderConstants;
import com.dev.lishaboramobile.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;

import static com.dev.lishabora.Views.CommonFuncs.getBalance;
import static com.dev.lishabora.Views.CommonFuncs.setCardActionStatus;

public class PayCard extends AppCompatActivity implements ApproveFarmerPayCardListener {
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
    private TraderViewModel traderViewModel;
    private BalncesViewModel balncesViewModel;

    private List<Collection> collections;
    private boolean firstDone = false;
    private List<DayCollectionModel> liveModel;
    BottomSheetBehavior sheetBehavior;
    private AVLoadingIndicatorView avi;
    private SearchView searchView;
    Double milkKsh = 0.0;
    private FamerModel famerModel;
    private MilkCardToolBarUI toolBar;
    PayoutFarmersCollectionModel model;
    double remaining = 0.0;
    double remainingOrderInstall = 0.0;


    private void loadCollections(String payoutCode, String farmerCode) {
        payoutsVewModel.getCollectionByDateByPayoutByFarmer(payoutCode, farmerCode).observe(this, collections -> {
            if (collections != null) {
                PayCard.this.collections = collections;
                getPayout(payoutCode, collections);
            }
        });
    }

    private void getPayout(String payoutCode, List<Collection> collections) {
        payoutsVewModel.getPayoutsByPayoutCode(payoutCode).observe(this, payouts -> {
            if (payouts != null) {
                setUpDayCollectionsModel(payouts, collections);
            }
        });

    }

    private void setUpList(List<DayCollectionModel> dayCollectionModels) {
        this.dayCollectionModels = dayCollectionModels;
        this.liveModel = dayCollectionModels;

        listAdapter.refresh(dayCollectionModels, payoutfarmermodel.getCardstatus() == 0);



    }

    private void setUpDayCollectionsModel(Payouts payouts, List<Collection> collections) {
        setUpList(CommonFuncs.setUpDayCollectionsModel(payouts, collections));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_card);
        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);
        traderViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);
        balncesViewModel = ViewModelProviders.of(this).get(BalncesViewModel.class);
        famerModel = (FamerModel) getIntent().getSerializableExtra("farmer");


        initList();
        toolBar = findViewById(R.id.toolbar);

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


//        status = findViewById(R.id.txt_status);
//        id = findViewById(R.id.txt_id);
//        name = findViewById(R.id.txt_name);
//        balance = findViewById(R.id.txt_balance);
//        searchView = findViewById(R.id.search);
//
//
//        milk = findViewById(R.id.txt_milk);
//        loan = findViewById(R.id.txt_loans);
//        order = findViewById(R.id.txt_orders);
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

    private void cancelApprove(Payouts payouts, PayoutFarmersCollectionModel model) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PayCard.this);
        alertDialog.setMessage("Confirm that you wish to cancel " + model.getFarmername() + "'s " + payouts.getCyclename() + " Collection card").setCancelable(false).setTitle("Cancel " + model.getFarmername() + " Card");


        alertDialog.setPositiveButton("Yes", (dialogInterface, i) -> {

            payoutsVewModel.cancelFarmersPayoutCard(model.getFarmercode(), model.getPayoutCode());
            model.setCardstatus(0);
            model.setStatusName("Canceled");
            setData(model);


            dialogInterface.dismiss();

        }).setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());

        AlertDialog alertDialogAndroid = alertDialog.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.show();
    }
    private void approve(Payouts payouts, PayoutFarmersCollectionModel model) {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PayCard.this);
//        alertDialog.setMessage("Confirm that you wish to approve " + model.getFarmername() + "'s " + payouts.getCyclename() + " Collection card").setCancelable(false).setTitle("Approve " + model.getFarmername() + " Card");
//
//
//        alertDialog.setPositiveButton("Yes", (dialogInterface, i) -> {
//
//            payoutsVewModel.approveFarmersPayoutCard(model.getFarmercode(), model.getPayoutNumber());
//            model.setCardstatus(1);
//            model.setStatusName("Approved");
//            setData(model);
//            dialogInterface.dismiss();
//
//
//        }).setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
//
//        AlertDialog alertDialogAndroid = alertDialog.create();
//        alertDialogAndroid.setCancelable(false);
//        alertDialogAndroid.show();
        CommonFuncs.doAprove(PayCard.this, balncesViewModel, traderViewModel, model, famerModel, payouts, this);


    }

    public void initBottomList(List<PayoutFarmersCollectionModel> models) {

        RecyclerView recyclerView = findViewById(R.id.recyclerViewbottom);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        PayoutFarmersAdapter listAdapter = new PayoutFarmersAdapter(this, models, new OnclickRecyclerListener() {
            @Override
            public void onMenuItem(int position, int menuItem) {

            }

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

    }

    private void setBottom() {
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setFitToContents(true);
        // sheetBehavior.setPeekHeight(300);
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

    private void setBalance(Double milkKsh) {
        toolBar.show(getBalance(String.valueOf(milkKsh), toolBar.getLoanTotal(), toolBar.getOrderTotal()));
    }

    private void setData(PayoutFarmersCollectionModel model) {

        this.model = model;

        this.payoutfarmermodel = model;

        boolean isApproved = false;
        boolean isPast = false;
        if (model.getCardstatus() == 1) {
            isApproved = true;
        } else if (model.getCardstatus() == 0) {

            isApproved = false;

        }

        toolBar.show(model.getMilktotal(), model.getLoanTotal(), model.getOrderTotal(), "", famerModel, payouts, isApproved, isPast);


        loadCollections("" + model.getPayoutCode(), model.getFarmercode());


//
        payoutsVewModel.getSumOfMilkForPayoutLtrs(model.getFarmercode(), model.getPayoutCode()).observe(this, integer -> {
            //milk.setText(String.valueOf(integer));
            toolBar.updateMilk(String.valueOf(integer));
            setBalance(milkKsh);
        });
        payoutsVewModel.getSumOfMilkForPayoutKsh(model.getFarmercode(), model.getPayoutCode()).observe(this, integer -> {
            milkKsh = integer;
            setBalance(milkKsh);
        });
//        payoutsVewModel.getSumOfLoansForPayout(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> {
//            //loan.setText(String.valueOf(integer));
//            toolBar.updateLoan(String.valueOf(integer));
//
//            setBalance(milkKsh);
//        });
//        payoutsVewModel.getSumOfOrdersForPayout(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> {
//            //order.setText(String.valueOf(integer));
//            toolBar.updateOrder(String.valueOf(integer));
//
//            setBalance(milkKsh);
//        });


        balncesViewModel.getFarmerLoanByFarmer(model.getFarmercode()).observe(this, farmerLoansTables -> {
            if (farmerLoansTables != null) {

                toolBar.updateLoan(CommonFuncs.getCardLoan(farmerLoansTables));
                setBalance(milkKsh);
            }
        });

        balncesViewModel.getFarmerOrderByFarmer(model.getFarmercode()).observe(this, farmerOrderTables -> {
            if (farmerOrderTables != null) {

                toolBar.updateOrder(CommonFuncs.getCardOrder(farmerOrderTables));
                setBalance(milkKsh);
            }
        });


        btnApprove.setOnClickListener(view -> approve(payouts, model));
        btnBack.setOnClickListener(view1 -> cancelApprove(payouts, model));
        setCardActionStatus(model, PayCard.this, btnApprove, btnBack, txtApprovalStatus);







    }

    public void editValue(boolean isEditable, int adapterPosition, int time, int type, String value, Object o, View editable, DayCollectionModel dayCollectionModel) {


        if (type == 1) {
            CommonFuncs.editValueMilk(isEditable, adapterPosition, time, type, value, o, dayCollectionModel, PayCard.this, avi, famerModel, (s, adapterPosition1, time1, type1, dayCollectionModel1, a) -> PayCard.this.updateCollectionValue(s, time, type, dayCollectionModel1, a, null, null));

        } else if (type == 2) {
            CommonFuncs.editValueLoan(isEditable, dayCollectionModel, PayCard.this, famerModel, (value1, loanModel, time12, dayCollectionModel12, alertDialogAndroid) -> PayCard.this.updateCollectionValue(value1, 0, type, dayCollectionModel12, alertDialogAndroid, loanModel, null));

        } else {
            OrderConstants.setFamerModel(famerModel);
            Intent intent2 = new Intent(PayCard.this, EditOrder.class);
            intent2.putExtra("farmer", famerModel);
            intent2.putExtra("dayCollection", dayCollectionModel);
            intent2.putExtra("isEditable", isEditable);
            startActivityForResult(intent2, 10004);


        }
    }

    private void updateCollectionValue(String s, int time, int type, DayCollectionModel dayCollectionModel, AlertDialog a, @Nullable LoanModel loanModel, @Nullable OrderModel orderModel) {
        CommonFuncs.updateCollectionValue(s, time, type, dayCollectionModel, payoutsVewModel, payouts, famerModel, loanModel, orderModel, new CollectionCreateUpdateListener() {
            @Override
            public void createCollection(Collection c) {
                payoutsVewModel.createCollections(c).observe(PayCard.this, responseModel -> {
                    if (responseModel != null) {
                        if (a != null) {
                            a.dismiss();


                        }
                        if (type == AppConstants.MILK) {
                            CommonFuncs.addBalance(famerModel, traderViewModel, balncesViewModel, c, responseModel.getPayoutCode(), type, null, null);
                        } else if (type == AppConstants.LOAN) {
                            FarmerLoansTable f = balncesViewModel.getFarmerLoanByCollectionOne(c.getCode());

                            CommonFuncs.addBalance(famerModel, traderViewModel, balncesViewModel, c, responseModel.getPayoutCode(), type, f, null);

                        } else if (type == AppConstants.ORDER) {

                            FarmerOrdersTable f = balncesViewModel.getFarmerOrderByCollectionOne(c.getCode());

                            CommonFuncs.addBalance(famerModel, traderViewModel, balncesViewModel, c, responseModel.getPayoutCode(), type, null, f);


                        }
                        MyToast.toast(responseModel.getResultDescription(), PayCard.this, R.drawable.ic_launcher, Toast.LENGTH_LONG);
                    }
                });
                if (a != null) {
                    a.dismiss();
                }
            }

            @Override
            public void updateCollection(Collection c) {
                payoutsVewModel.updateCollection(c).observe(PayCard.this, responseModel -> {
                    if (responseModel.getResultCode() == 1) {
                        if (a != null) {
                            a.dismiss();
                        }
                        if (type == AppConstants.MILK) {
                            CommonFuncs.addBalance(famerModel, traderViewModel, balncesViewModel, c, responseModel.getPayoutCode(), type, null, null);
                        } else if (type == AppConstants.LOAN) {
                            FarmerLoansTable f = balncesViewModel.getFarmerLoanByCollectionOne(c.getCode());

                            CommonFuncs.addBalance(famerModel, traderViewModel, balncesViewModel, c, responseModel.getPayoutCode(), type, f, null);


                        } else if (type == AppConstants.ORDER) {

                            FarmerOrdersTable f = balncesViewModel.getFarmerOrderByCollectionOne(c.getCode());

                            CommonFuncs.addBalance(famerModel, traderViewModel, balncesViewModel, c, responseModel.getPayoutCode(), type, null, f);



                        }
                    }
                });

            }

            @Override
            public void error(String error) {
                MyToast.toast(error, PayCard.this, R.drawable.ic_launcher, Toast.LENGTH_LONG);
                if (a != null) {
                    a.dismiss();
                }
            }
        });
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
                if (dayCollectionModels.get(adapterPosition).getPayoutStatus() == 0) {
                    if (DateTimeUtils.Companion.isPastLastDay(dayCollectionModels.get(adapterPosition).getDate(), 1)) {


                        CommonFuncs.ValueObject v = CommonFuncs.getValueObjectToEditFromDayCollection(dayCollectionModels.get(adapterPosition), time, type);
                        editValue(true, adapterPosition, time, type, v.getValue(), v.getO(), editable, dayCollectionModels.get(adapterPosition));


                    } else {
                        MyToast.toast("Future collections cannot be edited", PayCard.this, R.drawable.ic_error_outline_black_24dp, Toast.LENGTH_LONG);

                        // CommonFuncs.ValueObject v = CommonFuncs.getValueObjectToEditFromDayCollection(dayCollectionModels.get(adapterPosition), time, type);
                        // editValue(false,adapterPosition, time, type, v.getValue(), v.getO(), editable, dayCollectionModels.get(adapterPosition));

                    }
                } else {
                    CommonFuncs.ValueObject v = CommonFuncs.getValueObjectToEditFromDayCollection(dayCollectionModels.get(adapterPosition), time, type);
                    editValue(false, adapterPosition, time, type, v.getValue(), v.getO(), editable, dayCollectionModels.get(adapterPosition));

                    MyToast.toast("Cards in an approved payout cannot be edited", PayCard.this, R.drawable.ic_error_outline_black_24dp, Toast.LENGTH_LONG);

                }
            }

        }, false);


        recyclerView.setAdapter(listAdapter);

        listAdapter.notifyDataSetChanged();


    }


    @Override
    public void onBackPressed() {


        super.onBackPressed();


    }

    @Override
    public void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 10004:

                if (resultCode == RESULT_OK && data != null) {
                    OrderModel orderModel;
                    DayCollectionModel dayCollectionModel;
                    orderModel = (OrderModel) data.getSerializableExtra("orderDataModel");
                    dayCollectionModel = (DayCollectionModel) data.getSerializableExtra("dayCollection");
                    updateCollectionValue(orderModel.getTotalOrderAmount(), 0, 3, dayCollectionModel, null, null, orderModel);


                }
                break;

            default:
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void insertLoanPayment(double toLoanInstallmentPayment) {
        balncesViewModel.getFarmerLoanByFarmerByStatus(famerModel.getCode(), 0).observe(PayCard.this, new Observer<List<FarmerLoansTable>>() {
            @Override
            public void onChanged(@Nullable List<FarmerLoansTable> farmerLoansTables) {
                remaining = toLoanInstallmentPayment;

                if (farmerLoansTables != null) {
                    for (int a = 0; a < farmerLoansTables.size(); a++) {


                        FarmerLoansTable farmerLoan = farmerLoansTables.get(a);
                        Double amp = Double.valueOf(farmerLoan.getLoanAmount());
                        Double inst = Double.valueOf(farmerLoan.getInstallmentAmount());
                        LoanPayments loanPayments = new LoanPayments();
                        loanPayments.setLoanCode(farmerLoan.getCode());
                        loanPayments.setPaymentMethod("Payout");
                        loanPayments.setRefNo("" + payouts.getCode());
                        loanPayments.setPayoutCode("" + payouts.getCode());
                        loanPayments.setTimeStamp(DateTimeUtils.Companion.getNow());
                        loanPayments.setCode(GeneralUtills.Companion.createCode(farmerLoan.getFarmerCode()));

                        Double valueToPay = 0.0;

                        if (inst >= remaining) {
                            valueToPay = remaining;

                            loanPayments.setAmountPaid("" + valueToPay);
                            loanPayments.setAmountRemaining(String.valueOf(amp - valueToPay));

                            remaining = 0.0;
                            balncesViewModel.insertSingleLoanPayment(loanPayments);
                            break;
                        } else {


                            valueToPay = remaining - inst;

                            loanPayments.setAmountPaid("" + valueToPay);
                            loanPayments.setAmountRemaining(String.valueOf(amp - valueToPay));
                            remaining = toLoanInstallmentPayment - inst;
                            balncesViewModel.insertSingleLoanPayment(loanPayments);


                        }

                    }
                }
            }
        });

    }

    public void insertOrderPayment(double toOrderInstallmentPayment) {
        balncesViewModel.getFarmerOrderByFarmerByStatus(famerModel.getCode(), 0).observe(PayCard.this, new Observer<List<FarmerOrdersTable>>() {
            @Override
            public void onChanged(@Nullable List<FarmerOrdersTable> farmerOrdersTables) {
                remainingOrderInstall = toOrderInstallmentPayment;

                if (farmerOrdersTables != null) {
                    for (int a = 0; a < farmerOrdersTables.size(); a++) {


                        FarmerOrdersTable farmerOrders = farmerOrdersTables.get(a);
                        Double amp = Double.valueOf(farmerOrders.getOrderAmount());
                        Double inst = Double.valueOf(farmerOrders.getInstallmentAmount());

                        OrderPayments orderPayments = new OrderPayments();
                        orderPayments.setOrderCode(farmerOrders.getCode());
                        orderPayments.setPaymentMethod("Payout");
                        orderPayments.setRefNo(payouts.getCode());
                        orderPayments.setPayoutCode(payouts.getCode());
                        orderPayments.setTimestamp(DateTimeUtils.Companion.getNow());

                        Double valueToPay = 0.0;

                        if (inst >= remainingOrderInstall) {

                            valueToPay = remainingOrderInstall;

                            orderPayments.setAmountPaid("" + valueToPay);
                            orderPayments.setAmountRemaining(String.valueOf(amp - valueToPay));


                            remainingOrderInstall = 0.0;
                            balncesViewModel.insertSingleOrderPayment(orderPayments);


                            break;
                        } else {


                            valueToPay = remainingOrderInstall - inst;

                            orderPayments.setAmountPaid("" + valueToPay);
                            orderPayments.setAmountRemaining(String.valueOf(amp - valueToPay));
                            remaining = toOrderInstallmentPayment - inst;
                            balncesViewModel.insertSingleOrderPayment(orderPayments);


                        }

                    }
                }
            }
        });

    }


    public void approveCard(PayoutFarmersCollectionModel model) {
        payoutsVewModel.approveFarmersPayoutCard(model.getFarmercode(), model.getPayoutCode());
        model.setCardstatus(1);
        model.setStatusName("Approved");
        setData(model);
    }

    @Override
    public void onApprove(PayoutFarmersCollectionModel model, Double totalKshToPay, Double toLoanInstallmentPayment, Double toOrderInstallmentPayment) {
        insertLoanPayment(toLoanInstallmentPayment);
        insertOrderPayment(toOrderInstallmentPayment);
        approveCard(model);

    }

    @Override
    public void onApprovePayLoan(PayoutFarmersCollectionModel model, Double totalKshToPay, Double toLoanInstallmentPayment) {
        insertLoanPayment(toLoanInstallmentPayment);
        approveCard(model);
    }

    @Override
    public void onApprovePayOrder(PayoutFarmersCollectionModel model, Double totalKshToPay, Double toOrderInstallmentPayment) {
        insertOrderPayment(toOrderInstallmentPayment);
        approveCard(model);
    }

    @Override
    public void onApprove(PayoutFarmersCollectionModel model, Double totalKshToPay) {
        approveCard(model);
    }

    @Override
    public void onApprove(PayoutFarmersCollectionModel model) {
        approveCard(model);
    }

    @Override
    public void onApproveError(String error) {

    }

    @Override
    public void onApproveDismiss() {


    }





}
