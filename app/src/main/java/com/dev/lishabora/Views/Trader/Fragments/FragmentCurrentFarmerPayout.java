package com.dev.lishabora.Views.Trader.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dev.lishabora.Adapters.FarmerCollectionsAdapter;
import com.dev.lishabora.AppConstants;
import com.dev.lishabora.Application;
import com.dev.lishabora.Models.ApprovalRegisterModel;
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
import com.dev.lishabora.Models.UnitsModel;
import com.dev.lishabora.Utils.AdvancedOnclickRecyclerListener;
import com.dev.lishabora.Utils.ApproveListener;
import com.dev.lishabora.Utils.CollectionCreateUpdateListener;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.MaterialIntro;
import com.dev.lishabora.Utils.MilkEditValueListener;
import com.dev.lishabora.Utils.MyToast;
import com.dev.lishabora.Utils.PrefrenceManager;
import com.dev.lishabora.ViewModels.Trader.BalncesViewModel;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.dev.lishabora.Views.Trader.Activities.EditOrder;
import com.dev.lishabora.Views.Trader.MilkCardToolBarUI;
import com.dev.lishabora.Views.Trader.OrderConstants;
import com.dev.lishaboramobile.R;
import com.getkeepsafe.taptargetview.TapTarget;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.dev.lishabora.Views.CommonFuncs.getBalance;
import static com.dev.lishabora.Views.CommonFuncs.setCardActionStatus;

public class FragmentCurrentFarmerPayout extends Fragment {
    // implements ApproveFarmerPayCardListener {

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
    private MaterialDialog materialDialog;


    PayoutFarmersCollectionModel model;
    double remaining = 0.0;
    double remainingOrderInstall = 0.0;

    private ImageView arrowBack;


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



        arrowBack =  toolBar.findViewById(R.id.arrow_back);
        arrowBack.setVisibility(View.VISIBLE);
        arrowBack.setOnClickListener(v -> getActivity().onBackPressed());
        status = view.findViewById(R.id.txt_status);
        toolBar.findViewById(R.id.action_help).setOnClickListener(v -> showIntro());
        if (!new PrefrenceManager(getContext()).isFarmerCurrentPayoutFragmentIntroShown()) {
            showIntro();
        }
    }
    void showIntro() {
        final Display display = getActivity().getWindowManager().getDefaultDisplay();

        int canvasW = display.getWidth();
        int canvasH = display.getHeight();
        Point centerOfCanvas = new Point(canvasW / 2, canvasH / 2);
        int rectW = 10;
        int rectH = 10;
        int left = centerOfCanvas.x - (rectW / 2);
        int top = centerOfCanvas.y - (rectH / 2);
        int right = centerOfCanvas.x + (rectW / 2);
        int bottom = centerOfCanvas.y + (rectH / 2);
        Rect rect = new Rect(left, top, right, bottom);

        List<TapTarget> targets = new ArrayList<>();
        targets.add(TapTarget.forBounds(rect, "Click on any collection to edit .", getContext().getResources().getString(R.string.dismiss_intro)).cancelable(false).id(20).transparentTarget(true));
        targets.add(TapTarget.forView(toolBar.findViewById(R.id.action_help), "Click here to see this introduction again", getContext().getResources().getString(R.string.dismiss_intro)).cancelable(false).id(21).transparentTarget(true));

        MaterialIntro.Companion.showIntroSequence(getActivity(), targets);
        new PrefrenceManager(getContext()).setFarmerCurrentPayoutFragmentIntroShown(true);

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

        return CommonFuncs.getFarmersCollectionModel(famerModel, collections, payouts, balncesViewModel);

    }


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
                        FamerModel fm = famerModel;
                        if (type == AppConstants.MILK) {
                            fm = CommonFuncs.addBalance(famerModel, traderViewModel, balncesViewModel, c, responseModel.getPayoutCode(), type, null, null);
                        } else if (type == AppConstants.LOAN) {
                            FarmerLoansTable f = balncesViewModel.getFarmerLoanByCollectionOne(c.getCode());

                            fm = CommonFuncs.addBalance(famerModel, traderViewModel, balncesViewModel, c, responseModel.getPayoutCode(), type, f, null);


                        } else if (type == AppConstants.ORDER) {
                            FarmerOrdersTable f = balncesViewModel.getFarmerOrderByCollectionOne(c.getCode());

                            fm = CommonFuncs.addBalance(famerModel, traderViewModel, balncesViewModel, c, responseModel.getPayoutCode(), type, null, f);


                        }
                        traderViewModel.updateFarmer(fm, false, true);

                        MyToast.toast(responseModel.getResultDescription(), getContext());
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
                        if (responseModel != null && responseModel.getResultCode() == 1) {
                            if (a != null) {
                                a.dismiss();
                            }
                            FamerModel fm = famerModel;
                            if (type == AppConstants.MILK) {
                                fm = CommonFuncs.addBalance(famerModel, traderViewModel, balncesViewModel, c, responseModel.getPayoutCode(), type, null, null);
                            } else if (type == AppConstants.LOAN) {
                                FarmerLoansTable f = balncesViewModel.getFarmerLoanByCollectionOne(c.getCode());

                                fm = CommonFuncs.addBalance(famerModel, traderViewModel, balncesViewModel, c, responseModel.getPayoutCode(), type, f, null);


                            } else if (type == AppConstants.ORDER) {


                                FarmerOrdersTable f = balncesViewModel.getFarmerOrderByCollectionOne(c.getCode());

                                fm = CommonFuncs.addBalance(famerModel, traderViewModel, balncesViewModel, c, responseModel.getPayoutCode(), type, null, f);


                            }

                            traderViewModel.updateFarmer(fm, false, true);
                            MyToast.toast("Collection updated", getContext());

                        }
                    }
                });

            }

            @Override
            public void error(String error) {
                MyToast.toast(error, getContext());
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
                    if (dayCollectionModels.get(adapterPosition).getPayoutStatus() == 0 && model.getCardstatus() == 0) {
                        if (DateTimeUtils.Companion.isPastLastDay(dayCollectionModels.get(adapterPosition).getDate(), 1)) {


                            CommonFuncs.ValueObject v = CommonFuncs.getValueObjectToEditFromDayCollection(dayCollectionModels.get(adapterPosition), time, type);
                            editValue(true, adapterPosition, time, type, v.getValue(), v.getO(), editable, dayCollectionModels.get(adapterPosition));


                        } else {
                            MyToast.errorToast("Future collections cannot be edited", getContext());


                        }
                    } else {
                        CommonFuncs.ValueObject v = CommonFuncs.getValueObjectToEditFromDayCollection(dayCollectionModels.get(adapterPosition), time, type);
                        editValue(false, adapterPosition, time, type, v.getValue(), v.getO(), editable, dayCollectionModels.get(adapterPosition));

                        MyToast.errorToast("Cards in an approved payout cannot be edited", getContext());

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


//    public void approveCollections() {
//        for (Collection c : collections) {
//            c.setApproved(1);
//            traderViewModel.updateCollection(c);
//        }
//    }
//
//    public void approvePayoutBalance() {
//
//        FarmerBalance farmerBalance = balncesViewModel.getByFarmerCodeByPayoutOne(famerModel.getCode(), payouts.getCode());
//        farmerBalance.setPayoutStatus(1);
//
//        balncesViewModel.updateRecord(farmerBalance);
//        refreshFarmerBalance();
//    }

    private void cancelApprove(Payouts payouts, PayoutFarmersCollectionModel model, ApprovalRegisterModel approvalRegisterModel) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        alertDialog.setMessage("Confirm that you wish to cancel " + model.getFarmername() + "'s " + payouts.getCyclename() + " Collection card").setCancelable(false).setTitle("Cancel " + model.getFarmername() + " Card");


        alertDialog.setPositiveButton("Yes", (dialogInterface, i) -> {

//            cancelCollections(famerModel.getCode(), payouts.getCode());
//            cancelLoansandOrders(approvalRegisterModel);
//            cancelBalance(approvalRegisterModel);

            cancelCard(model, 0, approvalRegisterModel);

            dialogInterface.dismiss();

        }).setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());

        AlertDialog alertDialogAndroid = alertDialog.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.show();
    }

    private void setBalance(Double milkKsh) {
        toolBar.setMilkTotalKsh(String.valueOf(milkKsh));
        toolBar.show(getBalance(String.valueOf(milkKsh), toolBar.getLoanTotal(), toolBar.getOrderTotal()));

    }

    private void approve(Payouts payouts, PayoutFarmersCollectionModel model) {

        PayoutFarmersCollectionModel farmersCollectionModel = model;
        farmersCollectionModel.setMilktotalKsh(toolBar.getMilkTotalKsh());
        farmersCollectionModel.setLoanTotal(toolBar.getLoanTotal());
        farmersCollectionModel.setOrderTotal(toolBar.getOrderTotal());


        approveCard(model, 0);
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

        toolBar.show(model.getMilktotalKsh(), model.getMilktotalLtrs(), model.getLoanTotal(), model.getOrderTotal(), "", famerModel, payouts, isApproved, isPast);


        payoutsVewModel.getSumOfMilkForPayoutLtrs(model.getFarmercode(), model.getPayoutCode()).observe(this, integer -> {
            toolBar.updateMilkLtrs(String.valueOf(integer));
            setBalance(milkKsh);
        });

        payoutsVewModel.getSumOfMilkForPayoutKsh(model.getFarmercode(), model.getPayoutCode()).observe(this, integer -> {
            milkKsh = integer;
            toolBar.updateMilkKsh(String.valueOf(integer));

            setBalance(milkKsh);
        });


        balncesViewModel.getFarmerLoanByFarmer(model.getFarmercode()).observe(this, farmerLoansTables -> {
            if (farmerLoansTables != null) {

                toolBar.updateLoan(CommonFuncs.getCardLoan(farmerLoansTables, balncesViewModel));
                setBalance(milkKsh);
            }
        });

        balncesViewModel.getFarmerOrderByFarmer(model.getFarmercode()).observe(this, farmerOrderTables -> {
            if (farmerOrderTables != null) {

                toolBar.updateOrder(CommonFuncs.getCardOrder(farmerOrderTables, balncesViewModel));
                setBalance(milkKsh);
            }
        });


        btnApprove.setOnClickListener(view -> approve(payouts, model));


        ApprovalRegisterModel approvalRegisterModel = payoutsVewModel.getByFarmerPayoutCodeOne(famerModel.getCode(), payouts.getCode());//.observe(this, new Observer<ApprovalRegisterModel>() {

                setCardActionStatus(model,
                        getContext(),
                        btnApprove,
                        btnBack, txtApprovalStatus,
                        toolBar.getLoanTotal(),
                        toolBar.getOrderTotal(), approvalRegisterModel
                );

                btnBack.setOnClickListener(view1 -> cancelApprove(payouts, model, approvalRegisterModel));


    }


    private void cancelCard(PayoutFarmersCollectionModel model, int position, ApprovalRegisterModel approvalRegisterModel) {
        materialDialog = new MaterialDialog.Builder(Objects.requireNonNull(getActivity()))
                .title("Canceling farmer's card")
                .content("Please wait ..")
                .cancelable(false)
                .progress(true, 0).build();


        CancelFuncs.cancelCard(getActivity(), payouts.getCode(), model, approvalRegisterModel, traderViewModel, balncesViewModel, payoutsVewModel, new ApproveListener() {
            @Override
            public void onComplete() {
                if (materialDialog != null) {
                    materialDialog.dismiss();
                }
                model.setCardstatus(0);
                model.setCardstatus(0);
                model.setStatusName("Approval Canceled");

                model.setCardstatus(0);
                setData(model);
            }

            @Override
            public void onStart() {

                if (materialDialog != null) {
                    materialDialog.show();
                }
            }

            @Override
            public void onProgress(int progress) {

                if (materialDialog != null) {
                    materialDialog.setProgress(progress);
                }
            }

            @Override
            public void onError(String error) {
                if (materialDialog != null) {
                    materialDialog.dismiss();
                }
                MyToast.errorToast(error, getContext());
            }
        });

    }

    private void approveCard(PayoutFarmersCollectionModel model, int position) {
        materialDialog = new MaterialDialog.Builder(getActivity())
                .title("Approving farmer's card")
                .content("Please wait ..")
                .cancelable(false)
                .progress(true, 0).build();


        ApproveFuncs.approveCard(getActivity(), payouts.getCode(), model, traderViewModel, balncesViewModel, payoutsVewModel, new ApproveListener() {
            @Override
            public void onComplete() {
                if (materialDialog != null) {
                    materialDialog.dismiss();
                }
                model.setCardstatus(1);
                model.setStatusName("Approved");
                setData(model);

            }

            @Override
            public void onStart() {

                if (materialDialog != null) {
                    materialDialog.show();
                }
            }

            @Override
            public void onProgress(int progress) {

                if (materialDialog != null) {
                    materialDialog.setProgress(progress);
                }
            }

            @Override
            public void onError(String error) {
                if (materialDialog != null) {
                    materialDialog.dismiss();
                }
                MyToast.errorToast(error, getContext());
            }
        });

    }


//    public String insertLoanPayment(double toLoanInstallmentPayment) {
//        List<LoanPayments> apploanPayments = new LinkedList<>();
//
//        balncesViewModel.getFarmerLoanByFarmerByStatus(famerModel.getCode(), 0).observe(FragmentCurrentFarmerPayout.this, farmerLoansTables -> {
//            remaining = toLoanInstallmentPayment;
//            if (farmerLoansTables != null) {
//                for (int a = 0; a < farmerLoansTables.size(); a++) {
//
//
//                    FarmerLoansTable farmerLoan = farmerLoansTables.get(a);
//                    Double amp = Double.valueOf(farmerLoan.getLoanAmount());
//                    Double inst = Double.valueOf(farmerLoan.getInstallmentAmount());
//
//
//                    Double valueToPay = 0.0;
//                    LoanPayments loanPayments = new LoanPayments();
//                    loanPayments.setLoanCode(farmerLoan.getCode());
//                    loanPayments.setPaymentMethod("Payout");
//                    loanPayments.setRefNo("" + payouts.getCode());
//                    loanPayments.setPayoutCode("" + payouts.getCode());
//                    loanPayments.setTimeStamp(DateTimeUtils.Companion.getNow());
//                    loanPayments.setCode(GeneralUtills.Companion.createCode(farmerLoan.getFarmerCode()));
//
//                    if (inst >= remaining) {
//                        valueToPay = remaining;
//
//                        loanPayments.setAmountPaid("" + valueToPay);
//                        loanPayments.setAmountRemaining(String.valueOf(amp - valueToPay));
//
//                        remaining = 0.0;
//                        apploanPayments.add(loanPayments);
//
//                        balncesViewModel.insertSingleLoanPayment(loanPayments);
//                        break;
//                    } else {
//
//
//                        valueToPay = remaining - inst;
//
//                        loanPayments.setAmountPaid("" + valueToPay);
//                        loanPayments.setAmountRemaining(String.valueOf(amp - valueToPay));
//
//                        remaining = toLoanInstallmentPayment - inst;
//                        apploanPayments.add(loanPayments);
//
//                        balncesViewModel.insertSingleLoanPayment(loanPayments);
//
//
//                    }
//                    CommonFuncs.updateLoan(farmerLoan, balncesViewModel);
//
//
//                }
//            }
//        });
//        Gson gson = new Gson();
//        Type type = new TypeToken<List<LoanPayments>>() {
//        }.getType();
//        String json = gson.toJson(apploanPayments, type);
//        return json;
//    }
//
//    public String insertOrderPayment(double toOrderInstallmentPayment) {
//        List<OrderPayments> appOrderPayments = new LinkedList<>();
//
//        balncesViewModel.getFarmerOrderByFarmerByStatus(famerModel.getCode(), 0).observe(FragmentCurrentFarmerPayout.this, farmerOrdersTables -> {
//            remainingOrderInstall = toOrderInstallmentPayment;
//
//            if (farmerOrdersTables != null) {
//                for (int a = 0; a < farmerOrdersTables.size(); a++) {
//
//
//                    FarmerOrdersTable farmerOrders = farmerOrdersTables.get(a);
//                    Double amp = Double.valueOf(farmerOrders.getOrderAmount());
//                    Double inst = Double.valueOf(farmerOrders.getInstallmentAmount());
//
//                    OrderPayments orderPayments = new OrderPayments();
//                    orderPayments.setOrderCode(farmerOrders.getCode());
//                    orderPayments.setPaymentMethod("Payout");
//                    orderPayments.setRefNo("" + payouts.getCode());
//                    orderPayments.setPayoutCode("" + payouts.getCode());
//                    orderPayments.setTimestamp(DateTimeUtils.Companion.getNow());
//                    orderPayments.setCode(GeneralUtills.Companion.createCode(farmerOrders.getFarmerCode()));
//
//
//                    Double valueToPay = 0.0;
//
//                    if (inst >= remainingOrderInstall) {
//
//                        valueToPay = remainingOrderInstall;
//
//                        orderPayments.setAmountPaid("" + valueToPay);
//                        orderPayments.setAmountRemaining(String.valueOf(amp - valueToPay));
//
//
//                        remainingOrderInstall = 0.0;
//                        appOrderPayments.add(orderPayments);
//
//                        balncesViewModel.insertSingleOrderPayment(orderPayments);
//
//
//                        break;
//                    } else {
//
//
//                        valueToPay = remainingOrderInstall - inst;
//
//                        orderPayments.setAmountPaid("" + valueToPay);
//                        orderPayments.setAmountRemaining(String.valueOf(amp - valueToPay));
//                        remaining = toOrderInstallmentPayment - inst;
//                        appOrderPayments.add(orderPayments);
//
//                        balncesViewModel.insertSingleOrderPayment(orderPayments);
//
//
//                    }
//                    CommonFuncs.updateOrder(farmerOrders, balncesViewModel);
//
//
//                }
//            }
//        });
//        Gson gson = new Gson();
//        Type type = new TypeToken<List<OrderPayments>>() {
//        }.getType();
//        String json = gson.toJson(appOrderPayments, type);
//        return json;
//    }
//
//    public void approveCard(PayoutFarmersCollectionModel model, String loanpayments, String orderPayments) {
//        payoutsVewModel.approveFarmersPayoutCard(model.getFarmercode(), model.getPayoutCode());
//
//
//        approveCollections();
//        approvePayoutBalance();
//
//        ApprovalRegisterModel app = new ApprovalRegisterModel();
//        app.setApprovedOn(DateTimeUtils.Companion.getNow());
//        app.setFarmerCode(famerModel.getCode());
//        app.setPayoutCode(payouts.getCode());
//
//
//        if (loanpayments != null && orderPayments != null) {
//            app.setLoanPaymentCode(loanpayments);
//            app.setOrderPaymentCode(orderPayments);
//        } else if (loanpayments == null && orderPayments != null) {
//            app.setOrderPaymentCode(orderPayments);
//            app.setLoanPaymentCode(null);
//        } else if (orderPayments == null && loanpayments != null) {
//            app.setLoanPaymentCode(loanpayments);
//            app.setOrderPaymentCode(null);
//        } else {
//            app.setOrderPaymentCode(null);
//            app.setLoanPaymentCode(null);
//        }
//
//        payoutsVewModel.insertDirect(app);
//
//        model.setCardstatus(1);
//        model.setStatusName("Approved");
//        setData(model);
//    }
//
//
//    @Override
//    public void onApproveError(String error) {
//
//    }
//
//    @Override
//    public void onApproveDismiss() {
//
//
//    }
//
//
//    private void cancelCollections(String farmerCode, String payoutCode) {
//        payoutsVewModel.cancelFarmersPayoutCard(farmerCode, payoutCode);
//        model.setCardstatus(0);
//        model.setStatusName("Approval Canceled");
//        setData(model);
//
//
//    }
//
//    private void cancelLoansandOrders(ApprovalRegisterModel approvalRegisterModel) {
//        if (approvalRegisterModel.getLoanPaymentCode() != null) {
//            Gson gson = new Gson();
//            Type type = new TypeToken<List<LoanPayments>>() {
//            }.getType();
//            List<LoanPayments> fromJson = gson.fromJson(approvalRegisterModel.getLoanPaymentCode(), type);
//
//            for (LoanPayments task : fromJson) {
//                LoanPayments p = balncesViewModel.getLoanPaymentByCodeOne(task.getCode());
//                balncesViewModel.deleteRecordLoanPayment(p);
//            }
//
//        }
//
//        if (approvalRegisterModel.getOrderPaymentCode() != null) {
//            Gson gson = new Gson();
//            Type type = new TypeToken<List<OrderPayments>>() {
//            }.getType();
//            List<OrderPayments> fromJson = gson.fromJson(approvalRegisterModel.getOrderPaymentCode(), type);
//
//            for (OrderPayments task : fromJson) {
//                OrderPayments p = balncesViewModel.getOrderPaymentByCodeOne(task.getCode());
//                balncesViewModel.deleteRecordOrderPayment(p);
//            }
//
//        }
//
//        payoutsVewModel.deleteRecord(approvalRegisterModel);
//
//    }
//
//    private void cancelBalance(ApprovalRegisterModel approvalRegisterModel) {
//
//        FarmerBalance farmerBalance = balncesViewModel.getByFarmerCodeByPayoutOne(famerModel.getCode(), payouts.getCode());
//        if (farmerBalance != null) {
//            farmerBalance.setPayoutStatus(0);
//            balncesViewModel.updateRecordDirect(farmerBalance);
//
//        }
//        refreshFarmerBalance();
//
//
//    }
//
//
//    private void refreshFarmerBalance() {
//        FarmerBalance bal = CommonFuncs.getFarmerBalanceAfterPayoutCardApproval(famerModel, balncesViewModel, traderViewModel);
//
//        if (bal != null) {
//            famerModel.setTotalbalance(bal.getBalanceToPay());
//            traderViewModel.updateFarmer(famerModel, false, true);
//        }
//
//
//    }
//
//    @Override
//    public void onApprove(double farmerBalance, PayoutFarmersCollectionModel model, Double totalKshToPay, Double toLoanInstallmentPayment, Double toOrderInstallmentPayment) {
//        String loanPaymnets = insertLoanPayment(toLoanInstallmentPayment);
//        String orderPayments = insertOrderPayment(toOrderInstallmentPayment);
//
//
//        approveCard(model, loanPaymnets, orderPayments);
//
//    }
//
//    @Override
//    public void onApprovePayLoan(double farmerBalance, PayoutFarmersCollectionModel model, Double totalKshToPay, Double toLoanInstallmentPayment) {
//        String loanPaymnets = insertLoanPayment(toLoanInstallmentPayment);
//        approveCard(model, loanPaymnets, null);
//    }
//
//    @Override
//    public void onApprovePayOrder(double farmerBalance, PayoutFarmersCollectionModel model, Double totalKshToPay, Double toOrderInstallmentPayment) {
//        String orderPayments = insertOrderPayment(toOrderInstallmentPayment);
//        approveCard(model, null, orderPayments);
//    }
//
//    @Override
//    public void onApprove(double farmerBalance, PayoutFarmersCollectionModel model, Double totalKshToPay) {
//        approveCard(model, null, null);
//    }
//
//    @Override
//    public void onApprove(double farmerBalance, PayoutFarmersCollectionModel model) {
//        approveCard(model, null, null);
//    }



}
