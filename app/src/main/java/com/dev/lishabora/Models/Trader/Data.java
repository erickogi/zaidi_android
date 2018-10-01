package com.dev.lishabora.Models.Trader;

import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.Cycles;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.LoanModel;
import com.dev.lishabora.Models.Notifications;
import com.dev.lishabora.Models.OrderModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Models.ProductsModel;
import com.dev.lishabora.Models.RoutesModel;
import com.dev.lishabora.Models.UnitsModel;

import java.util.List;

public class Data {


    private List<RoutesModel> routeModels;
    private List<ProductsModel> productModels;
    private List<FamerModel> farmerModels;


    private List<OrderModel> orderModels;
    private List<LoanModel> loanModels;

    private List<LoanPayments> loanPaymentModels;
    private List<OrderPayments> orderPaymentModels;

    private List<Collection> collectionModels;
    private List<Payouts> payoutModels;


    private List<Cycles> cycleModels;
    private List<UnitsModel> unitsModels;
    private List<Notifications> notificationModels;


    private String ResultCode;
    private String ResultDescription;

    public List<RoutesModel> getRouteModels() {
        return routeModels;
    }

    public void setRouteModels(List<RoutesModel> routeModels) {
        this.routeModels = routeModels;
    }

    public List<ProductsModel> getProductModels() {
        return productModels;
    }

    public void setProductModels(List<ProductsModel> productModels) {
        this.productModels = productModels;
    }

    public List<FamerModel> getFarmerModels() {
        return farmerModels;
    }

    public void setFarmerModels(List<FamerModel> farmerModels) {
        this.farmerModels = farmerModels;
    }

    public List<OrderModel> getOrderModels() {
        return orderModels;
    }

    public void setOrderModels(List<OrderModel> orderModels) {
        this.orderModels = orderModels;
    }

    public List<LoanModel> getLoanModels() {
        return loanModels;
    }

    public void setLoanModels(List<LoanModel> loanModels) {
        this.loanModels = loanModels;
    }

    public List<LoanPayments> getLoanPaymentModels() {
        return loanPaymentModels;
    }

    public void setLoanPaymentModels(List<LoanPayments> loanPaymentModels) {
        this.loanPaymentModels = loanPaymentModels;
    }

    public List<OrderPayments> getOrderPaymentModels() {
        return orderPaymentModels;
    }

    public void setOrderPaymentModels(List<OrderPayments> orderPaymentModels) {
        this.orderPaymentModels = orderPaymentModels;
    }

    public List<Collection> getCollectionModels() {
        return collectionModels;
    }

    public void setCollectionModels(List<Collection> collectionModels) {
        this.collectionModels = collectionModels;
    }

    public List<Payouts> getPayoutModels() {
        return payoutModels;
    }

    public void setPayoutModels(List<Payouts> payoutModels) {
        this.payoutModels = payoutModels;
    }

    public List<Cycles> getCycleModels() {
        return cycleModels;
    }

    public void setCycleModels(List<Cycles> cycleModels) {
        this.cycleModels = cycleModels;
    }

    public List<UnitsModel> getUnitsModels() {
        return unitsModels;
    }

    public void setUnitsModels(List<UnitsModel> unitsModels) {
        this.unitsModels = unitsModels;
    }

    public List<Notifications> getNotificationModels() {
        return notificationModels;
    }

    public void setNotificationModels(List<Notifications> notificationModels) {
        this.notificationModels = notificationModels;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public String getResultDescription() {
        return ResultDescription;
    }

    public void setResultDescription(String resultDescription) {
        ResultDescription = resultDescription;
    }
}
