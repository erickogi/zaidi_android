package com.dev.lishabora.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "payouts", indices = {@Index("code"), @Index(value = {"startDate", "endDate"}),})

public class Payouts implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;


    private String cycleCode;
    private String startDate;
    //private String startDateMilliseconds;
    private String endDate;
    //private String endDateMilliseconds;
    private String cyclename;
    private int payoutnumber;
    private int status;


    @Ignore
    private String milkTotal;
    private String milkTotalLtrs;
    private String milkTotalKsh;
    @Ignore
    private String loanTotal;
    @Ignore
    private String orderTotal;
    @Ignore
    private String approvedCards;
    @Ignore
    private String pendingCards;
    @Ignore
    private String farmersCount;
    @Ignore
    private String statusName;
    @Ignore
    private String balance;
    @Ignore
    private String balanceTotal;


    @Ignore
    private String traderCode;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBalanceTotal() {
        return balanceTotal;
    }

    public void setBalanceTotal(String balanceTotal) {
        this.balanceTotal = balanceTotal;
    }

    public String getTraderCode() {
        return traderCode;
    }

    public void setTraderCode(String traderCode) {
        this.traderCode = traderCode;
    }


    public String getMilkTotalLtrs() {
        return milkTotalLtrs;
    }

    public void setMilkTotalLtrs(String milkTotalLtrs) {
        this.milkTotalLtrs = milkTotalLtrs;
    }

    public String getMilkTotalKsh() {
        return milkTotalKsh;
    }

    public void setMilkTotalKsh(String milkTotalKsh) {
        this.milkTotalKsh = milkTotalKsh;
    }

    public String getPendingCards() {
        return pendingCards;
    }

    public void setPendingCards(String pendingCards) {
        this.pendingCards = pendingCards;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCycleCode() {
        return cycleCode;
    }

    public void setCycleCode(String cycleCode) {
        this.cycleCode = cycleCode;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCyclename() {
        return cyclename;
    }

    public void setCyclename(String cyclename) {
        this.cyclename = cyclename;
    }

    public int getPayoutnumber() {
        return payoutnumber;
    }

    public void setPayoutnumber(int payoutnumber) {
        this.payoutnumber = payoutnumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMilkTotal() {
        return milkTotal;
    }

    public void setMilkTotal(String milkTotal) {
        this.milkTotal = milkTotal;
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

    public String getApprovedCards() {
        return approvedCards;
    }

    public void setApprovedCards(String approvedCards) {
        this.approvedCards = approvedCards;
    }

    public String getFarmersCount() {
        return farmersCount;
    }

    public void setFarmersCount(String farmersCount) {
        this.farmersCount = farmersCount;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
