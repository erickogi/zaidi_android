package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.lishabora.Adapters.FarmerCollectionsAdapter;
import com.dev.lishabora.AppConstants;
import com.dev.lishabora.Application;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.Cycles;
import com.dev.lishabora.Models.DayCollectionModel;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.LoanModel;
import com.dev.lishabora.Models.OrderModel;
import com.dev.lishabora.Models.PayoutFarmersCollectionModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Models.ResponseModel;
import com.dev.lishabora.Models.Trader.FarmerLoansTable;
import com.dev.lishabora.Models.Trader.FarmerOrdersTable;
import com.dev.lishabora.Models.Trader.LoanPayments;
import com.dev.lishabora.Models.Trader.OrderPayments;
import com.dev.lishabora.Models.UnitsModel;
import com.dev.lishabora.Utils.AdvancedOnclickRecyclerListener;
import com.dev.lishabora.Utils.ApproveFarmerPayCardListener;
import com.dev.lishabora.Utils.CollectionCreateUpdateListener;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.Utils.MilkEditValueListener;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.ViewModels.Trader.BalncesViewModel;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishabora.Views.Trader.Activities.EditOrder;
import com.dev.lishabora.Views.Trader.MilkCardToolBarUI;
import com.dev.lishabora.Views.Trader.OrderConstants;
import com.dev.lishaboramobile.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.dev.lishabora.Views.CommonFuncs.getBalance;
import static com.dev.lishabora.Views.CommonFuncs.setCardActionStatus;

public class FragmentCurrentFarmerPayout extends Fragment implements ApproveFarmerPayCardListener {

    public TextView status;//, id, name, balance, milk, loan, order;
    public RelativeLayout background;
    public View statusview;
    Double milkKsh = 0.0;
    Double loanKsh = 0.0;
    Double orderKsh = 0.0;


    private FamerModel famerModel;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private PayoutsVewModel payoutsVewModel;
    private TraderViewModel traderViewModel;
    private BalncesViewModel balncesViewModel;
    private Payouts payouts;
    private FarmerCollectionsAdapter listAdapter;
    private RecyclerView recyclerView;
    private List<DayCollectionModel> dayCollectionModels;
    private MaterialButton btnApprove, btnBack;
    private TextView txtApprovalStatus;
    private List<DayCollectionModel> liveModel;
    private List<Collection> collections;
    private AVLoadingIndicatorView avi;
    private View view;
    UnitsModel u = new UnitsModel();
    private MilkCardToolBarUI toolBar;

    PayoutFarmersCollectionModel model;
    double remaining = 0.0;
    double remainingOrderInstall = 0.0;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payoutsVewModel = ViewModelProviders.of(this).get(PayoutsVewModel.class);
        traderViewModel = ViewModelProviders.of(this).get(TraderViewModel.class);
        balncesViewModel = ViewModelProviders.of(this).get(BalncesViewModel.class);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_farmer_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        toolBar = view.findViewById(R.id.toolbar);
        //  toolBar.setOnPayNoClickListener(payNoClicked);


        famerModel = (FamerModel) getArguments().getSerializable("farmer");
        btnApprove = view.findViewById(R.id.btn_approve);
        btnApprove.setVisibility(View.GONE);

        btnBack = view.findViewById(R.id.btn_back);
        txtApprovalStatus = view.findViewById(R.id.txt_approval_status);

        btnBack.setVisibility(View.GONE);

        statusview = view.findViewById(R.id.status_view);
        background = view.findViewById(R.id.background);


        status = view.findViewById(R.id.txt_status);

    }


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onStart() {
        super.onStart();

        initList();
        initData();
    }

    DayCollectionModel dayCollectionModel;

    private void setUpList(List<DayCollectionModel> dayCollectionModels) {
        this.dayCollectionModels = dayCollectionModels;
        this.liveModel = dayCollectionModels;
        listAdapter.refresh(dayCollectionModels, payouts.getStatus() == 0);


    }

    private int time, type, adp;

    private void setUpDayCollectionsModel(Payouts payouts, List<Collection> collections) {

        setUpList(CommonFuncs.setUpDayCollectionsModel(payouts, collections));

    }

    private void initData() {

        Cycles c = new Cycles();
        c.setCycle(famerModel.getCyclename());
        c.setCode(Integer.valueOf(famerModel.getCyclecode()));


        this.payouts = traderViewModel.createPayout(c, famerModel);
        if (payouts != null) {
            payoutsVewModel.getCollectionByDateByPayoutByFarmer("" + payouts.getCode(), famerModel.getCode()).observe(this, (List<Collection> collections) -> {


                FragmentCurrentFarmerPayout.this.collections = collections;
                setUpDayCollectionsModel(payouts, collections);
                setData(getFarmersCollectionModel());


            });
        }



    }

    public PayoutFarmersCollectionModel getFarmersCollectionModel() {

        return CommonFuncs.getFarmersCollectionModel(famerModel, collections, payouts);

    }
    private void cancelApprove(Payouts payouts, PayoutFarmersCollectionModel model) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
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



    private void setBalance(Double milkKsh) {
        toolBar.show(getBalance(String.valueOf(milkKsh), toolBar.getLoanTotal(), toolBar.getOrderTotal()));

    }

    private View.OnClickListener payNoClicked = view -> {

        CommonFuncs.doAprove(getContext(), balncesViewModel, traderViewModel, model, famerModel, payouts, this);

    };

    public void editValue(boolean isEditable, int adapterPosition, int time, int type, String value, Object o, View editable, DayCollectionModel dayCollectionModel) {

        if (type == 1) {
            CommonFuncs.editValueMilk(isEditable, adapterPosition, time, type, value, o, dayCollectionModel, getContext(), avi, famerModel, new MilkEditValueListener() {
                @Override
                public void updateCollection(String s, int adapterPosition1, int time1, int type1, DayCollectionModel dayCollectionModel1, AlertDialog a) {
                    FragmentCurrentFarmerPayout.this.updateCollectionValue(s, time, type, dayCollectionModel1, a, null, null);
                }
            });

        } else if (type == 2) {
            CommonFuncs.editValueLoan(isEditable, dayCollectionModel, getContext(), famerModel, (value1, loanModel, time12, dayCollectionModel12, alertDialogAndroid) -> FragmentCurrentFarmerPayout.this.updateCollectionValue(value1, 0, type, dayCollectionModel12, alertDialogAndroid, loanModel, null));

        } else {


            OrderConstants.setFamerModel(famerModel);
            Intent intent2 = new Intent(getActivity(), EditOrder.class);
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


                payoutsVewModel.createCollections(c).observe(FragmentCurrentFarmerPayout.this, responseModel -> {


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
                        MyToast.toast(responseModel.getResultDescription(), getContext(), R.drawable.ic_launcher, Toast.LENGTH_LONG);
                    }
                });
                if (a != null) {
                    a.dismiss();
                }
            }

            @Override
            public void updateCollection(Collection c) {
                payoutsVewModel.updateCollection(c).observe(FragmentCurrentFarmerPayout.this, new Observer<ResponseModel>() {
                    @Override
                    public void onChanged(@Nullable ResponseModel responseModel) {
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
                            MyToast.toast("Collection updated", getContext(), R.drawable.ic_launcher, Toast.LENGTH_LONG);

                        }
                    }
                });

            }

            @Override
            public void error(String error) {
                MyToast.toast(error, getContext(), R.drawable.ic_launcher, Toast.LENGTH_LONG);
                if (a != null) {
                    a.dismiss();
                }
            }
        });
    }

    public void initList() {
        recyclerView = view.findViewById(R.id.recyclerView);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        if (dayCollectionModels == null) {
            dayCollectionModels = new LinkedList<>();
        }
        listAdapter = new FarmerCollectionsAdapter(getContext(), dayCollectionModels, new AdvancedOnclickRecyclerListener() {
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

//                CommonFuncs.ValueObject v = CommonFuncs.getValueObjectToEditFromDayCollection(dayCollectionModels.get(adapterPosition), time, type);
//                editValue(adapterPosition, time, type, v.getValue(), v.getO(), editable, dayCollectionModels.get(adapterPosition));


                if (Application.isTimeAutomatic()) {
                    if (dayCollectionModels.get(adapterPosition).getPayoutStatus() == 0) {
                        if (DateTimeUtils.Companion.isPastLastDay(dayCollectionModels.get(adapterPosition).getDate(), 1)) {


                            CommonFuncs.ValueObject v = CommonFuncs.getValueObjectToEditFromDayCollection(dayCollectionModels.get(adapterPosition), time, type);
                            editValue(true, adapterPosition, time, type, v.getValue(), v.getO(), editable, dayCollectionModels.get(adapterPosition));


                        } else {
                            MyToast.toast("Future collections cannot be edited", getContext(), R.drawable.ic_error_outline_black_24dp, Toast.LENGTH_LONG);


                        }
                    } else {
                        CommonFuncs.ValueObject v = CommonFuncs.getValueObjectToEditFromDayCollection(dayCollectionModels.get(adapterPosition), time, type);
                        editValue(false, adapterPosition, time, type, v.getValue(), v.getO(), editable, dayCollectionModels.get(adapterPosition));

                        MyToast.toast("Cards in an approved payout cannot be edited", getContext(), R.drawable.ic_error_outline_black_24dp, Toast.LENGTH_LONG);

                    }
                } else {
                    CommonFuncs.timeIs(getActivity());

                }
            }




        });
        recyclerView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();


    }


    @Override
    public void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 10004:

                if (resultCode == RESULT_OK && data != null) {
                    //String orderData = "";
                    OrderModel orderModel;
                    DayCollectionModel dayCollectionModel;
                    orderModel = (OrderModel) data.getSerializableExtra("orderDataModel");
                    // orderData = data.getStringExtra("orderData");
                    dayCollectionModel = (DayCollectionModel) data.getSerializableExtra("dayCollection");
                    updateCollectionValue(orderModel.getTotalOrderAmount(), 0, 3, dayCollectionModel, null, null, orderModel);


                }
                break;

            default:
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void approve(Payouts payouts, PayoutFarmersCollectionModel model) {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
//        alertDialog.setMessage("Confirm that you wish to approve " + model.getFarmername() + "'s " + payouts.getCyclename() + " Collection card").setCancelable(false).setTitle("Approve " + model.getFarmername() + " Card");
//
//
//        alertDialog.setPositiveButton("Yes", (dialogInterface, i) -> {
//
//            payoutsVewModel.approveFarmersPayoutCard(model.getFarmercode(), model.getPayoutNumber());
//            model.setCardstatus(1);
//            model.setStatusName("Approved");
//            setData(model);
//
//
//            dialogInterface.dismiss();
//
//        }).setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
//
//        AlertDialog alertDialogAndroid = alertDialog.create();
//        alertDialogAndroid.setCancelable(false);
//        alertDialogAndroid.show();

        CommonFuncs.doAprove(getContext(), balncesViewModel, traderViewModel, model, famerModel, payouts, this);

    }

    private void setData(PayoutFarmersCollectionModel model) {
        this.model = model;

        boolean isApproved = false;
        boolean isPast = false;
        if (model.getCardstatus() == 1) {
            isApproved = true;


        } else if (model.getCardstatus() == 0) {

            isApproved = false;

        }

        toolBar.show(model.getMilktotal(), model.getLoanTotal(), model.getOrderTotal(), "", famerModel, payouts, isApproved, isPast);


        payoutsVewModel.getSumOfMilkForPayoutLtrs(model.getFarmercode(), model.getPayoutCode()).observe(this, integer -> {
            toolBar.updateMilk(String.valueOf(integer));
            setBalance(milkKsh);
        });

        payoutsVewModel.getSumOfMilkForPayoutKsh(model.getFarmercode(), model.getPayoutCode()).observe(this, integer -> {
            milkKsh = integer;
            setBalance(milkKsh);
        });
//        payoutsVewModel.getSumOfLoansForPayout(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> {
//            toolBar.updateLoan(String.valueOf(integer));
//
//            setBalance(milkKsh);
//        });
//        payoutsVewModel.getSumOfOrdersForPayout(model.getFarmercode(), model.getPayoutNumber()).observe(this, integer -> {
//            toolBar.updateOrder(String.valueOf(integer));
//
//            setBalance(milkKsh);
//        });
//

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


        setCardActionStatus(model, getContext(), btnApprove, btnBack, txtApprovalStatus);


    }

    public void insertLoanPayment(double toLoanInstallmentPayment) {
        balncesViewModel.getFarmerLoanByFarmerByStatus(famerModel.getCode(), 0).observe(FragmentCurrentFarmerPayout.this, farmerLoansTables -> {
            remaining = toLoanInstallmentPayment;

            if (farmerLoansTables != null) {
                for (int a = 0; a < farmerLoansTables.size(); a++) {


                    FarmerLoansTable farmerLoan = farmerLoansTables.get(a);
                    Double amp = Double.valueOf(farmerLoan.getLoanAmount());
                    Double inst = Double.valueOf(farmerLoan.getInstallmentAmount());


                    Double valueToPay = 0.0;
                    LoanPayments loanPayments = new LoanPayments();
                    loanPayments.setLoanCode(farmerLoan.getCode());
                    loanPayments.setPaymentMethod("Payout");
                    loanPayments.setRefNo("" + payouts.getCode());
                    loanPayments.setPayoutCode("" + payouts.getCode());
                    loanPayments.setTimeStamp(DateTimeUtils.Companion.getNow());
                    loanPayments.setCode(GeneralUtills.Companion.createCode(farmerLoan.getFarmerCode()));

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
        });
    }

    public void insertOrderPayment(double toOrderInstallmentPayment) {
        balncesViewModel.getFarmerOrderByFarmerByStatus(famerModel.getCode(), 0).observe(FragmentCurrentFarmerPayout.this, farmerOrdersTables -> {
            remainingOrderInstall = toOrderInstallmentPayment;

            if (farmerOrdersTables != null) {
                for (int a = 0; a < farmerOrdersTables.size(); a++) {


                    FarmerOrdersTable farmerOrders = farmerOrdersTables.get(a);
                    Double amp = Double.valueOf(farmerOrders.getOrderAmount());
                    Double inst = Double.valueOf(farmerOrders.getInstallmentAmount());

                    OrderPayments orderPayments = new OrderPayments();
                    orderPayments.setOrderCode(farmerOrders.getCode());
                    orderPayments.setPaymentMethod("Payout");
                    orderPayments.setRefNo("" + payouts.getCode());
                    orderPayments.setPayoutCode("" + payouts.getCode());
                    orderPayments.setTimestamp(DateTimeUtils.Companion.getNow());
                    orderPayments.setCode(GeneralUtills.Companion.createCode(farmerOrders.getFarmerCode()));


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
