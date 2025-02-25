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

    private int cardstatus;
    private int payoutStatus;


    private String statusName;
    private String balance;
    private String cycleCode;
    private String payoutCode;

    private String payoutStart;
    private String payoutEnd;

    private FamerModel famerModel;

    private boolean isChecked = false;


    public PayoutFarmersCollectionModel() {
    }

    public PayoutFarmersCollectionModel(String farmercode, String farmername, String milktotal,
                                        String loanTotal, String orderTotal, int cardstatus, int payoutStatus, String statusName,
                                        String balance, String payoutCode, String cycleCode, String milktotalKsh,
                                        String milktotalLtrs, String payoutStart, String payoutEnd) {
        this.farmercode = farmercode;
        this.farmername = farmername;
        this.milktotal = milktotal;
        this.loanTotal = loanTotal;
        this.orderTotal = orderTotal;
        this.cardstatus = cardstatus;
        this.payoutStatus = payoutStatus;
        this.statusName = statusName;
        this.balance = balance;
        this.payoutCode = payoutCode;
        this.cycleCode = cycleCode;
        this.milktotalKsh = milktotalKsh;
        this.milktotalLtrs = milktotalLtrs;
        this.payoutStart = payoutStart;
        this.payoutEnd = payoutEnd;

    }

    public PayoutFarmersCollectionModel(String farmercode, String farmername, String milktotal,
                                        String loanTotal, String orderTotal, int cardstatus, int payoutStatus, String statusName,
                                        String balance, String payoutCode, String cycleCode, String milktotalKsh,
                                        String milktotalLtrs, String payoutStart, String payoutEnd, FamerModel famerModel) {
        this.farmercode = farmercode;
        this.farmername = farmername;
        this.milktotal = milktotal;
        this.loanTotal = loanTotal;
        this.orderTotal = orderTotal;
        this.cardstatus = cardstatus;
        this.payoutStatus = payoutStatus;
        this.statusName = statusName;
        this.balance = balance;
        this.payoutCode = payoutCode;
        this.cycleCode = cycleCode;
        this.milktotalKsh = milktotalKsh;
        this.milktotalLtrs = milktotalLtrs;
        this.payoutStart = payoutStart;
        this.payoutEnd = payoutEnd;
        this.famerModel = famerModel;

    }

    public PayoutFarmersCollectionModel(String farmercode, String farmername, String milktotal,
                                        String loanTotal, String orderTotal, int cardstatus, int payoutStatus, String statusName,
                                        String balance, String payoutCode, String cycleCode, String milktotalKsh,
                                        String milktotalLtrs, String payoutStart, String payoutEnd, FamerModel famerModel, boolean isChecked) {
        this.farmercode = farmercode;
        this.farmername = farmername;
        this.milktotal = milktotal;
        this.loanTotal = loanTotal;
        this.orderTotal = orderTotal;
        this.cardstatus = cardstatus;
        this.payoutStatus = payoutStatus;
        this.statusName = statusName;
        this.balance = balance;
        this.payoutCode = payoutCode;
        this.cycleCode = cycleCode;
        this.milktotalKsh = milktotalKsh;
        this.milktotalLtrs = milktotalLtrs;
        this.payoutStart = payoutStart;
        this.payoutEnd = payoutEnd;
        this.famerModel = famerModel;

        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public FamerModel getFamerModel() {
        return famerModel;
    }

    public void setFamerModel(FamerModel famerModel) {
        this.famerModel = famerModel;
    }

    public String getPayoutStart() {
        return payoutStart;
    }

    public void setPayoutStart(String payoutStart) {
        this.payoutStart = payoutStart;
    }

    public String getPayoutEnd() {
        return payoutEnd;
    }

    public void setPayoutEnd(String payoutEnd) {
        this.payoutEnd = payoutEnd;
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

    public String getPayoutCode() {
        return payoutCode;
    }

    public void setPayoutCode(String payoutCode) {
        this.payoutCode = payoutCode;
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

    public int getCardstatus() {
        return cardstatus;
    }

    public void setCardstatus(int cardstatus) {
        this.cardstatus = cardstatus;
    }

    public int getPayoutStatus() {
        return payoutStatus;
    }

    public void setPayoutStatus(int payoutStatus) {
        this.payoutStatus = payoutStatus;
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
