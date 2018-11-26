package com.dev.lishabora.Views.Trader.Fragments;

import android.app.Activity;

import com.dev.lishabora.Models.ApprovalRegisterModel;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.FarmerBalance;
import com.dev.lishabora.Models.PayoutFarmersCollectionModel;
import com.dev.lishabora.Models.Trader.LoanPayments;
import com.dev.lishabora.Models.Trader.OrderPayments;
import com.dev.lishabora.Utils.ApproveListener;
import com.dev.lishabora.ViewModels.Trader.BalncesViewModel;
import com.dev.lishabora.ViewModels.Trader.PayoutsVewModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CancelFuncs {
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
    static Activity activity;


    public static void cancelCard(Activity activitya, String payoutCodea, PayoutFarmersCollectionModel modela, ApprovalRegisterModel approvalRegisterModel, TraderViewModel traderViewModela, BalncesViewModel balncesViewModela, PayoutsVewModel payoutsVewModela, ApproveListener listeners) {

        new Thread(() -> {
            listener = listeners;
            activity = activitya;

            activity.runOnUiThread(listener::onStart);


            payoutsVewModel = payoutsVewModela;
            model = modela;
            payoutCode = payoutCodea;
            traderViewModel = traderViewModela;
            balncesViewModel = balncesViewModela;
            remaining = 0.0;
            remainingOrderInstall = 0.0;
            json = "";
            famerModel = modela.getFamerModel();


            if (approvalRegisterModel != null) {
                cancelLoansandOrders(approvalRegisterModel);
            }
            cancelCollections(famerModel.getCode(), payoutCode, approvalRegisterModel);


        }).start();


    }

    private static void cancelCollections(String farmerCode, String payoutCode, ApprovalRegisterModel approvalRegisterModel) {
        payoutsVewModel.cancelFarmersPayoutCard(farmerCode, payoutCode);


        activity.runOnUiThread(() -> listener.onProgress(30));

        cancelBalance(approvalRegisterModel);

    }

    private static void cancelLoansandOrders(ApprovalRegisterModel approvalRegisterModel) {
        if (approvalRegisterModel.getLoanPaymentCode() != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<LoanPayments>>() {
            }.getType();
            List<LoanPayments> fromJson = gson.fromJson(approvalRegisterModel.getLoanPaymentCode(), type);


            for (LoanPayments task : fromJson) {
                LoanPayments p = balncesViewModel.getLoanPaymentByCodeOne(task.getCode());
                balncesViewModel.deleteRecordLoanPayment(p);
            }

        }

        if (approvalRegisterModel.getOrderPaymentCode() != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<OrderPayments>>() {
            }.getType();
            List<OrderPayments> fromJson = gson.fromJson(approvalRegisterModel.getOrderPaymentCode(), type);

            for (OrderPayments task : fromJson) {
                OrderPayments p = balncesViewModel.getOrderPaymentByCodeOne(task.getCode());
                if (p != null) {
                    balncesViewModel.deleteRecordOrderPayment(p);
                }
            }

        }

        payoutsVewModel.deleteRecord(approvalRegisterModel);

    }

    private static void cancelBalance(ApprovalRegisterModel approvalRegisterModel) {

        FarmerBalance farmerBalance = balncesViewModel.getByFarmerCodeByPayoutOne(famerModel.getCode(), payoutCode);
        if (farmerBalance != null) {
            farmerBalance.setPayoutStatus(0);
            balncesViewModel.updateRecordDirect(farmerBalance);

        }
        refreshFarmerBalance();

        activity.runOnUiThread(() -> listener.onProgress(60));

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


}

