package com.dev.lishabora.Models;

public class FarmerListModel {
    private FamerModel famerModel;
    private Double balance;
    private String routeName;

    public FamerModel getFamerModel() {
        return famerModel;
    }

    public void setFamerModel(FamerModel famerModel) {
        this.famerModel = famerModel;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }
}
