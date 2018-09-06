package com.dev.lishabora.Models;

import java.io.Serializable;

public class PayoutFarmersCollectionModel implements Serializable {

    private String farmercode;
    private String farmername;

    private String milktotal;
    private String milktotalLtrs;
    private String milktotalKsh;

    private String loanTotal;
    private String orderTotal;

    private int status;
    private String statusName;
    private String balance;
    private String cycleCode;
    private int payoutNumber;


    public PayoutFarmersCollectionModel() {
    }

    public PayoutFarmersCollectionModel(String farmercode, String farmername, String milktotal,
                                        String loanTotal, String orderTotal, int status, String statusName,
                                        String balance, int payoutNumber, String cycleCode, String milktotalKsh, String milktotalLtrs) {
        this.farmercode = farmercode;
        this.farmername = farmername;
        this.milktotal = milktotal;
        this.loanTotal = loanTotal;
        this.orderTotal = orderTotal;
        this.status = status;
        this.statusName = statusName;
        this.balance = balance;
        this.payoutNumber = payoutNumber;
        this.cycleCode = cycleCode;
        this.milktotalKsh = milktotalKsh;
        this.milktotalLtrs = milktotalLtrs;
    }

    public String getMilktotalLtrs() {
        return milktotalLtrs;
    }

    public void setMilktotalLtrs(String milktotalLtrs) {
        this.milktotalLtrs = milktotalLtrs;
    }

    public String getMilktotalKsh() {
        return milktotalKsh;
    }

    public void setMilktotalKsh(String milktotalKsh) {
        this.milktotalKsh = milktotalKsh;
    }

    public String getCycleCode() {
        return cycleCode;
    }

    public void setCycleCode(String cycleCode) {
        this.cycleCode = cycleCode;
    }

    public int getPayoutNumber() {
        return payoutNumber;
    }

    public void setPayoutNumber(int payoutNumber) {
        this.payoutNumber = payoutNumber;
    }

    public String getFarmercode() {
        return farmercode;
    }

    public void setFarmercode(String farmercode) {
        this.farmercode = farmercode;
    }

    public String getFarmername() {
        return farmername;
    }

    public void setFarmername(String farmername) {
        this.farmername = farmername;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
