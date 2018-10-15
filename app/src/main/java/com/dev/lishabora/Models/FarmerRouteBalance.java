package com.dev.lishabora.Models;

import android.arch.persistence.room.Embedded;

public class FarmerRouteBalance {


    @Embedded
    FamerModel famerModel;


    @Embedded(prefix = "routee")
    RoutesModel routesModel;

    String balanceToPay;
    String balanceOwed;


    public FamerModel getFamerModel() {
        return famerModel;
    }

    public void setFamerModel(FamerModel famerModel) {
        this.famerModel = famerModel;
    }

    public RoutesModel getRoutesModel() {
        return routesModel;
    }

    public void setRoutesModel(RoutesModel routesModel) {
        this.routesModel = routesModel;
    }

    public String getBalanceToPay() {
        return balanceToPay;
    }

    public void setBalanceToPay(String balanceToPay) {
        this.balanceToPay = balanceToPay;
    }

    public String getBalanceOwed() {
        return balanceOwed;
    }

    public void setBalanceOwed(String balanceOwed) {
        this.balanceOwed = balanceOwed;
    }
}
