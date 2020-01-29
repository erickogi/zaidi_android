package com.dev.zaidi.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.dev.zaidi.Utils.DateTimeUtils;

import java.io.Serializable;

@Entity(tableName = "farmerBalance", indices = {@Index(value = {"id", "farmerCode", "code"}, unique = true)})

public class FarmerBalance implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String code;
    @Ignore
    private String traderCode;



    private String farmerCode;

    private String balanceToPay;

    private String balanceOwed;

    private String loans;

    private String orders;

    private String milk;

    private String payoutCode;

    private int payoutStatus;


    private String lastUpdated;

    public FarmerBalance() {
    }

    public FarmerBalance(String code, String farmerCode, String payoutCode, String balanceToPay,
                         String balanceOwed, String lastUpdated, String loans, String orders, String milk) {
        this.code = code;
        this.farmerCode = farmerCode;
        this.payoutCode = payoutCode;
        this.balanceToPay = balanceToPay;
        this.balanceOwed = balanceOwed;
        this.lastUpdated = lastUpdated;
        this.loans = loans;
        this.orders = orders;
        this.milk = milk;


    }

    public String getLoans() {
        return loans;
    }

    public void setLoans(String loans) {
        this.loans = loans;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public String getMilk() {
        return milk;
    }

    public void setMilk(String milk) {
        this.milk = milk;
    }

    public int getPayoutStatus() {
        return payoutStatus;
    }

    public void setPayoutStatus(int payoutStatus) {
        this.payoutStatus = payoutStatus;
    }

    public String getPayoutCode() {
        return payoutCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public void setPayoutCode(String payoutCode) {
        this.payoutCode = payoutCode;
    }

    public String getTraderCode() {
        return traderCode;
    }

    public void setTraderCode(String traderCode) {
        this.traderCode = traderCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFarmerCode() {
        return farmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        this.farmerCode = farmerCode;
    }

    public String getBalanceToPay() {
        if (balanceToPay != null) {
            return balanceToPay;
        } else {
            return "0";
        }
    }

    public void setBalanceToPay(String balanceToPay) {
        this.balanceToPay = balanceToPay;
    }

    public String getBalanceOwed() {
        if (balanceOwed != null) {
            return balanceOwed;
        } else {
            return "0";
        }
    }

    public void setBalanceOwed(String balanceOwed) {
        this.balanceOwed = balanceOwed;
    }

    public String getLastUpdated() {
        if (lastUpdated != null) {
            return lastUpdated;
        } else {
            return DateTimeUtils.Companion.getToday();
        }
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
