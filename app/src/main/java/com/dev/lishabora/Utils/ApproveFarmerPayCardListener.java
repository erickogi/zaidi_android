package com.dev.lishabora.Utils;

import com.dev.lishabora.Models.PayoutFarmersCollectionModel;

public interface ApproveFarmerPayCardListener {
    void onApprove(PayoutFarmersCollectionModel model, Double totalKshToPay, Double toLoanInstallmentPayment, Double toOrderInstallmentPayment);

    void onApprovePayLoan(PayoutFarmersCollectionModel model, Double totalKshToPay, Double toLoanInstallmentPayment);

    void onApprovePayOrder(PayoutFarmersCollectionModel model, Double totalKshToPay, Double toOrderInstallmentPayment);

    void onApprove(PayoutFarmersCollectionModel model, Double totalKshToPay);

    void onApprove(PayoutFarmersCollectionModel model);

    void onApproveError(String error);

    void onApproveDismiss();
}
