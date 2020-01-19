package com.dev.lishabora.Views.Trader.Fragments;

import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.FarmerBalance;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Models.Trader.FarmerLoansTable;
import com.dev.lishabora.Models.Trader.FarmerOrdersTable;
import com.dev.lishabora.Models.Trader.LoanPayments;
import com.dev.lishabora.Models.Trader.OrderPayments;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Utils.GeneralUtills;
import com.dev.lishabora.ViewModels.Trader.BalncesViewModel;
import com.dev.lishabora.ViewModels.Trader.TraderViewModel;

import java.util.List;

public class FarmerListBalanceFuncs {
    static BalncesViewModel balncesViewModel;
    static TraderViewModel traderViewModel;

    public static void calCBalances(TraderViewModel traderViewModela, BalncesViewModel balncesViewModela, List<FamerModel> famerModels) {
        new Thread(() -> {
            balncesViewModel = balncesViewModela;
            traderViewModel = traderViewModela;

            if (famerModels != null && famerModels.size() > 0) {
                for (FamerModel famerModel : famerModels) {

                    updateFarmer(famerModel);

                }
            } else {
                List<FamerModel> famerModels1 = traderViewModel.fetchAllFarmers();
                for (FamerModel famerModel : famerModels1) {

                    updateFarmer(famerModel);

                }
            }


        }).start();

    }

    public static void calCBalances(TraderViewModel traderViewModela, BalncesViewModel balncesViewModela, FamerModel famerModels) {
        new Thread(() -> {
            balncesViewModel = balncesViewModela;
            traderViewModel = traderViewModela;


            updateFarmer(famerModels);


        }).start();

    }

    public static void updateFarmer(FamerModel famerModel) {
        refreshTotalBalances(balncesViewModel, traderViewModel, famerModel);
    }

    public static int getPayoutStatus(String payoutCode) {
        if (payoutCode == null || payoutCode.equals("")) {
            return 5;
        } else {
            Payouts p = traderViewModel.getPayoutByCodeOne(payoutCode);
            if (DateTimeUtils.Companion.isPastLastDay(p.getEndDate())) {

                return 2;
            } else {
                return 1;
            }
        }

    }

    public static FamerModel refreshTotalBalances(BalncesViewModel balncesViewModel,
                                                  TraderViewModel traderViewModel,
                                                  FamerModel famerModel) {

        double totalMilkForCurrentPayout = 0.0;
        Double totalMilkForUnApprovedLtrs = 0.0;
        double totalMilkForUnApprovedKsh = 0.0;
        try {
            totalMilkForCurrentPayout = +traderViewModel.getSumOfMilkForPayoutKshD(famerModel.getCode(), famerModel.getCurrentPayoutCode());
        } catch (Exception nm) {
            nm.printStackTrace();

        }
        try {
            totalMilkForUnApprovedLtrs = +traderViewModel.getSumOfMilkFarmerByApproveStatusLtrs(famerModel.getCode(), 0);
        } catch (Exception nm) {
            nm.printStackTrace();

        }
        try {
            totalMilkForUnApprovedKsh = +traderViewModel.getSumOfMilkFarmerByApproveStatusKsh(famerModel.getCode(), 0);
        } catch (Exception nm) {
            nm.printStackTrace();

        }


        //  try {
        FarmerBalance farmerBalance = balncesViewModel.getByFarmerCodeByPayoutOne(famerModel.getCode(), famerModel.getCurrentPayoutCode());
        List<FarmerLoansTable> loansTables = balncesViewModel.getFarmerLoanByPayoutNumberByFarmerByStatus(famerModel.getCode(), 0);
        List<FarmerOrdersTable> ordersTables = balncesViewModel.getFarmerOrderByPayoutNumberByFarmerByStatus(famerModel.getCode(), 0);


        double loanTotalAmount = 0.0;
        double loanInstalmentAmount = 0.0;
        double loanPaid = 0.0;

        double orderTotalAmount = 0.0;
        double orderInstalmentAmount = 0.0;
        double orderPaid = 0.0;


        if (loansTables != null) {
            for (FarmerLoansTable fl : loansTables) {

                loanTotalAmount = +(Double.valueOf(fl.getLoanAmount()));
                loanInstalmentAmount = +(Double.valueOf(fl.getInstallmentAmount()));
                loanPaid = +balncesViewModel.getSumPaidLoanPayment(fl.getCode());

                if (loanInstalmentAmount > (loanTotalAmount - loanPaid)) {
                    loanInstalmentAmount = (loanTotalAmount - loanPaid);
                }
                loanInstalmentAmount = getSupposedToPay(loanInstalmentAmount, fl, null, famerModel, 1);

            }
        }

        if (ordersTables != null) {
            for (FarmerOrdersTable fo : ordersTables) {
                orderTotalAmount = +(Double.valueOf(fo.getOrderAmount()));
                orderInstalmentAmount = +(Double.valueOf(fo.getInstallmentAmount()));
                orderPaid = +balncesViewModel.getSumPaidOrderPayment(fo.getCode());

                if (orderInstalmentAmount > (orderTotalAmount - orderPaid)) {
                    orderInstalmentAmount = (orderTotalAmount - orderPaid);
                }

                orderInstalmentAmount = getSupposedToPay(orderInstalmentAmount, null, fo, famerModel, 2);


            }
        }


        if (farmerBalance == null) {


            farmerBalance = new FarmerBalance(GeneralUtills.Companion.createCode(famerModel.getCode()),
                    famerModel.getCode(), famerModel.getCurrentPayoutCode(), "", "", "", String.valueOf(loanTotalAmount), String.valueOf(orderTotalAmount), String.valueOf(totalMilkForCurrentPayout));

            farmerBalance.setBalanceOwed(String.valueOf((totalMilkForCurrentPayout - ((loanTotalAmount - loanPaid) + (orderTotalAmount - orderPaid)))));
            farmerBalance.setBalanceToPay(String.valueOf((totalMilkForUnApprovedKsh - ((loanInstalmentAmount) + (orderInstalmentAmount)))));


            famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());


            balncesViewModel.insertDirect(farmerBalance);
            famerModel.setTotalbalance(farmerBalance.getBalanceToPay());
            famerModel.setMilkbalance(String.valueOf(totalMilkForUnApprovedLtrs));

        } else {
            farmerBalance.setBalanceOwed(String.valueOf((totalMilkForCurrentPayout - ((loanTotalAmount - loanPaid) + (orderTotalAmount - orderPaid)))));
            farmerBalance.setBalanceToPay(String.valueOf((totalMilkForUnApprovedKsh - ((loanInstalmentAmount) + (orderInstalmentAmount)))));


            famerModel.setLastCollectionTime(DateTimeUtils.Companion.getNow());


            balncesViewModel.updateRecordDirect(farmerBalance);
            famerModel.setTotalbalance(farmerBalance.getBalanceToPay());
            famerModel.setMilkbalance(String.valueOf(totalMilkForUnApprovedLtrs));


        }
        famerModel.setMilkbalance(String.valueOf(totalMilkForUnApprovedLtrs));

        traderViewModel.updateFarmer("FarmerListBalanceFuncs -> refreshTotalBalances ",famerModel);

        return famerModel;

    }

    private static double getSupposedToPay(double instalmentAmount, FarmerLoansTable loansTables, FarmerOrdersTable ordersTable, FamerModel famerModel, int i) {

        try {
            Payouts p = traderViewModel.getPayoutByCodeOne(famerModel.getCurrentPayoutCode());

            Long dat1 = DateTimeUtils.Companion.getLongDate(p.getStartDate());
            Long dat2 = DateTimeUtils.Companion.getLongDate(p.getEndDate());
            double paind = 0.0;

            if (i == 1) {
                List<LoanPayments> loanPayments = balncesViewModel.getLoanPaymentsByLoanIdBetweenDatesorByPayoutCode(dat1, dat2, loansTables.getCode(), famerModel.getCurrentPayoutCode());

                if (loanPayments != null) {
                    for (int a = 0; a < loanPayments.size(); a++) {
                        try {
                            paind = paind + Double.valueOf(loanPayments.get(a).getAmountPaid());
                        } catch (Exception nm) {
                            nm.printStackTrace();

                        }
                    }
                }
            } else {
                List<OrderPayments> orderPayments = balncesViewModel.getOrderPaymentsByLoanIdBetweenDatesorByPayoutCode(dat1, dat2, ordersTable.getCode(), famerModel.getCurrentPayoutCode());

                if (orderPayments != null) {
                    for (int a = 0; a < orderPayments.size(); a++) {
                        try {
                            paind = paind + Double.valueOf(orderPayments.get(a).getAmountPaid());
                        } catch (Exception nm) {
                            nm.printStackTrace();
                        }
                    }
                }
            }
            double v = instalmentAmount - paind;
            return v < 1 ? 0 : v;
        } catch (Exception nm) {
            nm.printStackTrace();
            return 0;
        }


    }
}
