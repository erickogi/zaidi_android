package com.dev.lishabora.Models;

public class FarmerHistoryByDateModel {
    private MonthsDates monthsDates;
    private FamerModel famerModel;

    private String milktotal;
    private String loanTotal;
    private String orderTotal;
    private String cycleCode;

    public FarmerHistoryByDateModel() {

    }


    public FarmerHistoryByDateModel(MonthsDates monthsDates, FamerModel famerModel,
                                    String milktotal, String loanTotal,
                                    String orderTotal, String cycleCode) {
        this.monthsDates = monthsDates;
        this.famerModel = famerModel;
        this.milktotal = milktotal;
        this.loanTotal = loanTotal;
        this.orderTotal = orderTotal;
        this.cycleCode = cycleCode;
    }

    public MonthsDates getMonthsDates() {
        return monthsDates;
    }

    public void setMonthsDates(MonthsDates monthsDates) {
        this.monthsDates = monthsDates;
    }

    public FamerModel getFamerModel() {
        return famerModel;
    }

    public void setFamerModel(FamerModel famerModel) {
        this.famerModel = famerModel;
    }

    public String getMilktotal() {
        return milktotal;
    }

    public void setMilktotal(String milktotal) {
        this.milktotal = milktotal;
    }

    public String getLoanTotal() {
        return loanTotal;
    }

    public void setLoanTotal(String loanTotal) {
        this.loanTotal = loanTotal;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getCycleCode() {
        return cycleCode;
    }

    public void setCycleCode(String cycleCode) {
        this.cycleCode = cycleCode;
    }
}
