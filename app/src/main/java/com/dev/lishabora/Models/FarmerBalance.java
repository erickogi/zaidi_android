package com.dev.lishabora.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.dev.lishabora.Utils.DateTimeUtils;

@Entity(tableName = "farmerBalance", indices = {@Index(value = {"id", "farmerCode", "code"}, unique = true)})

public class FarmerBalance {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String code;
    @Ignore
    private String traderCode;



    private String farmerCode;

    private String balanceToPay;

    private String balanceOwed;

    private String payoutCode;


    private String lastUpdated;

    public FarmerBalance(String code, String farmerCode, String payoutCode, String balanceToPay, String balanceOwed, String lastUpdated) {
        this.code = code;
        this.farmerCode = farmerCode;
        this.payoutCode = payoutCode;
        this.balanceToPay = balanceToPay;
        this.balanceOwed = balanceOwed;
        this.lastUpdated = lastUpdated;
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

    public FarmerBalance() {
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
