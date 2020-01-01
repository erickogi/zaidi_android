package com.dev.lishabora.Views.Trader.Fragments;

import android.app.Activity;

import com.dev.lishabora.Models.ApprovalRegisterModel;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.FarmerBalance;
import com.dev.lishabora.Models.PayoutFarmersCollectionModel;
import com.dev.lishabora.Models.Trader.FarmerLoansTable;
import com.dev.lishabora.Models.Trader.FarmerOrdersTable;
import com.dev.lishabora.Models.Trader.LoanPayments;
import com.dev.lishabora.Models.Trader.OrderPayments;
import com.dev.lishabora.Utils.ApproveFarmerPayCardListener;
import com.dev.lishabora.Utils.ApproveListener;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.Utils.Logs;
import com.dev.lishabora.ViewModels.Trader.BalncesViewModel;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.dev.lishabora.Views.CommonFuncs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class ApproveFuncs {
    static Double loanInstallmentsd = 0.0;
    static Double orderInstallmentsD = 0.0;
    static Double milkCollectionD = 0.0;
    static Double loanTotalD = 0.0;
    static Double totalToPay = 0.0;
    static boolean isLoanEditable = false;
    static boolean isOrderEditable = false;
    static Double orderTotalD = 0.0;
    static Double remainingOrderInstall = 0.0;

    static double remaining = 0.0;

    static String json = "";

    static PayoutsVewModel payoutsVewModel;
    static BalncesViewModel balncesViewModel;
    static TraderViewModel traderViewModel;
    static FamerModel famerModel;
    static PayoutFarmersCollectionModel model;
    static String payoutCode;
    static ApproveListener listener;
    private static Activity activity;


    public static void approveCard(Activity activitya, String payoutCodea, PayoutFarmersCollectionModel modela, TraderViewModel traderViewModela, BalncesViewModel balncesViewModela, PayoutsVewModel payoutsVewModela, ApproveListener listeners) {

        new Thread(() -> {
            activity = activitya;
            listener = listeners;
            activity.runOnUiThread(listener::onStart);


            payoutsVewModel = payoutsVewModela;
            model = modela;
            traderViewModel = traderViewModela;
            balncesViewModel = balncesViewModela;
            payoutCode = payoutCodea;
            remaining = 0.0;
            remainingOrderInstall = 0.0;
            json = "";
            famerModel = modela.getFamerModel();
            doSilentFarmerCardPayoutCalc(model, new ApproveFarmerPayCardListener() {
                @Override
                public void onApprove(double farmerBalance, @NotNull PayoutFarmersCollectionModel model, @Nullable Double totalKshToPay, @Nullable Double toLoanInstallmentPayment, @Nullable Double toOrderInstallmentPayment) {

                    activity.runOnUiThread(() -> listener.onProgress(20));
                    String loanPaymnets = insertLoanPayment(toLoanInstallmentPayment);
                    String orderPayments = insertOrderPayment(toOrderInstallmentPayment);


                    approveCard(model, loanPaymnets, orderPayments);

                }

                @Override
                public void onApprovePayLoan(double farmerBalance, @NotNull PayoutFarmersCollectionModel model, @Nullable Double totalKshToPay, @Nullable Double toLoanInstallmentPayment) {
                    activity.runOnUiThread(() -> listener.onProgress(20));
                    String loanPaymnets = insertLoanPayment(toLoanInstallmentPayment);


                    approveCard(model, loanPaymnets, null);


                }

                @Override
                public void onApprovePayOrder(double farmerBalance, @NotNull PayoutFarmersCollectionModel model, @Nullable Double totalKshToPay, @Nullable Double toOrderInstallmentPayment) {
                    activity.runOnUiThread(() -> listener.onProgress(20));
                    String orderPayments = insertOrderPayment(toOrderInstallmentPayment);


                    approveCard(model, null, orderPayments);


                }

                @Override
                public void onApprove(double farmerBalance, @NotNull PayoutFarmersCollectionModel model, @Nullable Double totalKshToPay) {
                    activity.runOnUiThread(() -> listener.onProgress(20));


                    approveCard(model, null, null);


                }

                @Override
                public void onApprove(double farmerBalance, @NotNull PayoutFarmersCollectionModel model) {
                    activity.runOnUiThread(() -> listener.onProgress(20));

                    approveCard(model, null, null);


                }

                @Override
                public void onApproveError(@NotNull String error) {
                    activity.runOnUiThread(() -> listener.onError(error));


                }

                @Override
                public void onApproveDismiss() {

                    activity.runOnUiThread(listener::onComplete);
                }
            });


        }).start();


    }

    public static void cancelCard(PayoutFarmersCollectionModel model, ApprovalRegisterModel approvalRegisterModel, BalncesViewModel balncesViewModel, PayoutsVewModel payoutsVewModel, ApproveListener listener) {

        listener.onStart();


    }

    static String insertLoanPayment(double toLoanInstallmentPayment) {
        Logs.Companion.d("insertLoan", "to laon" , toLoanInstallmentPayment);
        json = "";
        List<LoanPayments> apploanPayments = new LinkedList<>();
        List<FarmerLoansTable> farmerLoansTables = balncesViewModel.getFarmerLoanByPayoutNumberByFarmerByStatus(famerModel.getCode(), 0);

        remaining = toLoanInstallmentPayment;

        if (farmerLoansTables != null) {
            for (int a = 0; a < farmerLoansTables.size(); a++) {
                Logs.Companion.d("insertLoan", farmerLoansTables.size());


                FarmerLoansTable farmerLoan = farmerLoansTables.get(a);
                Double amp = Double.valueOf(farmerLoan.getLoanAmount());
                Double inst = Double.valueOf(farmerLoan.getInstallmentAmount());


                LoanPayments loanPayments = new LoanPayments();
                loanPayments.setLoanCode(farmerLoan.getCode());
                loanPayments.setPaymentMethod("Payout");
                loanPayments.setRefNo("" + payoutCode);
                loanPayments.setPayoutCode("" + payoutCode);
                loanPayments.setTimeStamp(DateTimeUtils.Companion.getNow());
                loanPayments.setCode(GeneralUtills.Companion.createCode(farmerLoan.getFarmerCode()));

                Double valueToPay = 0.0;

                if (inst >= remaining) {
                    valueToPay = remaining;

                    loanPayments.setAmountPaid("" + valueToPay);
                    loanPayments.setAmountRemaining(String.valueOf(amp - valueToPay));

                    remaining = 0.0;
                    apploanPayments.add(loanPayments);

                    balncesViewModel.insertSingleLoanPayment(loanPayments);
                    break;
                } else {


                    valueToPay = remaining - inst;

                    loanPayments.setAmountPaid("" + valueToPay);
                    loanPayments.setAmountRemaining(String.valueOf(amp - valueToPay));
                    remaining = toLoanInstallmentPayment - inst;

                    apploanPayments.add(loanPayments);

                    balncesViewModel.insertSingleLoanPayment(loanPayments);


                }
                CommonFuncs.updateLoan(farmerLoan, balncesViewModel);


            }

            Gson gson = new Gson();
            Type type = new TypeToken<List<LoanPayments>>() {
            }.getType();
            json = gson.toJson(apploanPayments, type);
            Logs.Companion.d("insertLoan", "strig  " , json);

        }


        return json;
    }

    static String insertOrderPayment(double toOrderInstallmentPayment) {
        List<OrderPayments> appOrderPayments = new LinkedList<>();

        List<FarmerOrdersTable> farmerOrdersTables = balncesViewModel.getFarmerOrderByPayoutNumberByFarmerByStatus(famerModel.getCode(), 0);

        remainingOrderInstall = toOrderInstallmentPayment;

        if (farmerOrdersTables != null) {
            for (int a = 0; a < farmerOrdersTables.size(); a++) {


                FarmerOrdersTable farmerOrders = farmerOrdersTables.get(a);
                Double amp = Double.valueOf(farmerOrders.getOrderAmount());
                Double inst = Double.valueOf(farmerOrders.getInstallmentAmount());

                OrderPayments orderPayments = new OrderPayments();
                orderPayments.setOrderCode(farmerOrders.getCode());
                orderPayments.setPaymentMethod("Payout");
                orderPayments.setRefNo(payoutCode);
                orderPayments.setPayoutCode(payoutCode);
                orderPayments.setTimestamp(DateTimeUtils.Companion.getNow());
                orderPayments.setCode(GeneralUtills.Companion.createCode(farmerOrders.getFarmerCode()));

                Double valueToPay = 0.0;

                if (inst >= remainingOrderInstall) {

                    valueToPay = remainingOrderInstall;

                    orderPayments.setAmountPaid("" + valueToPay);
                    orderPayments.setAmountRemaining(String.valueOf(amp - valueToPay));


                    remainingOrderInstall = 0.0;
                    appOrderPayments.add(orderPayments);
                    balncesViewModel.insertSingleOrderPayment(orderPayments);


                    break;
                } else {


                    valueToPay = remainingOrderInstall - inst;

                    orderPayments.setAmountPaid("" + valueToPay);
                    orderPayments.setAmountRemaining(String.valueOf(amp - valueToPay));
                    remaining = toOrderInstallmentPayment - inst;

                    appOrderPayments.add(orderPayments);

                    balncesViewModel.insertSingleOrderPayment(orderPayments);


                }
                CommonFuncs.updateOrder(farmerOrders, balncesViewModel);

            }

            Gson gson = new Gson();
            Type type = new TypeToken<List<OrderPayments>>() {
            }.getType();
            json = gson.toJson(appOrderPayments, type);
        }


        return json;
    }

    static void doSilentFarmerCardPayoutCalc(PayoutFarmersCollectionModel model, ApproveFarmerPayCardListener listener) {
        try {
            milkCollectionD = Double.valueOf(model.getMilktotalKsh());
        } catch (Exception nm) {
            nm.printStackTrace();
        }
        try {
            loanTotalD = Double.valueOf(model.getLoanTotal());
        } catch (Exception nm) {
            nm.printStackTrace();
        }
        try {
            orderTotalD = Double.valueOf(model.getOrderTotal());
        } catch (Exception nm) {
            nm.printStackTrace();
        }


        double maxplayBalnce = milkCollectionD;


        if (milkCollectionD >= (loanTotalD + orderTotalD)) {

            if (loanTotalD > 0) {
                loanInstallmentsd = loanTotalD;


            } else {

            }
            if (orderTotalD > 0) {
                orderInstallmentsD = orderTotalD;

            } else {

            }

            totalToPay = 0.0;


            isLoanEditable = true;
            isOrderEditable = true;


        } else {
            if (orderTotalD > 0) {
                if (loanTotalD > 0) {
                    if (milkCollectionD > orderTotalD) {

                        orderInstallmentsD = orderTotalD;
                        loanInstallmentsd = milkCollectionD - orderTotalD;
                        totalToPay = 0.0;


                    } else {


                        orderInstallmentsD = milkCollectionD;
                        loanInstallmentsd = 0.0;
                        totalToPay = 0.0;
                    }

                } else {
                    if (milkCollectionD > orderTotalD) {

                        orderInstallmentsD = orderTotalD;
                        loanInstallmentsd = 0.0;
                        totalToPay = milkCollectionD - orderTotalD;

                    } else {

                        orderInstallmentsD = milkCollectionD - orderTotalD;
                        loanInstallmentsd = 0.0;
                        totalToPay = 0.0;

                    }
                }


            } else {

                if (loanTotalD > 0) {
                    if (milkCollectionD > loanTotalD) {


                        loanInstallmentsd = loanTotalD;
                        orderInstallmentsD = 0.0;
                        totalToPay = milkCollectionD - loanTotalD;

                    } else {


                        loanInstallmentsd = milkCollectionD;
                        orderInstallmentsD = 0.0;
                        totalToPay = 0.0;

                    }

                } else {


                    orderInstallmentsD = 0.0;
                    loanInstallmentsd = 0.0;
                    totalToPay = milkCollectionD;

                }
            }


        }
        if (loanInstallmentsd > 0 && orderInstallmentsD > 0) {
            listener.onApprove(0.0, model, totalToPay, loanInstallmentsd, orderInstallmentsD);
        } else if (loanInstallmentsd > 0 && orderInstallmentsD < 1) {
            listener.onApprovePayLoan(0.0, model, totalToPay, loanInstallmentsd);

        } else if (loanInstallmentsd < 1 && orderInstallmentsD > 0) {
            listener.onApprovePayOrder(0.0, model, totalToPay, orderInstallmentsD);

        } else if (loanInstallmentsd < 1 && orderInstallmentsD < 1) {
            listener.onApprove(0.0, model, totalToPay);

        } else {

            listener.onApprove(0.0, model);
        }


    }

    static void approvePayoutBalance() {


        FarmerBalance farmerBalance = balncesViewModel.getByFarmerCodeByPayoutOne(famerModel.getCode(), payoutCode);
        if (farmerBalance != null) {
            farmerBalance.setPayoutStatus(1);

            balncesViewModel.updateRecord(farmerBalance);
        }
        refreshFarmerBalance();

    }

    static void refreshFarmerBalance() {
        //doColl();
        FarmerBalance bal;//= CommonFuncs.getFarmerBalanceAfterPayoutCardApproval(famerModel, balncesViewModel, traderViewModel,payouts);


        bal = balncesViewModel.getByFarmerCodeByPayoutOne(famerModel.getCode(), payoutCode);
        if (bal != null) {
            famerModel.setTotalbalance(bal.getBalanceToPay());
            traderViewModel.updateFarmer(famerModel, false, true);
        }
        activity.runOnUiThread(listener::onComplete);


    }

    static void approveCard(PayoutFarmersCollectionModel model, String loanpayments, String orderPayments) {

        ApprovalRegisterModel app = new ApprovalRegisterModel();
        app.setApprovedOn(DateTimeUtils.Companion.getNow());
        app.setFarmerCode(famerModel.getCode());
        app.setPayoutCode(payoutCode);


        if (loanpayments != null && orderPayments != null) {
            app.setLoanPaymentCode(loanpayments);
            app.setOrderPaymentCode(orderPayments);
        } else if (loanpayments == null && orderPayments != null) {
            app.setOrderPaymentCode(orderPayments);
            app.setLoanPaymentCode(null);
        } else if (orderPayments == null && loanpayments != null) {
            app.setLoanPaymentCode(loanpayments);
            app.setOrderPaymentCode(null);
        } else {
            app.setOrderPaymentCode(null);
            app.setLoanPaymentCode(null);
        }

        payoutsVewModel.insertDirect(app);
        payoutsVewModel.approveFarmersPayoutCard(model.getFarmercode(), model.getPayoutCode());
        approvePayoutBalance();


    }

}

